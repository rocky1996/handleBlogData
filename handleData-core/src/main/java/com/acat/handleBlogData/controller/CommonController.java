package com.acat.handleBlogData.controller;

import com.acat.handleBlogData.aop.Auth;
import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.constants.UrlConstants;
import com.acat.handleBlogData.controller.resp.*;
import com.acat.handleBlogData.enums.BatchSearchFieldEnum;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.enums.RestEnum;
import com.acat.handleBlogData.outerService.outerInterface.TranslateOuterServiceImpl;
import com.acat.handleBlogData.service.esService.EsServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(UrlConstants.BLOG_SYSTEM_COMMON)
public class CommonController {

    @Resource
    private EsServiceImpl esService;
    @Resource
    private TranslateOuterServiceImpl translateOuterService;

    private static final String ZH = "zh";

    @Auth(required = false)
    @GetMapping("/getCountryList")
    public RestResult<SearchCountryResp> getCountryList() {

        try {
            return esService.getCountryList();
        }catch (Exception e) {
            log.error("CommonController.getCountryList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @GetMapping("/getCityList")
    public RestResult<SearchCityResp> getCityList() {

        try {
            return esService.getCityList();
        }catch (Exception e) {
            log.error("CommonController.getCityList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @GetMapping("/getMediaTypeList")
    public RestResult<List<MediaTypeResp>> getMediaTypeList() {

        try {
            return new RestResult(RestEnum.SUCCESS,
                    Arrays.stream(MediaSourceEnum.values()).map(e ->
                            MediaTypeResp
                                    .builder()
                                    .code(e.getCode())
                                    .desc(e.getDesc())
                                    .totalSize(esService.getMediaIndexSize(e))
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
                                                    .build())
                                    .collect(Collectors.toList()))
                    .build());
        }catch (Exception e) {
            log.error("CommonController.getBatchQueryField has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @GetMapping("/getTranResult")
    public RestResult<TranResp> getTranResult(String tranValue) {
        try {
            if (StringUtils.isBlank(tranValue)) {
                return new RestResult<>(RestEnum.TRAN_VALUE_IS_EMPTY);
            }

            String languageType = translateOuterService.getLanguageDelectResult(tranValue);
            if (StringUtils.isBlank(languageType)) {
                return new RestResult<>(RestEnum.SERVICE_IS_ERROR);
            }

            if (ZH.equals(languageType)) {
                return new RestResult<>(RestEnum.SUCCESS, TranResp.builder().tranTextValue(tranValue).build());
            }
            return new RestResult(RestEnum.SUCCESS, TranResp.builder().tranTextValue(translateOuterService.getTranslateValue(languageType, tranValue)).build());
        }catch (Exception e) {
            log.error("CommonController.getBatchQueryField has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }
}
