package com.acat.handleBlogData.controller.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchReq {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户全名
     */
    private String userQuanName;

    /**
     * 曾用名
     */
    private String beforeName;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 国家
     */
    private String country;

    /**
     * 城市
     */
    private String city;

    /**
     * 用户简介
     */
    private String userSummary;

    /**
     * 数据来源
     */
    private Integer mediaType;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 是否是分词查询
     * 0:分词
     * 1:精准
     */
    private Integer isParticiple;

    /**
     * 分页
     */
    private Integer pageNum;

    /**
     * 分页
     */
    private Integer pageSize;
}
