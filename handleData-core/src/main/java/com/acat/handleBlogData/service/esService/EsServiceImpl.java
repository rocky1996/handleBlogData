package com.acat.handleBlogData.service.esService;

import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.controller.req.SearchDetailReq;
import com.acat.handleBlogData.controller.req.SearchReq;
import com.acat.handleBlogData.controller.resp.*;
import com.acat.handleBlogData.domain.*;
import com.acat.handleBlogData.enums.*;
import com.acat.handleBlogData.service.emailService.SendEmailServiceImpl;
import com.acat.handleBlogData.service.emailService.vo.SendEmailReq;
import com.acat.handleBlogData.service.esService.repository.*;
import com.acat.handleBlogData.service.redisService.RedisLockServiceImpl;
import com.acat.handleBlogData.util.ReaderFileUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EsServiceImpl {

    @Resource
    private TwitterRepository twitterRepository;
    @Resource
    private InstagramRepository instagramRepository;
    @Resource
    private FbImplRepository fbImplRepository;
    @Resource
    private FbHistoryRepository fbHistoryRepository;
    @Resource
    private FqImplRepository fqImplRepository;
    @Resource
    private FqHistoryRepository fqHistoryRepository;
    @Resource
    private LinkSchoolRepository linkSchoolRepository;
    @Resource
    private LinkBusinessRepository linkBusinessRepository;
    @Resource
    private SendEmailServiceImpl sendEmailService;
    @Resource
    private RedisLockServiceImpl redisLock;
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Value("${spring.profiles.active}")
    private String env;
    //标准桶大小
    private static final Integer LIMIT_SIZE = 100;
    private static final String PRO_PIC_URL = "https://20.10.0.11:9002/gateway/api-file/file/download?fileName=";
    private static final String PROD_PIC_URL = "";

    private static String[] indexArray = new String[]{
        MediaSourceEnum.TWITTER.getEs_index(),
        MediaSourceEnum.INSTAGRAM.getEs_index(),
        MediaSourceEnum.FB_IMPL.getEs_index(),
        MediaSourceEnum.FB_HISTORY.getEs_index(),
        MediaSourceEnum.FQ_IMPL.getEs_index(),
        MediaSourceEnum.FQ_HISTORY.getEs_index(),
        MediaSourceEnum.LINKEDIN_BUSINESS.getEs_index(),
        MediaSourceEnum.LINKEDIN_SCHOOL.getEs_index()
    };

    /**
     *
     * @param file
     * @param mediaSourceEnum
     * @return
//     */
//    @Transactional
    public boolean insertEsData(MultipartFile file, MediaSourceEnum mediaSourceEnum) {
        String  lockKey = String.valueOf(System.currentTimeMillis());
        long time = System.currentTimeMillis() + 1000*10;
        try {
            boolean isLock = redisLock.getLock(lockKey, time);
            if (!isLock) {
                throw new RuntimeException("当前锁拥挤获取锁失败,请重试！！！");
            }

            if (file == null) {
                return false;
            }

            switch (mediaSourceEnum) {
                case TWITTER:
                    List<TwitterUserData> twitterUserDataList = (List<TwitterUserData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.TWITTER);
                    if (!CollectionUtils.isEmpty(twitterUserDataList)) {
                        List<TwitterUserData> dataList = (List<TwitterUserData>) twitterRepository.saveAll(twitterUserDataList);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.TWITTER));
                            return false;
                        }
                    }
                    break;
                case INSTAGRAM:
                    List<InstagramUserData> instagramUserDataList = (List<InstagramUserData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.INSTAGRAM);
                    if (!CollectionUtils.isEmpty(instagramUserDataList)) {
                        List<InstagramUserData> dataList = (List<InstagramUserData>) instagramRepository.saveAll(instagramUserDataList);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.INSTAGRAM));
                            return false;
                        }
                    }
                    break;
                case FB_IMPL:
                    List<FbUserImplData> fbUserImplDataList = (List<FbUserImplData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FB_IMPL);
                    if (!CollectionUtils.isEmpty(fbUserImplDataList)) {
                        List<FbUserImplData> dataList = (List<FbUserImplData>) fbImplRepository.saveAll(fbUserImplDataList);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FB_IMPL));
                            return false;
                        }
                    }
                    break;
                case FB_HISTORY:
                    List<FbUserHistoryData> fbUserHistoryDataList = (List<FbUserHistoryData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FB_HISTORY);
                    if (!CollectionUtils.isEmpty(fbUserHistoryDataList)) {
                        List<FbUserHistoryData> dataList = (List<FbUserHistoryData>) fbHistoryRepository.saveAll(fbUserHistoryDataList);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FB_HISTORY));
                            return false;
                        }
                    }
                    break;
                case FQ_IMPL:
                    List<FqUserImplData> fqUserImplDataList = (List<FqUserImplData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FQ_IMPL);
                    if (!CollectionUtils.isEmpty(fqUserImplDataList)) {
                        List<FqUserImplData> dataList = (List<FqUserImplData>) fqImplRepository.saveAll(fqUserImplDataList);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FQ_IMPL));
                            return false;
                        }
                    }
                    break;
                case FQ_HISTORY:
                    List<FqUserHistoryData> fqUserHistoryData = (List<FqUserHistoryData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FQ_HISTORY);
                    if (!CollectionUtils.isEmpty(fqUserHistoryData)) {
                        List<FqUserHistoryData> dataList = (List<FqUserHistoryData>) fqHistoryRepository.saveAll(fqUserHistoryData);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FQ_HISTORY));
                            return false;
                        }
                    }
                    break;
                case LINKEDIN_IMPL:
                    break;
                case LINKEDIN_HISTORY:
                    break;
                case LINKEDIN_BUSINESS:
                    List<LinkBusinessUserData> linkBusinessUserData = (List<LinkBusinessUserData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.LINKEDIN_BUSINESS);
                    if (!CollectionUtils.isEmpty(linkBusinessUserData)) {
                        List<LinkBusinessUserData> dataList = (List<LinkBusinessUserData>) linkBusinessRepository.saveAll(linkBusinessUserData);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.LINKEDIN_BUSINESS));
                            return false;
                        }
                    }
                    break;
                case LINKEDIN_SCHOOL:
                    List<LinkSchoolUserData> linkSchoolUserData = (List<LinkSchoolUserData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.LINKEDIN_SCHOOL);
                    if (!CollectionUtils.isEmpty(linkSchoolUserData)) {
                        List<LinkSchoolUserData> dataList = (List<LinkSchoolUserData>) linkSchoolRepository.saveAll(linkSchoolUserData);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.LINKEDIN_SCHOOL));
                            return false;
                        }
                    }
                    break;
                default:
                    break;
            }
            return true;
        }catch (Exception e) {
            log.error("EsServiceImpl.insertEsData has error:{}",e.getMessage());
        }finally {
            redisLock.unLock(lockKey);
        }
        return false;
    }

    /**
     * 搜索查询
     * https://www.csdn.net/tags/MtTaEgxsNzk1ODAwLWJsb2cO0O0O.html
     * @param searchReq
     * @return
     */
    public RestResult<SearchResp> searchData(SearchReq searchReq) {
        try {
//            if (!judgeSearchParamAllEmpty(searchReq)) {
//                return new RestResult<>(RestEnum.PLEASE_ADD_PARAM);
//            }

            if(searchReq.isParticiple()) {
                return new RestResult<>(RestEnum.PLEASE_ADD_PARAM.getCode(), "现不支持模糊分词查询,请更换精准匹配!!!");
            }

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            assembleParam(searchReq, boolQueryBuilder);

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.from((searchReq.getPageNum() > 0 ? (searchReq.getPageNum() - 1) : 0) * searchReq.getPageSize()).size(searchReq.getPageSize());
            sourceBuilder.trackTotalHits(true);
            //            sourceBuilder.sort("registered_time.keyword", SortOrder.DESC);

            SearchRequest searchRequest = new SearchRequest();
            if (!judgeSearchParamAllEmpty(searchReq)) {
                searchRequest.indices(indexArray);
            }else {
                searchRequest.indices(getEsIndex(searchReq).stream().toArray(String[]::new));
            }
            searchRequest.types("_doc");
            searchRequest.source(sourceBuilder);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }
            return new RestResult<>(RestEnum.SUCCESS, assembleParam(response));
        }catch (Exception e) {
            log.error("EsServiceImpl.searchData has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED);
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
            searchRequest.indices(MediaSourceEnum.getMediaSourceEnum(searchDetailReq.getMediaCode()).getEs_index());
            searchRequest.types("_doc");
            searchRequest.source(builder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
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
                userDetailResp.setUserAvatar(hit.getSourceAsMap().get("user_avatar") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_avatar")));
            }else if ("pre".equals(env)) {
                userDetailResp.setUserAvatar(hit.getSourceAsMap().get("user_avatar") == null ? "" : PRO_PIC_URL + String.valueOf(hit.getSourceAsMap().get("user_avatar")));
            }else {
                userDetailResp.setUserAvatar(hit.getSourceAsMap().get("user_avatar") == null ? "" : PROD_PIC_URL + String.valueOf(hit.getSourceAsMap().get("user_avatar")));
            }

            userDetailResp.setGender(
                    hit.getSourceAsMap().get("gender") == null ? GenderEnum.WEI_ZHI.getDesc() :
                            (ReaderFileUtil.isChinese(String.valueOf(hit.getSourceAsMap().get("gender"))) ? String.valueOf(hit.getSourceAsMap().get("gender")) :
                                    GenderEnum.getGenderEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("gender")))).getDesc())
            );

            userDetailResp.setUserName(hit.getSourceAsMap().get("screen_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("screen_name")));
            userDetailResp.setUserQuanName(hit.getSourceAsMap().get("use_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("use_name")));
            userDetailResp.setBornTime(hit.getSourceAsMap().get("born_time") == null ? "" : String.valueOf(hit.getSourceAsMap().get("born_time")));
            userDetailResp.setFollowersCount(hit.getSourceAsMap().get("followers_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("followers_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("followers_count"))));
            userDetailResp.setFriendCount(hit.getSourceAsMap().get("friend_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("friend_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("friend_count"))));
            userDetailResp.setPostCount(hit.getSourceAsMap().get("post_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("post_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("post_count"))));
            userDetailResp.setLikeCount(hit.getSourceAsMap().get("like_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("like_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("like_count"))));
            userDetailResp.setDataId(hit.getSourceAsMap().get("source_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("source_id")));
            userDetailResp.setUserId(hit.getSourceAsMap().get("user_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_id")));
            userDetailResp.setUserHomePage(hit.getSourceAsMap().get("user_web_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_web_url")));

            userDetailResp.setUserType(
                    hit.getSourceAsMap().get("user_type") == null ? UserTypeEnum.WEI_ZHI.getDesc() :
                            (ReaderFileUtil.isChinese(String.valueOf(hit.getSourceAsMap().get("user_type"))) ? String.valueOf(hit.getSourceAsMap().get("user_type")) :
                                    UserTypeEnum.getUserTypeEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("user_type")))).getDesc())
            );
            userDetailResp.setVerified(
                    hit.getSourceAsMap().get("verified") == null ? VerifiedEnum.WEIZHI.getDesc() :
                            (ReaderFileUtil.isChinese(String.valueOf(hit.getSourceAsMap().get("verified"))) ? String.valueOf(hit.getSourceAsMap().get("user_type")) :
                                    VerifiedEnum.getVerifiedEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("verified")))).getDesc())
            );

            userDetailResp.setNameUserdBefore(hit.getSourceAsMap().get("name_userd_before") == null ? "" : String.valueOf(hit.getSourceAsMap().get("name_userd_before")));
            userDetailResp.setMarriage(hit.getSourceAsMap().get("marriage") == null ? "" : String.valueOf(hit.getSourceAsMap().get("marriage")));

            userDetailResp.setCountry(
                    hit.getSourceAsMap().get("country") == null ? "" :
                            (ReaderFileUtil.isChinese(String.valueOf(hit.getSourceAsMap().get("country"))) ? String.valueOf(hit.getSourceAsMap().get("country")) :
                                    ReaderFileUtil.countryMap(String.valueOf(hit.getSourceAsMap().get("country"))))
            );

            userDetailResp.setCity(hit.getSourceAsMap().get("city") == null ? "" : String.valueOf(hit.getSourceAsMap().get("city")));
            userDetailResp.setUserReligion(hit.getSourceAsMap().get("user_religion") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_religion")));
            userDetailResp.setPhoneNum(hit.getSourceAsMap().get("mobile") == null ? "" : String.valueOf(hit.getSourceAsMap().get("mobile")));
            userDetailResp.setEmail(hit.getSourceAsMap().get("email") == null ? "" : String.valueOf(hit.getSourceAsMap().get("email")));
            userDetailResp.setWorks(hit.getSourceAsMap().get("works") == null ? "" : String.valueOf(hit.getSourceAsMap().get("works")));
            userDetailResp.setPositionMessage(hit.getSourceAsMap().get("location") == null ? "" : String.valueOf(hit.getSourceAsMap().get("location")));
            userDetailResp.setHomeAddress(hit.getSourceAsMap().get("home_town") == null ? "" : String.valueOf(hit.getSourceAsMap().get("home_town")));
            userDetailResp.setLanguage(hit.getSourceAsMap().get("language_type") == null ? "" : String.valueOf(hit.getSourceAsMap().get("language_type")));
            userDetailResp.setSourceCreateTime(hit.getSourceAsMap().get("source_create_time") == null ? "" : String.valueOf(hit.getSourceAsMap().get("source_create_time")));
            userDetailResp.setUserSummary(hit.getSourceAsMap().get("user_summary") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_summary")));
            userDetailResp.setFieldMap(hit.getSourceAsMap());
            return new RestResult<>(RestEnum.SUCCESS, userDetailResp);
        }catch (Exception e) {
            log.error("EsServiceImpl.retrieveUserDetail has error:{}",e.getMessage());
        }
        return new RestResult<>(RestEnum.FAILED);
    }

    /**
     * 获取不同索引的数量
     * @param mediaSourceEnum
     * @return
     */
    public Long getMediaIndexSize(MediaSourceEnum mediaSourceEnum) {
        try {
            if (MediaSourceEnum.LINKEDIN_HISTORY == mediaSourceEnum
                    || MediaSourceEnum.LINKEDIN_IMPL == mediaSourceEnum) {
                return 0L;
            }

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchAllQuery())
                    .trackTotalHits(true);
            //搜索
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(mediaSourceEnum.getEs_index());
            searchRequest.types("_doc");
            searchRequest.source(builder);

            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return response == null ? 0L : response.getHits().getTotalHits().value;
        }catch (Exception e) {
            log.error("EsServiceImpl.getMediaIndexSize has error:{}",e.getMessage());
        }
        return 0L;
    }

    /**
     * 批量搜索
     * @param searchField
     * @param fieldList
     * @param isParticiple
     * @return
     */
    public RestResult<SearchResp> batchQuery(String searchField, List<String> fieldList, boolean isParticiple) {
        try {
            BoolQueryBuilder bigBuilder = QueryBuilders.boolQuery();
            BoolQueryBuilder channelQueryBuilder = new BoolQueryBuilder();
            for(String fieldValue: fieldList){
                channelQueryBuilder.should(isParticiple ? QueryBuilders.matchQuery(searchField, fieldValue) : QueryBuilders.matchQuery(searchField + ".keyword", fieldValue));
            }
            bigBuilder.must(channelQueryBuilder);

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(bigBuilder)
//                    .from((pageNum > 0 ? (pageNum - 1) : 0) * pageSize).size(pageSize)
                    .trackTotalHits(true);
            //搜索
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexArray);
            searchRequest.types("_doc");
            searchRequest.source(builder);

            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }
            return new RestResult<>(RestEnum.SUCCESS, assembleParam(response));
        }catch (Exception e) {
            log.error("EsServiceImpl.batchQuery has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED);
        }
    }

    /**
     * 获取国家列表
     * @return
     */
    public RestResult<SearchCountryResp> getCountryList() {

        try {
            List<String> countryList = Lists.newArrayList();
            String[] includeFields = new String[]{"country"};
            CollapseBuilder collapseBuilder = new CollapseBuilder("country.keyword");
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchAllQuery())
                    .fetchSource(includeFields, null)
                    .collapse(collapseBuilder)
                    .trackTotalHits(true);

            //搜索
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexArray);
            searchRequest.types("_doc");
            searchRequest.source(builder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }

            SearchHit[] searchHits = response.getHits().getHits();
