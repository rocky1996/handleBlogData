package com.acat.handleBlogData.controller.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResp {

    /**
     * 总数
     */
    public Long totalSize;

    public List<UserData> dataList;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserData {

        /**
         * 用户id
         */
        private String userId;

        /**
         * 用户id
         */
        private String uuid;

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
         * 用户主页
         */
        private String userHomePage;

        /**
         * 用户性别
         */
        private String gender;

        /**
         * 婚姻情况
         */
        private String marriage;

        /**
         * 粉丝数
         */
        private String followersCount;

        /**
         * 关注数
         */
        private String friendCount;

        /**
         * 曾用名
         */
        private String maidernName;

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
         * 数据来源
         */
        private MediaTypeResp mediaTypeResp;
    }
}
