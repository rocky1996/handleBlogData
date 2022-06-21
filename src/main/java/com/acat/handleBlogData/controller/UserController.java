package com.acat.handleBlogData.controller;

import com.acat.handleBlogData.aop.Auth;
import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.constants.UrlConstants;
import com.acat.handleBlogData.controller.bo.LoginReqBo;
import com.acat.handleBlogData.controller.vo.LoginRespVo;
import com.acat.handleBlogData.enums.RestEnum;
import com.acat.handleBlogData.service.TokenService;
import com.acat.handleBlogData.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(UrlConstants.USER_URL)
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private TokenService tokenService;

    @Auth(required = false)
    @PostMapping("/loginUser")
    public RestResult<LoginRespVo> login(HttpServletRequest httpServletRequest,
                                         @RequestBody LoginReqBo loginReqBo) {
        try {
            if (StringUtils.isBlank(loginReqBo.getUsername())) {
                return new RestResult<>(RestEnum.USERNAME_EMPTY_PARAM);
            }
            if (StringUtils.isBlank(loginReqBo.getPassword())) {
                return new RestResult<>(RestEnum.PASSWORD_EMPTY_PARAM);
            }

            LoginRespVo loginRespVo = userService.login(loginReqBo.getUsername(), loginReqBo.getPassword());
            if (loginReqBo == null) {
                return new RestResult<>(RestEnum.USER_NOT_EXISTS);
            }

            String token = tokenService.getToken(loginRespVo);
            httpServletRequest.getSession().setAttribute("token", token);
            return new RestResult<>(RestEnum.SUCCESS, loginRespVo);
        } catch (Exception e) {
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth
    @GetMapping("/getMessage")
    public Object getMessage(){
        return new RestResult<>(RestEnum.SUCCESS);
    }
}
