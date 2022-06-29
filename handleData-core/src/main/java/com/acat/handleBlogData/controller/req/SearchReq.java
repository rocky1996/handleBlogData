package com.acat.handleBlogData.controller.req;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
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
     * 分页
     */
    private Integer pageNum;

    /**
     * 分页
     */
    private Integer pageSize;

    public SearchReq() {}

    public SearchReq(String userId, String userName, String userQuanName, String phoneNum, String email, String country, String city, Integer mediaType, Date startTime, Date endTime, Integer pageNum, Integer pageSize) {
        this.userId = userId;
        this.userName = userName;
        this.userQuanName = userQuanName;
        this.phoneNum = phoneNum;
        this.email = email;
        this.country = country;
        this.city = city;
        this.mediaType = mediaType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
