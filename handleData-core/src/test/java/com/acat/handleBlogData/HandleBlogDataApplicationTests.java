package com.acat.handleBlogData;

import com.acat.handleBlogData.service.emailService.SendEmailServiceImpl;
import com.acat.handleBlogData.service.esService.repository.TwitterRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
@Component
class HandleBlogDataApplicationTests {

    @Resource
    private TwitterRepository twitterRepository;
    @Resource
    private SendEmailServiceImpl sendEmailService;
    @Value("${spring.profiles.active}")
    private String env;
    @Value("${spring.elasticsearch.rest.uris}")
    private String hostList;
    @Resource
    private RestHighLevelClient restHighLevelClient;

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
//        SendEmailReq sendEmailReq = SendEmailReq
//                .builder()
//                .toEmail("2791752775@qq.com")
//                .subject("数学")
//                .content("数学及格了")
//                .build();
//        sendEmailService.sendSimpleEmail(sendEmailReq);

        System.out.println(hostList);
    }

    /**
     * https://www.qb5200.com/article/492194.html
     * https://www.csdn.net/tags/MtTaEgxsNzk1ODAwLWJsb2cO0O0O.html
     */
    @Test
    public void test02() throws Exception{
        // 创建请求
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .query(QueryBuilders.termsQuery("country", "国"));

        //搜索
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("twitter");
        searchRequest.types("_doc");
        searchRequest.source(builder);
        // 执行请求
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 解析查询结果
        System.out.println(response.toString());
    }
}
