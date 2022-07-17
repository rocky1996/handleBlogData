package com.acat.handleBlogData.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {

    public static String checkEmailAndGet(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }

        String regex = "[a-zA-Z0-9_-]+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return m.group();
        }else {
            return str;
        }
    }

    public static String checkPhoneAndGet(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }

        Pattern p = Pattern.compile("1[345678]\\d{9}");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return m.group();
        }else {
            return "";
        }
    }

    public static void main(String[] args) {
//        String str = "debfryfbri,defnrur@ndfjrf.com,defrfr,frrfr,swerfrf,er";
//        System.out.println(checkEmailAndGet(str));

        String strPhone = "张三：13539558064";
        System.out.println(checkPhoneAndGet(strPhone));
    }
}
