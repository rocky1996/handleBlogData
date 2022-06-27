package main.java.com.acat.handleBlogData.config;

import main.java.com.acat.handleBlogData.interceptor.RequestLimitingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 请求限流
        registry.addInterceptor(new RequestLimitingInterceptor()).addPathPatterns("/blogSystem/openApi/**");
    }
}
