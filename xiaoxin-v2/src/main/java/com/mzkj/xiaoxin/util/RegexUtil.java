package com.mzkj.xiaoxin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by zhouzhenyang on 2018/9/18
 */
public class RegexUtil {

    private static Pattern pattern;

    public static String getCode(String msg) {
        if (pattern == null) {
            pattern = Pattern.compile("\\d{6}");
        }
        final Matcher m = pattern.matcher(msg);
        while (m.find()) {
            return m.group();
        }
        return null;
    }
}
