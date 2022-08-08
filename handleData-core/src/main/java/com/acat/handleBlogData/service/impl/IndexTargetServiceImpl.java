package com.acat.handleBlogData.service.impl;

import com.acat.handleBlogData.dao.IndexTargetDao;
import com.acat.handleBlogData.domain.entity.BlogSystemIndexTargetDataEntity;
import com.acat.handleBlogData.service.IndexTargetService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class IndexTargetServiceImpl implements IndexTargetService {

    @Resource
    private IndexTargetDao indexTargetDao;

    @Override
    public List<BlogSystemIndexTargetDataEntity> getAll() {
        List<BlogSystemIndexTargetDataEntity> blogSystemIndexTargetDataEntityList = indexTargetDao.getAllIndexEntity();
        return CollectionUtils.isEmpty(blogSystemIndexTargetDataEntityList) ? Lists.newArrayList() : blogSystemIndexTargetDataEntityList;
    }
}
