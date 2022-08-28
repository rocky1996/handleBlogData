package com.acat.handleBlogData.controller;

import com.acat.handleBlogData.aop.Auth;
import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.constants.UrlConstants;
import com.acat.handleBlogData.controller.req.SearchReq;
import com.acat.handleBlogData.controller.resp.*;
import com.acat.handleBlogData.domain.entity.BlogSystemIndexTargetDataEntity;
import com.acat.handleBlogData.enums.BatchSearchFieldEnum;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.enums.RestEnum;
import com.acat.handleBlogData.outerService.outerInterface.TranslateOuterServiceImpl;
import com.acat.handleBlogData.service.IndexTargetService;
import com.acat.handleBlogData.service.esService.EsServiceImpl;
import com.acat.handleBlogData.service.esService.EsServiceV2Impl;
import com.acat.handleBlogData.service.impl.IndexTargetServiceImpl;
import com.acat.handleBlogData.service.redisService.RedisServiceImpl;
import com.acat.handleBlogData.util.JwtUtils;
import com.acat.handleBlogData.util.LanguageUtil;
import com.acat.handleBlogData.util.ReaderFileUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(UrlConstants.BLOG_SYSTEM_COMMON)
public class CommonController {

//    @Resource
//    private EsServiceImpl esService;
    @Resource
    private EsServiceV2Impl esServiceV2;
    @Resource
    private TranslateOuterServiceImpl translateOuterService;
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Resource
    private IndexTargetService indexTargetService;
    @Resource
    private RedisServiceImpl redisService;

    private static final String ZH = "zh";
    private static LoginRespVo loginRespVo = null;
    static {
        loginRespVo = new LoginRespVo();
        loginRespVo.setId(8);
        loginRespVo.setUserName("xuanwu-004");
        loginRespVo.setPassWord("xuanwu-004");
        loginRespVo.setUserNickname("玄武");
    }

    //需要过滤的
    private static List<MediaSourceEnum> mediaSourceEnumList = Lists.newArrayList(MediaSourceEnum.FB_HISTORY, MediaSourceEnum.FQ_HISTORY, MediaSourceEnum.LINKEDIN_HISTORY);

    @Auth(required = false)
    @GetMapping("/getBlogSystemInterfaceToken")
    public RestResult<Map<String, Object>> getBlogSystemInterfaceToken() {

        try {
            return new RestResult<>(RestEnum.SUCCESS,
                    ImmutableMap.of("token", JwtUtils.sign(loginRespVo), "message", "请获取token后,将token传入请求接口的http请求头里面即可！！！"));
        }catch (Exception e) {
            return new RestResult<>(RestEnum.FAILED.getCode(), "获取token失败,请稍后重试或联系管理员!!!", null);
        }
    }

