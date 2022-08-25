package com.acat.handleBlogData;

import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import com.acat.handleBlogData.dao.CountryDao;
import com.acat.handleBlogData.domain.entity.BlogSystemCountryDataEntity;
import com.acat.handleBlogData.util.DingTalkUtil;
import com.google.common.collect.Maps;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
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
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.AcknowledgedResponse;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private CountryDao countryDao;
    @Value("${spring.profiles.active}")
    private String env;
    @Value("${spring.max_result_window}")
    private Integer max_result_window;

    private static final List<String> fieldList = Lists.newArrayList("台湾", "香港", "澳门", "中国台湾", "中国香港", "中国澳门");
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
////        SearchSourceBuilder builder = new SearchSourceBuilder()
////                .query(QueryBuilders.matchQuery("uuid", "ed8badcc-19a9-4fb1-a8a7-a58fecc7d643"));
//
////        SearchSourceBuilder builder = new SearchSourceBuilder()
////                .query(QueryBuilders.queryStringQuery("glas").field("use_name"));
//
//        SearchSourceBuilder builder = new SearchSourceBuilder()
//                .query(QueryBuilders.wildcardQuery("use_name","*glas*"));
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

//    @Test
//    public void test07() {
//        List<String> list = Lists.newArrayList(
//                "ddengle003",
//                "NEDonBoard",
//                "Christian Theological Seminary",
//                "Christ School",
//                "Loughborough University",
//                "John XXIII College | Perth",
//                "Wright State University",
//                "Queen Mary University of London",
//                "Eastern Michigan University",
//                "Harvard Extension School",
//                "The British School of Paris",
//                "Polytechnic Institute of Setúbal",
//                "William & Mary",
//                "Botho University",
//                "KCC INSTITUTE OF TECHNOLOGY AND MANAGEMENT",
//                "cs2i Bourgogne - Ecole Supérieure d'Informatique",
//                "Lackawanna College",
//                "AIM WA",
//                "Indian Institute for Human Settlements",
//                "Converse University",
//                "tbsc-institute",
//                "hebrew-union-college",
//                "College of the Sequoias",
//                "Comp Lair™",
//                "frankfurtschool",
//                "Universidad ICESI",
//                "university-of-nottingham-ningbo-china",
//                "UNC School of Education",
//                "Renmin University of China",
//                "escola-superior-de-ecommerce",
//                "Trinity College Dublin",
//                "gateway-community-college_2",
//                "42heilbronn",
//                "UT Southwestern Medical Center",
//                "tekedia",
//                "stony-brook-shtm",
//                "enae-business-school",
//                "spelman-college_2",
//                "university-of-buckingham",
//                "frontier-nursing-university",
//                "lancastertheologicalseminary",
//                "INACAP",
//                "stocktonuniversity",
//                "GenCo Legal®",
//                "rotella-legal-group-p-a-",
//                "DeepTarget Inc.",
//                "artfusion",
//                "cole-schotz-p-c-",
//                "uk-department-for-education",
//                "coraphysicaltherapy",
//                "ls-lexjus-sinacta",
//                "LS Lexjus Sinacta",
//                "SESAR 3 Joint Undertaking",
//                "We are now ASBN - Follow our new page linked in the tagline",
//                "ASTEC",
//                "Guru.com",
//                "i-com-global",
//                "McGill immobilier | McGill Real Estate",
//                "Siegel+Gale",
//                "LeTort Trust",
//                "aurigo-software-technologies",
//                "ls2g",
//                "LS2group",
//                "Cedar Falls Tourism & Visitors Bureau",
//                "MCL IT GmbH",
//                "BRONX Fashion B.V.",
//                "CryptoStopper™",
//                "bnp-paribas-switzerland",
//                "Wheeler Trigg O'Donnell LLP",
//                "advance-digital-graphics-ltd",
//                "penner-international",
//                "Linkfluence - a Meltwater company",
//                "censa---council-for-emerging-national-security-affairs",
//                "c21-media",
//                "es-relazioni-istituzionali-&-comunicazione",
//                "Bryant Miller Olive P.A.",
//                "Laboratorios LAM",
//                "Portland Business Journal",
//                "ajev",
//                "ccsfundraising",
//                "B.E. Smith",
//                "CM&B Inc.",
//                "berkeley-research-group-llc",
//                "B&B TOOLS",
//                "ymca-of-greater-providence",
//                "southlight-inc.",
//                "127-worldwide",
//                "MEDI COMP",
//                "ADR India & MyNeta",
//                "usaging",
//                "massachusetts-life-sciences-center",
//                "eastridge-workforce-solutions",
//                "ambler-savings-bank",
//                "thekidzclub",
//                "northholm-grammar-school-ltd",
//                "3d-perception",
//                "USPCA",
//                "ummedicalsystem",
//                "castlight-health",
//                "ATEM-Polska Sp. z o.o."
//        );
//        System.out.println(list.size());
//        Map<String, String> resultMap = Maps.newHashMap();
//        list.forEach(e -> {
//            String result =  translateOuterService.getTranslateValue("en", e);
//            resultMap.put(e, result);
//        });
//        System.out.println(JacksonUtil.beanToStr(resultMap));
//    }
//
//    @Test
//    public void test08() {
//
//        List<String> list = Lists.newArrayList(
//                "당신이 가자",
////                "금기",
//                "사탕",
//                "의 꿈",
//                "인기",
//                "세요",
//                "햇볕과너",
//                "미래",
//                "거짓말",
//                "제멋대로",
//                "추억",
//                "슈퍼맨",
//                "옛정",
//                "분홍",
//                "더 이상 만 나",
//                "마음 만약 양지",
//                "이야기 너와",
//                "나 안 이야기",
//                "마치 어제",
//                "뒷모습",
//                "방종하다",
//                "낯익다",
//                "손잡고먼곳",
//                "차가운중가",
//                "밤하늘 의 크리스털",
//                "사랑한다",
//                "사랑해",
//                "외로운 환자",
//                "사람",
//                "오래 된 유령",
//                "미쳤어",
//                "나 잊지 마",
//                "좋아해요",
//                "사랑 기침",
//                "그 는 그녀 와",
//                "나 중 에",
//                "卞白贤",
//                "마음에 들어",
//                "한 종이 난 언",
//                "듣는편지",
//                "김종대RLA+WHD+EO",
//                "말없는",
//                "엑소",
//                "치유 자",
//                "레이",
//                "풋내기",
//                "하루하루",
//                "안다싫증나다",
//                "루 한, 당신은 지출하지 않았다",
//                "그레이 사진",
//                "저 를",
//                "소탈하다",
//                "얼음이없는 레몬 슬라이스",
//                "김민선",
//                "변백현",
//                "떠 나",
//                "닿지않는다",
//                "안 정 감",
//                "싱크대",
//                "범인",
//                "김종인",
//                "무지개 의 미",
//                "가시 돋친 어떻게 둘러싼다",
//                "따뜻한 내",
//                "장대한",
//                "피 노 키 오",
//                "땡글",
//                "무금반",
//                "단어뜻단어뜻",
//                "물건",
//                "탁자",
//                "자전",
//                "의자",
//                "신문 알리다",
//                "시계 시간",
//                "잡지",
//                "시계겉",
//                "글자",
//                "안경",
//                "문장",
//                "우산",
//                "그림",
//                "치약",
//                "색깔",
//                "성냥",
//                "라이타",
//                "노임,임금",
//                "급료,월급",
//                "상자",
//                "지역,곳,장소",
//                "텔레비젼",
//                "전부",
//                "라디오",
//                "냉장고",
//                "방향",
//                "선풍기",
//                "세계",
//                "에어콘",
//                "국가",
//                "세탁기",
//                "에스카레이터"
////
////　　70. 计算机(dian nao)컴퓨터 乡下(xiang xia)시골.
////
////　　71. 照相机(zhao xiang ji)사진기 城市(cheng shi)도시
////
////　　72. 电话(dian hua)전화 街(jie)거리,길
////
////　　73. 电报(dian bao)전보 道(dao)도로,길
////
////　　74. 信(xin)편지 路(lu)길,도로
////
////　　75. 邮票(you piao)우표 桥(qiao)다리
////
////　　76. 明信片(ming xin pian)엽서 樓(lou)건물,층
////
////　　77. 名片(ming pian)명함 工厂(gongchang)공장
////
////　　78. 照片(zhao pian)사진 邮局(you ju)우체국
////
////　　79. 自行车(zi xing che )자전거 银行(yin hang)은행
////
////　　80. 汽车(qi che)자동차 公司(gong si)회사
////
////　　81. 公共汽车(gonggongqi che)버스 市场(shi chang)시장
////
////　　82. 出租车(chu zu che)택시 铺子(pu zi)가게,상점
////
////　　83. 火车(huo che)기차 饭店(fan dian)호텔,여관
////
////　　84. 地下铁(di xia tie)지하철 公园(gong yuan)공원
////
////　　85. 船(chuan)배 工程(gong cheng)공사,공정
////
////　　86. 飞机(fei ji)비행기 故事(gu shi)이야기,고사
////
////　　87. 交通(jiao tong)교통 电影(dian ying)영화
////
////　　88. 旅行(lu xing)여행 话剧(hua ju)연극
////
////　　89. 车站(che zhan)정거장，역 学校(xue xiao)학교
////
////　　90. 票(piao)표 敎室(jiao shi)교실
////
////　　91. 门票(men piao)입장권 图书馆(tu shu guan)도서관
////
////　　92. 纸(zhi)종이 黑板(hei ban)칠판
////
////　　93. 铅笔(qian bi)연필 功课(gong ke)과목,학습
////
////　　94. 毛笔(mao bi)붓 考试(kao shi)시험
////
////　　95. 分笔fen bi)분필 事情(shi qing)일,사건
////
////　　96. 刚笔(gang bi)만년필 方法(fang fa)방법
////
////　　97. 圆珠笔(yuan zhu bi)볼펜 情形(qing xing)일의상황,정황,형편
////
////　　98. 墨水(mo shui)먹물,잉크 新闻(xin wen)뉴스
////
////　　99. 단어뜻단어뜻
////
////　　100. 毛病(mao bing)질병,약점,나쁜버릇 将来(jiang lai)장래,미래
//
//
//        );
//
////        System.out.println(list.size());
//
////        String str = "땡글";
//
//        Map<String, String> resultMap = new HashMap<>();
//        list.forEach(e -> {
//            String result = translateOuterService.getLanguageDelectResult(e);
//            resultMap.put(e, result);
//        });
////        String result = translateOuterService.getLanguageDelectResult(str);
////        System.out.println("result:" + result);
//        System.out.println(JacksonUtil.beanToStr(resultMap));
//    }


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
//        String qName = "Katie Bethea";
//        String languageType = translateOuterService.getLanguageDelectResult(qName);
//        if ("zh".equals(languageType)) {
//            System.out.println(qName.trim());
//        }else if ("en".equals(languageType) || "vi".equals(languageType)) {
//            System.out.println(CountryUtil.handleStr(qName));
//        }else {
//            System.out.println(qName);
//        }
    }

    @Test
    public void test13() {
//        redisService.push("aaa", "111", true);
//        List list = redisService.range("country", 0L, -1L);
//        list.forEach(e-> {
//            System.out.println(e);
//        });
//        String str = "{\n" +
//                "    \"userFacebookUrl\":\"https://www.facebook.com/profile.php?id=100025599426924\",\n" +
//                "    \"userFormerNickname\":\"\",\n" +
//                "    \"userId\":\"100025599426924\",\n" +
//                "    \"userFormerName\":\"PatelRakesh Amrut\",\n" +
//                "    \"userFormerNameDate\":\"2022-04-05 17:34:20\"\n" +
//                "}";
//
//        Map<String, Objects> map = JacksonUtil.strToBean(str, Map.class);
//        System.out.println(map);
    }

    @Test
    public void test14() {
//        System.out.println(redisService.getValue(RedisKeyConstants.TWITTER_PRO_GOV_NUM_KEY));
//        String str = "{\"user_political_views\":\"\",\"user_systent_name\":\"\",\"w3_fb_url\":\"\",\"institution_id\":\"\",\"is_community_page\":\"\",\"communication_philosophy\":\"\",\"have_product\":\"\",\"exchange_number\":\"\",\"visit_number\":\"\",\"first_name\":\"\",\"last_name\":\"\",\"teach_message\":\"\",\"acquisition_time\":\"2022-03-05 18:54:01\",\"affective_state\":\"\",\"background_picture_url\":\"https://scontent-sjc3-1.xx.fbcdn.net/v/t39.30808-6/242615571_227562046078401_7075016216550821750_n.jpg?stp\\u003dcp0_dst-jpg_e15_fr_q65\\u0026_nc_cat\\u003d105\\u0026ccb\\u003d1-5\\u0026_nc_sid\\u003de3f864\\u0026_nc_ohc\\u003dD7ZbIIMBmjwAX_F-GAQ\\u0026_nc_ad\\u003dz-m\\u0026_nc_cid\\u003d0\\u0026_nc_ht\\u003dscontent-sjc3-1.xx\\u0026oh\\u003d00_AT9kfFigFQTV8JLIplhpWS4Y4OSoE5lvxLqQt4ztmo5PRQ\\u0026oe\\u003d622876F0\",\"business_story\":\"\",\"classify_message\":\"\",\"com_from\":\"blogger\",\"company_profile\":\"\",\"country_region\":\"China|中国\",\"country_region_city\":\"\",\"detailed_summary\":\"\",\"favorite_quotes\":\"\",\"found\":\"2010年5月21日\",\"gender_orientation\":\"\",\"go_through\":\"\",\"like_number_int\":\"\",\"local_profile_pic_background_url\":\"\",\"media_title\":\"头像|背景图\",\"media_type_embeded\":\"P|P\",\"media_url\":\"https://scontent-sjc3-1.xx.fbcdn.net/v/t39.30808-1/242579847_227562042745068_5573555501940023031_n.jpg?stp\\u003dcp0_dst-jpg_e15_fr_q65\\u0026_nc_cat\\u003d104\\u0026ccb\\u003d1-5\\u0026_nc_sid\\u003dc6021c\\u0026_nc_ohc\\u003d_wo2E9voadsAX-HJssY\\u0026_nc_ad\\u003dz-m\\u0026_nc_cid\\u003d0\\u0026_nc_ht\\u003dscontent-sjc3-1.xx\\u0026oh\\u003d00_AT8fSoES3CBriFODSgsZeuewHPyalUuA2qO64dcQdkdF3g\\u0026oe\\u003d6227913F|https://scontent-sjc3-1.xx.fbcdn.net/v/t39.30808-6/242615571_227562046078401_7075016216550821750_n.jpg?stp\\u003dcp0_dst-jpg_e15_fr_q65\\u0026_nc_cat\\u003d105\\u0026ccb\\u003d1-5\\u0026_nc_sid\\u003de3f864\\u0026_nc_ohc\\u003dD7ZbIIMBmjwAX_F-GAQ\\u0026_nc_ad\\u003dz-m\\u0026_nc_cid\\u003d0\\u0026_nc_ht\\u003dscontent-sjc3-1.xx\\u0026oh\\u003d00_AT9kfFigFQTV8JLIplhpWS4Y4OSoE5lvxLqQt4ztmo5PRQ\\u0026oe\\u003d622876F0\",\"media_url_name\":\"fb_info_100064738204514_photo_0_.jpg|fb_info_100064738204514_banner_0_.jpg\",\"opening_hours\":\"\",\"personal_web_url\":\"http://www.MurrietaCA.gov/\",\"photo_album_url\":\"\",\"photo_wall\":\"\",\"position_message\":\"\",\"register_number\":\"\",\"registration_date\":\"\",\"related_home_page\":\"\",\"shop_content\":\"\",\"family_and_relation_ships\":\"\",\"skill\":\"\",\"user_birthday\":\"\",\"user_classify\":\"政府机构\",\"user_description\":\"\",\"follower_number_int\":\"\",\"uuid\":\"b97cb952-8f5a-48cb-a2a3-c4e4ae01626b\",\"platform\":\"FB\",\"data_source\":\"fb_20220726120408_00\",\"create_time\":\"2022-07-26 12:04:09\",\"importance\":\"0\",\"remark\":\"\",\"language_type\":\"en\",\"source_id\":\"56858\",\"user_id\":\"100064738204514\",\"screen_name\":\"CityofMurrieta\",\"use_name\":\"City of Murrieta - City Government\",\"user_url\":\"https://www.facebook.com/profile.php?id\\u003d100064738204514\",\"user_avatar\":\"https://scontent-sjc3-1.xx.fbcdn.net/v/t39.30808-1/242579847_227562042745068_5573555501940023031_n.jpg?stp\\u003dcp0_dst-jpg_e15_fr_q65\\u0026_nc_cat\\u003d104\\u0026ccb\\u003d1-5\\u0026_nc_sid\\u003dc6021c\\u0026_nc_ohc\\u003d_wo2E9voadsAX-HJssY\\u0026_nc_ad\\u003dz-m\\u0026_nc_cid\\u003d0\\u0026_nc_ht\\u003dscontent-sjc3-1.xx\\u0026oh\\u003d00_AT8fSoES3CBriFODSgsZeuewHPyalUuA2qO64dcQdkdF3g\\u0026oe\\u003d6227913F\",\"local_photo_url\":\"fb_Info_100064738204514_photo.jpg\",\"gender\":\"未知\",\"country\":\"China|中国\",\"city\":\"\",\"user_type\":\"个人账号\",\"verified\":\"非认证\",\"followers_count\":\"0\",\"friend_count\":\"0\",\"post_count\":\"0\",\"like_count\":\"0\",\"source_create_time\":\"2022-07-26 12:04:09\",\"mobile\":\"\",\"email\":\"\",\"name_userd_before\":\"\",\"language\":\"en\",\"user_religion\":\"\",\"works\":\"\",\"location\":\"\",\"marriage\":\"\",\"home_town\":\"\",\"user_summary\":\"\",\"integrity\":23,\"impl_or_history_type\":\"imp\"}";
//        FbUserData_v2 fbUserData_v2 = JacksonUtil.strToBean(str, FbUserData_v2.class);
//        System.out.println(fbUserData_v2);
    }

