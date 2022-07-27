package com.acat.handleBlogData;

import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import com.acat.handleBlogData.constants.RedisKeyConstants;
import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.dao.UserDao;
//import com.acat.handleBlogData.domain.BlogSystemUser;
//import com.acat.handleBlogData.domain.BlogSystemUserExample;
//import com.acat.handleBlogData.mapper.BlogSystemUserMapper;
import com.acat.handleBlogData.domain.esEntityV2.FbUserData_v2;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.enums.RestEnum;
import com.acat.handleBlogData.outerService.outerInterface.TranslateOuterServiceImpl;
import com.acat.handleBlogData.service.redisService.RedisServiceImpl;
import com.acat.handleBlogData.util.CountryUtil;
import com.acat.handleBlogData.util.JacksonUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.ejml.dense.row.decomposition.qr.QrUpdate_DDRM;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.AcknowledgedResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    @Resource
    private UserDao userDao;
//    @Autowired
//    private ElasticsearchTemplate esTemplate;

//    @Resource
//    private EsServiceImpl esService;
//    @Value("${spring.elasticsearch.rest.uris}")
//    private String hostList;

//    @Resource
//    private BlogSystemUserMapper blogSystemUserMapper;
    @Resource
    private TranslateOuterServiceImpl translateOuterService;
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Resource
    private RedisServiceImpl redisService;
//    @Resource
//    private SendEmailService sendEmailService;

//    /**
//     * 旧索引
//     */
//    private static String[] indexArray = new String[]{
//            MediaSourceEnum.TWITTER.getEs_index_v1(),
//            MediaSourceEnum.INSTAGRAM.getEs_index_v1(),
//            MediaSourceEnum.FB_IMPL.getEs_index_v1(),
//            MediaSourceEnum.FB_HISTORY.getEs_index_v1(),
//            MediaSourceEnum.FQ_IMPL.getEs_index_v1(),
//            MediaSourceEnum.FQ_HISTORY.getEs_index_v1(),
//            MediaSourceEnum.LINKEDIN_IMPL.getEs_index_v1(),
//            MediaSourceEnum.LINKEDIN_HISTORY.getEs_index_v1(),
//            MediaSourceEnum.LINKEDIN_BUSINESS.getEs_index_v1(),
//            MediaSourceEnum.LINKEDIN_SCHOOL.getEs_index_v1(),
//    };

    /**
     * 新的索引
     */
    private static String[] indexArray_v2 = new String[]{
            MediaSourceEnum.TWITTER.getEs_index_v2(),
            MediaSourceEnum.INSTAGRAM.getEs_index_v2(),
            MediaSourceEnum.FB_IMPL.getEs_index_v2(),
            MediaSourceEnum.FQ_IMPL.getEs_index_v2(),
            MediaSourceEnum.LINKEDIN_IMPL.getEs_index_v2(),
            MediaSourceEnum.LINKEDIN_BUSINESS.getEs_index_v2(),
            MediaSourceEnum.LINKEDIN_SCHOOL.getEs_index_v2(),
    };


//    @Override
//    public BlogSystemUser userLogin(String userName, String password) {
//        BlogSystemUserExample example = new BlogSystemUserExample();
//        example.createCriteria()
//                .andUsernameEqualTo(userName)
//                .andPasswordEqualTo(password);
//        return blogSystemUserMapper.selectByExample(example).stream().findFirst().orElse(null);
//    }

    @Test
    void contextLoads() {
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

    @Test
    public void test03() throws Exception{
//        SearchSourceBuilder builder = new SearchSourceBuilder()
//                .query(QueryBuilders.matchQuery("uuid", "ed8badcc-19a9-4fb1-a8a7-a58fecc7d643"));

//        SearchSourceBuilder builder = new SearchSourceBuilder()
//                .query(QueryBuilders.queryStringQuery("glas").field("use_name"));

        SearchSourceBuilder builder = new SearchSourceBuilder()
                .query(QueryBuilders.wildcardQuery("use_name","*glas*"));

        //搜索
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("twitter");
        searchRequest.types("_doc");
        searchRequest.source(builder);
        // 执行请求
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response);
    }

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

//    @Test
//    public void test06() throws Exception{
//        SearchSourceBuilder builder = new SearchSourceBuilder()
//                .query(QueryBuilders.matchAllQuery())
//                .trackTotalHits(true);
//        //搜索
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices("twitter");
//        searchRequest.types("_doc");
//        searchRequest.source(builder);
//
//        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
////        if (response == null) {
////            log.info("error!!!");
////        }
////        System.out.println(response);
//        System.out.println(response == null ? 0 : response.getHits().getTotalHits().value);
//    }

    @Test
    public void test07() {
        String str = "There was nothing casual about this";
        String result =  translateOuterService.getTranslateValue("en", str);
        System.out.println(result);
    }

    @Test
    public void test08() {
        String str = "nullただただ呟く";
        String result = translateOuterService.getLanguageDelectResult(str);
        System.out.println("result:" + result);
    }


    /**
     * 分词
     */
