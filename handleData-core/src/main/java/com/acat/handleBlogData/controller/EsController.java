package com.acat.handleBlogData.controller;

import com.acat.handleBlogData.aop.Auth;
import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.constants.UrlConstants;
import com.acat.handleBlogData.controller.bo.LoginReqBo;
import com.acat.handleBlogData.controller.vo.LoginRespVo;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.enums.RestEnum;
import com.acat.handleBlogData.service.esService.EsServiceImpl;
import com.acat.handleBlogData.service.esService.vo.SearchReq;
import com.acat.handleBlogData.service.esService.vo.SearchResp;
import com.acat.handleBlogData.service.esService.vo.UserDetailResp;
import com.acat.handleBlogData.util.ReaderFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;


@Slf4j
@RestController
@RequestMapping(UrlConstants.BLOG_SYSTEM_ES_URL)
public class EsController {

    @Resource
    private EsServiceImpl esService;

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
    @GetMapping("/retrieveUserDetail")
    public RestResult<UserDetailResp> retrieveUserDetail(@PathVariable("userId") String userId) {

        try {
            if (StringUtils.isBlank(userId)) {
                return new RestResult<>(RestEnum.USER_ID_ERROR);
            }
            return esService.retrieveUserDetail(userId);
        }catch (Exception e) {
            log.error("EsController.retrieveUserDetail has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }
}