    @Auth(required = false)
    @GetMapping("/deleteIndex")
    public RestResult deleteIndex(Integer mediaSourceCode) {
        try {
            MediaSourceEnum mediaSourceEnum = MediaSourceEnum.getMediaSourceEnum(mediaSourceCode);
            if (MediaSourceEnum.ALL == mediaSourceEnum
                    || mediaSourceEnum == null) {
                return new RestResult<>(RestEnum.MEDIA_SOURCE_ERROR);
            }
            elasticsearchRestTemplate.indexOps(IndexCoordinates.of(mediaSourceEnum.getEs_index_v2())).delete();
            return new RestResult<>(RestEnum.SUCCESS);
        }catch (Exception e) {
            log.error("CommonController.deleteIndex has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @GetMapping("/deleteRedisKey")
    public RestResult deleteRedisKey(String redisKey) {

        try {
            boolean result = redisService.deleteKey(redisKey);
            return new RestResult<>(RestEnum.SUCCESS.getCode(), result == true ? "删除成功" : "删除失败", null);
        }catch (Exception e) {
            log.error("CommonController.deleteRedisKey has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @GetMapping("/getCountryList")
    public RestResult<SearchCountryResp> getCountryList() {

        try {
            return esServiceV2.getCountryList();
        }catch (Exception e) {
            log.error("CommonController.getCountryList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @GetMapping("/getCityList")
    public RestResult<SearchCityResp> getCityList() {

        try {
            return esServiceV2.getCityList();
        }catch (Exception e) {
            log.error("CommonController.getCityList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @GetMapping("/getIntegrityList")
    public RestResult<SearchIntegrityResp> getIntegrityList() {

        try {
            return esServiceV2.getIntegrityList();
        }catch (Exception e) {
            log.error("CommonController.getIntegrityList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @GetMapping("/getMediaTypeList")
    public RestResult<List<MediaTypeResp>> getMediaTypeList() {

        try {
            return new RestResult(RestEnum.SUCCESS,
                    Arrays.stream(MediaSourceEnum.values())
                            .filter(e -> !mediaSourceEnumList.contains(e))
                            .map(e ->
                            MediaTypeResp
                                    .builder()
                                    .code(e.getCode())
                                    .desc(e.getDesc())
                                    .totalSize(esServiceV2.getMediaIndexSize(e))
                                    .build())
                            .collect(Collectors.toList()));
        }catch (Exception e) {
            log.error("CommonController.getMediaTypeList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @GetMapping("/getBatchQueryField")
    public RestResult<BatchQueryResp> getBatchQueryField() {
        try {
            return new RestResult(RestEnum.SUCCESS, BatchQueryResp
                    .builder()
                    .queryFieldList(
                            Arrays.stream(BatchSearchFieldEnum.values()).map(e ->
                                            BatchQueryResp.Field
                                                    .builder()
                                                    .fieldName(e.getFieldName())
                                                    .fieldValue(e.getFieldValue())
                                                    .isFuzzy(e.isFuzzy())
                                                    .build())
                                    .collect(Collectors.toList()))
                    .build());
        }catch (Exception e) {
            log.error("CommonController.getBatchQueryField has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

//    @Auth
    @PostMapping("/getTranResult")
    public RestResult<TranResp> getTranResult(@RequestBody Map<String, String> tranMap) {
        try {
            Map<String, Object> tranResultMap = Maps.newHashMap();
            for (String key : tranMap.keySet()) {

                String tranValue = tranMap.get(key);
                if (StringUtils.isBlank(tranValue)) {
                    tranResultMap.put(key, "");
                    continue;
                }

                if ("language".equals(key) && StringUtils.isNotBlank(tranValue)) {
                    tranResultMap.put(key, LanguageUtil.getLanguageName(tranValue));
                    continue;
                }

//                if (ReaderFileUtil.isChinese(tranValue)) {
//                    tranResultMap.put(key, tranValue);
//                    continue;
//                }

                String languageType = translateOuterService.getLanguageDelectResult(tranValue);
                if (StringUtils.isBlank(languageType)) {
                    tranResultMap.put(key, "");
                    continue;
                }

                if (ZH.equals(languageType)) {
                    tranResultMap.put(key, "");
                    continue;
                }

                String translateStr = translateOuterService.getTranslateValue(languageType, tranValue);
                tranResultMap.put(key, translateStr);
            }
            return new RestResult(RestEnum.SUCCESS, TranResp.builder().tranMap(tranResultMap).build());
        }catch (Exception e) {
            log.error("CommonController.getBatchQueryField has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @GetMapping("/getIndexTargetNum")
    public RestResult<List<IndexTargetResp>> getIndexTargetNum() {

        try {
            List<BlogSystemIndexTargetDataEntity> blogSystemIndexTargetDataEntityList = indexTargetService.getAll();

            List<IndexTargetResp> indexTargetRespList = Lists.newArrayList();
            if (CollectionUtils.isEmpty(blogSystemIndexTargetDataEntityList)) {
                return new RestResult(RestEnum.SUCCESS, indexTargetRespList);
            }

            blogSystemIndexTargetDataEntityList.forEach(e -> {
                indexTargetRespList.add(
                        IndexTargetResp
                                .builder()
                                .index(e.getIndex())
                                .indexChineseName(e.getIndexChineseName())
                                .beforeTreatment(e.getBeforeTreatment())
                                .afterTreatment(e.getAfterTreatment())
                                .governanceFailure(e.getGovernanceFailure())
                                .warehousingSucceeded(e.getWarehousingSucceeded())
                                .warehousingFailed(e.getWarehousingFailed())
                                .removalQuantity(e.getRemovalQuantity())
                                .build()
                );
            });
            return new RestResult(RestEnum.SUCCESS, indexTargetRespList);
        }catch (Exception e) {
            log.error("CommonController.getIndexTargetNum has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth
    @PostMapping("/downloadFile")
    public RestResult downloadFile(@RequestBody SearchReq searchReq, HttpServletResponse response) {
        try {
            return esServiceV2.downloadFile(searchReq, response);
        }catch (Exception e) {
            log.error("CommonController.downloadFile has error:{}",e);
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }
}
