package com.acat.handleBlogData.service.esService;

import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.controller.req.SearchDetailReq;
import com.acat.handleBlogData.controller.req.SearchReq;
import com.acat.handleBlogData.controller.resp.*;
import com.acat.handleBlogData.enums.*;
import com.acat.handleBlogData.service.emailService.SendEmailServiceImpl;
import com.acat.handleBlogData.service.emailService.vo.SendEmailReq;
import com.acat.handleBlogData.util.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
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
    private SendEmailServiceImpl sendEmailService;
    @Value("${spring.profiles.active}")
    private String env;

    private static final String PRO_PIC_URL = "https://20.10.0.11:9002/gateway/api-file/file/download?fileName=";
    private static final String PROD_PIC_URL = "http://big-data-project-department.dc.gtcom.prod/big-data-project-department/fb/info/";

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
     * 搜索查询
     * https://www.csdn.net/tags/MtTaEgxsNzk1ODAwLWJsb2cO0O0O.html
     * @param searchReq
     * @return
     */
    public RestResult<SearchResp> searchData(SearchReq searchReq) {
        try {
            if (searchReq.getIsParticiple() == null) {
                searchReq.setIsParticiple(1);
            }

            if (searchReq.getIsParticiple().equals(1) && StringUtils.isNotBlank(searchReq.getUserSummary())) {
                return new RestResult<>(RestEnum.FIELD_NOT_SUPPORT_DIM_SEARCH,  "用户简介不支持精准查询,请改为模糊(分词)查询");
            }

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            assembleParam(searchReq, boolQueryBuilder);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.from((searchReq.getPageNum() > 0 ? (searchReq.getPageNum() - 1) : 0) * searchReq.getPageSize()).size(searchReq.getPageSize());
            sourceBuilder.trackTotalHits(true);
            sourceBuilder.sort("integrity", SortOrder.DESC);

            SearchRequest searchRequest = new SearchRequest();
            if (!judgeSearchParamAllEmpty(searchReq)) {
                searchRequest.indices(indexArray_v2);
            }else {
                searchRequest.indices(getEsIndex(searchReq).stream().toArray(String[]::new));
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
            return new RestResult<>(RestEnum.SUCCESS, assembleResult(response));
        }catch (Exception e) {
            log.error("EsServiceV2Impl.searchData has error:{}",e.getMessage());
            sendEmailService.sendSimpleEmail(assemblingBean(e, "搜索查询接口", searchReq));
            return new RestResult<>(RestEnum.FAILED, "您好,此搜索条件会存在超时风险,请更换搜索条件,系统正在持续优化中ing！！！");
        }
    }

    /**
     * 详情
     */
    public RestResult<UserDetailResp> retrieveUserDetail(SearchDetailReq searchDetailReq) {

        try {
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchQuery("uuid", searchDetailReq.getUuid()));

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
            userDetailResp.setLanguage(hit.getSourceAsMap().get("language_type") == null ? "" : String.valueOf(hit.getSourceAsMap().get("language_type")));
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
                        .uuid(PatternUtil.handleStr(hit.getSourceAsMap().get("uuid") == null ? "" : String.valueOf(hit.getSourceAsMap().get("uuid"))))
                        .userQuanName(PatternUtil.handleStr(hit.getSourceAsMap().get("use_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("use_name"))))
                        .mediaTypeResp(MediaTypeResp.builder().code(mediaSourceEnum.getCode()).desc(mediaSourceEnum.getDesc()).build())
                        .userUrl(PatternUtil.handleStr(hit.getSourceAsMap().get("user_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_url"))))
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
            log.error("EsServiceV2Impl.retrieveUserDetail has error:{}",e.getMessage());
            sendEmailService.sendSimpleEmail(assemblingBean(e, "查询详情接口", searchDetailReq));
        }
        return new RestResult<>(RestEnum.FAILED);
    }

    /**
     * 批量搜索
     * @param searchField
     * @param fieldList
     * @param isParticiple
     * @return
     */
    public RestResult<SearchResp> batchQuery(String searchField, List<String> fieldList, Integer isParticiple, Integer pageNum, Integer pageSize) {
        try {
            BoolQueryBuilder bigBuilder = QueryBuilders.boolQuery();
            BoolQueryBuilder channelQueryBuilder = new BoolQueryBuilder();
            for(String fieldValue: fieldList){
                if (isParticiple.equals(1)) {
                    channelQueryBuilder.should(QueryBuilders.matchQuery(searchField + ".keyword", fieldValue));
                }else {
                    channelQueryBuilder.should(QueryBuilders.wildcardQuery(searchField, "*"+fieldValue+"*"));
                    channelQueryBuilder.should(QueryBuilders.queryStringQuery("*"+fieldValue+"*").field(searchField));
                }
            }
            bigBuilder.must(channelQueryBuilder);

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(bigBuilder)
                    .from((pageNum > 0 ? (pageNum - 1) : 0) * pageSize).size(pageSize)
                    .trackTotalHits(true);

            //搜索
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexArray_v2);
            searchRequest.types("_doc");
            searchRequest.source(builder);

            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }
            return new RestResult<>(RestEnum.SUCCESS, assembleResult(response));
        }catch (Exception e) {
            log.error("EsServiceV2Impl.batchQuery has error:{}",e.getMessage());
            sendEmailService.sendSimpleEmail(assemblingBean(e, "批量查询接口", ImmutableMap.of("searchField",searchField,"fieldList",fieldList,"isParticiple",isParticiple,"pageNum",pageNum,"pageSize",pageSize)));
            return new RestResult<>(RestEnum.FAILED);
        }
    }

    /**
     * 获取不同索引的数量
     * @param mediaSourceEnum
     * @return
     */
    public Long getMediaIndexSize(MediaSourceEnum mediaSourceEnum) {
        try {
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchAllQuery())
                    .trackTotalHits(true);
            //搜索
            SearchRequest searchRequest = new SearchRequest();
            if (MediaSourceEnum.ALL == mediaSourceEnum) {
                searchRequest.indices(indexArray_v2);
            }else {
                searchRequest.indices(mediaSourceEnum.getEs_index_v2());
            }
            searchRequest.types("_doc");
            searchRequest.source(builder);
            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
            return response == null ? 0L : response.getHits().getTotalHits().value;
        }catch (Exception e) {
            log.error("EsServiceV2Impl.getMediaIndexSize has error:{}",e.getMessage());
            sendEmailService.sendSimpleEmail(assemblingBean(e, "查询索引数量接口", mediaSourceEnum));
        }
        return 0L;
    }

    /**
     * 获取国家列表
     * @return
     */
    public RestResult<SearchCountryResp> getCountryList() {

        try {
//            List<String> countryListFromCache = redisService.range(COUNTRY_KEY, 0L, -1L);
//            if (!CollectionUtils.isEmpty(countryListFromCache)) {
//                return new RestResult<>(RestEnum.SUCCESS,
//                        SearchCountryResp.builder().countryList(countryListFromCache).build());
//            }

            String[] includeFields = new String[]{"country"};
            CollapseBuilder collapseBuilder = new CollapseBuilder("country.keyword");
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchAllQuery())
                    .fetchSource(includeFields, null)
                    .collapse(collapseBuilder)
//                    .from(0).size(10000)
                    .trackTotalHits(true);
            if ("test".equals(env) || "pre".equals(env)) {
                builder.from(0).size(10000);
            }else {
                builder.from(0).size(900000000);
            }

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
                        SearchCountryResp.builder().countryList(Lists.newArrayList()).build());
            }

            List<String> countryList = Arrays.stream(searchHits)
                    .filter(e -> StringUtils.isNotBlank(String.valueOf(e.getSourceAsMap().get("country"))))
                    .map(e -> ReaderFileUtil.isChinese((String) e.getSourceAsMap().get("country")) ? (String) e.getSourceAsMap().get("country") : ((String) e.getSourceAsMap().get("country")).toUpperCase())
                    .distinct()
                    .collect(Collectors.toList());

