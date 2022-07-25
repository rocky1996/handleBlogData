package com.acat.handleBlogData.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class FieldUtils {

    public static Map<String, String> fieldMap = Maps.newHashMap();
    static {

        /**
         * 公共字段
         */
        fieldMap.put("uuid", "用户唯一ID");
        fieldMap.put("integrity", "完整度");
        fieldMap.put("platform", "来源平台");
        fieldMap.put("data_source", "数据来源");
        fieldMap.put("create_time", "数据入库时间");
        fieldMap.put("importance", "重要等级");
        fieldMap.put("remark", "扩展字段,对重要数据手动编辑的批注信息");
        fieldMap.put("language_type", "语种");
        fieldMap.put("source_id", "社交源id");
        fieldMap.put("user_id", "博主id");
        fieldMap.put("screen_name", "用户名");
        fieldMap.put("use_name", "用户全名");
        fieldMap.put("user_url", "博主url");
        fieldMap.put("user_avatar", "用户头像链接");
        fieldMap.put("local_photo_url", "用户头像本地路径");
        fieldMap.put("gender", "用户性别");
        fieldMap.put("country", "国家");
        fieldMap.put("city", "城市");
        fieldMap.put("user_type", "用户类型");
        fieldMap.put("verified", "是否认证");
        fieldMap.put("followers_count", "粉丝数");
        fieldMap.put("friend_count", "关注数");
        fieldMap.put("post_count", "发文数");
        fieldMap.put("like_count", "收藏数");
        fieldMap.put("source_create_time", "采集时间");
        fieldMap.put("name_userd_before", "曾用名");
        fieldMap.put("mobile", "手机号");
        fieldMap.put("email", "用户邮箱");
        fieldMap.put("user_religion", "宗教信仰");
        fieldMap.put("works", "工作信息");
        fieldMap.put("location", "位置信息");
        fieldMap.put("marriage", "婚姻情况");
        fieldMap.put("home_town", "家乡地址");
        fieldMap.put("user_summary", "用户简介");

        /**
         * twitter
         */
        fieldMap.put("language", "用户语言");
        fieldMap.put("user_web_url", "个人站点");
        fieldMap.put("born_time", "出生日期");
        fieldMap.put("registered_time", "注册时间");
        fieldMap.put("bkgd_url", "背景图片url");
        fieldMap.put("user_flag", "用户描述");
        fieldMap.put("listed", "所属公共列表数(数字)");
        fieldMap.put("moments", "瞬间(数字)");
        fieldMap.put("protected", "是否是锁定账号");
        fieldMap.put("tf_effective", "是否有博文");
        fieldMap.put("time_zone", "用户所在时区");
        fieldMap.put("com_from", "来源");
        fieldMap.put("diff_time", "注册日期");
        fieldMap.put("extend", "扩展信息");

        /**
         * instagram
         */

    }

    public static String getFieldNameFromZh(String key) {
        if (StringUtils.isBlank(key)) {
            return "";
        }

        String fieldName = fieldMap.get(key);
        return StringUtils.isNotBlank(fieldName) ? fieldName : key;
    }
}