//            Arrays.stream(searchHits).collect(Collectors.toList()).forEach(e -> countryList.add(e.getSourceAsMap().get("country").toString()));
            for (SearchHit documentFields : Arrays.stream(searchHits).collect(Collectors.toList())) {
                Map<String, Object> map = documentFields.getSourceAsMap();
                if (map.get("country") != null) {
                    countryList.add((String) map.get("country"));
                }
            }
            return new RestResult<>(RestEnum.SUCCESS,
                    SearchCountryResp.builder().countryList(countryList).build());
        }catch (Exception e) {
            log.error("EsServiceImpl.getCountryList has error:{}",e.getMessage());
        }
        return new RestResult<>(RestEnum.FAILED);
    }

    /**
     * 获取城市列表
     * @return
     */
    public RestResult<SearchCityResp> getCityList() {
        try {
            List<String> cityList = Lists.newArrayList();
            String[] includeFields = new String[]{"city"};
            CollapseBuilder collapseBuilder = new CollapseBuilder("city.keyword");
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchAllQuery())
                    .fetchSource(includeFields, null)
                    .collapse(collapseBuilder)
                    .trackTotalHits(true);

            //搜索
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexArray);
            searchRequest.types("_doc");
            searchRequest.source(builder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }

            SearchHit[] searchHits = response.getHits().getHits();
