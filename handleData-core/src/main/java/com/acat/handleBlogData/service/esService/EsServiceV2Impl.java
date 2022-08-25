package com.acat.handleBlogData.service.esService;

import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.controller.req.SearchDetailReq;
import com.acat.handleBlogData.controller.req.SearchReq;
import com.acat.handleBlogData.controller.resp.*;
import com.acat.handleBlogData.dao.CountryDao;
import com.acat.handleBlogData.domain.entity.BlogSystemCountryDataEntity;
import com.acat.handleBlogData.enums.*;
import com.acat.handleBlogData.service.redisService.RedisServiceImpl;
import com.acat.handleBlogData.util.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EsServiceV2Impl {

    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private RedisServiceImpl redisService;
    @Resource
    private CountryDao countryDao;

    @Value("${spring.profiles.active}")
    private String env;
    @Value("${spring.max_result_window}")
    private Integer max_result_window;

    private static final String PRO_PIC_URL = "https://20.10.0.11:9002/gateway/api-file/file/download?fileName=";
    private static final String PROD_PIC_URL = "http://big-data-project-department.dc.gtcom.prod/big-data-project-department/fb/info/";

    //redis->key
    public static final String COUNTRY_KEY = "country_v4";
    public static final String CITY_KEY = "city_v4";
    public static final String INTEGRITY_KEY = "integrity_v3";

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

    /**
     * 跑脚本list
     */
    private static final List<String> fieldList_one = Lists.newArrayList("台湾","香港","澳门","中国台湾","中国香港","中国澳门");
    private static final List<String> fieldList_taiwan = Lists.newArrayList("台湾");


    /**
     * 搜索查询
     * https://www.csdn.net/tags/MtTaEgxsNzk1ODAwLWJsb2cO0O0O.html
     * @param searchReq
     * @return
     */
    public RestResult<SearchResp> searchData(SearchReq searchReq) {
        try {
            Integer pageSize = searchReq.getPageSize();
            Integer pageNum = searchReq.getPageNum();
            if (pageSize * pageNum > max_result_window) {
                return new RestResult<>(RestEnum.FIELD_NOT_SUPPORT_DIM_SEARCH,  "分页查询只支持前" + max_result_window/pageSize + "页数据,或请进行条件查询！！！");
            }

            if (searchReq.getIsParticiple() == null) {
                searchReq.setIsParticiple(1);
            }

            //youhua -> add
            if (judgeParamIsEmpty(searchReq)) {
                SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
                sourceBuilder.query(QueryBuilders.boolQuery());
                sourceBuilder.from((pageNum > 0 ? (pageNum - 1) : 0) * pageSize).size(pageSize);
                sourceBuilder.trackTotalHits(true);
                sourceBuilder.sort("integrity", SortOrder.DESC);

                SearchRequest searchRequest = new SearchRequest();
                searchRequest.indices(MediaSourceEnum.LINKEDIN_SCHOOL.getEs_index_v2());
                searchRequest.types("_doc");
                searchRequest.source(sourceBuilder);
                SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
                if (response == null) {
                    return new RestResult<>(RestEnum.PLEASE_TRY);
                }

                SearchHits searchHits = response.getHits();
                if (searchHits == null || searchHits.getHits() == null) {
                    return new RestResult<>(RestEnum.FIELD_NOT_SUPPORT_DIM_SEARCH,
                            "您好,此搜索条件会存在超时风险,请更换搜索条件,系统正在持续优化中ing！！！");
                }
                return new RestResult<>(RestEnum.SUCCESS, assembleResult(response, true));
            }

            if (searchReq.getIsParticiple().equals(1)
                    && StringUtils.isNotBlank(searchReq.getUserSummary())) {
                return new RestResult<>(RestEnum.FIELD_NOT_SUPPORT_DIM_SEARCH,  "用户简介不支持精准查询,请改为模糊(分词)查询");
            }
            if (searchReq.getIsParticiple().equals(1)
                    && StringUtils.isNotBlank(searchReq.getNameUserdBefore())) {
                return new RestResult<>(RestEnum.FIELD_NOT_SUPPORT_DIM_SEARCH,  "曾用名不支持精准查询,请改为模糊(分词)查询");
            }

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            assembleParam(searchReq, boolQueryBuilder);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.from((pageNum > 0 ? (pageNum - 1) : 0) * pageSize).size(pageSize);
            sourceBuilder.trackTotalHits(true);
            sourceBuilder.sort("integrity", SortOrder.DESC);

            SearchRequest searchRequest = new SearchRequest();
            if (!judgeSearchParamAllEmpty(searchReq)) {
                searchRequest.indices(indexArray_v2);
            }else {
                searchRequest.indices(getEsIndex(searchReq.getMediaType()).stream().toArray(String[]::new));
            }
            searchRequest.types("_doc");
            searchRequest.source(sourceBuilder);
            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }

            SearchHits searchHits = response.getHits();
            if (searchHits == null || searchHits.getHits() == null) {
                return new RestResult<>(RestEnum.FIELD_NOT_SUPPORT_DIM_SEARCH,
                        "您好,此搜索条件会存在超时风险,请更换搜索条件,系统正在持续优化中ing！！！");
            }
            return new RestResult<>(RestEnum.SUCCESS, assembleResult(response, false));
        }catch (Exception e) {
            log.error("EsServiceV2Impl.searchData has error,",e);
            DingTalkUtil.sendDdMessage(assemblingStr(e, "搜索查询接口", searchReq));
            return new RestResult<>(RestEnum.FAILED, "您好,此搜索条件会存在超时风险,请更换搜索条件,系统正在持续优化中ing！！！");
        }
    }

    /**
     * 详情
     */
    public RestResult<UserDetailResp> retrieveUserDetail(SearchDetailReq searchDetailReq) {

        try {
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.termQuery("uuid.keyword", searchDetailReq.getUuid()));

            //搜索
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(MediaSourceEnum.getMediaSourceEnum(searchDetailReq.getMediaCode()).getEs_index_v2());
            searchRequest.types("_doc");
            searchRequest.source(builder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }

            SearchHit hit = response.getHits().getHits()[0];
            if (hit == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }

            UserDetailResp userDetailResp = new UserDetailResp();
            MediaSourceEnum mediaSourceEnum = MediaSourceEnum.getMediaSourceEnum(searchDetailReq.getMediaCode());
            userDetailResp.setMediaSource(MediaTypeResp.builder().code(mediaSourceEnum.getCode()).desc(mediaSourceEnum.getDesc()).build());

            if ("test".equals(env)) {
                userDetailResp.setLocalPhotoUrl(hit.getSourceAsMap().get("local_photo_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("local_photo_url")));
            }else if ("pre".equals(env)) {
                userDetailResp.setLocalPhotoUrl(hit.getSourceAsMap().get("local_photo_url") == null ? "" : PRO_PIC_URL + String.valueOf(hit.getSourceAsMap().get("local_photo_url")));
            }else {
                userDetailResp.setLocalPhotoUrl(hit.getSourceAsMap().get("local_photo_url") == null ? "" : PROD_PIC_URL + String.valueOf(hit.getSourceAsMap().get("local_photo_url")));
            }
            userDetailResp.setUserAvatar(hit.getSourceAsMap().get("user_avatar") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_avatar")));
            userDetailResp.setGender(hit.getSourceAsMap().get("gender") == null ? "" : String.valueOf(hit.getSourceAsMap().get("gender")));
            String userId = hit.getSourceAsMap().get("user_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_id"));
            String userName = hit.getSourceAsMap().get("screen_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("screen_name"));
            userDetailResp.setUserId(userId);
            userDetailResp.setUserName(userName);
            userDetailResp.setUserQuanName(hit.getSourceAsMap().get("use_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("use_name")));
            userDetailResp.setBornTime(hit.getSourceAsMap().get("born_time") == null ? "" : String.valueOf(hit.getSourceAsMap().get("born_time")));
            userDetailResp.setFollowersCount(hit.getSourceAsMap().get("followers_count") == null ? "0" : String.valueOf(hit.getSourceAsMap().get("followers_count")));
            userDetailResp.setFriendCount(hit.getSourceAsMap().get("friend_count") == null ? "0" : String.valueOf(hit.getSourceAsMap().get("friend_count")));
            userDetailResp.setPostCount(hit.getSourceAsMap().get("post_count") == null ? "0" : String.valueOf(hit.getSourceAsMap().get("post_count")));
            userDetailResp.setLikeCount(hit.getSourceAsMap().get("like_count") == null ? "0" : String.valueOf(hit.getSourceAsMap().get("like_count")));
            userDetailResp.setDataId(hit.getSourceAsMap().get("source_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("source_id")));
            userDetailResp.setUserHomePage(hit.getSourceAsMap().get("user_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_url")));
            userDetailResp.setUserType(hit.getSourceAsMap().get("user_type") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_type")));
            userDetailResp.setVerified(hit.getSourceAsMap().get("verified") == null ? "" : String.valueOf(hit.getSourceAsMap().get("verified")));
            userDetailResp.setNameUserdBefore(hit.getSourceAsMap().get("name_userd_before") == null ? "" : String.valueOf(hit.getSourceAsMap().get("name_userd_before")));
            userDetailResp.setMarriage(hit.getSourceAsMap().get("marriage") == null ? "" : String.valueOf(hit.getSourceAsMap().get("marriage")));
            userDetailResp.setCountry(hit.getSourceAsMap().get("country") == null ? "" : String.valueOf(hit.getSourceAsMap().get("country")));
            userDetailResp.setCity(hit.getSourceAsMap().get("city") == null ? "" : String.valueOf(hit.getSourceAsMap().get("city")));
            userDetailResp.setUserReligion(hit.getSourceAsMap().get("user_religion") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_religion")));
            userDetailResp.setPhoneNum(hit.getSourceAsMap().get("mobile") == null ? "" : String.valueOf(hit.getSourceAsMap().get("mobile")));
            userDetailResp.setEmail(hit.getSourceAsMap().get("email") == null ? "" : String.valueOf(hit.getSourceAsMap().get("email")));
            userDetailResp.setWorks(hit.getSourceAsMap().get("works") == null ? "" : String.valueOf(hit.getSourceAsMap().get("works")));
            userDetailResp.setPositionMessage(hit.getSourceAsMap().get("location") == null ? "" : String.valueOf(hit.getSourceAsMap().get("location")));
            userDetailResp.setHomeAddress(hit.getSourceAsMap().get("home_town") == null ? "" : String.valueOf(hit.getSourceAsMap().get("home_town")));
            if (hit.getSourceAsMap().get("language_type") == null) {
                userDetailResp.setLanguage("");
            }else {
                userDetailResp.setLanguage(LanguageUtil.getLanguageName(String.valueOf(hit.getSourceAsMap().get("language_type"))));
            }

            userDetailResp.setUserSummary(hit.getSourceAsMap().get("user_summary") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_summary")));
            userDetailResp.setSourceCreateTime(hit.getSourceAsMap().get("source_create_time") == null ? "" : String.valueOf(hit.getSourceAsMap().get("source_create_time")));


            /******新增字段*******/
            List<BeforeNameInfo> beforeNameInfoList = searchBeforeNameInfoV2(userId, userName);
            if (!CollectionUtils.isEmpty(beforeNameInfoList)) {
                userDetailResp.setBeforeNameInfoList(beforeNameInfoList);
            }else {
                BeforeNameInfo beforeNameInfo = BeforeNameInfo
                        .builder()
                        .userId(userId)
                        .userName(userName)
                        .uuid(hit.getSourceAsMap().get("uuid") == null ? "" : String.valueOf(hit.getSourceAsMap().get("uuid")))
                        .userQuanName(hit.getSourceAsMap().get("use_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("use_name")))
                        .mediaTypeResp(MediaTypeResp.builder().code(mediaSourceEnum.getCode()).desc(mediaSourceEnum.getDesc()).build())
                        .userUrl(hit.getSourceAsMap().get("user_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_url")))
                        .build();
                List<BeforeNameInfo> beforeNameList = Lists.newArrayList(beforeNameInfo);
                userDetailResp.setBeforeNameInfoList(beforeNameList);
            }


            /*****处理原始数据 *****/
            Map<String, Object> newObjectMap = Maps.newHashMap();
            Map<String, Object> stringObjectMap = hit.getSourceAsMap();
            if (!Objects.isNull(stringObjectMap)) {
                for (String key : stringObjectMap.keySet()) {
                    if (StringUtils.isBlank(key)) {
                        continue;
                    }
                    if ("_class".equals(key)) {
                        continue;
                    }
                    newObjectMap.put(
                            FieldUtils.getFieldNameFromZh(key) != null ? FieldUtils.getFieldNameFromZh(key) : key,
                            "impl_or_history_type".equals(key) ? ("imp".equals(stringObjectMap.get(key)) ? "完整属性" : "部分属性") : stringObjectMap.get(key)
                    );
                }
            }
            userDetailResp.setFieldMap(newObjectMap);
            return new RestResult<>(RestEnum.SUCCESS, userDetailResp);
        }catch (Exception e) {
            log.error("EsServiceV2Impl.retrieveUserDetail has error,",e);
            DingTalkUtil.sendDdMessage(assemblingStr(e, "查询详情接口", searchDetailReq));
        }
        return new RestResult<>(RestEnum.FAILED);
    }

    /**
     * 批量搜索
     * @param searchField
     * @param fieldList
     * @param isParticiple
     * @param pageNum
     * @param pageSize
     * @return
     */
    public RestResult<SearchResp> batchQuery(String searchField, List<String> fieldList, Integer isParticiple, MediaSourceEnum mediaSourceEnum, Integer pageNum, Integer pageSize) {
        try {
            if (pageSize * pageNum > max_result_window) {
                return new RestResult<>(RestEnum.FIELD_NOT_SUPPORT_DIM_SEARCH,  "分页查询只支持前" + max_result_window/pageSize + "页数据,或请进行条件查询！！！");
            }

            BoolQueryBuilder bigBuilder = QueryBuilders.boolQuery();
            BoolQueryBuilder channelQueryBuilder = new BoolQueryBuilder();
            for(String fieldValue: fieldList){
                if (isParticiple.equals(1)) {
                    channelQueryBuilder.should(QueryBuilders.termQuery(searchField + ".keyword", fieldValue));
                }else {
                    channelQueryBuilder.should(QueryBuilders.wildcardQuery(searchField + ".keyword", "*"+fieldValue+"*"));
                    channelQueryBuilder.should(QueryBuilders.queryStringQuery("*"+fieldValue+"*").field(searchField + ".keyword"));
                }
            }
            bigBuilder.must(channelQueryBuilder);

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(bigBuilder)
                    .from((pageNum > 0 ? (pageNum - 1) : 0) * pageSize).size(pageSize)
                    .trackTotalHits(true);

            //搜索
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(getEsIndex(mediaSourceEnum.getCode()).stream().toArray(String[]::new));
//            searchRequest.indices(indexArray_v2);
            searchRequest.types("_doc");
            searchRequest.source(builder);

            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }
            return new RestResult<>(RestEnum.SUCCESS, assembleResult(response, false));
        }catch (Exception e) {
            log.error("EsServiceV2Impl.batchQuery has error,",e);

            Map<String, Object> batchMap = Maps.newHashMap();
            batchMap.put("searchField", searchField);
            batchMap.put("fieldList", fieldList);
            batchMap.put("isParticiple", isParticiple);
            batchMap.put("mediaSourceEnum", mediaSourceEnum);
            batchMap.put("pageNum", pageNum);
            batchMap.put("pageSize", pageSize);
            DingTalkUtil.sendDdMessage(assemblingStr(e, "批量查询接口", batchMap));
            return new RestResult<>(RestEnum.BATCH_QUERY_FIELD_HAS_SP_CHAR);
        }
    }

    /**
     * 获取不同索引的数量
     * @param mediaSourceEnum
     * @return
     */
    public Long getMediaIndexSize(MediaSourceEnum mediaSourceEnum) {
        try {
            CountRequest countRequest = new CountRequest();
            if (MediaSourceEnum.ALL == mediaSourceEnum) {
                countRequest.indices(indexArray_v2);
            }else {
                countRequest.indices(mediaSourceEnum.getEs_index_v2());
            }
            CountResponse countResponse = restHighLevelClient.count(countRequest, toBuilder());
            return countResponse == null ? 0L : countResponse.getCount();
        }catch (Exception e) {
            log.error("EsServiceV2Impl.getMediaIndexSize has error,",e);
            DingTalkUtil.sendDdMessage(assemblingStr(e, "查询索引数量接口", mediaSourceEnum));
        }
        return 0L;
    }

    /**
     * 获取国家列表
     * @return
     */
    public RestResult<SearchCountryResp> getCountryList() {
        try {
            List<String> countryListFromCache = redisService.rangeV2(COUNTRY_KEY, 0L, -1L);
            if (!CollectionUtils.isEmpty(countryListFromCache)) {
                return new RestResult<>(RestEnum.SUCCESS,
                        SearchCountryResp.builder().countryList(countryListFromCache).build());
            }

//            SearchSourceBuilder builder = new SearchSourceBuilder()
//                    .query(QueryBuilders.matchAllQuery())
//                    .fetchSource(new String[]{"country"}, null)
//                    .collapse(new CollapseBuilder("country.keyword"))
////                    .from(0).size(10000)
//                    .trackTotalHits(true);
////            if ("test".equals(env) || "pre".equals(env)) {
//                builder.from(0).size(1000);
////            }else {
////                builder.from(0).size(10000);
////            }
//
//            //搜索
//            SearchRequest searchRequest = new SearchRequest();
//            searchRequest.indices(indexArray_v2);
//            searchRequest.types("_doc");
//            searchRequest.source(builder);
//            // 执行请求
//            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
//            if (response == null) {
//                return new RestResult<>(RestEnum.PLEASE_TRY);
//            }
//
//            SearchHit[] searchHits = response.getHits().getHits();
//            if (CollectionUtils.isEmpty(Arrays.asList(searchHits))) {
//                return new RestResult<>(RestEnum.SUCCESS,
//                        SearchCountryResp.builder().countryList(Lists.newArrayList()).build());
//            }

            List<BlogSystemCountryDataEntity> blogSystemCountryDataList = countryDao.getAllCountryEntity();
            if (CollectionUtils.isEmpty(blogSystemCountryDataList)) {
                return new RestResult<>(RestEnum.SUCCESS,
                        SearchCountryResp.builder().countryList(Lists.newArrayList()).build());
            }

            List<String> countryList = blogSystemCountryDataList.stream().map(e -> e.getCountry()).distinct().collect(Collectors.toList());
//            List<String> countryList = Arrays.stream(searchHits)
//                    .filter(e -> StringUtils.isNotBlank(String.valueOf(e.getSourceAsMap().get("country"))))
////                    .filter(e -> !fieldList_one.contains(String.valueOf(e.getSourceAsMap().get("country"))))
//                    .map(e -> ReaderFileUtil.isChinese((String) e.getSourceAsMap().get("country")) ? (String) e.getSourceAsMap().get("country") : ((String) e.getSourceAsMap().get("country")).toUpperCase())
//                    .distinct()
//                    .collect(Collectors.toList());

            if(!CollectionUtils.isEmpty(countryList)) {
                redisService.leftPushAll(COUNTRY_KEY, countryList);
//                DingTalkUtil.sendDdMessage("落河系统通知: redis-key:" + COUNTRY_KEY + "入redis缓存完毕！！！");
            }
            return new RestResult<>(RestEnum.SUCCESS,
                    SearchCountryResp.builder().countryList(countryList).build());
        }catch (Exception e) {
            log.error("EsServiceImpl2.getCountryList has error,",e);
            DingTalkUtil.sendDdMessage(assemblingStr(e, "查询国家列表接口", ""));
        }
        return new RestResult<>(RestEnum.FAILED, "获取国家列表失败");
    }

    /**
     * 获取城市列表
     * @return
     */
    public RestResult<SearchCityResp> getCityList() {
        try {
            List<String> cityListFromCache = redisService.rangeV2(CITY_KEY, 0L, -1L);
            if (!CollectionUtils.isEmpty(cityListFromCache)) {
                return new RestResult<>(RestEnum.SUCCESS,
                        SearchCityResp.builder().cityList(cityListFromCache).build());
            }

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchAllQuery())
                    .fetchSource(new String[]{"city"}, null)
                    .collapse(new CollapseBuilder("city.keyword"))
                    //做限制
//                    .from(0).size(1000000)
                    .trackTotalHits(true);
//            if ("test".equals(env) || "pre".equals(env)) {
                builder.from(0).size(5000);
//            }else {
//                builder.from(0).size(10000);
//            }

            //搜索
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexArray_v2);
            searchRequest.types("_doc");
            searchRequest.source(builder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }

            SearchHit[] searchHits = response.getHits().getHits();
            if (CollectionUtils.isEmpty(Arrays.asList(searchHits))) {
                return new RestResult<>(RestEnum.SUCCESS,
                        SearchCityResp.builder().cityList(Lists.newArrayList()).build());
            }

            List<String> cityList = Arrays.stream(searchHits)
                    .filter(e -> StringUtils.isNotBlank(String.valueOf(e.getSourceAsMap().get("city"))))
                    .map(e -> (String) e.getSourceAsMap().get("city"))
                    .distinct()
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(cityList)) {
                redisService.leftPushAll(CITY_KEY, cityList);
//                DingTalkUtil.sendDdMessage("落河系统通知: redis-key:" + CITY_KEY + "入redis缓存完毕！！！");
            }
            return new RestResult<>(RestEnum.SUCCESS,
                    SearchCityResp.builder().cityList(cityList).build());
        }catch (Exception e) {
            log.error("EsServiceImpl2.getCityList has error,",e);
            DingTalkUtil.sendDdMessage(assemblingStr(e, "查询城市列表接口", ""));
        }
        return new RestResult<>(RestEnum.FAILED, "获取城市列表失败");
    }


    /**
     * 获取完整度列表
     * @return
     */
    public RestResult<SearchIntegrityResp> getIntegrityList() {
        try {
            List<String> integrityListFromCache = redisService.range(INTEGRITY_KEY, 0L, -1L);
            if (!CollectionUtils.isEmpty(integrityListFromCache)) {
                return new RestResult<>(RestEnum.SUCCESS,
                        SearchIntegrityResp.builder().integrityList(integrityListFromCache).build());
            }

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchAllQuery())
                    .fetchSource(new String[]{"integrity"}, null)
                    .collapse(new CollapseBuilder("integrity"))
//                    .from(0).size(10000)
                    .trackTotalHits(true);
//            if ("test".equals(env) || "pre".equals(env)) {
                builder.from(0).size(10000);
//            }else {
//                builder.from(0).size(900000000);
//            }

            //搜索
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexArray_v2);
            searchRequest.types("_doc");
            searchRequest.source(builder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }

            SearchHit[] searchHits = response.getHits().getHits();
            if (CollectionUtils.isEmpty(Arrays.asList(searchHits))) {
                return new RestResult<>(RestEnum.SUCCESS,
                        SearchIntegrityResp.builder().integrityList(Lists.newArrayList()).build());
            }

            List<String> integrityList = Arrays.stream(searchHits)
                    .filter(e -> e.getSourceAsMap().get("integrity") != null)
                    .map(e -> e.getSourceAsMap().get("integrity").toString())
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(integrityList)) {
                redisService.leftPushAll(INTEGRITY_KEY, integrityList);
                DingTalkUtil.sendDdMessage("落河系统通知: redis-key:" + INTEGRITY_KEY + "入redis缓存完毕！！！");
            }
            return new RestResult<>(RestEnum.SUCCESS,
                    SearchIntegrityResp.builder().integrityList(integrityList).build());
        }catch (Exception e) {
            log.error("EsServiceImpl2.getIntegrityList has error,",e);
            DingTalkUtil.sendDdMessage(assemblingStr(e, "查询数据完整度列表接口", ""));
        }
        return new RestResult<>(RestEnum.FAILED, "获取完整度列表失败");
    }


    /**
     * 城市或国家搜索
     * @param textValue
     * @param fieldName
     * @return
     */
    public RestResult<List<String>> queryCountryOrCity(String textValue, String fieldName) {

        try {

//            if ("country".equals(fieldName)) {
//                List<String> list = redisService.range(fieldName, 0L, -1L);
//                if (!CollectionUtils.isEmpty(list)) {
//                    return new RestResult<>(RestEnum.SUCCESS, list.stream().filter(e -> e.contains(textValue)).distinct().collect(Collectors.toList()));
//                }
//            }

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(fieldName + ".keyword", "*" + textValue + "*"));
//            boolQueryBuilder.should(QueryBuilders.queryStringQuery("*" + textValue + "*").field(fieldName + ".keyword"));

            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(boolQueryBuilder);
            builder.trackTotalHits(true);
//            if ("test".equals(env) || "pre".equals(env)) {
                builder.from(0).size(10000);
//            }else {
//                builder.from(0).size(10000);
//            }

            //搜索
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexArray_v2);
            searchRequest.types("_doc");
            searchRequest.source(builder);

            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }

            List<String> resultList = Lists.newArrayList();
            SearchHit[] searchHits = response.getHits().getHits();
            if (!CollectionUtils.isEmpty(Arrays.stream(searchHits).collect(Collectors.toList()))) {
                for (SearchHit hit : Arrays.stream(searchHits).collect(Collectors.toList())) {
                    if (hit == null) {
                        continue;
                    }

                    if ("country".equals(fieldName)) {
                        resultList.add(hit.getSourceAsMap().get("country") == null ? "" : String.valueOf(hit.getSourceAsMap().get("country")));
                    }else if ("city".equals(fieldName)) {
                        resultList.add(hit.getSourceAsMap().get("city") == null ? "" : String.valueOf(hit.getSourceAsMap().get("city")));
                    }
                }
            }
            return new RestResult<>(RestEnum.SUCCESS, resultList.stream().distinct().collect(Collectors.toList()));
        }catch (Exception e) {
            log.error("EsServiceImpl2.queryCountryOrCity has error,",e);
            DingTalkUtil.sendDdMessage(assemblingStr(e, "搜索国家/城市接口", ImmutableMap.of("textValue",textValue,"fieldName",fieldName)));
        }
        return new RestResult<>(RestEnum.FAILED.getCode(), "搜索国家/城市失败");
    }


    public RestResult<SearchBeforeNameResp> searchBeforeNameInfo(String userId, String userName) {

        try {
            // 创建请求
            List<BeforeNameInfo> userIdList = Lists.newArrayList();
            List<BeforeNameInfo> userNameList = Lists.newArrayList();
            if (StringUtils.isNotBlank(userId)) {
                userIdList = yi_ci_search(true, userId);
            }
            if (StringUtils.isNotBlank(userName)) {
                userNameList = yi_ci_search(false, userName);
            }

            List<BeforeNameInfo> bigList = Lists.newArrayList();
            bigList.addAll(userIdList);
            bigList.addAll(userNameList);

            if (CollectionUtils.isEmpty(bigList)) {
                return new RestResult<>(RestEnum.SUCCESS,
                        SearchBeforeNameResp.builder().beforeNameInfoList(new ArrayList<>()).build());
            }

            ArrayList<BeforeNameInfo> resultList =
                    bigList.stream().collect(Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(
                                    Comparator.comparing(BeforeNameInfo::getUuid))), ArrayList::new));

            return new RestResult<>(RestEnum.SUCCESS,
                    SearchBeforeNameResp.builder().beforeNameInfoList(resultList).build());
        }catch (Exception e) {
            log.error("EsServiceImpl.searchBeforeNameInfo has error,",e);
            DingTalkUtil.sendDdMessage(assemblingStr(e, "搜索曾用名接口", ImmutableMap.of("userId",userId,"userName",userName)));
        }
        return new RestResult<>(RestEnum.FAILED);
    }


    /**
     * 分别去搜索
     * @param whereValue
     * @param value
     * @return
     * @throws Exception
     */
    private List<BeforeNameInfo> yi_ci_search(boolean whereValue, String value) throws Exception{
        SearchSourceBuilder builder = new SearchSourceBuilder()
//                .query(boolQueryBuilder)
                .trackTotalHits(true);
        if (whereValue) {
            builder.query(QueryBuilders.termsQuery("user_id.keyword", value));
        }else {
            builder.query(QueryBuilders.termsQuery("screen_name.keyword", value));
        }

//        if ("test".equals(env) || "pre".equals(env)) {
            builder.from(0).size(10000);
//        } else {
//            builder.from(0).size(900000000);
//        }

        //搜索
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexArray_v2);
        searchRequest.types("_doc");
        searchRequest.source(builder);

        List<BeforeNameInfo> searchBeforeNameRespList = Lists.newArrayList();
        SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
        if (response == null) {
            return searchBeforeNameRespList;
        }

        SearchHit[] searchHits = response.getHits().getHits();
        if (!CollectionUtils.isEmpty(Arrays.stream(searchHits).collect(Collectors.toList()))) {
            for (SearchHit hit : Arrays.stream(searchHits).collect(Collectors.toList())) {
                if (hit == null) {
                    continue;
                }

                BeforeNameInfo beforeNameInfo = new BeforeNameInfo();
                beforeNameInfo.setUuid(hit.getSourceAsMap().get("uuid") == null ? "" : String.valueOf(hit.getSourceAsMap().get("uuid")));
                beforeNameInfo.setUserId(hit.getSourceAsMap().get("user_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_id")));
                beforeNameInfo.setUserName(hit.getSourceAsMap().get("screen_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("screen_name")));
                beforeNameInfo.setUserQuanName(hit.getSourceAsMap().get("use_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("use_name")));
                beforeNameInfo.setUserUrl(hit.getSourceAsMap().get("user_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_url")));
                MediaSourceEnum mediaSourceEnum = MediaSourceEnum.getMediaSourceEnumByIndex(hit.getIndex());
                beforeNameInfo.setMediaTypeResp(MediaTypeResp.builder().code(mediaSourceEnum.getCode()).desc(mediaSourceEnum.getDesc()).build());
                searchBeforeNameRespList.add(beforeNameInfo);
            }
        }
        return searchBeforeNameRespList;
    }


    public List<BeforeNameInfo> searchBeforeNameInfoV2(String userId, String userName) {
        try {
            // 创建请求
            List<BeforeNameInfo> userIdList = Lists.newArrayList();
            List<BeforeNameInfo> userNameList = Lists.newArrayList();
            if (StringUtils.isNotBlank(userId)) {
                userIdList = yi_ci_search(true, userId);
            }
            if (StringUtils.isNotBlank(userName)) {
                userNameList = yi_ci_search(false, userName);
            }

            List<BeforeNameInfo> bigList = Lists.newArrayList();
            bigList.addAll(userIdList);
            bigList.addAll(userNameList);

            if (CollectionUtils.isEmpty(bigList)) {
                return new ArrayList<>();
            }

            ArrayList<BeforeNameInfo> resultList =
                    bigList.stream().collect(Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(
                                    Comparator.comparing(BeforeNameInfo::getUuid))), ArrayList::new));
            return resultList;
        }catch (Exception e) {
            log.error("EsServiceImpl2.searchBeforeNameInfoV2 has error,",e);
        }
        return new ArrayList<>();
    }




    /*******************************************************************/

    /**
     * 组装查询参数
     * @param searchReq
     * @param boolQueryBuilder
     */
    private void assembleParam(SearchReq searchReq, BoolQueryBuilder boolQueryBuilder) {
        //精准查询
        if (searchReq.getIsParticiple().equals(1)) {
            if (StringUtils.isNotBlank(searchReq.getUserId())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("user_id.keyword", searchReq.getUserId()));
            }
            if (StringUtils.isNotBlank(searchReq.getUserName())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("screen_name.keyword", searchReq.getUserName()));
            }
            if (StringUtils.isNotBlank(searchReq.getUserQuanName())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("use_name.keyword", searchReq.getUserQuanName()));
            }
            if (StringUtils.isNotBlank(searchReq.getNameUserdBefore())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("name_userd_before.keyword", searchReq.getNameUserdBefore()));
            }
            if (StringUtils.isNotBlank(searchReq.getPhoneNum())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("mobile.keyword", searchReq.getPhoneNum()));
            }
            if (StringUtils.isNotBlank(searchReq.getEmail())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("email.keyword", searchReq.getEmail()));
            }
            if (StringUtils.isNotBlank(searchReq.getCountry())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("country.keyword", searchReq.getCountry()));
            }
            if (StringUtils.isNotBlank(searchReq.getCity())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("city.keyword", searchReq.getCity()));
            }
        }else {
            //分词查询
            if (StringUtils.isNotBlank(searchReq.getUserId())) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("user_id.keyword", "*"+searchReq.getUserId()+"*"));
