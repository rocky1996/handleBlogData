package com.acat.handleBlogData.outerService.outerConstants;

public class OutUrlConstants {

    /*************语种识别**************/
    /**
     * http://121.196.186.253:24024/
     * http://http://20.10.0.10:24024/detect?q=nullただただ呟く
     *
     */
    public static String LANGUAGE_IDENTIFY_URL = "http://121.196.186.253:24024/detect";

    /*************中译语音翻译**************/
    public static String TRANSLATE_URL = "http://translateport.yeekit.com/translate";

    /**
     * 企业微信通知
     */
    public static String WX_GET_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpid}&corpsecret={corpsecret}";
    public static String WX_SEND_MSG = "https://qyapi.weixin.qq.com/cgi-bin/message/send";
}
