package com.hzgc.common.util.empty;

import java.util.List;

public class IsEmpty {

    public static boolean strIsRight(String str) {
        return null != str && str.length() > 0;
    }

    public static boolean listIsRight(List list) {
        return null != list && list.size() > 0;
    }
}
