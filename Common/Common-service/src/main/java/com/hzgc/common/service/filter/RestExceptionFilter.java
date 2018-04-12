package com.hzgc.common.service.filter;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.hzgc.common.service.ResponseResult;
import com.hzgc.common.service.error.BusinessException;
import com.hzgc.common.service.error.RestErrorCode;
import com.hzgc.common.service.error.RestErrorTranslator;
import com.hzgc.common.service.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * restful接口统一异常处理
 *
 * @author liuzk
 */
public class RestExceptionFilter implements Filter {
    private final Logger logger;
    private static final ResourceBundle TABLE_BUSINESS_RES = ResourceBundle.getBundle("META-INF/message/TableBusiness");
    private static final ResourceBundle UNIQUE_KEY_RES = ResourceBundle.getBundle("META-INF/message/UniqueKeyName");
    private static final ResourceBundle PRODUCTION_RES = ResourceBundle.getBundle("META-INF/message/Production");

    private static final String BIGDATA_DUBBO_PACKAGE_PREFIX = "com.hzgc.dubbo";

    private static final Pattern DB_REFERENCE_TABLE_PATTERN = Pattern.compile("a foreign key constraint fails \\(`[^`]*`.`([^`]*)`");
    private static final Pattern DB_MAIN_TABLE_PATTERN = Pattern.compile("\\) REFERENCES `([^`]*)`");
    private static final Pattern DB_METHOD_PATTERN = Pattern.compile("add or update a child row");
    private static final Pattern DB_UNIQUE_KEY_PATTERN = Pattern.compile("for key '([^']*)'");
    private static final Pattern RPC_TIMEOUT_ATTACHMENT_PATH_PATTERN = Pattern.compile("attachments=\\{path=([^,]*),");

    public RestExceptionFilter() {
        this(LoggerFactory.getLogger(RestExceptionFilter.class));
    }

