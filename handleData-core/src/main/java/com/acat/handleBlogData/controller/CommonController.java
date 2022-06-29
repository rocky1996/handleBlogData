package com.acat.handleBlogData.controller;

import com.acat.handleBlogData.aop.Auth;
import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.constants.UrlConstants;
import com.acat.handleBlogData.enums.RestEnum;
import com.acat.handleBlogData.service.esService.EsServiceImpl;
import com.acat.handleBlogData.service.esService.vo.SearchCityResp;
import com.acat.handleBlogData.service.esService.vo.SearchCountryResp;
import com.acat.handleBlogData.service.esService.vo.UserDetailResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(UrlConstants.BLOG_SYSTEM_COMMON)
public class CommonController {

    @Resource
    private EsServiceImpl esService;

    @Auth
    @GetMapping("/getCountryList")
    public RestResult<List<SearchCountryResp>> getCountryList() {

        try {
            return esService.getCountryList();
        }catch (Exception e) {
            log.error("CommonController.getCountryList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth
    @GetMapping("/getCityList")
    public RestResult<List<SearchCityResp>> getCityList() {

        try {
            return esService.getCityList();
        }catch (Exception e) {
            log.error("CommonController.getCityList has error:{}",e.getMessage());
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }
}
