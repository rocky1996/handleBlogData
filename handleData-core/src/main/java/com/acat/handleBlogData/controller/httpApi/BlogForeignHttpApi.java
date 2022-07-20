package com.acat.handleBlogData.controller.httpApi;

import com.acat.handleBlogData.aop.Auth;
import com.acat.handleBlogData.aop.RequestLimiter;
import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.constants.UrlConstants;
import com.acat.handleBlogData.controller.req.SearchDetailReq;
import com.acat.handleBlogData.controller.req.SearchReq;
import com.acat.handleBlogData.controller.resp.*;
import com.acat.handleBlogData.enums.BatchSearchFieldEnum;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.enums.RestEnum;
import com.acat.handleBlogData.outerService.outerInterface.TranslateOuterServiceImpl;
import com.acat.handleBlogData.service.esService.EsServiceImpl;
import com.acat.handleBlogData.util.ReaderFileUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(UrlConstants.BLOG_SYSTEM_OPEN_API_URL)
public class BlogForeignHttpApi {

    @Resource
    private EsServiceImpl esService;
    @Resource
    private TranslateOuterServiceImpl translateOuterService;
    public static final String TXT_EXTENSION = ".txt";
    private static final String ZH = "zh";

    @RequestLimiter
    @Auth
    @GetMapping("/getCountryList")
    public RestResult<SearchCountryResp> getCountryList() {

        try {
            return esService.getCountryList();
        }catch (Exception e) {
            log.error("CommonController.getCountryList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @RequestLimiter
    @Auth
    @GetMapping("/getCityList")
    public RestResult<SearchCityResp> getCityList() {

        try {
            return esService.getCityList();
        }catch (Exception e) {
            log.error("CommonController.getCityList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @RequestLimiter
    @Auth
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

    @RequestLimiter
    @Auth
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

    @RequestLimiter
    @Auth
    @PostMapping("/getTranResult")
    public RestResult<TranResp> getTranResult(@RequestBody Map<String, String> tranMap) {
        try {
            Map<String, Object> tranResultMap = Maps.newHashMap();
            for (String key : tranMap.keySet()) {
                String tranValue = tranMap.get(key);
                if (StringUtils.isBlank(tranValue)) {
                    //tranList.add(ImmutableMap.of(key, ""));
                    tranResultMap.put(key, "");
                    continue;
                }

                String languageType = translateOuterService.getLanguageDelectResult(tranValue);
                if (StringUtils.isBlank(languageType)) {
//                    tranList.add(ImmutableMap.of(key, ""));
                    tranResultMap.put(key, "");
                    continue;
                }

                if (ZH.equals(languageType)) {
//                    tranList.add(ImmutableMap.of(key, tranValue));
                    tranResultMap.put(key, "");
                    continue;
                }
//                tranList.add(ImmutableMap.of(key, translateOuterService.getTranslateValue(languageType, tranValue)));
                tranResultMap.put(key, translateOuterService.getTranslateValue(languageType, tranValue));
            }
            return new RestResult(RestEnum.SUCCESS, TranResp.builder().tranMap(tranResultMap).build());
        }catch (Exception e) {
            log.error("CommonController.getBatchQueryField has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @RequestLimiter
    @Auth
    @PostMapping("/retrieveDataList")
    public RestResult<SearchResp> retrieveDataList(@RequestBody SearchReq searchReq) {

        try {
            if (searchReq.getPageNum() == null || searchReq.getPageSize() == null) {
                return new RestResult<>(RestEnum.FEN_YE_ERROR);
            }
            return esService.searchData(searchReq);
        }catch (Exception e) {
            log.error("EsController.retrieveDataList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @RequestLimiter
    @Auth
    @PostMapping("/retrieveUserDetail")
    public RestResult<UserDetailResp> retrieveUserDetail(@RequestBody SearchDetailReq searchDetailReq) {

        try {
            if (Objects.isNull(searchDetailReq.getMediaCode())
                    || StringUtils.isBlank(searchDetailReq.getUuid())) {
                return new RestResult<>(RestEnum.PARAM_IS_NOT_EMPTY);
            }

            if (MediaSourceEnum.getMediaSourceEnum(searchDetailReq.getMediaCode()) == null) {
                return new RestResult<>(RestEnum.MEDIA_SOURCE_ERROR);
            }
            return esService.retrieveUserDetail(searchDetailReq);
        }catch (Exception e) {
            log.error("EsController.retrieveUserDetail has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @RequestLimiter
    @Auth
    @PostMapping("/batchQuery")
    public RestResult<SearchResp> batchQuery(HttpServletRequest httpServletRequest,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("searchField") String searchField,
                                             @RequestParam("isParticiple") Integer isParticiple,
                                             @RequestParam("pageNum") Integer pageNum,
                                             @RequestParam("pageSize") Integer pageSize
    ) {
        try {
            if (pageNum == null || pageSize == null) {
                return new RestResult<>(RestEnum.FEN_YE_ERROR);
            }

            String originalFilename = file.getOriginalFilename();
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!TXT_EXTENSION.equals(fileType)) {
                return new RestResult<>(RestEnum.FILE_TYPE_ERROR);
            }

            if (StringUtils.isBlank(searchField)) {
                return new RestResult<>(RestEnum.BATCH_QUERY_FIELD_EMPTY);
            }

            if (isParticiple.equals(1) && BatchSearchFieldEnum.mustDimSearchField().contains(searchField)) {
                return new RestResult<>(RestEnum.FIELD_NOT_SUPPORT_DIM_SEARCH, searchField + "字段不支持精准查询,请改为模糊(分词)查询");
            }

            List<String> fieldList = ReaderFileUtil.readFile(file);
            if (CollectionUtils.isEmpty(fieldList)) {
                return new RestResult<>(RestEnum.BATCH_QUERY_FIELD_LIST_EMPTY);
            }
            if (fieldList.size() > 1000) {
                return new RestResult<>(RestEnum.BATCH_QUERY_FIELD_SIZE_TOO_LARGE);
            }
            return esService.batchQuery(searchField, fieldList, isParticiple, pageNum, pageSize);
        }catch (Exception e) {
            log.error("EsController.retrieveDataList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }
}
