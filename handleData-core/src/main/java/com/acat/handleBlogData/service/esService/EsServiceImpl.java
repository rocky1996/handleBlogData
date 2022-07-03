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
    private SendEmailServiceImpl sendEmailService;
    @Resource
    private RestHighLevelClient restHighLevelClient;
    //标准桶大小
    private static final Integer LIMIT_SIZE = 100;

    private static String[] indexArray = new String[]{
            MediaSourceEnum.TWITTER.getEs_index(),
            MediaSourceEnum.INSTAGRAM.getEs_index(),
            MediaSourceEnum.FB_IMPL.getEs_index(),
            MediaSourceEnum.FB_HISTORY.getEs_index(),
            MediaSourceEnum.FQ_IMPL.getEs_index(),
            MediaSourceEnum.FQ_HISTORY.getEs_index()
    };

    /**
     *
     * @param file
     * @param mediaSourceEnum
     * @return
     */
    @Transactional
    public boolean insertEsData(MultipartFile file, MediaSourceEnum mediaSourceEnum) {
        if (file == null) {
            return false;
        }
        try {
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
                        }                    }
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
                default:
                    break;
            }
            return true;
        }catch (Exception e) {
            log.error("EsServiceImpl.insertEsData has error:{}",e.getMessage());
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
            if (!judgeSearchParamAllEmpty(searchReq)) {
                return new RestResult<>(RestEnum.PLEASE_ADD_PARAM);
            }

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
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
            if (!Objects.isNull(searchReq.getStartTime()) && !Objects.isNull(searchReq.getEndTime())) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("source_create_time").lte(searchReq.getEndTime()).gte(searchReq.getStartTime()));
            }

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(boolQueryBuilder);
            sourceBuilder.from((searchReq.getPageNum() > 0 ? (searchReq.getPageNum() - 1) : 0) * searchReq.getPageSize()).size(searchReq.getPageSize());
