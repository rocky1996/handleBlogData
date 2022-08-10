package com.acat.handleBlogData.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class DingTalkUtil {

    public static final String DING_DING_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=f9f5604d86d7a6f5e797e57857a7ff5c10300d4712b2ed3596eb0cb7b74885d1";
    public static void sendDdMessage(String msg){
        //钉钉的webhook
        //请求的JSON数据，这里用map在工具类里转成json格式
        Map<String,Object> json = Maps.newHashMap();
        Map<String,Object> text = Maps.newHashMap();
        json.put("msgtype","text");
        text.put("content","项目告警通知:" + msg + "\n关键字信息为:" + "rsync," + "kafka," + "ftp," + "sjzx," + "api, " + "lh");
        json.put("text",text);
        //发送post请求
        String response = SendHttps.sendPostByMap(DING_DING_TOKEN, json);
        log.error("项目告警发送钉钉，响应结果：{}",response);
    }

    public static void main(String[] args) throws Exception{
        sendDdMessage("Hello");
    }
}
