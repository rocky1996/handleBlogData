package com.acat.handleBlogData.controller;

import com.acat.handleBlogData.aop.Auth;
import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.constants.UrlConstants;
import com.acat.handleBlogData.controller.req.SearchDetailReq;
import com.acat.handleBlogData.controller.resp.SearchBeforeNameResp;
import com.acat.handleBlogData.enums.BatchSearchFieldEnum;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.enums.RestEnum;
import com.acat.handleBlogData.service.esService.EsServiceImpl;
import com.acat.handleBlogData.controller.req.SearchReq;
import com.acat.handleBlogData.controller.resp.SearchResp;
import com.acat.handleBlogData.controller.resp.UserDetailResp;
import com.acat.handleBlogData.service.esService.EsServiceV2Impl;
import com.acat.handleBlogData.util.ReaderFileUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping(UrlConstants.BLOG_SYSTEM_ES_URL)
public class EsController {

    @Resource
    private EsServiceImpl esService;
    @Resource
    private EsServiceV2Impl esServiceV2;

    public static final String TXT_EXTENSION = ".txt";

    public static List<String> statList = Lists.newArrayList("country", "city");

    @Auth(required = false)
    @PostMapping("/upload")
    public RestResult upload(HttpServletRequest httpServletRequest,
                            @RequestParam("file") MultipartFile file,
                            Integer mediaSourceCode,
                            String preGovernanceNum,
                            boolean isNewVersion) {
        try {
            MediaSourceEnum mediaSourceEnum = MediaSourceEnum.getMediaSourceEnum(mediaSourceCode);
            if (mediaSourceEnum == null) {
                return new RestResult<>(RestEnum.MEDIA_SOURCE_ERROR);
            }

            if (!NumberUtils.isNumber(preGovernanceNum)) {
                return new RestResult<>(RestEnum.FIELD_NOT_SUPPORT_DIM_SEARCH, "治理前数字格式不正确！！！");
            }

            boolean isOk = esService.insertEsData(file, mediaSourceEnum, preGovernanceNum, isNewVersion);
            if (isOk) {
                return new RestResult<>(RestEnum.SUCCESS);
            }else {
                return new RestResult<>(RestEnum.FAILED);
            }
        } catch (Exception e) {
            log.error("EsController.upload has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

//    @Auth
    @PostMapping("/retrieveDataList")
    public RestResult<SearchResp> retrieveDataList(@RequestBody SearchReq searchReq) {

        try {
            if (searchReq.getPageNum() == null || searchReq.getPageSize() == null) {
                return new RestResult<>(RestEnum.FEN_YE_ERROR);
            }
            return esServiceV2.searchData(searchReq);
        }catch (Exception e) {
            log.error("EsController.retrieveDataList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

//    @Auth
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
            return esServiceV2.retrieveUserDetail(searchDetailReq);
        }catch (Exception e) {
            log.error("EsController.retrieveUserDetail has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth
    @PostMapping("/batchQuery")
    public RestResult<SearchResp> batchQuery(HttpServletRequest httpServletRequest,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("searchField") String searchField,
                                             @RequestParam("isParticiple") Integer isParticiple,
                                             @RequestParam(required = false, name = "mediaCode") Integer mediaCode,
                                             @RequestParam("pageNum") Integer pageNum,
                                             @RequestParam("pageSize") Integer pageSize
    ) {
        try {
            if (pageNum == null || pageSize == null) {
                return new RestResult<>(RestEnum.FEN_YE_ERROR);
            }

            MediaSourceEnum mediaSourceEnum = MediaSourceEnum.ALL;
            if (mediaCode != null) {
                if (MediaSourceEnum.getMediaSourceEnum(mediaCode) != null) {
                    mediaSourceEnum = MediaSourceEnum.getMediaSourceEnum(mediaCode);
                }
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
            if (fieldList.size() > 500) {
                return new RestResult<>(RestEnum.BATCH_QUERY_FIELD_SIZE_TOO_LARGE);
            }

            boolean flag = false;
            for (String field : fieldList) {
                if (field.contains("/")) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                return new RestResult<>(RestEnum.BATCH_QUERY_FIELD_HAS_SP_CHAR);
            }
            return esServiceV2.batchQuery(searchField, fieldList, isParticiple, mediaSourceEnum, pageNum, pageSize);
        }catch (Exception e) {
            log.error("EsController.retrieveDataList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }


    @Auth(required = false)
    @GetMapping("/queryCountryOrCity")
    public RestResult<List<String>> queryCountryOrCity(String textValue, String fieldName) {

        try {
            if (StringUtils.isBlank(textValue)) {
                return new RestResult<>(RestEnum.TRAN_VALUE_IS_EMPTY.getCode(), "搜索字段不能为空！！！");
            }
            if (StringUtils.isBlank(fieldName)
            || !statList.contains(fieldName)) {
                return new RestResult<>(RestEnum.TRAN_VALUE_IS_EMPTY.getCode(), "国家或城市字段搜索错误！！！");
            }
            return esServiceV2.queryCountryOrCity(textValue, fieldName);
        }catch (Exception e) {
            log.error("EsController.queryCountryOrCity has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth
    @GetMapping("/searchBeforeNameInfo")
    public RestResult<SearchBeforeNameResp> searchBeforeNameInfo(String userId, String userName) {

        try {
            if (StringUtils.isBlank(userId)
                    && StringUtils.isBlank(userName)) {
                return new RestResult<>(RestEnum.TRAN_VALUE_IS_EMPTY.getCode(), "用户Id字段和用户名不能都为空！！！");
            }
            return esServiceV2.searchBeforeNameInfo(userId, userName);
        }catch (Exception e) {
            log.error("EsController.searchBeforeNameInfo has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }
}