//            Arrays.stream(searchHits).collect(Collectors.toList()).forEach(
//                    country -> country.getFields().get("city.keyword").forEach(e -> cityList.add(String.valueOf(e)))
//            );
            for (SearchHit documentFields : Arrays.stream(searchHits).collect(Collectors.toList())) {
                Map<String, Object> map = documentFields.getSourceAsMap();
                if (map.get("city") != null) {
                    cityList.add((String) map.get("city"));
                }
            }
            return new RestResult<>(RestEnum.SUCCESS,
                    SearchCityResp.builder().cityList(cityList).build());
        }catch (Exception e) {
            log.error("EsServiceImpl.SearchCityResp has error:{}",e.getMessage());
        }
        return new RestResult<>(RestEnum.FAILED);
    }

    /**
     * 组装
     * @param mediaSourceEnum
     * @return
     */
    private SendEmailReq covBean(MediaSourceEnum mediaSourceEnum) {
        SendEmailReq emailReq = SendEmailReq
                .builder()
                .subject("落es库失败")
                .content("您好,系统于北京时间" + new Date() + "入" + mediaSourceEnum.name() + "类型数据时失败,请联系rd紧急排查,谢谢！！！")
                .build();
        return emailReq;
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
            return Arrays.stream(indexArray).collect(Collectors.toList());
        }else {
            return Lists.newArrayList(sourceEnum.getEs_index());
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
     * 组装搜索的返回参数
     * @param response
     * @return
     */
    private SearchResp assembleParam(SearchResponse response) {
        List<SearchResp.UserData> userDataList = Lists.newArrayList();
        SearchHit[] searchHits = response.getHits().getHits();
        if (!CollectionUtils.isEmpty(Arrays.stream(searchHits).collect(Collectors.toList()))) {
            for (SearchHit hit : Arrays.stream(searchHits).collect(Collectors.toList())) {
                SearchResp.UserData userData = new SearchResp.UserData();
                userData.setUserId(hit.getSourceAsMap().get("user_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_id")));
                userData.setUuid(hit.getSourceAsMap().get("uuid") == null ? "" : String.valueOf(hit.getSourceAsMap().get("uuid")));
                userData.setUserName(hit.getSourceAsMap().get("screen_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("screen_name")));
                userData.setUserQuanName(hit.getSourceAsMap().get("use_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("use_name")));
                userData.setPhoneNum(hit.getSourceAsMap().get("mobile") == null ? "" : String.valueOf(hit.getSourceAsMap().get("mobile")));
                userData.setEmail(hit.getSourceAsMap().get("email") == null ? "" : String.valueOf(hit.getSourceAsMap().get("email")));

                userData.setCountry(
                        hit.getSourceAsMap().get("country") == null ? "" :
                                (ReaderFileUtil.isChinese(String.valueOf(hit.getSourceAsMap().get("country"))) ? String.valueOf(hit.getSourceAsMap().get("country")) :
                                        ReaderFileUtil.countryMap(String.valueOf(hit.getSourceAsMap().get("country"))))
                );

                userData.setCountry(hit.getSourceAsMap().get("country") == null ? "" : String.valueOf(hit.getSourceAsMap().get("country")));
                userData.setCity(hit.getSourceAsMap().get("city") == null ? "" : String.valueOf(hit.getSourceAsMap().get("city")));
                userData.setUserHomePage(hit.getSourceAsMap().get("user_web_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_web_url")));

                userData.setGender(
                        hit.getSourceAsMap().get("gender") == null ? GenderEnum.WEI_ZHI.getDesc() :
                                (ReaderFileUtil.isChinese(String.valueOf(hit.getSourceAsMap().get("gender"))) ? String.valueOf(hit.getSourceAsMap().get("gender")) :
                                        GenderEnum.getGenderEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("gender")))).getDesc())
                );

                userData.setMarriage(hit.getSourceAsMap().get("marriage") == null ? "未知" : String.valueOf(hit.getSourceAsMap().get("marriage")));
                userData.setFollowersCount(hit.getSourceAsMap().get("followers_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("followers_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("followers_count"))));
                userData.setFriendCount(hit.getSourceAsMap().get("friend_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("friend_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("friend_count"))));
                userData.setMaidernName(hit.getSourceAsMap().get("name_userd_before") == null ? "" : String.valueOf(hit.getSourceAsMap().get("name_userd_before")));
                userData.setUserReligion(hit.getSourceAsMap().get("user_religio") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_religio")));
                userData.setWorks(hit.getSourceAsMap().get("works") == null ? "" : String.valueOf(hit.getSourceAsMap().get("works")));
                userData.setPositionMessage(hit.getSourceAsMap().get("location") == null ? "" : String.valueOf(hit.getSourceAsMap().get("location")));
                userData.setHomeAddress(hit.getSourceAsMap().get("home_town") == null ? "" : String.valueOf(hit.getSourceAsMap().get("home_town")));

                MediaSourceEnum mediaSourceEnum = MediaSourceEnum.getMediaSourceEnumByIndex(hit.getIndex());
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
     * 组装查询参数
     * @param searchReq
     * @param boolQueryBuilder
     */
    private void assembleParam(SearchReq searchReq, BoolQueryBuilder boolQueryBuilder) {
        //精准查询
        if (!searchReq.isParticiple()) {
            if (StringUtils.isNotBlank(searchReq.getUserId())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("user_id.keyword", searchReq.getUserId()));
            }
            if (StringUtils.isNotBlank(searchReq.getUserName())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("screen_name.keyword", searchReq.getUserName()));
            }
            if (StringUtils.isNotBlank(searchReq.getUserQuanName())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("use_name.keyword", searchReq.getUserQuanName()));
            }
            if (StringUtils.isNotBlank(searchReq.getBeforeName())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("name_userd_before.keyword", searchReq.getBeforeName()));
            }
            if (StringUtils.isNotBlank(searchReq.getPhoneNum())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("mobile.keyword", searchReq.getPhoneNum()));
            }
            if (StringUtils.isNotBlank(searchReq.getEmail())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("email.keyword", searchReq.getEmail()));
            }
            if (StringUtils.isNotBlank(searchReq.getCountry())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("country.keyword", searchReq.getCountry()));
            }
            if (StringUtils.isNotBlank(searchReq.getCity())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("city.keyword", searchReq.getCity()));
            }
