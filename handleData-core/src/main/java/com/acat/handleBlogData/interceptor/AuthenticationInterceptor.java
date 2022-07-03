package com.acat.handleBlogData.interceptor;

import com.acat.handleBlogData.aop.Auth;
import com.acat.handleBlogData.cache.UserCacheService;
//import com.acat.handleBlogData.domain.BlogSystemUser;
import com.acat.handleBlogData.controller.resp.LoginRespVo;
import com.acat.handleBlogData.dao.UserDao;
import com.acat.handleBlogData.domain.entity.BlogSystemUserEntity;
import com.acat.handleBlogData.util.JwtUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

//    @Resource
//    private UserCacheService userCacheService;
    @Autowired
    private UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();

        String token = request.getHeader("token");
//        String token = request.getSession().getAttribute("token") != null ? request.getSession().getAttribute("token").toString() : null;
        if (method.isAnnotationPresent(Auth.class)) {
            Auth auth = method.getAnnotation(Auth.class);
            if (!auth.required()) {
                return true;
            }else {
                if (StringUtils.isBlank(token)) {
                    throw new RuntimeException("您尚未登录，请先登录！！！");
                }

                try {
//                    String userId = JwtUtils.getUserId(token);
//                    BlogSystemUserEntity blogSystemUser = userDao.selectById(Integer.parseInt(userId));
//                    if (blogSystemUser == null) {
//                        throw new RuntimeException("该用户不存在，请重新登录");
//                    }
                    JwtUtils.checkSign(token);
                }catch (JWTDecodeException e) {
                    log.error("AuthenticationInterceptor.preHandle has error",e.getMessage());
                    throw new RuntimeException("token认证名称错误，请重新登录");
                }
            }
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex) throws Exception {

    }
}
