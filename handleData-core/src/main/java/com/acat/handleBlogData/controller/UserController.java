package com.acat.handleBlogData.controller;

import com.acat.handleBlogData.aop.Auth;
import com.acat.handleBlogData.constants.RestResult;
import com.acat.handleBlogData.constants.UrlConstants;
import com.acat.handleBlogData.controller.req.LoginReqBo;
import com.acat.handleBlogData.controller.req.UserReq;
import com.acat.handleBlogData.controller.resp.LoginRespVo;
import com.acat.handleBlogData.domain.entity.BlogSystemUserEntity;
import com.acat.handleBlogData.enums.RestEnum;
import com.acat.handleBlogData.enums.StatusEnum;
import com.acat.handleBlogData.service.tokenService.TokenServiceImpl;
import com.acat.handleBlogData.service.UserService;
import com.acat.handleBlogData.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(UrlConstants.BLOG_SYSTEM_USER_URL)
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private TokenServiceImpl tokenServiceImpl;

//    @Auth(required = false)
    @GetMapping("/getAllUser")
    public RestResult<List<BlogSystemUserEntity>> getAllUser(HttpServletRequest httpServletRequest) {
        try {
            List<BlogSystemUserEntity> blogSystemUserEntityList = userService.getAllUser();
            return new RestResult<>(RestEnum.SUCCESS, blogSystemUserEntityList);
        }catch (Exception e) {
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

//    @Auth(required = false)
    @PostMapping("/addAccount")
    public RestResult addAccount(HttpServletRequest httpServletRequest,
                                  @RequestBody UserReq userReq) {
        try {
            if (StringUtils.isBlank(userReq.getUserName())) {
                return new RestResult<>(RestEnum.USERNAME_EMPTY_PARAM);
            }
            if (StringUtils.isBlank(userReq.getPassWord())) {
                return new RestResult<>(RestEnum.PASSWORD_EMPTY_PARAM);
            }
            if (StringUtils.isBlank(userReq.getUserNickname())) {
                return new RestResult<>(RestEnum.NICK_NAME_EMPTY);
            }

            List<BlogSystemUserEntity> blogSystemUserEntityList = userService.getUserByNameAndPassword(userReq.getUserName(), userReq.getPassWord());
            if (!CollectionUtils.isEmpty(blogSystemUserEntityList)) {
                return new RestResult<>(RestEnum.USER_IS_EMPTY.getCode(), "该用户已经存在,请勿重复添加！！！");
            }
            userService.addOrUpdate(userReq);
            return new RestResult<>(RestEnum.SUCCESS);
        }catch (Exception e) {
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

//    @Auth(required = false)
    @GetMapping("/updateUserStatus")
    public RestResult updateUserStatus(Integer userId, Integer statusCode) {

        try {
            BlogSystemUserEntity blogSystemUser = userService.getUserById(userId);
            if (blogSystemUser == null) {
                return new RestResult<>(RestEnum.USER_IS_EMPTY);
            }

            StatusEnum statusEnum = StatusEnum.getStatusEnum(statusCode);
            if (statusEnum == null) {
                return new RestResult<>(RestEnum.USER_IS_EMPTY.getCode(), "用户状态参数错误,标准状态,0:开启,1:关闭");
            }

            BlogSystemUserEntity updateUser = new BlogSystemUserEntity();
            updateUser.setId(userId);
            updateUser.setUsername(blogSystemUser.getUsername());
            updateUser.setPassword(blogSystemUser.getPassword());
            updateUser.setUserNickname(blogSystemUser.getUserNickname());
            updateUser.setIsFlag(statusCode);
            updateUser.setCreateTime(blogSystemUser.getCreateTime());
            updateUser.setUpdateTime(new Date());
            userService.updateUserStatus(updateUser);
            return new RestResult<>(RestEnum.SUCCESS);
        }catch (Exception e) {
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

//    @Auth(required = false)
    @GetMapping("/updatePassword")
    public RestResult updateUserStatus(String userName, String password, String newPassword) {

        try {

            if (StringUtils.isBlank(userName)) {
                return new RestResult<>(RestEnum.USERNAME_EMPTY_PARAM);
            }
            if (StringUtils.isBlank(password)) {
                return new RestResult<>(RestEnum.PASSWORD_EMPTY_PARAM);
            }
            if (StringUtils.isBlank(newPassword)) {
                return new RestResult<>(RestEnum.NICK_NAME_EMPTY.getCode(), "新密码不能为空！！！");
            }
            if (password.equals(newPassword)) {
                return new RestResult<>(RestEnum.NICK_NAME_EMPTY.getCode(), "新老密码不能一致,请重新修改");
            }

            LoginRespVo loginRespVo = userService.login(userName, password);
            if (loginRespVo == null) {
                return new RestResult<>(RestEnum.USER_IS_EMPTY);
            }

            BlogSystemUserEntity updateUser = new BlogSystemUserEntity();
            updateUser.setId(loginRespVo.getId());
            updateUser.setUsername(loginRespVo.getUserName());
            updateUser.setPassword(newPassword);
            updateUser.setUserNickname(loginRespVo.getUserNickname());
            updateUser.setIsFlag(loginRespVo.getIsFlag());
            updateUser.setCreateTime(loginRespVo.getCreateTime());
            updateUser.setUpdateTime(new Date());
            userService.updateUserStatus(updateUser);
            return new RestResult<>(RestEnum.SUCCESS);
        }catch (Exception e) {
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @GetMapping("/deleteUser")
    public RestResult deleteUser(Integer userId) {
        try {
            if (userId == null) {
                return new RestResult<>(RestEnum.USER_ID_ERROR);
            }
            userService.deleteUser(userId);
            return new RestResult<>(RestEnum.SUCCESS);
        }catch (Exception e) {
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @PostMapping("/login")
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
            if (Objects.isNull(loginRespVo)) {
                return new RestResult<>(RestEnum.USER_NOT_EXISTS);
            }

//            String token = tokenServiceImpl.getToken(loginRespVo);
            String token = JwtUtils.sign(loginRespVo);
            loginRespVo.setToken(token);
//            httpServletRequest.getSession().setAttribute("token", token);
            return new RestResult<>(RestEnum.SUCCESS, loginRespVo);
        } catch (Exception e) {
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }

    @Auth(required = false)
    @PostMapping("/logout")
    public RestResult logout(HttpServletRequest httpServletRequest) {
        try {
            httpServletRequest.getSession().removeAttribute("token");
            httpServletRequest.getSession().invalidate();
            return new RestResult<>(RestEnum.SUCCESS);
        } catch (Exception e) {
            return new RestResult<>(RestEnum.FAILED.getCode(), e.getMessage(), null);
        }
    }
}
