package com.acat.handleBlogData.cache;

import com.acat.handleBlogData.dao.UserDao;
import com.acat.handleBlogData.domain.BlogSystemUser;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Component
public class UserCacheService {

    @Resource
    private UserDao userDao;

    private final LoadingCache<Integer, Optional<BlogSystemUser>> userCache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<Integer, Optional<BlogSystemUser>>() {

                @Override
                public Optional<BlogSystemUser> load(Integer id) {
                    return Optional.ofNullable(userDao.selectById(id));
                }
            });

    public BlogSystemUser getUser(Integer id) {
        return userCache.getUnchecked(id).orElse(null);
    }
}