    public RestExceptionFilter(Logger logger) {
        this.logger = logger;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            Result result = invoker.invoke(invocation);
            if (result.hasException() && GenericService.class != invoker.getInterface()) {
                ResponseResult response = ResponseResult.ok();

                try {
                    Throwable ex = result.getException();

                    // 先记录错误信息日志
                    logger.error("Got unchecked and undeclared exception which called by "
                            + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName()
                            + ", method: " + invocation.getMethodName() + ", exception: " + ex.getClass().getName()
                            + ": " + ex.getMessage(), ex);

                    int httpStatus = HttpStatus.SC_OK;

                    if (ex instanceof IllegalArgumentException) {
                        response.setErrorEx(RestErrorCode.ILLEGAL_ARGUMENT, ex.getMessage());
                    } else if (ex instanceof UnauthenticatedException || ex instanceof UnauthorizedException) {
                        response.setErrorEx(RestErrorCode.NO_PERMISSION);
                    } else if (ex instanceof RpcException) {
                        // rpc异常，解析是否是超时异常
                        if (null != ex.getCause() && ex.getCause() instanceof TimeoutException) {
                            TimeoutException cause = (TimeoutException) ex.getCause();
                            response.setErrorEx(RestErrorCode.RPC_TIMEOUT, parseRPCTimeoutExceptionProduction(cause));
                        } else {
                            response.setErrorEx(RestErrorCode.RPC_EXCEPTION);
                        }
                    } else if (ex instanceof DataAccessException || ex instanceof SQLException) {
                        // 数据库异常，解析是否是主键冲突或者数据库数据完整性冲突
                        int errorCode = RestErrorCode.DB_OPERATE_FAIL;
                        if (ex instanceof DuplicateKeyException) {
                            response.setErrorEx(errorCode, parseDuplicateKeyException((DuplicateKeyException) ex));
                        } else if (ex instanceof DataIntegrityViolationException) {
                            response.setErrorEx(errorCode,
                                    parseDataIntegrityViolationException((DataIntegrityViolationException) ex));
                        } else {
                            response.setErrorEx(errorCode, ex.getMessage());
                        }
                    } else if (ex instanceof BusinessException) {
                        // 业务异常，直接返回业务错误信息
                        int errorCode = ((BusinessException) ex).getErrorCode();
                        response.setError(errorCode, ex.getMessage());
                    } else {
                        response.setErrorEx(RestErrorCode.UNKNOWN);
                    }

                    return responseHttpError(httpStatus, response);
                } catch (Throwable e) {
                    logger.warn("Fail to ExceptionFilter when called by " + RpcContext.getContext().getRemoteHost()
                            + ". service: " + invoker.getInterface().getName() + ", method: "
                            + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": "
                            + e.getMessage(), e);

                    response.setErrorEx(RestErrorCode.INTERNAL_SERVER);
                    return responseHttpError(HttpStatus.SC_INTERNAL_SERVER_ERROR, response);
                }
            }
            return result;
        } catch (Throwable e) {
            logger.error("Got unchecked and undeclared exception which called by "
                    + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName()
                    + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": "
                    + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 解析rpc超时异常的产品
     *
     * @param e
     * @return
     */
    private String parseRPCTimeoutExceptionProduction(TimeoutException e) {
        boolean isBigdataRpcException = false;
        String path = "";

        Matcher matcher = RPC_TIMEOUT_ATTACHMENT_PATH_PATTERN.matcher(e.getMessage());
        if (matcher.find()) {
            path = matcher.group(1);
        }
        if (path.startsWith(BIGDATA_DUBBO_PACKAGE_PREFIX)) {
            isBigdataRpcException = true;
        }

        String productName = "";
        if (isBigdataRpcException) {
            productName = PRODUCTION_RES.getString("product.name.bidatasrv");
        } else {
            productName = PRODUCTION_RES.getString("product.name.unknown");
        }
        return productName;
    }

    /**
     * 解析数据库完整性异常
     *
     * @param e 异常对象
     * @return 异常描述
     */
    private String parseDataIntegrityViolationException(DataIntegrityViolationException e) {
        String errorMsg = "";

        String message = e.getMessage();

        // 主表
        String mainTable = null;
        // 引用表
        String refTable = null;
        Matcher matcher1 = DB_REFERENCE_TABLE_PATTERN.matcher(message);
        Matcher matcher2 = DB_MAIN_TABLE_PATTERN.matcher(message);
        Matcher matcher3 = DB_METHOD_PATTERN.matcher(message);
        // 是否是delete删除sql语句
        boolean isSqlDeleteStatement = true;
        if (matcher1.find()) {
            refTable = matcher1.group(1);
        }
        if (matcher2.find()) {
            mainTable = matcher2.group(1);
        }
        if (matcher3.find()) {
            isSqlDeleteStatement = false;
        }

        if (StringUtils.isNotEmpty(mainTable) && StringUtils.isNotEmpty(refTable)) {
            String mainTableName = "";
            String refTableName = "";

            try {
                mainTableName = TABLE_BUSINESS_RES.getString(mainTable);
            } catch (MissingResourceException ex) {
                logger.error(e.getMessage());
                mainTableName = mainTable;
            }
            try {

                refTableName = TABLE_BUSINESS_RES.getString(refTable);
            } catch (MissingResourceException ex) {
                logger.error(e.getMessage());
                refTableName = refTable;
            }

            if (isSqlDeleteStatement) {
                errorMsg = RestErrorTranslator.get(RestErrorCode.DATA_INTEGRITY_VIOLATION_4_DELETE, mainTableName,
                        refTableName);
            } else {
                errorMsg = RestErrorTranslator.get(RestErrorCode.DATA_INTEGRITY_VIOLATION_4_UPDATE, refTableName,
                        mainTableName);
            }
        }

        return errorMsg;
    }

    /**
     * 解析数据库主键冲突异常
     *
     * @param e 异常对象
     * @return 异常描述
     */
    private String parseDuplicateKeyException(DuplicateKeyException e) {
        String errorMsg = "";

        String uniqueKey = null;
        String message = e.getMessage();
        Matcher matcher1 = DB_UNIQUE_KEY_PATTERN.matcher(message);
        if (matcher1.find()) {
            uniqueKey = matcher1.group(1);
        }
        if (StringUtils.isNotEmpty(uniqueKey)) {
            String uniqueKeyName = "";
            try {
                uniqueKeyName = UNIQUE_KEY_RES.getString(uniqueKey);
            } catch (MissingResourceException ex) {
                logger.error(ex.getMessage());
                uniqueKeyName = uniqueKey;
            }
            errorMsg = RestErrorTranslator.get(RestErrorCode.DB_DUPLICAET_KEY, uniqueKeyName);
        }
        return errorMsg;
    }

    /**
     * 返回http错误
     *
     * @param httpStatus http状态码
     * @param response   响应对象
     * @return rcpresult
     */
    private RpcResult responseHttpError(int httpStatus, ResponseResult<?> response) {
        HttpServletResponse httpResponse = (HttpServletResponse) RpcContext.getContext().getResponse();

        try {
            String result = JsonUtils.obj2json(response);
            httpResponse.setContentType("application/json; charset=utf-8");
            httpResponse.setHeader("Access-Control-Allow-Origin", AccessControlAllowFilter.ALLOW_ORIGIN);
            httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,x-requested-with,Authorization,Access-Control-Allow-Origin");
            httpResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
            httpResponse.setStatus(httpStatus);
            httpResponse.getWriter().write(result);
            return new RpcResult();
        } catch (Exception e) {
            logger.info("a exception has occur,details as follow" + e.getMessage());
            return new RpcResult(new RuntimeException(e));
        } finally {
            try {
                httpResponse.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}