package main.java.com.acat.handleBlogData.aop;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestLimiter {

    double qps() default 10D;

    long timeOut() default 500;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    String msg() default "请稍后重试";
}