//            if(!CollectionUtils.isEmpty(countryList)) {
//                redisService.leftPushAll(COUNTRY_KEY, countryList);
//            }
            return new RestResult<>(RestEnum.SUCCESS,
                    SearchCountryResp.builder().countryList(countryList).build());
        }catch (Exception e) {
            log.error("EsServiceImpl2.getCountryList has error:{}",e.getMessage());
            sendEmailService.sendSimpleEmail(assemblingBean(e, "查询国家列表接口", ""));
        }
        return new RestResult<>(RestEnum.FAILED, "获取国家列表失败");
    }

    /**
     * 获取城市列表
     * @return
     */
    public RestResult<SearchCityResp> getCityList() {
        try {
//            List<String> cityListFromCache = redisService.range(CITY_KEY, 0L, -1L);
//            if (!CollectionUtils.isEmpty(cityListFromCache)) {
//                return new RestResult<>(RestEnum.SUCCESS,
//                        SearchCityResp.builder().cityList(cityListFromCache).build());
//            }

            String[] includeFields = new String[]{"city"};
            CollapseBuilder collapseBuilder = new CollapseBuilder("city.keyword");
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchAllQuery())
                    .fetchSource(includeFields, null)
                    .collapse(collapseBuilder)
                    //做限制
//                    .from(0).size(1000000)
                    .trackTotalHits(true);
            if ("test".equals(env) || "pre".equals(env)) {
                builder.from(0).size(10000);
            }else {
                builder.from(0).size(10000);
            }

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
//            if (!CollectionUtils.isEmpty(cityList)) {
//                redisService.leftPushAll(CITY_KEY, cityList);
//            }

            return new RestResult<>(RestEnum.SUCCESS,
                    SearchCityResp.builder().cityList(cityList).build());
        }catch (Exception e) {
            log.error("EsServiceImpl2.getCityList has error:{}",e.getMessage());
            sendEmailService.sendSimpleEmail(assemblingBean(e, "查询城市列表接口", ""));
        }
        return new RestResult<>(RestEnum.FAILED, "获取城市列表失败");
    }


    /**
     * 获取完整度列表
     * @return
     */
    public RestResult<SearchIntegrityResp> getIntegrityList() {
        try {
            String[] includeFields = new String[]{"integrity"};
            CollapseBuilder collapseBuilder = new CollapseBuilder("integrity");
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchAllQuery())
                    .fetchSource(includeFields, null)
                    .collapse(collapseBuilder)
//                    .from(0).size(10000)
                    .trackTotalHits(true);
            if ("test".equals(env) || "pre".equals(env)) {
                builder.from(0).size(10000);
            }else {
                builder.from(0).size(900000000);
            }

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
            return new RestResult<>(RestEnum.SUCCESS,
                    SearchIntegrityResp.builder().integrityList(integrityList).build());
        }catch (Exception e) {
            log.error("EsServiceImpl2.getIntegrityList has error:{}",e.getMessage());
            sendEmailService.sendSimpleEmail(assemblingBean(e, "查询数据完整度列表接口", ""));
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
            boolQueryBuilder.should(QueryBuilders.wildcardQuery(fieldName, "*" + textValue + "*"));
            boolQueryBuilder.should(QueryBuilders.queryStringQuery("*" + textValue + "*").field(fieldName));

            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(boolQueryBuilder);
            builder.trackTotalHits(true);
            if ("test".equals(env) || "pre".equals(env)) {
                builder.from(0).size(10000);
            }else {
                builder.from(0).size(900000000);
            }

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
                        resultList.add(PatternUtil.handleStr(hit.getSourceAsMap().get("country") == null ? "" : String.valueOf(hit.getSourceAsMap().get("country"))));
                    }else if ("city".equals(fieldName)) {
                        resultList.add(PatternUtil.handleStr(hit.getSourceAsMap().get("city") == null ? "" : String.valueOf(hit.getSourceAsMap().get("city"))));
                    }
                }
            }
            return new RestResult<>(RestEnum.SUCCESS, resultList.stream().distinct().collect(Collectors.toList()));
        }catch (Exception e) {
            log.error("EsServiceImpl2.queryCountryOrCity has error:{}",e.getMessage());
            sendEmailService.sendSimpleEmail(assemblingBean(e, "搜索国家/城市接口", ImmutableMap.of("textValue",textValue,"fieldName",fieldName)));
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
            log.error("EsServiceImpl.searchBeforeNameInfo has error:{}",e.getMessage());
            sendEmailService.sendSimpleEmail(assemblingBean(e, "搜索曾用名接口", ImmutableMap.of("userId",userId,"userName",userName)));
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

        if ("test".equals(env) || "pre".equals(env)) {
            builder.from(0).size(10000);
        } else {
            builder.from(0).size(900000000);
        }

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
                beforeNameInfo.setUuid(PatternUtil.handleStr(hit.getSourceAsMap().get("uuid") == null ? "" : String.valueOf(hit.getSourceAsMap().get("uuid"))));
                beforeNameInfo.setUserId(PatternUtil.handleStr(hit.getSourceAsMap().get("user_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_id"))));
                beforeNameInfo.setUserName(PatternUtil.handleStr(hit.getSourceAsMap().get("screen_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("screen_name"))));
                beforeNameInfo.setUserQuanName(PatternUtil.handleStr(hit.getSourceAsMap().get("use_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("use_name"))));
                beforeNameInfo.setUserUrl(PatternUtil.handleStr(hit.getSourceAsMap().get("user_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_url"))));
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
            log.error("EsServiceImpl2.searchBeforeNameInfoV2 has error:{}",e.getMessage());
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
                boolQueryBuilder.must(QueryBuilders.matchQuery("user_id.keyword", searchReq.getUserId()));
            }
            if (StringUtils.isNotBlank(searchReq.getUserName())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("screen_name.keyword", searchReq.getUserName()));
            }
            if (StringUtils.isNotBlank(searchReq.getUserQuanName())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("use_name.keyword", searchReq.getUserQuanName()));
            }
            if (StringUtils.isNotBlank(searchReq.getNameUserdBefore())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("name_userd_before.keyword", searchReq.getNameUserdBefore()));
            }
            if (StringUtils.isNotBlank(searchReq.getPhoneNum())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("mobile.keyword", searchReq.getPhoneNum()));
            }
            if (StringUtils.isNotBlank(searchReq.getEmail())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("email.keyword", searchReq.getEmail()));
            }
            if (StringUtils.isNotBlank(searchReq.getCountry())) {
                //均大写
                if (ReaderFileUtil.isAcronym(searchReq.getCountry(), true)) {
                    boolQueryBuilder.should(QueryBuilders.matchQuery("country.keyword", searchReq.getCountry()));
                    boolQueryBuilder.should(QueryBuilders.matchQuery("country.keyword", searchReq.getCountry().toLowerCase()));
                }
                //均为小写
                else if (ReaderFileUtil.isAcronym(searchReq.getCountry(), false)) {
                    boolQueryBuilder.should(QueryBuilders.matchQuery("country.keyword", searchReq.getCountry()));
                    boolQueryBuilder.should(QueryBuilders.matchQuery("country.keyword", searchReq.getCountry().toUpperCase()));
                }else {
                    boolQueryBuilder.must(QueryBuilders.matchQuery("country.keyword", searchReq.getCountry()));
                }
            }
            if (StringUtils.isNotBlank(searchReq.getCity())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("city.keyword", searchReq.getCity()));
            }
        }else {
            //分词查询
            if (StringUtils.isNotBlank(searchReq.getUserId())) {
                boolQueryBuilder.should(QueryBuilders.wildcardQuery("user_id", "*"+searchReq.getUserId()+"*"));
                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getUserId()+"*").field("user_id"));
            }
            if (StringUtils.isNotBlank(searchReq.getUserName())) {
                boolQueryBuilder.should(QueryBuilders.wildcardQuery("screen_name", "*"+searchReq.getUserName()+"*"));
                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getUserName()+"*").field("screen_name"));
            }
            if (StringUtils.isNotBlank(searchReq.getUserQuanName())) {
                boolQueryBuilder.should(QueryBuilders.wildcardQuery("use_name", "*"+searchReq.getUserQuanName()+"*"));
                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getUserQuanName()+"*").field("use_name"));
            }
            if (StringUtils.isNotBlank(searchReq.getNameUserdBefore())) {
                boolQueryBuilder.should(QueryBuilders.wildcardQuery("name_userd_before", "*"+searchReq.getNameUserdBefore()+"*"));
                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getNameUserdBefore()+"*").field("name_userd_before"));
            }
            if (StringUtils.isNotBlank(searchReq.getPhoneNum())) {
//                boolQueryBuilder.must(QueryBuilders.fuzzyQuery("mobile", "*"+searchReq.getPhoneNum()+"*").fuzziness(Fuzziness.AUTO));
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("mobile", "*"+searchReq.getPhoneNum()+"*"));
            }
            if (StringUtils.isNotBlank(searchReq.getEmail())) {
                if (searchReq.getEmail().contains(".")) {
                    String[] emailArray = searchReq.getEmail().split("\\.");
                    if (!CollectionUtils.isEmpty(Lists.newArrayList(emailArray))) {
                        Lists.newArrayList(emailArray).forEach(e -> boolQueryBuilder.should(QueryBuilders.wildcardQuery("email", "*"+e+"*")));
                    }
                }else {
                    if (searchReq.getEmail().contains("@")) {
                        String[] emailArr = searchReq.getEmail().split("@");
                        List<String> emList = Lists.newArrayList("@");
                        if (!CollectionUtils.isEmpty(Lists.newArrayList(emailArr))) {
                            emList.addAll(Lists.newArrayList(emailArr));
                        }
                        emList.forEach(e -> boolQueryBuilder.should(QueryBuilders.wildcardQuery("email", "*"+e+"*")));
                    }else {
                        boolQueryBuilder.should(QueryBuilders.wildcardQuery("email", "*"+searchReq.getEmail()+"*"));
                        boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getEmail()+"*").field("email"));
                    }
                }
            }
            if (StringUtils.isNotBlank(searchReq.getCountry())) {
                //均大写
                if (ReaderFileUtil.isAcronym(searchReq.getCountry(), true)) {
                    boolQueryBuilder.should(QueryBuilders.wildcardQuery("country", "*"+searchReq.getCountry()+"*"));
                    boolQueryBuilder.should(QueryBuilders.wildcardQuery("country", "*"+searchReq.getCountry().toLowerCase()+"*"));
                }
                //均小写
                else if (ReaderFileUtil.isAcronym(searchReq.getCountry(), false)) {
                    boolQueryBuilder.should(QueryBuilders.wildcardQuery("country", "*"+searchReq.getCountry()+"*"));
                    boolQueryBuilder.should(QueryBuilders.wildcardQuery("country", "*"+searchReq.getCountry().toUpperCase()+"*"));
                }else {
                    boolQueryBuilder.should(QueryBuilders.wildcardQuery("country", "*"+searchReq.getCountry()+"*"));
                    boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getCountry()+"*").field("country"));
                }
            }
            if (StringUtils.isNotBlank(searchReq.getCity())) {
                boolQueryBuilder.should(QueryBuilders.wildcardQuery("city", "*"+searchReq.getCity()+"*"));
                boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getCity()+"*").field("city"));
            }
        }
        if (StringUtils.isNotBlank(searchReq.getUserSummary())) {
            boolQueryBuilder.should(QueryBuilders.wildcardQuery("user_summary", "*"+searchReq.getUserSummary()+"*"));
            boolQueryBuilder.should(QueryBuilders.queryStringQuery("*"+searchReq.getUserSummary()+"*").field("user_summary"));
        }
        if (searchReq.getIntegrity() != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("integrity", searchReq.getIntegrity()));
        }
    }

    /**
     * 组装搜索的返回参数
     * @param response
     * @return
     */
    private SearchResp assembleResult(SearchResponse response) {
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
        return SearchResp
                .builder()
                .totalSize(totalHits.value)
                .dataList(userDataList)
                .build();
    }

    /**
     * 返回索引
     * @param searchReq
     * @return
     */
    private List<String> getEsIndex(SearchReq searchReq) {
        MediaSourceEnum sourceEnum = MediaSourceEnum.getMediaSourceEnum(searchReq.getMediaType());
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
        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(5000 * 1024 * 1024));
        return builder.build();
    }

    /**
     * 组装
     * @param e
     * @p interFaceName
     * @param object
     * @return
     */
    private SendEmailReq assemblingBean(Exception e, String interFaceName, Object object) {
        return SendEmailReq
                .builder()
                .subject("系统报错通知")
                .content("当前时间" + DateUtils.dateToStr(new Date()) + interFaceName + "报错," + "报错信息:" + e.getMessage() + "," + "入参为:" + JacksonUtil.beanToStr(object))
                .build();
    }
}