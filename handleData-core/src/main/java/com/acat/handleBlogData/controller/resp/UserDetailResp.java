package com.acat.handleBlogData.controller.resp;

import cn.hutool.db.DaoTemplate;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResp {

    /**
     * 媒介
     */
    private MediaTypeResp mediaSource;

    /**
     * 本地用户图像
     */
    private String localPhotoUrl;

    /**
     * 外网用户头像
     */
    private String userAvatar;

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户全名
     */
    private String userQuanName;

    /**
     * 出生日期
     */
    private String bornTime;

    /**
     * 粉丝数
     */
    private String followersCount;

    /**
     * 关注数
     */
    private String friendCount;

    /**
     * 发文数
     */
    private String postCount;

    /**
     * 喜欢数
     */
    private String likeCount;

    /**
     * 数据原始id
     */
    private String dataId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户主页
     */
    private String userHomePage;

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
     * 手机号
     */
    private String phoneNum;

    /**
     * 邮箱
     */
    private String email;

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
     * 数据产生时间
     */
    private String sourceCreateTime;

    /**
     * 用户简介
     */
    private String userSummary;

    /**
     *
     */
    private Map<String, Object> fieldMap = Maps.newHashMap();
//    private List<Field> fieldList;
//
//    @Data
//    @Builder
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class Field{
//
//        private String field;
//
//        private String fieldExplanation;
//    }
}
