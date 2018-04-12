package com.hzgc.common.service.filter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 这样传参：
 * ?group_id=&department_id=&sort=&start=0
 * 参数解析出来是一个空串而非 null。
 * <p>创建时间：2017-6-15 11:00</p>
 *
 * @author 娄存银
 * @version 1.0
 */
@Component
@Aspect
public class FilterEmptyStringAspect {
    private static final String POINTCUT = "@within(com.gosun.isap.common.filter.FilterEmptyString) "
            + "|| @annotation(com.gosun.isap.common.filter.FilterEmptyString)";

    /**
     * 把前端传入的空白字符参数转换成 null
     *
     * @param joinPoint joinPoint
     * @return 执行结果
     * @throws Throwable 可能抛出的异常
     */
    @Around(POINTCUT)
    public Object filter(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        if (objects != null && objects.length > 0) {
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object != null && object instanceof String) {
                    String string = ((String) object).trim();
                    if (!string.isEmpty()) {
                        objects[i] = string;
                    }else {
                        objects[i] = null;
                    }
                }
            }
        }
        return joinPoint.proceed(objects);
    }
}
