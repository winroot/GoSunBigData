package com.hzgc.common.service.error;

import com.hzgc.common.service.ResponseResult;
import org.jboss.resteasy.api.validation.ResteasyViolationException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * 将 ResteasyViolationException 转换为 ResponseResult，只取 ResteasyConstraintViolation message 拼接 errorMsg
 *
 * @author loucy
 */
public class ValidationExceptionMapper implements ExceptionMapper<ResteasyViolationException> {
    private static final String DIVIDER = ",";

    @Override
    public Response toResponse(ResteasyViolationException e) {
        // 拼装错误信息
        StringBuilder builder = new StringBuilder();
        e.getViolations().forEach(violation -> {
            builder.append(violation.getMessage())
                    .append(DIVIDER);
        });
        builder.deleteCharAt(builder.lastIndexOf(DIVIDER));
        // 生成 ResponseResult
        ResponseResult result = new ResponseResult();
        result.setErrorEx(RestErrorCode.ILLEGAL_ARGUMENT, builder.toString());

        return Response
                .ok(result, MediaType.APPLICATION_JSON_TYPE.withCharset("utf-8"))
                .build();
    }
}
