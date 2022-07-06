package com.acat.handleBlogData;

import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.dao.UserDao;
//import com.acat.handleBlogData.domain.BlogSystemUser;
//import com.acat.handleBlogData.domain.BlogSystemUserExample;
//import com.acat.handleBlogData.mapper.BlogSystemUserMapper;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.enums.RestEnum;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.ejml.dense.row.decomposition.qr.QrUpdate_DDRM;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
@Component
class HandleBlogDataApplicationTests {

//    @Resource
//    private TwitterRepository twitterRepository;
//    @Resource
//    private SendEmailServiceImpl sendEmailService;
//    @Value("${spring.profiles.active}")
//    private String env;
//    @Value("${spring.elasticsearch.rest.uris}")
//    private String hostList;
    @Resource
    private RestHighLevelClient restHighLevelClient;
//    @Resource
//    private UserDao userDao;
//    @Autowired
//    private ElasticsearchTemplate esTemplate;

//    @Resource
//    private EsServiceImpl esService;
//    @Value("${spring.elasticsearch.rest.uris}")
//    private String hostList;

//    @Resource
//    private BlogSystemUserMapper blogSystemUserMapper;

//    private static String[] indexArray = new String[]{
//            MediaSourceEnum.TWITTER.getEs_index(),
//            MediaSourceEnum.INSTAGRAM.getEs_index(),
//            MediaSourceEnum.FB_IMPL.getEs_index(),
//            MediaSourceEnum.FB_HISTORY.getEs_index(),
//            MediaSourceEnum.FQ_IMPL.getEs_index(),
//            MediaSourceEnum.FQ_HISTORY.getEs_index()
//    };

//    @Override
//    public BlogSystemUser userLogin(String userName, String password) {
//        BlogSystemUserExample example = new BlogSystemUserExample();
//        example.createCriteria()
//                .andUsernameEqualTo(userName)
//                .andPasswordEqualTo(password);
//        return blogSystemUserMapper.selectByExample(example).stream().findFirst().orElse(null);
//    }

//    @Test
//    void contextLoads() {
//        try{
//            BlogSystemUserExample example = new BlogSystemUserExample();
//            example.createCriteria()
//                    .andUsernameEqualTo("baihu-002")
//                    .andPasswordEqualTo("baihu-002");
//            List<BlogSystemUser> list = blogSystemUserMapper.selectByExample(example);
//            System.out.println(list);
//        }catch (Exception e){
//            log.error("{}",e.getMessage());
//        }
//    }

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

//    @Test
//    public void testTwitter() {
////        String filePath = "D:\\en-001.txt";
////        String textValue = ReaderFileUtil.reader(filePath);
////        TwitterUserData twitterUserData = JacksonUtil.strToBean(textValue, TwitterUserData.class);
////        TwitterUserData es = twitterRepository.save(twitterUserData);
////        System.out.println(JacksonUtil.beanToStr(es));
//    }

//    @Test
//    public void test01() {
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

//        System.out.println(hostList);
//    }