//                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getUserId()+"*").field("user_id.keyword"));
            }
            if (StringUtils.isNotBlank(searchReq.getUserName())) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("screen_name.keyword", "*"+searchReq.getUserName()+"*"));
//                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getUserName()+"*").field("screen_name.keyword"));
            }
            if (StringUtils.isNotBlank(searchReq.getUserQuanName())) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("use_name.keyword", "*"+searchReq.getUserQuanName()+"*"));
//                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getUserQuanName()+"*").field("use_name.keyword"));
            }
            if (StringUtils.isNotBlank(searchReq.getNameUserdBefore())) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("name_userd_before.keyword", "*"+searchReq.getNameUserdBefore()+"*"));
//                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getNameUserdBefore()+"*").field("name_userd_before.keyword"));
            }
            if (StringUtils.isNotBlank(searchReq.getPhoneNum())) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("mobile.keyword", "*"+searchReq.getPhoneNum()+"*"));
//                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getPhoneNum()+"*").field("mobile.keyword"));
            }
            if (StringUtils.isNotBlank(searchReq.getEmail())) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("email.keyword", "*"+searchReq.getEmail()+"*"));
//                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getEmail()+"*").field("email.keyword"));
            }
            if (StringUtils.isNotBlank(searchReq.getCountry())) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("country.keyword", "*"+searchReq.getCountry()+"*"));