//    @Test
//    public void test15() throws Exception{
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.should(
//                QueryBuilders.rangeQuery("source_create_time.keyword").format("yyyy-MM-dd HH:mm:ss").gte("2021-01-01").lte("2021-12-30")
//        );
//
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(boolQueryBuilder);
//        sourceBuilder.from(0).size(10);
//        sourceBuilder.trackTotalHits(true);
//        sourceBuilder.sort("integrity", SortOrder.DESC);
//
//        SearchRequest searchRequest = new SearchRequest();
////        if (!judgeSearchParamAllEmpty(searchReq)) {
////            searchRequest.indices(indexArray_v2);
////        }else {
//            searchRequest.indices("twitter_v2");
////        }
//        searchRequest.types("_doc");
//        searchRequest.source(sourceBuilder);
//        SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
//        if (response == null) {
//            System.out.println();
//        }
//    }

//    @Test
//    public void test17() throws Exception {
//
//        BoolQueryBuilder bigBuilder = QueryBuilders.boolQuery();
//        BoolQueryBuilder channelQueryBuilder = new BoolQueryBuilder();
//        for(String fieldValue: fieldList){
//            channelQueryBuilder.should(QueryBuilders.matchQuery("country", fieldValue));
//        }
//        bigBuilder.must(channelQueryBuilder);
//
//        SearchSourceBuilder builder = new SearchSourceBuilder()
//                .query(bigBuilder)
//                .trackTotalHits(true);
//
//
//        //搜索
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices("twitter_v2");
//        searchRequest.types("_doc");
//        searchRequest.source(builder);
//        // 执行请求
//        SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
//        if (response == null) {
//
//        }
//
//        SearchHit[] searchHits = response.getHits().getHits();
//        if (CollectionUtils.isEmpty(Arrays.stream(searchHits).collect(Collectors.toList()))) {
//            return;
//        }
//
////            Arrays.stream(searchHits).collect(Collectors.toList()).forEach(e -> {
////
////                Map map = new HashMap();
////                map.put("country", "中国");
////                UpdateRequest updateRequest = new UpdateRequest("twitter_v2", e.getId()).doc(map);
////                restHighLevelClient.update(updateRequest, toBuilder());
//////
////            });
//        for (SearchHit documentFields : Arrays.stream(searchHits).collect(Collectors.toList())) {
//            Map map = new HashMap();
//            map.put("country", "中国");
//            UpdateRequest updateRequest = new UpdateRequest("twitter_v2", documentFields.getId()).doc(map);
//            restHighLevelClient.update(updateRequest, toBuilder());
//        }
//    }

    @Test
    public void test18() throws Exception{
//        CountRequest countRequest = new CountRequest();
//        countRequest.indices("twitter_v4", "instagram_v4", "fb_v4");
////        countRequest.query();
//        CountResponse countResponse = restHighLevelClient.count(countRequest, toBuilder());
//        System.out.println((countResponse.getCount()));
//
//
//
//
//        GetIndexRequest request = new GetIndexRequest("twitter_v4", "instagram_v4");
//        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(request, toBuilder());
//        System.out.println(JacksonUtil.beanToStr(getIndexResponse.getAliases()));
//        System.out.println(JacksonUtil.beanToStr(getIndexResponse.getIndices()));
//        System.out.println(JacksonUtil.beanToStr(getIndexResponse.getMappings()));
//        System.out.println(JacksonUtil.beanToStr(getIndexResponse.getSettings()));
//        System.out.println(JacksonUtil.beanToStr(getIndexResponse.getDefaultSettings()));
//
////        GetSettingsRequest request = new GetSettingsRequest();
////        GetAliasesResponse getAliasesResponse =  restHighLevelClient.indices().getSettings(request, RequestOptions.DEFAULT);
////        Map<String, Set<AliasMetadata>> map = getAliasesResponse.getAliases();
////        Set<String> indices = map.keySet();
////        for (String key : indices) {
////            System.out.println(key);
////        }
    }

    @Test
    public void test19() throws Exception{
//        CollapseBuilder collapseBuilder = new CollapseBuilder("country.keyword");
//        SearchSourceBuilder builder = new SearchSourceBuilder()
//                .query(QueryBuilders.matchAllQuery())
//                .fetchSource("country", null)
//                .collapse(collapseBuilder)
//                .trackTotalHits(true)
//                .from(0).size(10000);
//
//        //搜索
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.indices(indexArray_v2);
//        searchRequest.types("_doc");
//        searchRequest.source(builder);
//        // 执行请求
//        SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
//        if (response == null) {
//            log.info("");
//        }
//        System.out.println("ababababa");
    }

    @Test
    public void test20() {
//        System.out.println(env);
//        System.out.println(max_result_window);
    }

    @Test
    public void test21() {
//        List<String> countryList = Lists.newArrayList(
//                "印度",
//                "美国",
//                "巴西",
//                "阿根廷",
//                "意大利",
//                "墨西哥",
//                "中国",
//                "俄罗斯",
//                "越南",
//                "马来西亚",
//                "德国",
//                "法国",
//                "斯洛文尼亚",
//                "捷克",
//                "印度尼西亚",
//                "日本",
//                "丹麦",
//                "挪威",
//                "芬兰",
//                "克罗地亚",
//                "波兰",
//                "泰国",
//                "卡斯提尔",
//                "保加利亚",
//                "葡萄牙",
//                "爱沙尼亚",
//                "罗马尼亚",
//                "荷兰",
//                "爱尔兰",
//                "土耳其",
//                "瑞典",
//                "阿拉伯",
//                "韩国",
//                "波斯",
//                "匈牙利",
//                "斯洛伐克",
//                "希腊",
//                "乌克兰",
//                "斯瓦希里",
//                "奧克",
//                "阿尔巴尼亚",
//                "北印度",
//                "索马里",
//                "孟加拉",
//                "高棉",
//                "加利西亚",
//                "马耳他",
//                "巴斯克",
//                "塔加拉",
//                "塞尔维亚",
//                "布列塔尼",
//                "威尔士",
//                "拉脱维亚",
//                "立陶宛",
//                "冰岛",
//                "海地克里奥尔",
//                "希伯来",
//                "加泰罗尼亚",
//                "旁遮普",
//                "泰米尔",
//                "马拉地",
//                "尼泊尔",
//                "乌尔都",
//                "马其顿",
//                "中国台湾",
//                "卡塔尔",
//                "菲律宾",
//                "多米尼加联邦",
//                "英国",
//                "中国香港",
//                "加拿大",
//                "斯里兰卡",
//                "新加坡",
//                "澳大利亚",
//                "瑞士",
//                "埃及",
//                "巴拉圭",
//                "南非",
//                "新西兰",
//                "土尔其",
//                "比利时",
//                "西班牙",
//                "以色列",
//                "哈萨克斯坦",
//                "科威特",
//                "阿拉伯联合酋长国",
//                "孟加拉国",
//                "老挝",
//                "摩洛哥",
//                "巴哈马",
//                "智利",
//                "柬埔寨",
//                "伊拉克",
//                "格鲁吉亚",
//                "巴林",
//                "塞舌尔",
//                "肯尼亚",
//                "亚美尼亚",
//                "巴基斯坦",
//                "文莱布鲁萨兰",
//                "冈比亚",
//                "沙特阿拉伯王国",
//                "伊朗",
//                "哥斯达黎加",
//                "波多黎各",
//                "尼日利亚",
//                "秘鲁",
//                "哥伦比亚",
//                "吉尔吉斯斯坦",
//                "塞普路斯",
//                "尼加拉瓜",
//                "突尼斯",
//                "威尔士公国",
//                "捷克共和国",
//                "坦桑尼亚",
//                "北马其顿",
//                "印度卡纳塔克邦",
//                "摩尔多瓦",
//                "奥地利",
//                "乌拉圭",
//                "特立尼达和多巴哥",
//                "加纳",
//                "约旦",
//                "中国澳门",
//                "卢森堡",
//                "塞内加尔",
//                "马拉维",
//                "多哥",
//                "不丹",
//                "安哥拉",
//                "巴布亚新几内亚",
//                "刚果(金)",
//                "厄瓜多尔",
//                "列支顿士登",
//                "莫桑比克",
//                "阿尔及利亚",
//                "马达加斯加",
//                "萨尔瓦多",
//                "伯里兹",
//                "百慕大",
//                "阿富汗",
//                "卢旺达",
//                "斯罗文尼亚",
//                "波黑",
//                "泽西岛",
//                "多米尼加共和国",
//                "斯威士兰",
//                "博茨瓦纳",
//                "苏丹",
//                "蒙古",
//                "乌干达",
//                "荷属安德列斯",
//                "毛里求斯",
//                "委内瑞拉",
//                "摩纳哥",
//                "洪都拉斯",
//                "阿塞拜疆",
//                "黎巴嫩",
//                "瓦努阿鲁",
//                "刚果(布)",
//                "圣基茨和尼维斯",
//                "圣卢西亚",
//                "斐济",
//                "喀麦隆",
//                "纳米比亚",
//                "危地马拉",
//                "玻利维亚",
//                "开曼群岛",
//                "白俄罗斯",
//                "古巴",
//                "圣多美和普林西比",
//                "圭亚那",
//                "巴巴多斯",
//                "英属维京群岛",
//                "乌兹别克斯坦",
//                "牙买加",
//                "阿曼",
//                "赞比亚",
//                "利比里亚",
//                "贝宁",
//                "直布罗陀",
//                "马里",
//                "几内亚",
//                "所罗门群岛",
//                "法罗群岛",
//                "格陵兰岛",
//                "捷克斯洛伐克",
//                "苏里南",
//                "毛里塔尼亚",
//                "布基纳法索",
//                "中非",
//                "加蓬",
//                "埃塞俄比亚",
//                "马尔代夫",
//                "佛得角",
//                "法属尼留旺岛",
//                "缅甸",
//                "海地",
//                "科摩罗",
//                "布隆迪",
//                "赤道几内亚",
//                "安提瓜和巴布达",
//                "库克群岛",
//                "莱索托",
//                "乍得",
//                "关岛",
//                "塞拉利昂",
//                "新卡里多尼亚",
//                "吉布提",
//                "利比亚",
//                "法属德洛普群岛",
//                "塔吉克斯坦",
//                "叙利亚",
//                "圣文森特和格陵纳丁斯",
//                "圣马力诺",
//                "美属维京群岛",
//                "格林纳达",
//                "尼日尔",
//                "几内亚比绍",
//                "马拉雅拉姆",
//                "坎纳达",
//                "泰卢固",
//                "瓜拉尼",
//                "意第绪");
//
//        List<BlogSystemCountryDataEntity> list = Lists.newArrayList();
//        countryList.forEach(e -> {
//            BlogSystemCountryDataEntity blogSystemCountryDataEntity = new BlogSystemCountryDataEntity();
//            blogSystemCountryDataEntity.setCountry(e);
//            list.add(blogSystemCountryDataEntity);
//        });
//        countryDao.saveAll(list);
    }


    private RequestOptions toBuilder() {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(5000 * 1024 * 1024));
        return builder.build();
    }
}