//            if (StringUtils.isNotBlank(searchReq.getUserSummary())) {
//                boolQueryBuilder.must(QueryBuilders.matchQuery("user_summary.keyword", searchReq.getCity()));
//            }
        }else {
            //分词查询
            //todo
            if (StringUtils.isNotBlank(searchReq.getUserId())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("user_id", searchReq.getUserId()));
            }
            if (StringUtils.isNotBlank(searchReq.getUserName())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("screen_name", searchReq.getUserName()));
            }
            if (StringUtils.isNotBlank(searchReq.getUserQuanName())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("use_name", searchReq.getUserQuanName()));
            }
            if (StringUtils.isNotBlank(searchReq.getBeforeName())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("name_userd_before", searchReq.getBeforeName()));
            }
            if (StringUtils.isNotBlank(searchReq.getPhoneNum())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("mobile", searchReq.getPhoneNum()));
            }
            if (StringUtils.isNotBlank(searchReq.getEmail())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("email", searchReq.getEmail()));
            }
            if (StringUtils.isNotBlank(searchReq.getCountry())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("country", searchReq.getCountry()));
            }
            if (StringUtils.isNotBlank(searchReq.getCity())) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("city", searchReq.getCity()));
            }
        }
        if (StringUtils.isNotBlank(searchReq.getUserSummary())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("user_summary", searchReq.getCity()));
        }
    }
}
