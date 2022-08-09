package com.acat.handleBlogData.outerService.outerInterface;

import com.acat.handleBlogData.outerService.outerConstants.OutUrlConstants;
import com.acat.handleBlogData.outerService.outerResp.WxTokenResp;
import com.acat.handleBlogData.util.JacksonUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
@Component
public class WxNoticeServiceImpl {

    @Resource
    private RestTemplate restTemplate;

    private static final String CORP_ID = "ww31b32c7bd27bbf9e";
    private static final String SECRET = "mrnTvOcCneu9q-Nh5tKWKhfpocp06OvdejJxmAqsuIs";

    /**
     * 获取企业微信token
     * @return
     */
    public String getWxNoticeToken() {
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("corpid", CORP_ID);
            paramMap.put("corpsecret", SECRET);
            WxTokenResp wxTokenResp = restTemplate.getForObject(OutUrlConstants.WX_GET_TOKEN, WxTokenResp.class, paramMap);
            stopWatch.stop();
            log.info("获取企业微信token,paramMap:{},outerResp:{},time:{}", JacksonUtil.beanToStr(paramMap), JacksonUtil.beanToStr(wxTokenResp), stopWatch.getTotalTimeMillis());

            if (wxTokenResp.getErrcode() == 0 && "ok".equals(wxTokenResp.getErrmsg())) {
                return StringUtils.isBlank(wxTokenResp.getAccess_token()) ? "" : wxTokenResp.getAccess_token();
            }
        }catch (Exception e) {
            log.error("WxNoticeServiceImpl.getWxNoticeToken has error",e.getMessage());
        }
        return null;
    }

    public void sendWxMsg(String token, String msg) {
        try {
            if (StringUtils.isBlank(token) || StringUtils.isBlank(msg)) {
                return;
            }

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            HttpHeaders httpHeader = new HttpHeaders();
            httpHeader.setContentType(MediaType.APPLICATION_JSON);
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("access_token", token);

            HashMap map = new HashMap();
            map.put("touser", "@all");
            map.put("msgtype", "text");
            map.put("agentid", 1000005);
            map.put("safe", 0);

            HashMap textMap = Maps.newHashMap();
            textMap.put("content", msg);
            map.put("text", textMap);
            paramMap.put("send_msg", map);

            HttpEntity<HashMap<String, Object>> requestParam = new HttpEntity<>(paramMap, httpHeader);
            ResponseEntity<String> outResp = restTemplate.postForEntity(OutUrlConstants.WX_SEND_MSG, requestParam, String.class);
            stopWatch.stop();

            System.out.println(JacksonUtil.beanToStr(outResp));
        }catch (Exception e) {
            log.error("WxNoticeServiceImpl.sendWxMsg has error",e.getMessage());
        }
    }
}
