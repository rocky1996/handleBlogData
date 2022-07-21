package com.acat.handleBlogData.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {

    public static String handleStr(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }

        if ("<>".equals(str) || "()".equals(str) || "[]".equals(str) || "{}".equals(str)) {
            return "";
        }
        return str;
    }

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
            if (str.contains("{") || str.contains("}") || str.contains("[") || str.contains("]")) {
                String tempStr = str.replaceAll(String.valueOf("\\{"), "")
                        .replaceAll(String.valueOf("\\}"), "")
                        .replaceAll(String.valueOf("\\["), "")
                        .replaceAll(String.valueOf("\\]"), "");
                return tempStr;
            }
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

    /**
     * 处理粉丝数
     * @return
     */
    public static String handleFollowersCount(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }

        if (NumberUtils.isNumber(str)) {
            return str;
        }else {
            return str.substring(0, str.indexOf("f")).trim();
        }
    }

    public static String handleWorks(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }

        int strStartIndex = str.indexOf("title:");
        int strEndIndex = str.indexOf(",details");
        if (strStartIndex > 0 && strEndIndex < 0) {
            String s1 = str.substring(strStartIndex, str.length()-1).substring("title:".length());
            return StringUtils.isBlank(s1) ? "" : s1;
        }
        if (strStartIndex > 0 && strEndIndex > 0) {
            String s2 = str.substring(strStartIndex, strEndIndex).substring("title:".length());
            return StringUtils.isBlank(s2) ? "" : s2;
        }
        return "";
    }

    public static void main(String[] args) {
//        String str = "debfryfbri,defnrur@ndfjrf.com,defrfr,frrfr,swerfrf,er";
//        System.out.println(checkEmailAndGet(str));

//        String strPhone = "张三：13539558064";
//        System.out.println(checkPhoneAndGet(strPhone));

//        System.out.println(handleFollowersCount("2.1K followers"));
//        System.out.println(handleWorks("{title:A X設計整合工作室,details: [2010年9月23日 - 现在,]}"));
//        System.out.println(handleWorks("{title:目前就职：PT. Sumber Alfaria Trijaya, Tbk (Alfamart)}"));
//        System.out.println(handleWorks("{title:UOB Bank Singapore - Staff management,details: [2017年3月28日 - 现在,]}|{title:曾在 MD Entertainment 担任 Artist}"));
        System.out.println(checkEmailAndGet("{\"Instagram\":\"https://www.instagram.com/qwert9632\"}"));
    }
}
