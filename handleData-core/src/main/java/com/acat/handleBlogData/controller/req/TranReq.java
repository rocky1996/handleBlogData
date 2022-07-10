package com.acat.handleBlogData.controller.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranReq {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户全名
     */
    private String userQuanName;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户认证
     */
    private String verified;

    /**
     * 曾用名
     */
    private String nameUserdBefore;

    /**
     * 婚姻情况
     */
    private String marriage;

    /**
     * 国家
     */
    private String country;

    /**
     * 城市
     */
    private String city;

    /**
     * 宗教信仰
     */
    private String userReligion;

    /**
     * 工作信息
     */
    private String works;

    /**
     * 位置信息
     */
    private String positionMessage;

    /**
     * 家乡地址
     */
    private String homeAddress;

    /**
     * 用户语言
     */
    private String language;

    /**
     * 用户简介
     */
    private String userSummary;
}