//    @Test
//    public void test09() throws Exception{
//
//        Integer pageNum = 1;
//        Integer pageSize = 10;
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
////        if (StringUtils.isNotBlank(searchReq.getUserId())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("user_id.keyword", searchReq.getUserId()));
////        }
////        if (StringUtils.isNotBlank(searchReq.getUserName())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("screen_name.keyword", searchReq.getUserName()));
////        }
////        if (StringUtils.isNotBlank(searchReq.getUserQuanName())) {
//            boolQueryBuilder.must(QueryBuilders.matchQuery("use_name", "King"));
////        }
////        if (StringUtils.isNotBlank(searchReq.getBeforeName())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("name_userd_before.keyword", searchReq.getBeforeName()));
////        }
////        if (StringUtils.isNotBlank(searchReq.getPhoneNum())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("mobile.keyword", searchReq.getPhoneNum()));
////        }
////        if (StringUtils.isNotBlank(searchReq.getEmail())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("email.keyword", searchReq.getEmail()));
////        }
////        if (StringUtils.isNotBlank(searchReq.getCountry())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("country.keyword", searchReq.getCountry()));
////        }
////        if (StringUtils.isNotBlank(searchReq.getCity())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("city.keyword", searchReq.getCity()));
////        }
////        if (StringUtils.isNotBlank(searchReq.getUserSummary())) {
////            boolQueryBuilder.must(QueryBuilders.matchQuery("user_summary.keyword", searchReq.getCity()));
////        }
//
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(boolQueryBuilder);
//        sourceBuilder.from((pageNum > 0 ? (pageNum - 1) : 0) * pageSize).size(pageSize);
//        sourceBuilder.trackTotalHits(true);
//        //            sourceBuilder.sort("registered_time.keyword", SortOrder.DESC);
//
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices("twitter");
//        searchRequest.types("_doc");
//        searchRequest.source(sourceBuilder);
//        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        if (response == null) {
//            log.error("");
//        }
//        System.out.println(response);
//    }

