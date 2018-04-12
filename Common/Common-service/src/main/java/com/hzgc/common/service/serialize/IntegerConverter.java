package com.hzgc.common.service.serialize;

import org.jboss.resteasy.spi.StringParameterUnmarshaller;

import java.lang.annotation.Annotation;

/**
 * Url 传参整型转换。
 * 以插件的形式配置到 rest 协议中，避免因为解析不到正确的 Integer param 导致的 404 错误。
 * <p>创建时间：2017-6-6 14:51</p>
 *
 * @author 娄存银
 * @version 1.0
 */
public class IntegerConverter implements StringParameterUnmarshaller<Integer> {
    @Override
    public void setAnnotations(Annotation[] annotations) {

    }

    @Override
    public Integer fromString(String s) {
        Integer integer = null;
        try {
            integer = Integer.parseInt(s);
        } catch (NumberFormatException ignored) {
        }
        return integer;
    }
}
