package com.acat.handleBlogData;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
@Component
class HandleBlogDataApplicationTests {

    @Test
    void contextLoads() {
    }

//    @Test
//    public void testLanguageIdentify() {
//        String awaitingTranValue = "こんにちは";
//        Object language = languageIdentifyOuterService.getLanguageIdentifyResult(awaitingTranValue);
//        log.info("language:{}",language);
//    }
}
