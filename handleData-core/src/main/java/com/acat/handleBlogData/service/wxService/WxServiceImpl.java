package com.acat.handleBlogData.service.wxService;

import com.acat.handleBlogData.outerService.outerInterface.WxNoticeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class WxServiceImpl {

    @Resource
    private WxNoticeServiceImpl wxNoticeService;

    @Async
    public void sendWxMsg(String msg) {
        String token = wxNoticeService.getWxNoticeToken();
        if (StringUtils.isBlank(token)) {
            log.info("WxServiceImpl.sendWxMsg token is null");
            return;
        }
        wxNoticeService.sendWxMsg(token, msg);
    }

}
