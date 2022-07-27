package com.acat.handleBlogData.service.esService;

import com.acat.handleBlogData.constants.RedisKeyConstants;
import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.controller.req.SearchDetailReq;
import com.acat.handleBlogData.controller.req.SearchReq;
import com.acat.handleBlogData.controller.resp.*;
import com.acat.handleBlogData.domain.*;
import com.acat.handleBlogData.domain.esEntityV2.*;
import com.acat.handleBlogData.enums.*;
import com.acat.handleBlogData.service.emailService.SendEmailServiceImpl;
import com.acat.handleBlogData.service.emailService.vo.SendEmailReq;
import com.acat.handleBlogData.service.esService.repository.*;
import com.acat.handleBlogData.service.esService.repository.v2.*;
import com.acat.handleBlogData.service.redisService.RedisLockServiceImpl;
import com.acat.handleBlogData.service.redisService.RedisServiceImpl;
import com.acat.handleBlogData.util.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchResponseSections;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
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
    private LinkImplRepository linkImplRepository;
    @Resource
    private LinkHistoryRepository linkHistoryRepository;
    @Resource
    private LinkSchoolRepository linkSchoolRepository;
    @Resource
    private LinkBusinessRepository linkBusinessRepository;
    @Resource
    private TwitterV2Repository twitterV2Repository;
    @Resource
    private InstagramV2Repository instagramV2Repository;
    @Resource
    private FbV2Repository fbV2Repository;
    @Resource
    private FqV2Repository fqV2Repository;
    @Resource
    private LinkV2Repository linkV2Repository;
    @Resource
    private LinkSchoolV2Repository linkSchoolV2Repository;
    @Resource
    private LinkBusinessV2Repository linkBusinessV2Repository;
    @Resource
    private SendEmailServiceImpl sendEmailService;
    @Resource
    private RedisLockServiceImpl redisLock;
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private RedisServiceImpl redisService;
//    @Resource
//    private TranslateOuterServiceImpl translateOuterService;
    @Value("${spring.profiles.active}")
    private String env;

//    标准桶大小
//    private static final Integer LIMIT_SIZE = 100;

    //默认时间
    private static final String MO_REN_TIME = "2022-07-12 14:12:04";

    private static final String PRO_PIC_URL = "https://20.10.0.11:9002/gateway/api-file/file/download?fileName=";
    private static final String PROD_PIC_URL = "http://big-data-project-department.dc.gtcom.prod/big-data-project-department/fb/info/";

    //redis->key
    public static final String COUNTRY_KEY = "country";
    public static final String CITY_KEY = "city";

    /**
     * 旧索引
     */
    private static String[] indexArray = new String[]{
        MediaSourceEnum.TWITTER.getEs_index_v1(),
        MediaSourceEnum.INSTAGRAM.getEs_index_v1(),
        MediaSourceEnum.FB_IMPL.getEs_index_v1(),
        MediaSourceEnum.FB_HISTORY.getEs_index_v1(),
        MediaSourceEnum.FQ_IMPL.getEs_index_v1(),
        MediaSourceEnum.FQ_HISTORY.getEs_index_v1(),
        MediaSourceEnum.LINKEDIN_IMPL.getEs_index_v1(),
        MediaSourceEnum.LINKEDIN_HISTORY.getEs_index_v1(),
        MediaSourceEnum.LINKEDIN_BUSINESS.getEs_index_v1(),
        MediaSourceEnum.LINKEDIN_SCHOOL.getEs_index_v1(),
    };

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
     *
     * @param file
     * @param mediaSourceEnum
     * @param preGovernanceNum
     * @return