    /**
     * https://www.qb5200.com/article/492194.html
     * https://www.csdn.net/tags/MtTaEgxsNzk1ODAwLWJsb2cO0O0O.html
     */
//    @Test
//    /**
//     * https://blog.csdn.net/W_Meng_H/article/details/123940475?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-123940475-blog-122363360.pc_relevant_aa&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-123940475-blog-122363360.pc_relevant_aa&utm_relevant_index=1
//     * 深度分页场景
//     */
//    public void test02() throws Exception{
//
//        try {
//            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
////        if (StringUtils.isNotBlank(searchReq.getUserId())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("user_id", searchReq.getUserId()));
////        }
////        if (StringUtils.isNotBlank(searchReq.getUserName())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("source_id", "95b25fe3c9720b0963ca6969593fd55e"));
////        }
////        if (StringUtils.isNotBlank(searchReq.getUserQuanName())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("use_name", searchReq.getUserQuanName()));
////        }
////        if (StringUtils.isNotBlank(searchReq.getPhoneNum())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("mobile", searchReq.getPhoneNum()));
////        }
////        if (StringUtils.isNotBlank(searchReq.getEmail())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("email", searchReq.getEmail()));
////        }
//            //  if (StringUtils.isNotBlank(searchReq.getCountry())) {
//            //boolQueryBuilder.must(QueryBuilders.matchQuery("country", "美国"));
////        boolQueryBuilder.must(QueryBuilders.matchQuery("user_type", "-1"));
////        boolQueryBuilder.must(QueryBuilders.matchQuery("screen_name", "everybery_photo".trim()));
//            //   }
////        if (StringUtils.isNotBlank(searchReq.getCity())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("city", searchReq.getCity()));
////        }
////        if (!Objects.isNull(searchReq.getStartTime()) && !Objects.isNull(searchReq.getEndTime())) {
////            boolQueryBuilder.must(QueryBuilders.rangeQuery("source_create_time").lte(searchReq.getEndTime()).gte(searchReq.getStartTime()));
////        }
//
//            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//            sourceBuilder.query(boolQueryBuilder);
//            sourceBuilder.from(0).size(10).sort("source_create_time.keyword", SortOrder.DESC);
//            //            sourceBuilder.from(searchReq.getPageNum()).size(searchReq.getPageSize());
//            //sourceBuilder.sort("registered_time.keyword", SortOrder.DESC);
//
//            SearchRequest searchRequest = new SearchRequest();
//            searchRequest.indices("instagram");
//            searchRequest.types("_doc");
//            searchRequest.source(sourceBuilder);
//            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//            System.out.println(response);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @Test
//    public void test03() throws Exception{
//        SearchSourceBuilder builder = new SearchSourceBuilder()
//                .query(QueryBuilders.matchQuery("uuid", "ed8badcc-19a9-4fb1-a8a7-a58fecc7d643"));
//
//        //搜索
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices("twitter");
//        searchRequest.types("_doc");
//        searchRequest.source(builder);
//        // 执行请求
//        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        System.out.println(response);
//    }

//    @Test
//    public void test04() throws Exception{
//        String[] includeFields = new String[] {"country"};
//        CollapseBuilder collapseBuilder = new CollapseBuilder("country.keyword");
//        SearchSourceBuilder builder = new SearchSourceBuilder()
//                .query(QueryBuilders.matchAllQuery())
//                .fetchSource(includeFields, null)
//                .collapse(collapseBuilder);
//
//
//
//        //搜索
//        //"instagram","fq_history","fq_impl","fb_history","fb_impl"
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices("twitter","instagram","fq_history","fq_impl","fb_history","fb_impl");
//        searchRequest.types("_doc");
//        searchRequest.source(builder);
//        // 执行请求
//        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        System.out.println(response);
//    }

//    @Test
//    public void test05() throws Exception{
//        BoolQueryBuilder builder = QueryBuilders.boolQuery();
//        BoolQueryBuilder channelQueryBuilder = new BoolQueryBuilder();
//        List<String> list = Lists.newArrayList("ideolo", "astro", "Joe");
//        for(String channel: list){
//            channelQueryBuilder.should(QueryBuilders.matchPhraseQuery("use_name",channel));
//        }
//        builder.must(channelQueryBuilder);
//
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(builder);
//
//
//        //搜索
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices(indexArray);
//        searchRequest.types("_doc");
//        searchRequest.source(sourceBuilder);
//
//        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        if (response == null) {
//            log.info("");
//        }
//    }


    @Test
    /**
     * 分词
     */
    public void test06() {
        try {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        if (StringUtils.isNotBlank(searchReq.getUserId())) {
//            boolQueryBuilder.must(QueryBuilders.matchQuery("user_id", searchReq.getUserId()));
//        }
//        if (StringUtils.isNotBlank(searchReq.getUserName())) {
//            boolQueryBuilder.must(QueryBuilders.matchQuery("source_id", "95b25fe3c9720b0963ca6969593fd55e"));
//        }
//        if (StringUtils.isNotBlank(searchReq.getUserQuanName())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("use_name", "Douglas"));
//        }
//        if (StringUtils.isNotBlank(searchReq.getPhoneNum())) {
//            boolQueryBuilder.must(QueryBuilders.matchQuery("mobile", searchReq.getPhoneNum()));
//        }
//        if (StringUtils.isNotBlank(searchReq.getEmail())) {
//            boolQueryBuilder.must(QueryBuilders.matchQuery("email", searchReq.getEmail()));
//        }
            //  if (StringUtils.isNotBlank(searchReq.getCountry())) {
//            boolQueryBuilder.must(QueryBuilders.matchQuery("country.keyword", "美国"));
//        boolQueryBuilder.must(QueryBuilders.matchQuery("user_type", "-1"));
//        boolQueryBuilder.must(QueryBuilders.matchQuery("screen_name", "everybery_photo".trim()));
            //   }
//        if (StringUtils.isNotBlank(searchReq.getCity())) {
//            boolQueryBuilder.must(QueryBuilders.matchQuery("city", searchReq.getCity()));
//        }
//        if (!Objects.isNull(searchReq.getStartTime()) && !Objects.isNull(searchReq.getEndTime())) {
//            boolQueryBuilder.must(QueryBuilders.rangeQuery("source_create_time").lte(searchReq.getEndTime()).gte(searchReq.getStartTime()));
//        }

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.from(0).size(10);
            sourceBuilder.trackTotalHits(true);

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices("twitter");
            searchRequest.types("_doc");
            searchRequest.source(sourceBuilder);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(response);
        }catch (Exception e) {
            log.error("{}:",e.getMessage());
        }
    }

}
