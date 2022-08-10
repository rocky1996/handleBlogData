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

//    private static final String token = "Ccm471W0nzt_ltYJrdrjdpcPM7C3eWUCIHN-A9b15V7mQoamow1jcKJME4USgCZsyFvoZcVatoHi640MH8gICaJq64JI3199CAGiCu3krl6Nk6c_MXX5NBnudGXC3fl8gJYjqNODdqBHL7VFE3G8RJt6yQzH1JsUjq6KaQq9Al5-BWDUR37ztK3wypBEW7bul7PPrSMZK5oZAJc9gP937Q";
//    private static final String token = "yTdHLG0AzjjkB0knLZDL67vMP3oxGsTEUEzsFu_kYWYVuzUxW55und3BYdH6i3TCF6vbX3nGZP6cyUWI0DWgFN3T6FujKTZPLHU7H2Izq8M523rb1Fq8vL3r8Pz0sxBD7P2N2vwYptJz-UzX1N22kIxV0Dahec3_asT9enUB_pyu_T8X7OBIX23i_vH_kVB8ohbUUxf46H82PmMXOZ1aTA";
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

    public void sendWxMsg(String msg) {
        try {
            if (StringUtils.isBlank(msg)) {
                return;
            }

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            HttpHeaders httpHeader = new HttpHeaders();
            httpHeader.setContentType(MediaType.APPLICATION_JSON);
            HashMap<String, Object> paramMap = new HashMap<>();
//            paramMap.put("access_token", token);

            HashMap map = new HashMap();
            map.put("touser", "touser");
            map.put("msgtype", "text");
            map.put("agentid", 1000005);
            map.put("toparty", "2");
            map.put("safe", "0");

            HashMap textMap = Maps.newHashMap();
            textMap.put("content", msg);
            map.put("text", textMap);
            paramMap.put("send_msg", map);

            HttpEntity<HashMap<String, Object>> requestParam = new HttpEntity<>(paramMap, httpHeader);
            ResponseEntity<String> outResp = restTemplate.postForEntity(OutUrlConstants.WX_SEND_MSG, requestParam, String.class);
            stopWatch.stop();
            log.info("发送企业微信,paramMap:{},outerResp:{},time:{}", JacksonUtil.beanToStr(paramMap), JacksonUtil.beanToStr(outResp), stopWatch.getTotalTimeMillis());
        }catch (Exception e) {
            log.error("WxNoticeServiceImpl.sendWxMsg has error",e.getMessage());
        }
    }
}
