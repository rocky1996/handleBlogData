package com.acat.handleBlogData.controller;

import com.acat.handleBlogData.aop.Auth;
import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.constants.UrlConstants;
import com.acat.handleBlogData.controller.req.SearchDetailReq;
import com.acat.handleBlogData.enums.BatchSearchFieldEnum;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.enums.RestEnum;
import com.acat.handleBlogData.service.esService.EsServiceImpl;
import com.acat.handleBlogData.controller.req.SearchReq;
import com.acat.handleBlogData.controller.resp.SearchResp;
import com.acat.handleBlogData.controller.resp.UserDetailResp;
import com.acat.handleBlogData.util.ReaderFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    public static final String TXT_EXTENSION = ".txt";

    @Auth(required = false)
    @PostMapping("/upload")
    public RestResult upload(HttpServletRequest httpServletRequest,
                            @RequestParam("file") MultipartFile file,
                            Integer mediaSourceCode) {
        try {
            MediaSourceEnum mediaSourceEnum = MediaSourceEnum.getMediaSourceEnum(mediaSourceCode);
            if (mediaSourceEnum == null) {
                return new RestResult<>(RestEnum.MEDIA_SOURCE_ERROR);
            }

            boolean isOk = esService.insertEsData(file, mediaSourceEnum);
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

    @Auth(required = false)
    @PostMapping("/batchQuery")
    public RestResult<SearchResp> batchQuery(HttpServletRequest httpServletRequest,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("searchField") String searchField,
                                             @RequestParam("isParticiple") boolean isParticiple
//                                             @RequestParam("pageNum") Integer pageNum,
//                                             @RequestParam("pageSize") Integer pageSize
    ) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!TXT_EXTENSION.equals(fileType)) {
                return new RestResult<>(RestEnum.FILE_TYPE_ERROR);
            }

//            if (pageNum == null || pageSize == null) {
//                return new RestResult<>(RestEnum.FEN_YE_ERROR);
//            }

            if (StringUtils.isBlank(searchField)) {
                return new RestResult<>(RestEnum.BATCH_QUERY_FIELD_EMPTY);
            }

            if (!isParticiple && BatchSearchFieldEnum.mustDimSearchField().contains(searchField)) {
                return new RestResult<>(RestEnum.FIELD_NOT_SUPPORT_DIM_SEARCH, searchField + "字段不支持精准查询,请改为模糊(分词)查询");
            }

            List<String> fieldList = ReaderFileUtil.readFile(file);
            if (CollectionUtils.isEmpty(fieldList)) {
                return new RestResult<>(RestEnum.BATCH_QUERY_FIELD_LIST_EMPTY);
            }
            if (fieldList.size() > 2000) {
                return new RestResult<>(RestEnum.BATCH_QUERY_FIELD_SIZE_TOO_LARGE);
            }
            return esService.batchQuery(searchField, fieldList, isParticiple);
        }catch (Exception e) {
            log.error("EsController.retrieveDataList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }
}
