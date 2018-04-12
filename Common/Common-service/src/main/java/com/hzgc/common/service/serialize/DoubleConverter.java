package com.hzgc.common.service.serialize;

import org.jboss.resteasy.spi.StringParameterUnmarshaller;

import java.lang.annotation.Annotation;

public class DoubleConverter implements StringParameterUnmarshaller<Double> {
    @Override
    public void setAnnotations(Annotation[] annotations) {

    }

    @Override
    public Double fromString(String str) {
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