//                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getCountry()+"*").field("country.keyword"));
            }
            if (StringUtils.isNotBlank(searchReq.getCity())) {
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("city.keyword", "*"+searchReq.getCity()+"*"));
//                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getCity()+"*").field("city.keyword"));
            }
        }
        if (StringUtils.isNotBlank(searchReq.getUserSummary())) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("user_summary.keyword", "*"+searchReq.getUserSummary()+"*"));
//            boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getUserSummary()+"*").field("user_summary.keyword"));
        }
        if (StringUtils.isNotBlank(searchReq.getIntegrity())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("integrity", Integer.valueOf(searchReq.getIntegrity())));
        }
        if (StringUtils.isNotBlank(searchReq.getStartTime()) && StringUtils.isNotBlank(searchReq.getEndTime())) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("source_create_time.keyword").gte(searchReq.getStartTime()).lte(searchReq.getEndTime()).format("yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * 组装搜索的返回参数
     * @param response
     * @return
     */
    private SearchResp assembleResult(SearchResponse response, boolean flag) {
        List<SearchResp.UserData> userDataList = Lists.newArrayList();
        SearchHit[] searchHits = response.getHits().getHits();
        if (!CollectionUtils.isEmpty(Arrays.stream(searchHits).collect(Collectors.toList()))) {
            for (SearchHit hit : Arrays.stream(searchHits).collect(Collectors.toList())) {
                if (hit == null) {
                    continue;
                }

                SearchResp.UserData userData = new SearchResp.UserData();
                userData.setUserId(hit.getSourceAsMap().get("user_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_id")));
                userData.setUuid(hit.getSourceAsMap().get("uuid") == null ? "" : String.valueOf(hit.getSourceAsMap().get("uuid")));
                userData.setUserName(hit.getSourceAsMap().get("screen_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("screen_name")));
                userData.setUserQuanName(hit.getSourceAsMap().get("use_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("use_name")));
                userData.setPhoneNum(hit.getSourceAsMap().get("mobile") == null ? "" : String.valueOf(hit.getSourceAsMap().get("mobile")));
                userData.setEmail(hit.getSourceAsMap().get("email") == null ? "" : String.valueOf(hit.getSourceAsMap().get("email")));
                userData.setCountry(hit.getSourceAsMap().get("country") == null ? "" : String.valueOf(hit.getSourceAsMap().get("country")));
                userData.setCity(hit.getSourceAsMap().get("city") == null ? "" : String.valueOf(hit.getSourceAsMap().get("city")));
                userData.setUserHomePage(hit.getSourceAsMap().get("user_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_url")));
                userData.setGender(hit.getSourceAsMap().get("gender") == null ? "" : String.valueOf(hit.getSourceAsMap().get("gender")));
                userData.setMarriage(hit.getSourceAsMap().get("marriage") == null ? "" : String.valueOf(hit.getSourceAsMap().get("marriage")));
                userData.setFollowersCount(hit.getSourceAsMap().get("followers_count") == null ? "0" : String.valueOf(hit.getSourceAsMap().get("followers_count")));
                userData.setFriendCount(hit.getSourceAsMap().get("friend_count") == null ? "0" : String.valueOf(hit.getSourceAsMap().get("friend_count")));
                userData.setMaidernName(hit.getSourceAsMap().get("name_userd_before") == null ? "" : String.valueOf(hit.getSourceAsMap().get("name_userd_before")));
                userData.setUserReligion(hit.getSourceAsMap().get("user_religio") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_religio")));
                userData.setWorks(hit.getSourceAsMap().get("works") == null ? "" : String.valueOf(hit.getSourceAsMap().get("works")));
                userData.setPositionMessage(hit.getSourceAsMap().get("location") == null ? "" : String.valueOf(hit.getSourceAsMap().get("location")));
                userData.setHomeAddress(hit.getSourceAsMap().get("home_town") == null ? "" : String.valueOf(hit.getSourceAsMap().get("home_town")));

                MediaSourceEnum mediaSourceEnum = MediaSourceEnum.getMediaSourceEnumByIndexV2(hit.getIndex());
                userData.setMediaTypeResp(MediaTypeResp.builder().code(mediaSourceEnum.getCode()).desc(mediaSourceEnum.getDesc()).build());
                userDataList.add(userData);
            }
        }

        TotalHits totalHits = response.getHits().getTotalHits();

        SearchResp searchResp = new SearchResp();
        searchResp.setDataList(userDataList);
        if (flag) {
            Long size = getMediaIndexSize(MediaSourceEnum.ALL);
            searchResp.setTotalSize(size);
        }else {
            searchResp.setTotalSize(totalHits.value);
        }
        return searchResp;
    }

    /**
     * 返回索引
     * @param mediaCode
     * @return
     */
    private List<String> getEsIndex(Integer mediaCode) {
        MediaSourceEnum sourceEnum = MediaSourceEnum.getMediaSourceEnum(mediaCode);
        if (MediaSourceEnum.ALL == sourceEnum
                || null == sourceEnum) {
            return Arrays.stream(indexArray_v2).collect(Collectors.toList());
        }else {
            return Lists.newArrayList(sourceEnum.getEs_index_v2());
        }
    }


    /**
     * 字段判空
     * @param searchReq
     * @return
     */
    private boolean judgeSearchParamAllEmpty(SearchReq searchReq) {
        MediaSourceEnum sourceEnum = MediaSourceEnum.getMediaSourceEnum(searchReq.getMediaType());
        if (sourceEnum == null) {
            return false;
        }
        return true;
    }

    /**
     * 自定义build
     * @return
     */
    private RequestOptions toBuilder() {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(1024 * 1024 * 1024));
        return builder.build();
    }

    /**
     * 组装
     * @param e
     * @p interFaceName
     * @param object
     * @return
     */
    private String assemblingStr(Exception e, String interFaceName, Object object) {
        return "落河系统("+env+"环境)报错通知: 当前时间" + DateUtils.dateToStr(new Date()) + interFaceName + "报错,报错信息: " + JacksonUtil.beanToStr(e) + ", 入参为: " + JacksonUtil.beanToStr(object);
    }

    /**
     * 处理国家字段等
     * @param country
     * @return
     */
    private String handleCountry(String country){
        if (StringUtils.isBlank(country)) {
            return "";
        }
        if (fieldList_one.contains(country)) {
            return "中国";
        }
        return country;
    }

    /*************************************************************************/
    public RestResult updateEsInfo(MediaSourceEnum mediaSourceEnum) {
        try {
            BoolQueryBuilder bigBuilder = QueryBuilders.boolQuery();
            BoolQueryBuilder channelQueryBuilder = new BoolQueryBuilder();
            for(String fieldValue: fieldList_taiwan){
                channelQueryBuilder.should(QueryBuilders.matchQuery("country", fieldValue));
            }
            bigBuilder.must(channelQueryBuilder);
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(bigBuilder)
                    .trackTotalHits(true);

            //搜索
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(mediaSourceEnum.getEs_index_v2());
            searchRequest.types("_doc");
            searchRequest.source(builder);
            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }

            SearchHit[] searchHits = response.getHits().getHits();
            if (CollectionUtils.isEmpty(Arrays.stream(searchHits).collect(Collectors.toList()))) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }

            for (SearchHit documentFields : Arrays.stream(searchHits).collect(Collectors.toList())) {
                Map map = new HashMap();
                map.put("country", "中国台湾");
                UpdateRequest updateRequest = new UpdateRequest(mediaSourceEnum.getEs_index_v2(), documentFields.getId()).doc(map);
                restHighLevelClient.update(updateRequest, toBuilder());
            }

            DingTalkUtil.sendDdMessage(mediaSourceEnum.getEs_index_v2() + "索引数据已经刷完,请查看！！！");
            return new RestResult<>(RestEnum.SUCCESS);
        }catch (Exception e) {
            log.error("EsServiceImpl2.updateEsInfo has error,",e);
            DingTalkUtil.sendDdMessage(assemblingStr(e, "刷" + mediaSourceEnum.getEs_index_v2() + "索引的脚本接口", ""));
        }
        return new RestResult<>(RestEnum.FAILED.getCode(), "刷脚本失败");
    }

    public boolean judgeParamIsEmpty(SearchReq searchReq) {
        boolean flag = false;
        if (StringUtils.isBlank(searchReq.getUserId())
            && StringUtils.isBlank(searchReq.getUserName())
            && StringUtils.isBlank(searchReq.getUserQuanName())
            && StringUtils.isBlank(searchReq.getNameUserdBefore())
            && StringUtils.isBlank(searchReq.getPhoneNum())
            && StringUtils.isBlank(searchReq.getEmail())
            && StringUtils.isBlank(searchReq.getCountry())
            && StringUtils.isBlank(searchReq.getCity())
            && StringUtils.isBlank(searchReq.getUserSummary())
            && StringUtils.isBlank(searchReq.getStartTime())
            && StringUtils.isBlank(searchReq.getEndTime())
            && StringUtils.isBlank(searchReq.getIntegrity())
            && searchReq.getMediaType() == null) {
            flag = true;
        }
        return flag;
    }
}
