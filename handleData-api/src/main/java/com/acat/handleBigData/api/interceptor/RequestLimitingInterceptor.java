package com.acat.handleBigData.api.interceptor;

import com.acat.handleBigData.api.aop.RequestLimiter;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Slf4j
/**
 * https://www.zhangshengrong.com/p/yOXDZRrjaB/
 */
public class RequestLimitingInterceptor implements HandlerInterceptor {

    private final Map<String, RateLimiter> rateLimiterMap = Maps.newConcurrentMap();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequestLimiter rateLimit = handlerMethod.getMethodAnnotation(RequestLimiter.class);
            if (rateLimit == null) {
                return true;
            }

            //获取请求的url
            String url = request.getRequestURI();
            RateLimiter rateLimiter;
            if (!rateLimiterMap.containsKey(url)) {
                rateLimiter = RateLimiter.create(rateLimit.qps());
                rateLimiterMap.put(url, rateLimiter);
            }

            rateLimiter = rateLimiterMap.get(url);
            boolean acquire = rateLimiter.tryAcquire(rateLimit.timeOut(), rateLimit.timeUnit());
            if (!acquire) {
                log.warn("接口被限流量,url:{}",request.getServletPath());
                throw new RuntimeException("玩命加载中,请稍后再试");
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
