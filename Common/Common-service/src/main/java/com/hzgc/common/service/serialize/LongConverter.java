package com.hzgc.common.service.serialize;

import org.jboss.resteasy.spi.StringParameterUnmarshaller;

import java.lang.annotation.Annotation;

/**
 * <p>创建时间：2017-6-9 10:09</p>
 *
 * @author 娄存银
 * @version 1.0
 */
public class LongConverter implements StringParameterUnmarshaller<Long> {
    @Override
    public void setAnnotations(Annotation[] annotations) {

    }

    @Override
    public Long fromString(String s) {
        Long integer = null;
        try {
            integer = Long.parseLong(s);
        } catch (NumberFormatException ignored) {
        }
        return integer;
    }
}