//            sourceBuilder.sort("registered_time.keyword", SortOrder.DESC);

            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(getEsIndex(searchReq).stream().toArray(String[]::new));
            searchRequest.types("_doc");
            searchRequest.source(sourceBuilder);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }

            List<SearchResp.UserData> userDataList = Lists.newArrayList();
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit hit : Arrays.stream(searchHits).collect(Collectors.toList())) {
                SearchResp.UserData userData = new SearchResp.UserData();
                userData.setUserId(String.valueOf(hit.getSourceAsMap().get("user_id")));
                userData.setUserName(hit.getSourceAsMap().get("screen_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("screen_name")));
                userData.setUserQuanName(hit.getSourceAsMap().get("use_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("use_name")));
                userData.setPhoneNum(String.valueOf(hit.getSourceAsMap().get("mobile")));
                userData.setEmail(String.valueOf(hit.getSourceAsMap().get("use_name")));
                userData.setCountry(String.valueOf(hit.getSourceAsMap().get("country")));
                userData.setCity(String.valueOf(hit.getSourceAsMap().get("city")));
                userData.setUserHomePage(String.valueOf(hit.getSourceAsMap().get("user_web_url")));
                userData.setGender(hit.getSourceAsMap().get("gender") == null ? GenderEnum.WEI_ZHI.getDesc() : GenderEnum.getGenderEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("gender")))).getDesc());
                userData.setMarriage(hit.getSourceAsMap().get("marriage") == null ? "未知" : String.valueOf(hit.getSourceAsMap().get("marriage")));
                userData.setFollowersCount(hit.getSourceAsMap().get("followers_count") == null ? "" : String.valueOf(hit.getSourceAsMap().get("followers_count")));
                userData.setFriendCount(hit.getSourceAsMap().get("friend_count") == null ? "" : String.valueOf(hit.getSourceAsMap().get("friend_count")));
                userData.setMaidernName(hit.getSourceAsMap().get("name_userd_before") == null ? "" : String.valueOf(hit.getSourceAsMap().get("name_userd_before")));
                userData.setUserReligion(hit.getSourceAsMap().get("user_religio") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_religio")));
                userData.setWorks(hit.getSourceAsMap().get("works") == null ? "" : String.valueOf(hit.getSourceAsMap().get("works")));
                userData.setPositionMessage(hit.getSourceAsMap().get("location") == null ? "" : String.valueOf(hit.getSourceAsMap().get("location")));
                userData.setHomeAddress(hit.getSourceAsMap().get("home_town") == null ? "" : String.valueOf(hit.getSourceAsMap().get("home_town")));

                MediaSourceEnum mediaSourceEnum = MediaSourceEnum.getMediaSourceEnumByIndex(hit.getIndex());
                userData.setMediaTypeResp(MediaTypeResp.builder().code(mediaSourceEnum.getCode()).desc(mediaSourceEnum.getDesc()).build());
                userDataList.add(userData);
            }

            TotalHits totalHits = response.getHits().getTotalHits();
            SearchResp searchResp = SearchResp
                    .builder()
                    .totalSize(totalHits.value)
                    .dataList(userDataList)
                    .build();
            return new RestResult<>(RestEnum.SUCCESS, searchResp);
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
            userDetailResp.setUserAvatar(hit.getSourceAsMap().get("user_avatar") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_avatar")));
            userDetailResp.setGender(hit.getSourceAsMap().get("gender") == null ? GenderEnum.WEI_ZHI.getDesc() : GenderEnum.getGenderEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("gender")))).getDesc());
            userDetailResp.setUserName(hit.getSourceAsMap().get("screen_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("screen_name")));
            userDetailResp.setUserQuanName(hit.getSourceAsMap().get("use_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("use_name")));
            userDetailResp.setBornTime(String.valueOf(hit.getSourceAsMap().get("born_time")));
            userDetailResp.setFollowersCount(hit.getSourceAsMap().get("followers_count") == null ? "" : String.valueOf(hit.getSourceAsMap().get("followers_count")));
            userDetailResp.setFriendCount(hit.getSourceAsMap().get("friend_count") == null ? "" : String.valueOf(hit.getSourceAsMap().get("friend_count")));
            userDetailResp.setPostCount(hit.getSourceAsMap().get("post_count") == null ? "" : String.valueOf(hit.getSourceAsMap().get("post_count")));
            userDetailResp.setLikeCount(hit.getSourceAsMap().get("like_count") == null ? "" : String.valueOf(hit.getSourceAsMap().get("like_count")));
            userDetailResp.setDataId(hit.getSourceAsMap().get("source_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("source_id")));
            userDetailResp.setUserId(String.valueOf(hit.getSourceAsMap().get("user_id")));
            userDetailResp.setUserHomePage(String.valueOf(hit.getSourceAsMap().get("user_web_url")));
            userDetailResp.setUserType(hit.getSourceAsMap().get("user_type") == null ? UserTypeEnum.WEI_ZHI.getDesc() : UserTypeEnum.getUserTypeEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("user_type")))).getDesc());
            userDetailResp.setVerified(hit.getSourceAsMap().get("source_id") == null ? "未知" : VerifiedEnum.getVerifiedEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("gender")))).getDesc());
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
            userDetailResp.setSourceCreateTime(hit.getSourceAsMap().get("source_create_time") == null ? "" : String.valueOf(hit.getSourceAsMap().get("source_create_time")));
            userDetailResp.setSourceCreateTime(hit.getSourceAsMap().get("user_summary") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_summary")));
            return new RestResult<>(RestEnum.SUCCESS, userDetailResp);
        }catch (Exception e) {
            log.error("EsServiceImpl.retrieveUserDetail has error:{}",e.getMessage());
        }
        return new RestResult<>(RestEnum.FAILED);
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
                    .collapse(collapseBuilder);

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
            Arrays.stream(searchHits).collect(Collectors.toList()).forEach(
                    country -> country.getFields().get("country.keyword").forEach(e -> countryList.add(String.valueOf(e)))
            );
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
                    .collapse(collapseBuilder);

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
            Arrays.stream(searchHits).collect(Collectors.toList()).forEach(
                    country -> country.getFields().get("city.keyword").forEach(e -> cityList.add(String.valueOf(e)))
            );
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
}