//     */
//    @Transactional
    public synchronized boolean insertEsData(MultipartFile file, MediaSourceEnum mediaSourceEnum, String preGovernanceNum, boolean isNewVersion) {
//        String  lockKey = String.valueOf(System.currentTimeMillis());
//        long time = System.currentTimeMillis() + 1000*10;
        try {
//            boolean isLock = redisLock.getLock(lockKey, time);
//            if (!isLock) {
//                throw new RuntimeException("当前锁拥挤获取锁失败,请重试！！！");
//            }

            if (file == null) {
                return false;
            }

            switch (mediaSourceEnum) {
                case TWITTER:
                    if (isNewVersion) {
                        List<TwitterUserData_v2> twitterUserData_v2List = (List<TwitterUserData_v2>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.TWITTER, true);
                        if (!CollectionUtils.isEmpty(twitterUserData_v2List)) {

                            //统计治理前数量
                            addCacheFromRedis(RedisKeyConstants.TWITTER_PRO_GOV_NUM_KEY, preGovernanceNum);
                            List<TwitterUserData_v2> dataList = (List<TwitterUserData_v2>) twitterV2Repository.saveAll(twitterUserData_v2List);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.TWITTER));
                                return false;
                            }
                        }
                    }else {
                        List<TwitterUserData> twitterUserDataList = (List<TwitterUserData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.TWITTER, false);
                        if (!CollectionUtils.isEmpty(twitterUserDataList)) {
                            List<TwitterUserData> dataList = (List<TwitterUserData>) twitterRepository.saveAll(twitterUserDataList);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.TWITTER));
                                return false;
                            }
                        }
                    }
                    break;
                case INSTAGRAM:
                    if (isNewVersion) {
                        List<InstagramUserData_v2> instagramUserData_v2List = (List<InstagramUserData_v2>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.INSTAGRAM, true);
                        if (!CollectionUtils.isEmpty(instagramUserData_v2List)) {

                            //统计治理前数量
                            addCacheFromRedis(RedisKeyConstants.INSTAGRAM_PRO_GOV_NUM_KEY, preGovernanceNum);
                            List<InstagramUserData_v2> dataList = (List<InstagramUserData_v2>) instagramV2Repository.saveAll(instagramUserData_v2List);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.INSTAGRAM));
                                return false;
                            }
                        }
                    }else {
                        List<InstagramUserData> instagramUserDataList = (List<InstagramUserData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.INSTAGRAM, false);
                        if (!CollectionUtils.isEmpty(instagramUserDataList)) {
                            List<InstagramUserData> dataList = (List<InstagramUserData>) instagramRepository.saveAll(instagramUserDataList);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.INSTAGRAM));
                                return false;
                            }
                        }
                    }
                    break;
                case FB_IMPL:
                    if (isNewVersion) {
                        List<FbUserData_v2> fbUserData_v2List = (List<FbUserData_v2>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FB_IMPL, true);
                        if (!CollectionUtils.isEmpty(fbUserData_v2List)) {

//                            Set<String> userIdImplSet = fbUserData_v2List.stream().filter(e -> "imp".equals(e.getImpl_or_history_type())).map(e -> e.getUser_id()).collect(Collectors.toSet());
//                            if (!CollectionUtils.isEmpty(userIdImplSet)) {
//                                redisService.addAll(RedisKeyConstants.FB_IMPL_USER_ID_NUM_KEY, userIdImplSet);
//                            }

//                            Set<String> userIdHistorySet = fbUserData_v2List.stream().filter(e -> "history".equals(e.getImpl_or_history_type())).map(e -> e.getUser_id()).collect(Collectors.toSet());
//                            if (!CollectionUtils.isEmpty(userIdHistorySet)) {
//
//                                List<String> cfList = Lists.newArrayList();
//                                Set<String> implUserIdSet = redisService.members(RedisKeyConstants.FB_IMPL_USER_ID_NUM_KEY);
//                                if (!CollectionUtils.isEmpty(implUserIdSet)) {
//                                    for (String userId : userIdHistorySet) {
//                                        if (implUserIdSet.contains(userId)) {
//                                            cfList.add(userId);
//                                        }
//                                    }
//                                }
//
//                                if (!CollectionUtils.isEmpty(cfList)) {
//                                    List cfSearchList = batchQueryFromUserId(MediaSourceEnum.FB_IMPL.getEs_index_v2(), cfList);
//                                    if (!CollectionUtils.isEmpty(cfSearchList)) {
//
//                                    }
//                                }
//
//                            }
                            //统计治理前数量
                            addCacheFromRedis(RedisKeyConstants.FB_PRO_GOV_NUM_KEY, preGovernanceNum);

                            List<FbUserData_v2> dataList = (List<FbUserData_v2>) fbV2Repository.saveAll(fbUserData_v2List);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FB_IMPL));
                                return false;
                            }
                        }
                    }else {
                        List<FbUserImplData> fbUserImplDataList = (List<FbUserImplData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FB_IMPL, false);
                        if (!CollectionUtils.isEmpty(fbUserImplDataList)) {
                            List<FbUserImplData> dataList = (List<FbUserImplData>) fbImplRepository.saveAll(fbUserImplDataList);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FB_IMPL));
                                return false;
                            }
                        }
                    }
                    break;
                case FB_HISTORY:
                    if (isNewVersion) {
                        List<FbUserData_v2> fbUserData_v2List = (List<FbUserData_v2>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FB_HISTORY, true);
                        if (!CollectionUtils.isEmpty(fbUserData_v2List)) {

//                            Set<String> userIdImplSet = fbUserData_v2List.stream().filter(e -> "imp".equals(e.getImpl_or_history_type())).map(e -> e.getUser_id()).collect(Collectors.toSet());
//                            if (!CollectionUtils.isEmpty(userIdImplSet)) {
//                                redisService.addAll(RedisKeyConstants.FB_IMPL_USER_ID_NUM_KEY, userIdImplSet);
//                            }

                            //统计治理前数量
                            addCacheFromRedis(RedisKeyConstants.FB_PRO_GOV_NUM_KEY, preGovernanceNum);
                            List<FbUserData_v2> dataList = (List<FbUserData_v2>) fbV2Repository.saveAll(fbUserData_v2List);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FB_HISTORY));
                                return false;
                            }
                        }
                    }else {
                        List<FbUserHistoryData> fbUserHistoryDataList = (List<FbUserHistoryData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FB_HISTORY, false);
                        if (!CollectionUtils.isEmpty(fbUserHistoryDataList)) {
                            List<FbUserHistoryData> dataList = (List<FbUserHistoryData>) fbHistoryRepository.saveAll(fbUserHistoryDataList);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FB_HISTORY));
                                return false;
                            }
                        }
                    }
                    break;
                case FQ_IMPL:
                    if (isNewVersion) {
                        List<FqUserData_v2> fqUserData_v2List = (List<FqUserData_v2>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FQ_IMPL, true);
                        if (!CollectionUtils.isEmpty(fqUserData_v2List)) {

//                            Set<String> userIdImplSet = fbUserData_v2List.stream().filter(e -> "imp".equals(e.getImpl_or_history_type())).map(e -> e.getUser_id()).collect(Collectors.toSet());
//                            if (!CollectionUtils.isEmpty(userIdImplSet)) {
//                                redisService.addAll(RedisKeyConstants.FB_IMPL_USER_ID_NUM_KEY, userIdImplSet);
//                            }

                            //统计治理前数量
                            addCacheFromRedis(RedisKeyConstants.FQ_PRO_GOV_NUM_KEY, preGovernanceNum);
                            List<FqUserData_v2> dataList = (List<FqUserData_v2>) fqV2Repository.saveAll(fqUserData_v2List);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FQ_IMPL));
                                return false;
                            }
                        }
                    }else {
                        List<FqUserImplData> fqUserImplDataList = (List<FqUserImplData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FQ_IMPL,false);
                        if (!CollectionUtils.isEmpty(fqUserImplDataList)) {
                            List<FqUserImplData> dataList = (List<FqUserImplData>) fqImplRepository.saveAll(fqUserImplDataList);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FQ_IMPL));
                                return false;
                            }
                        }
                    }
                    break;
                case FQ_HISTORY:
                    if (isNewVersion) {
                        List<FqUserData_v2> fqUserData_v2List = (List<FqUserData_v2>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FQ_IMPL, true);
                        if (!CollectionUtils.isEmpty(fqUserData_v2List)) {

                            //统计治理前数量
                            addCacheFromRedis(RedisKeyConstants.FQ_PRO_GOV_NUM_KEY, preGovernanceNum);
                            List<FqUserData_v2> dataList = (List<FqUserData_v2>) fqV2Repository.saveAll(fqUserData_v2List);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FQ_IMPL));
                                return false;
                            }
                        }
                    }else {
                        List<FqUserHistoryData> fqUserHistoryData = (List<FqUserHistoryData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FQ_HISTORY, false);
                        if (!CollectionUtils.isEmpty(fqUserHistoryData)) {
                            List<FqUserHistoryData> dataList = (List<FqUserHistoryData>) fqHistoryRepository.saveAll(fqUserHistoryData);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FQ_HISTORY));
                                return false;
                            }
                        }
                    }
                    break;
                case LINKEDIN_IMPL:
                    if (isNewVersion) {
                        List<LinkUserData_v2> linkUserData_v2List = (List<LinkUserData_v2>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.LINKEDIN_IMPL, true);
                        if (!CollectionUtils.isEmpty(linkUserData_v2List)) {

                            //统计治理前数量
                            addCacheFromRedis(RedisKeyConstants.LINK_PRO_GOV_NUM_KEY, preGovernanceNum);
//                            List<LinkUserData_v2> dataList = (List<LinkUserData_v2>) linkV2Repository.saveAll(linkUserData_v2List);
                            Lists.partition(linkUserData_v2List, 1000).forEach(e -> linkV2Repository.saveAll(e));
//                            if (CollectionUtils.isEmpty(dataList)) {
//                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.LINKEDIN_IMPL));
//                                return false;
//                            }
                        }
                    }else {
                        List<LinkUserImplData> linkUserImplData = (List<LinkUserImplData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.LINKEDIN_IMPL, false);
                        if (!CollectionUtils.isEmpty(linkUserImplData)) {
                            List<LinkUserImplData> dataList = (List<LinkUserImplData>) linkImplRepository.saveAll(linkUserImplData);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.LINKEDIN_IMPL));
                                return false;
                            }
                        }
                    }
                    break;
                case LINKEDIN_HISTORY:
                    if (isNewVersion) {
                        List<LinkUserData_v2> linkUserData_v2List = (List<LinkUserData_v2>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.LINKEDIN_IMPL, true);
                        if (!CollectionUtils.isEmpty(linkUserData_v2List)) {

                            //统计治理前数量
                            addCacheFromRedis(RedisKeyConstants.LINK_PRO_GOV_NUM_KEY, preGovernanceNum);
                            List<LinkUserData_v2> dataList = (List<LinkUserData_v2>) linkV2Repository.saveAll(linkUserData_v2List);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.LINKEDIN_IMPL));
                                return false;
                            }
                        }
                    }else {
                        List<LInkUserHistoryData> lInkUserHistoryData = (List<LInkUserHistoryData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.LINKEDIN_HISTORY, false);
                        if (!CollectionUtils.isEmpty(lInkUserHistoryData)) {
                            List<LInkUserHistoryData> dataList = (List<LInkUserHistoryData>) linkHistoryRepository.saveAll(lInkUserHistoryData);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.LINKEDIN_HISTORY));
                                return false;
                            }
                        }
                    }
                    break;
                case LINKEDIN_BUSINESS:
                    if (isNewVersion) {
                        List<LinkBusinessUserData_v2> linkBusinessUserData_v2List = (List<LinkBusinessUserData_v2>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.LINKEDIN_BUSINESS, true);
                        if (!CollectionUtils.isEmpty(linkBusinessUserData_v2List)) {

                            //统计治理前数量
                            addCacheFromRedis(RedisKeyConstants.LINK_BUSINESS_PRO_GOV_NUM_KEY, preGovernanceNum);
                            List<LinkBusinessUserData_v2> dataList = (List<LinkBusinessUserData_v2>) linkBusinessV2Repository.saveAll(linkBusinessUserData_v2List);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.LINKEDIN_BUSINESS));
                                return false;
                            }
                        }
                    }else {
                        List<LinkBusinessUserData> linkBusinessUserData = (List<LinkBusinessUserData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.LINKEDIN_BUSINESS, false);
                        if (!CollectionUtils.isEmpty(linkBusinessUserData)) {
                            List<LinkBusinessUserData> dataList = (List<LinkBusinessUserData>) linkBusinessRepository.saveAll(linkBusinessUserData);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.LINKEDIN_BUSINESS));
                                return false;
                            }
                        }
                    }
                    break;
                case LINKEDIN_SCHOOL:
                    if (isNewVersion) {
                        List<LinkSchoolUserData_v2> linkSchoolUserData_v2List = (List<LinkSchoolUserData_v2>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.LINKEDIN_SCHOOL, true);
                        if (!CollectionUtils.isEmpty(linkSchoolUserData_v2List)) {

                            //统计治理前数量
                            addCacheFromRedis(RedisKeyConstants.LINK_SCHOOL_PRO_GOV_NUM_KEY, preGovernanceNum);
                            List<LinkSchoolUserData_v2> dataList = (List<LinkSchoolUserData_v2>) linkSchoolV2Repository.saveAll(linkSchoolUserData_v2List);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.LINKEDIN_SCHOOL));
                                return false;
                            }
                        }
                    }else {
                        List<LinkSchoolUserData> linkSchoolUserData = (List<LinkSchoolUserData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.LINKEDIN_SCHOOL, false);
                        if (!CollectionUtils.isEmpty(linkSchoolUserData)) {
                            List<LinkSchoolUserData> dataList = (List<LinkSchoolUserData>) linkSchoolRepository.saveAll(linkSchoolUserData);
                            if (CollectionUtils.isEmpty(dataList)) {
                                sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.LINKEDIN_SCHOOL));
                                return false;
                            }
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
//        finally {
//            redisLock.unLock(lockKey);
//        }
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
//            sourceBuilder.sort("integrity.keyword", SortOrder.DESC);

            SearchRequest searchRequest = new SearchRequest();
            if (!judgeSearchParamAllEmpty(searchReq)) {
                searchRequest.indices(indexArray);
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
            return new RestResult<>(RestEnum.SUCCESS, assembleParam(response));
        }catch (Exception e) {
            log.error("EsServiceImpl.searchData has error:{}",e.getMessage());
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
            searchRequest.indices(MediaSourceEnum.getMediaSourceEnum(searchDetailReq.getMediaCode()).getEs_index_v1());
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
                userDetailResp.setLocalPhotoUrl(PatternUtil.handleStr(hit.getSourceAsMap().get("local_photo_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("local_photo_url"))));
            }else if ("pre".equals(env)) {
                userDetailResp.setLocalPhotoUrl(PatternUtil.handleStr(hit.getSourceAsMap().get("local_photo_url") == null ? "" : PRO_PIC_URL + String.valueOf(hit.getSourceAsMap().get("local_photo_url"))));
            }else {
                userDetailResp.setLocalPhotoUrl(PatternUtil.handleStr(hit.getSourceAsMap().get("local_photo_url") == null ? "" : PROD_PIC_URL + String.valueOf(hit.getSourceAsMap().get("local_photo_url"))));
            }
            userDetailResp.setUserAvatar(PatternUtil.handleStr(hit.getSourceAsMap().get("user_avatar") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_avatar"))));

            userDetailResp.setGender(PatternUtil.handleStr(
                    hit.getSourceAsMap().get("gender") == null ? GenderEnum.WEI_ZHI.getDesc() :
                            (!ReaderFileUtil.isNumber(String.valueOf(hit.getSourceAsMap().get("gender"))) ? String.valueOf(hit.getSourceAsMap().get("gender")) :
                                    GenderEnum.getGenderEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("gender")))).getDesc()))
            );

            String userId = PatternUtil.handleStr(hit.getSourceAsMap().get("user_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_id")));
            String userName = PatternUtil.handleStr(hit.getSourceAsMap().get("screen_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("screen_name")));
            userDetailResp.setUserId(userId);
            userDetailResp.setUserName(userName);

            if (hit.getSourceAsMap().get("use_name") == null) {
                userDetailResp.setUserQuanName("");
            }else {
                String qName = String.valueOf(hit.getSourceAsMap().get("use_name"));
                String languageType = String.valueOf(hit.getSourceAsMap().get("language_type"));
                if ("zh".equals(languageType)) {
                    userDetailResp.setUserQuanName(PatternUtil.handleStr(qName.trim()));
                } else {
                    userDetailResp.setUserQuanName(PatternUtil.handleStr(qName));
                }
            }

            userDetailResp.setBornTime(PatternUtil.handleStr(hit.getSourceAsMap().get("born_time") == null ? "" : String.valueOf(hit.getSourceAsMap().get("born_time"))));
            userDetailResp.setFollowersCount(PatternUtil.handleStr(PatternUtil.handleFollowersCount(hit.getSourceAsMap().get("followers_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("followers_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("followers_count"))))));
            userDetailResp.setFriendCount(PatternUtil.handleStr(PatternUtil.handleFollowersCount(hit.getSourceAsMap().get("friend_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("friend_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("friend_count"))))));
            userDetailResp.setPostCount(PatternUtil.handleStr(PatternUtil.handleFollowersCount(hit.getSourceAsMap().get("post_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("post_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("post_count"))))));
            userDetailResp.setLikeCount(PatternUtil.handleStr(PatternUtil.handleFollowersCount(hit.getSourceAsMap().get("like_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("like_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("like_count"))))));
            userDetailResp.setDataId(PatternUtil.handleStr(hit.getSourceAsMap().get("source_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("source_id"))));
            userDetailResp.setUserHomePage(PatternUtil.handleStr(hit.getSourceAsMap().get("user_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_url"))));

            userDetailResp.setUserType(PatternUtil.handleStr(
                    hit.getSourceAsMap().get("user_type") == null ? UserTypeEnum.WEI_ZHI.getDesc() :
                            (!ReaderFileUtil.isNumber(String.valueOf(hit.getSourceAsMap().get("user_type"))) ? String.valueOf(hit.getSourceAsMap().get("user_type")) :
                                    UserTypeEnum.getUserTypeEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("user_type")))).getDesc()))
            );
            userDetailResp.setVerified(PatternUtil.handleStr(
                    hit.getSourceAsMap().get("verified") == null ? VerifiedEnum.WEIZHI.getDesc() :
                            (!ReaderFileUtil.isNumber(String.valueOf(hit.getSourceAsMap().get("verified"))) ? String.valueOf(hit.getSourceAsMap().get("user_type")) :
                                    VerifiedEnum.getVerifiedEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("verified")))).getDesc()))
            );

            if (hit.getSourceAsMap().get("name_userd_before") == null) {
                userDetailResp.setNameUserdBefore("");
            }else {
                boolean isJson = JacksonUtil.isJSON2(String.valueOf(hit.getSourceAsMap().get("name_userd_before")));
                if (isJson) {
                    Map<String, Object> map = JacksonUtil.strToBean(String.valueOf(hit.getSourceAsMap().get("name_userd_before")), Map.class);
                    userDetailResp.setNameUserdBefore(PatternUtil.handleStr(map == null ? "" : (map.get("userFormerName") == null ? "" : String.valueOf(map.get("userFormerName")))));
                }else {
                    userDetailResp.setNameUserdBefore("");
                }
            }

            userDetailResp.setMarriage(hit.getSourceAsMap().get("marriage") == null ? "" : String.valueOf(hit.getSourceAsMap().get("marriage")));
            userDetailResp.setCountry(PatternUtil.handleStr(
                    hit.getSourceAsMap().get("country") == null ? "" :
                            (ReaderFileUtil.isNumber(String.valueOf(hit.getSourceAsMap().get("country"))) ? String.valueOf(hit.getSourceAsMap().get("country")) :
                                    CountryUtil.getCountryName(String.valueOf(hit.getSourceAsMap().get("country")))))
            );

            userDetailResp.setCity(PatternUtil.handleStr(hit.getSourceAsMap().get("city") == null ? "" : String.valueOf(hit.getSourceAsMap().get("city"))));
            userDetailResp.setUserReligion(PatternUtil.handleStr(hit.getSourceAsMap().get("user_religion") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_religion"))));
            userDetailResp.setPhoneNum(PatternUtil.handleStr(hit.getSourceAsMap().get("mobile") == null ? "" : String.valueOf(hit.getSourceAsMap().get("mobile"))));

            if (hit.getSourceAsMap().get("email") == null) {
                userDetailResp.setEmail("");
            }else {
                userDetailResp.setEmail(PatternUtil.handleStr(PatternUtil.checkEmailAndGet(String.valueOf(hit.getSourceAsMap().get("email")))));
            }

            userDetailResp.setWorks(PatternUtil.handleStr(PatternUtil.handleWorks(hit.getSourceAsMap().get("works") == null ? "" : String.valueOf(hit.getSourceAsMap().get("works")))));
            userDetailResp.setPositionMessage(PatternUtil.handleStr(hit.getSourceAsMap().get("location") == null ? "" : String.valueOf(hit.getSourceAsMap().get("location"))));
            userDetailResp.setHomeAddress(PatternUtil.handleStr(hit.getSourceAsMap().get("home_town") == null ? "" : String.valueOf(hit.getSourceAsMap().get("home_town"))));
            userDetailResp.setLanguage(PatternUtil.handleStr(hit.getSourceAsMap().get("language_type") == null ? "" : String.valueOf(hit.getSourceAsMap().get("language_type"))));
            userDetailResp.setUserSummary(PatternUtil.handleStr(hit.getSourceAsMap().get("user_summary") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_summary"))));

            if (hit.getSourceAsMap().get("source_create_time") == null) {
                userDetailResp.setSourceCreateTime(MO_REN_TIME);
            }else {
                if (DateUtils.isDateVail(String.valueOf(hit.getSourceAsMap().get("source_create_time")))) {
                    userDetailResp.setSourceCreateTime(String.valueOf(hit.getSourceAsMap().get("source_create_time")));
                }else {
                    userDetailResp.setSourceCreateTime(MO_REN_TIME);
                }
            }

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


            /*****处理原始数据 _____特殊处理 *****/
            Map<String, Object> stringObjectMap = hit.getSourceAsMap();
            if (!Objects.isNull(stringObjectMap)) {
                for (String key : stringObjectMap.keySet()) {
                    if (stringObjectMap.get(key) != null) {
                        stringObjectMap.put(key, PatternUtil.handleStr(String.valueOf(stringObjectMap.get(key))));
                    }
                }
            }
            stringObjectMap.remove("_class");
            userDetailResp.setFieldMap(stringObjectMap);
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
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchAllQuery())
                    .trackTotalHits(true);
            //搜索
            SearchRequest searchRequest = new SearchRequest();

            if (MediaSourceEnum.ALL == mediaSourceEnum) {
                searchRequest.indices(indexArray);
            }else {
                searchRequest.indices(mediaSourceEnum.getEs_index_v1());
            }
            searchRequest.types("_doc");
            searchRequest.source(builder);

            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
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
            searchRequest.indices(indexArray);
            searchRequest.types("_doc");
            searchRequest.source(builder);

            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
            if (response == null) {
                return new RestResult<>(RestEnum.PLEASE_TRY);
            }
            return new RestResult<>(RestEnum.SUCCESS, assembleParam(response));
        }catch (Exception e) {
            log.error("EsServiceImpl.batchQuery has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED);
        }
    }

//    /**
//     * 批量搜索
//     * @param index
//     * @param userIdList
//     * @return
//     */
//    public List batchQueryFromUserId(String index, List<String> userIdList) {
//        try {
//            if (CollectionUtils.isEmpty(userIdList)) {
//                return Lists.newArrayList();
//            }
//
//            BoolQueryBuilder bigBuilder = QueryBuilders.boolQuery();
//            BoolQueryBuilder channelQueryBuilder = new BoolQueryBuilder();
//            for(String userId: userIdList){
//                channelQueryBuilder.should(QueryBuilders.matchQuery( "user_id.keyword", userId));
//            }
//            bigBuilder.must(channelQueryBuilder);
//
//            SearchSourceBuilder builder = new SearchSourceBuilder()
//                    .query(bigBuilder)
//                    .trackTotalHits(true);
//
//            //搜索
//            SearchRequest searchRequest = new SearchRequest();
//            searchRequest.indices(index);
//            searchRequest.types("_doc");
//            searchRequest.source(builder);
//
//            SearchResponse response = restHighLevelClient.search(searchRequest, toBuilder());
//            if (response == null) {
//                return null;
//            }
//            SearchHit[] searchHits = response.getHits().getHits();
//            List searchList = Arrays.stream(searchHits).collect(Collectors.toList());
//            return CollectionUtils.isEmpty(searchList) ? Lists.newArrayList() : searchList;
//        }catch (Exception e) {
//            log.error("EsServiceImpl.batchQueryFromUserId has error:{}",e.getMessage());
//            return Lists.newArrayList();
//        }
//    }


    /**
     * 城市或国家搜索
     * @param textValue
     * @param fieldName
     * @return
     */
    public RestResult<List<String>> queryCountryOrCity(String textValue, String fieldName) {

        try {

            if ("country".equals(fieldName)) {
                List<String> list = redisService.range(fieldName, 0L, -1L);
                if (!CollectionUtils.isEmpty(list)) {
                    return new RestResult<>(RestEnum.SUCCESS, list.stream().filter(e -> e.contains(textValue)).distinct().collect(Collectors.toList()));
                }
            }

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
            searchRequest.indices(indexArray);
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
            log.error("EsServiceImpl.queryCountryOrCity has error:{}",e.getMessage());
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
        }
        return new RestResult<>(RestEnum.FAILED);
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
            log.error("EsServiceImpl.searchBeforeNameInfoV2 has error:{}",e.getMessage());
        }
        return new ArrayList<>();
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
        searchRequest.indices(indexArray);
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

    /**
     * 获取国家列表
     * @return
     */
    public RestResult<SearchCountryResp> getCountryList() {

        try {
            List<String> countryListFromCache = redisService.range(COUNTRY_KEY, 0L, -1L);
            if (!CollectionUtils.isEmpty(countryListFromCache)) {
                return new RestResult<>(RestEnum.SUCCESS,
                        SearchCountryResp.builder().countryList(countryListFromCache).build());
            }

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
            searchRequest.indices(indexArray);
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

            if(!CollectionUtils.isEmpty(countryList)) {
                redisService.leftPushAll(COUNTRY_KEY, countryList);
            }
            return new RestResult<>(RestEnum.SUCCESS,
                    SearchCountryResp.builder().countryList(countryList).build());
        }catch (Exception e) {
            log.error("EsServiceImpl.getCountryList has error:{}",e.getMessage());
        }
        return new RestResult<>(RestEnum.FAILED, "获取国家列表失败");
    }

    /**
     * 获取城市列表
     * @return
     */
    public RestResult<SearchCityResp> getCityList() {
        try {
            List<String> cityListFromCache = redisService.range(CITY_KEY, 0L, -1L);
            if (!CollectionUtils.isEmpty(cityListFromCache)) {
                return new RestResult<>(RestEnum.SUCCESS,
                        SearchCityResp.builder().cityList(cityListFromCache).build());
            }

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
            searchRequest.indices(indexArray);
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
            }

            return new RestResult<>(RestEnum.SUCCESS,
                    SearchCityResp.builder().cityList(cityList).build());
        }catch (Exception e) {
            log.error("EsServiceImpl.SearchCityResp has error:{}",e.getMessage());
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
            searchRequest.indices(indexArray);
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
            log.error("EsServiceImpl.getIntegrityList has error:{}",e.getMessage());
        }
        return new RestResult<>(RestEnum.FAILED, "获取完整度列表失败");
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
            return Lists.newArrayList(sourceEnum.getEs_index_v1());
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
                if (hit == null) {
                    continue;
                }

                SearchResp.UserData userData = new SearchResp.UserData();
                userData.setUserId(PatternUtil.handleStr(hit.getSourceAsMap().get("user_id") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_id"))));
                userData.setUuid(PatternUtil.handleStr(hit.getSourceAsMap().get("uuid") == null ? "" : String.valueOf(hit.getSourceAsMap().get("uuid"))));
                userData.setUserName(PatternUtil.handleStr(hit.getSourceAsMap().get("screen_name") == null ? "" : String.valueOf(hit.getSourceAsMap().get("screen_name"))));


                if (hit.getSourceAsMap().get("use_name") == null) {
                    userData.setUserQuanName("");
                }else {
                    String qName = String.valueOf(hit.getSourceAsMap().get("use_name"));
                    String languageType = String.valueOf(hit.getSourceAsMap().get("language_type"));
                    if ("zh".equals(languageType)) {
                        userData.setUserQuanName(PatternUtil.handleStr(qName.trim()));
                    } else {
                        userData.setUserQuanName(PatternUtil.handleStr(qName));
                    }
                }

                userData.setPhoneNum(PatternUtil.handleStr(hit.getSourceAsMap().get("mobile") == null ? "" : String.valueOf(hit.getSourceAsMap().get("mobile"))));

                if (hit.getSourceAsMap().get("email") == null) {
                    userData.setEmail("");
                }else {
                    userData.setEmail(PatternUtil.handleStr(PatternUtil.checkEmailAndGet(String.valueOf(hit.getSourceAsMap().get("email")))));
                }

                userData.setCountry(PatternUtil.handleStr(
                        hit.getSourceAsMap().get("country") == null ? "" :
                                (ReaderFileUtil.isNumber(String.valueOf(hit.getSourceAsMap().get("country"))) ? String.valueOf(hit.getSourceAsMap().get("country")) :
                                        CountryUtil.getCountryName(String.valueOf(hit.getSourceAsMap().get("country")))))
                );

                userData.setCity(PatternUtil.handleStr(hit.getSourceAsMap().get("city") == null ? "" : String.valueOf(hit.getSourceAsMap().get("city"))));
                userData.setUserHomePage(PatternUtil.handleStr(hit.getSourceAsMap().get("user_url") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_url"))));

                userData.setGender(PatternUtil.handleStr(
                        hit.getSourceAsMap().get("gender") == null ? GenderEnum.WEI_ZHI.getDesc() :
                                (!ReaderFileUtil.isNumber(String.valueOf(hit.getSourceAsMap().get("gender"))) ? String.valueOf(hit.getSourceAsMap().get("gender")) :
                                        GenderEnum.getGenderEnum(Integer.parseInt(String.valueOf(hit.getSourceAsMap().get("gender")))).getDesc()))
                );

                userData.setMarriage(PatternUtil.handleStr(hit.getSourceAsMap().get("marriage") == null ? "未知" : String.valueOf(hit.getSourceAsMap().get("marriage"))));
                userData.setFollowersCount(PatternUtil.handleStr(PatternUtil.handleFollowersCount(hit.getSourceAsMap().get("followers_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("followers_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("followers_count"))))));
                userData.setFriendCount(PatternUtil.handleStr(PatternUtil.handleFollowersCount(hit.getSourceAsMap().get("friend_count") == null ? "0" : ("null".equals(String.valueOf(hit.getSourceAsMap().get("friend_count"))) ? "0" : String.valueOf(hit.getSourceAsMap().get("friend_count"))))));

                if (hit.getSourceAsMap().get("name_userd_before") == null) {
                    userData.setMaidernName("");
                }else {
                    boolean isJson = JacksonUtil.isJSON2(String.valueOf(hit.getSourceAsMap().get("name_userd_before")));
                    if (isJson) {
                        Map<String, Object> map = JacksonUtil.strToBean(String.valueOf(hit.getSourceAsMap().get("name_userd_before")), Map.class);
                        userData.setMaidernName(PatternUtil.handleStr(map == null ? "" : (map.get("userFormerName") == null ? "" : String.valueOf(map.get("userFormerName")))));
                    }else {
                        userData.setMaidernName("");
                    }
                }

                userData.setUserReligion(PatternUtil.handleStr(hit.getSourceAsMap().get("user_religio") == null ? "" : String.valueOf(hit.getSourceAsMap().get("user_religio"))));
                userData.setWorks(PatternUtil.handleStr(PatternUtil.handleWorks(hit.getSourceAsMap().get("works") == null ? "" : String.valueOf(hit.getSourceAsMap().get("works")))));
                userData.setPositionMessage(PatternUtil.handleStr(hit.getSourceAsMap().get("location") == null ? "" : String.valueOf(hit.getSourceAsMap().get("location"))));
                userData.setHomeAddress(PatternUtil.handleStr(hit.getSourceAsMap().get("home_town") == null ? "" : String.valueOf(hit.getSourceAsMap().get("home_town"))));

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
     * 自定义build
     * @return
     */
    public RequestOptions toBuilder() {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(5000 * 1024 * 1024));
        return builder.build();
    }

    /**
     * 统计治理前数据
     * @param key
     * @param preGovernanceNum
     */
    public void addCacheFromRedis(String key, String preGovernanceNum) {
        if (StringUtils.isBlank(key)) {
            return;
        }

        boolean isKey = redisService.hasKey(key);
        if (isKey) {
            return;
        }
        redisService.set(key, preGovernanceNum, null, null, false);
    }
}
