package com.acat.handleBlogData.outerService.outerInterface;

import com.acat.handleBlogData.outerService.outerConstants.OutUrlConstants;
import com.acat.handleBlogData.outerService.outerResp.WxTokenResp;
import com.acat.handleBlogData.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            log.error("KnowledgeOuterServiceImpl.getKnowLedgeInfo has error",e.getMessage());
        }
        return null;
    }
}