//    @Test
//    public void deleteUserIndex() {
//        IndexCoordinates indexCoordinates = IndexCoordinates.of("link_v2");
//        indexCoordinates.getIndexNames();
//        elasticsearchRestTemplate.indexOps(indexCoordinates).delete();
//    }

    @Test
    public void test12() {
        String qName = "Katie Bethea";
        String languageType = translateOuterService.getLanguageDelectResult(qName);
        if ("zh".equals(languageType)) {
            System.out.println(qName.trim());
        }else if ("en".equals(languageType) || "vi".equals(languageType)) {
            System.out.println(CountryUtil.handleStr(qName));
        }else {
            System.out.println(qName);
        }
    }

    @Test
    public void test13() {
//        redisService.push("aaa", "111", true);
//        List list = redisService.range("country", 0L, -1L);
//        list.forEach(e-> {
//            System.out.println(e);
//        });
        String str = "{\n" +
                "    \"userFacebookUrl\":\"https://www.facebook.com/profile.php?id=100025599426924\",\n" +
                "    \"userFormerNickname\":\"\",\n" +
                "    \"userId\":\"100025599426924\",\n" +
                "    \"userFormerName\":\"PatelRakesh Amrut\",\n" +
                "    \"userFormerNameDate\":\"2022-04-05 17:34:20\"\n" +
                "}";

        Map<String, Objects> map = JacksonUtil.strToBean(str, Map.class);
        System.out.println(map);
    }

    @Test
    public void test14() {
//        System.out.println(redisService.getValue(RedisKeyConstants.TWITTER_PRO_GOV_NUM_KEY));
        String str = "{\"user_political_views\":\"\",\"user_systent_name\":\"\",\"w3_fb_url\":\"\",\"institution_id\":\"\",\"is_community_page\":\"\",\"communication_philosophy\":\"\",\"have_product\":\"\",\"exchange_number\":\"\",\"visit_number\":\"\",\"first_name\":\"\",\"last_name\":\"\",\"teach_message\":\"\",\"acquisition_time\":\"2022-03-05 18:54:01\",\"affective_state\":\"\",\"background_picture_url\":\"https://scontent-sjc3-1.xx.fbcdn.net/v/t39.30808-6/242615571_227562046078401_7075016216550821750_n.jpg?stp\\u003dcp0_dst-jpg_e15_fr_q65\\u0026_nc_cat\\u003d105\\u0026ccb\\u003d1-5\\u0026_nc_sid\\u003de3f864\\u0026_nc_ohc\\u003dD7ZbIIMBmjwAX_F-GAQ\\u0026_nc_ad\\u003dz-m\\u0026_nc_cid\\u003d0\\u0026_nc_ht\\u003dscontent-sjc3-1.xx\\u0026oh\\u003d00_AT9kfFigFQTV8JLIplhpWS4Y4OSoE5lvxLqQt4ztmo5PRQ\\u0026oe\\u003d622876F0\",\"business_story\":\"\",\"classify_message\":\"\",\"com_from\":\"blogger\",\"company_profile\":\"\",\"country_region\":\"China|中国\",\"country_region_city\":\"\",\"detailed_summary\":\"\",\"favorite_quotes\":\"\",\"found\":\"2010年5月21日\",\"gender_orientation\":\"\",\"go_through\":\"\",\"like_number_int\":\"\",\"local_profile_pic_background_url\":\"\",\"media_title\":\"头像|背景图\",\"media_type_embeded\":\"P|P\",\"media_url\":\"https://scontent-sjc3-1.xx.fbcdn.net/v/t39.30808-1/242579847_227562042745068_5573555501940023031_n.jpg?stp\\u003dcp0_dst-jpg_e15_fr_q65\\u0026_nc_cat\\u003d104\\u0026ccb\\u003d1-5\\u0026_nc_sid\\u003dc6021c\\u0026_nc_ohc\\u003d_wo2E9voadsAX-HJssY\\u0026_nc_ad\\u003dz-m\\u0026_nc_cid\\u003d0\\u0026_nc_ht\\u003dscontent-sjc3-1.xx\\u0026oh\\u003d00_AT8fSoES3CBriFODSgsZeuewHPyalUuA2qO64dcQdkdF3g\\u0026oe\\u003d6227913F|https://scontent-sjc3-1.xx.fbcdn.net/v/t39.30808-6/242615571_227562046078401_7075016216550821750_n.jpg?stp\\u003dcp0_dst-jpg_e15_fr_q65\\u0026_nc_cat\\u003d105\\u0026ccb\\u003d1-5\\u0026_nc_sid\\u003de3f864\\u0026_nc_ohc\\u003dD7ZbIIMBmjwAX_F-GAQ\\u0026_nc_ad\\u003dz-m\\u0026_nc_cid\\u003d0\\u0026_nc_ht\\u003dscontent-sjc3-1.xx\\u0026oh\\u003d00_AT9kfFigFQTV8JLIplhpWS4Y4OSoE5lvxLqQt4ztmo5PRQ\\u0026oe\\u003d622876F0\",\"media_url_name\":\"fb_info_100064738204514_photo_0_.jpg|fb_info_100064738204514_banner_0_.jpg\",\"opening_hours\":\"\",\"personal_web_url\":\"http://www.MurrietaCA.gov/\",\"photo_album_url\":\"\",\"photo_wall\":\"\",\"position_message\":\"\",\"register_number\":\"\",\"registration_date\":\"\",\"related_home_page\":\"\",\"shop_content\":\"\",\"family_and_relation_ships\":\"\",\"skill\":\"\",\"user_birthday\":\"\",\"user_classify\":\"政府机构\",\"user_description\":\"\",\"follower_number_int\":\"\",\"uuid\":\"b97cb952-8f5a-48cb-a2a3-c4e4ae01626b\",\"platform\":\"FB\",\"data_source\":\"fb_20220726120408_00\",\"create_time\":\"2022-07-26 12:04:09\",\"importance\":\"0\",\"remark\":\"\",\"language_type\":\"en\",\"source_id\":\"56858\",\"user_id\":\"100064738204514\",\"screen_name\":\"CityofMurrieta\",\"use_name\":\"City of Murrieta - City Government\",\"user_url\":\"https://www.facebook.com/profile.php?id\\u003d100064738204514\",\"user_avatar\":\"https://scontent-sjc3-1.xx.fbcdn.net/v/t39.30808-1/242579847_227562042745068_5573555501940023031_n.jpg?stp\\u003dcp0_dst-jpg_e15_fr_q65\\u0026_nc_cat\\u003d104\\u0026ccb\\u003d1-5\\u0026_nc_sid\\u003dc6021c\\u0026_nc_ohc\\u003d_wo2E9voadsAX-HJssY\\u0026_nc_ad\\u003dz-m\\u0026_nc_cid\\u003d0\\u0026_nc_ht\\u003dscontent-sjc3-1.xx\\u0026oh\\u003d00_AT8fSoES3CBriFODSgsZeuewHPyalUuA2qO64dcQdkdF3g\\u0026oe\\u003d6227913F\",\"local_photo_url\":\"fb_Info_100064738204514_photo.jpg\",\"gender\":\"未知\",\"country\":\"China|中国\",\"city\":\"\",\"user_type\":\"个人账号\",\"verified\":\"非认证\",\"followers_count\":\"0\",\"friend_count\":\"0\",\"post_count\":\"0\",\"like_count\":\"0\",\"source_create_time\":\"2022-07-26 12:04:09\",\"mobile\":\"\",\"email\":\"\",\"name_userd_before\":\"\",\"language\":\"en\",\"user_religion\":\"\",\"works\":\"\",\"location\":\"\",\"marriage\":\"\",\"home_town\":\"\",\"user_summary\":\"\",\"integrity\":23,\"impl_or_history_type\":\"imp\"}";
        FbUserData_v2 fbUserData_v2 = JacksonUtil.strToBean(str, FbUserData_v2.class);
        System.out.println(fbUserData_v2);



    }
}
