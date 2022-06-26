package com.acat.handleBlogData;

import com.acat.handleBlogData.domain.esDb.TwitterUserData;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.service.esService.repository.TwitterRepository;
import com.acat.handleBlogData.util.ReaderFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
@Component
class HandleBlogDataApplicationTests {

    @Resource
    private TwitterRepository twitterRepository;

    @Test
    void contextLoads() {
    }

//    @Test
//    public void testLanguageIdentify() {
//        String awaitingTranValue = "こんにちは";
//        Object language = languageIdentifyOuterService.getLanguageIdentifyResult(awaitingTranValue);
//        log.info("language:{}",language);
//    }

//    @Test
//    public void testArticle() {
//        Article article = Article.builder()
//                .id(1)
//                .title("标题")
//                .userId(001)
//                .content("标题标题标题")
//                .createTime(new Date())
//                .build();
//        List<Article> articleList = null;
//        articleRepository.saveAll(articleList);
//        Article es = articleRepository.save(article);
//        boolean res = es.getId() != null;
//        System.out.println(res);
//    }

    @Test
    public void testTwitter() {
//        String filePath = "D:\\en-001.txt";
//        String textValue = ReaderFileUtil.reader(filePath);
//        TwitterUserData twitterUserData = JacksonUtil.strToBean(textValue, TwitterUserData.class);
//        TwitterUserData es = twitterRepository.save(twitterUserData);
//        System.out.println(JacksonUtil.beanToStr(es));
    }

    @Test
    public void test01() {
//        String filePath = "D:\\es-r-00000";
//        List<TwitterUserData> twitterUserDataList = (List<TwitterUserData>) ReaderFileUtil.readFile(filePath, MediaSourceEnum.TWITTER);
//        if (!CollectionUtils.isEmpty(twitterUserDataList)) {
//            twitterRepository.saveAll(twitterUserDataList);
//        }
    }
}
