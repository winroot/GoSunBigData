package com.hzgc.service.dynrepo;

public enum  CaptureStatuCode {
    THRESHOLDNULL("FEATURENULL");
    private String code;
    CaptureStatuCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
