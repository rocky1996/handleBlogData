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
        fieldMap.put("integrity", "数据完整度");
        fieldMap.put("platform", "来源平台");
        fieldMap.put("data_source", "数据来源");
        fieldMap.put("create_time", "数据入库时间");
        fieldMap.put("importance", "重要等级");
        fieldMap.put("remark", "扩展字段,对重要数据手动编辑的批注信息");
        fieldMap.put("language_type", "语种");
        fieldMap.put("source_id", "数据原始ID");
        fieldMap.put("user_id", "用户唯一标识ID");
        fieldMap.put("screen_name", "用户名");
        fieldMap.put("use_name", "用户全名");
        fieldMap.put("user_url", "用户主页URL");
        fieldMap.put("user_avatar", "用户头像");
        fieldMap.put("local_photo_url", "用户头像本地路径");
        fieldMap.put("gender", "用户性别");
        fieldMap.put("country", "国家");
        fieldMap.put("city", "城市");
        fieldMap.put("user_type", "用户类型");
        fieldMap.put("verified", "用户认证");
        fieldMap.put("followers_count", "粉丝数");
        fieldMap.put("friend_count", "关注数");
        fieldMap.put("post_count", "发文数");
        fieldMap.put("like_count", "喜欢数");
        fieldMap.put("source_create_time", "数据产生时间");
        fieldMap.put("name_userd_before", "曾用名");
        fieldMap.put("mobile", "手机号");
        fieldMap.put("email", "邮箱地址");
        fieldMap.put("user_religion", "宗教信仰");
        fieldMap.put("works", "工作信息");
        fieldMap.put("location", "居住地");
        fieldMap.put("marriage", "婚姻情况");
        fieldMap.put("home_town", "家乡地址");
        fieldMap.put("user_summary", "用户简介");
        fieldMap.put("original_mailbox", "原始邮箱");
        fieldMap.put("original_contact", "原始联系方式");

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
        fieldMap.put("protect_ed", "是否锁定账号");
        fieldMap.put("tf_effective", "是否有博文");
        fieldMap.put("time_zone", "时区");
        fieldMap.put("com_from", "来源");
        fieldMap.put("registration_date", "治理后的注册日期");
        fieldMap.put("extend", "扩展信息");

        /**
         * instagram
         */
        fieldMap.put("external_url", "社交联系方式");
        fieldMap.put("fetch_day", "更新日期");
        fieldMap.put("profile_pic_url_oss", "头像oss地址");
        fieldMap.put("media_url", "媒介URL");
        fieldMap.put("person", "人物标签");
        fieldMap.put("sent_num", "情感度");

        /**
         * facebook
         */
        fieldMap.put("impl_or_history_type", "类型");
        fieldMap.put("user_political_views", "政治观点");
        fieldMap.put("user_systent_name", "用户系统名");
        fieldMap.put("w3_fb_url", "博主地址");
        fieldMap.put("institution_id", "隶属机构ID");
        fieldMap.put("is_community_page", "是否社区page页");
        fieldMap.put("communication_philosophy", "传播理念");
        fieldMap.put("have_product", "拥有产品,企业账号");
        fieldMap.put("exchange_number", "交流人数");
        fieldMap.put("visit_number", "访问人数");
        fieldMap.put("first_name", "名称前缀");
        fieldMap.put("last_name", "名称后缀");
        fieldMap.put("teach_message", "教育信息");
        fieldMap.put("acquisition_time", "采集时间");
        fieldMap.put("affective_state", "情感状态");
        fieldMap.put("background_picture_url", "背景图片");
        fieldMap.put("business_story", "商家故事");
        fieldMap.put("classify_message", "分类信息");
        fieldMap.put("com_from", "来源");
        fieldMap.put("dm_tag1", "人物标签-算法");
        fieldMap.put("company_profile", "公司简介");
        fieldMap.put("country_region", "地理位置信息");
        fieldMap.put("country_region_city", "地理位置信息-城市");
        fieldMap.put("detailed_summary", "详细简介");
        fieldMap.put("favorite_quotes", "格言信息");
        fieldMap.put("found", "成立时间");
        fieldMap.put("gender_orientation", "性取向");
        fieldMap.put("go_through", "经历,生活纪事");
        fieldMap.put("like_number_int", "喜欢人数");
        fieldMap.put("local_profile_pic_background_url", "本地背景图路径");
        fieldMap.put("media_title", "附件媒体文字");
        fieldMap.put("media_type_embeded", "媒介类型");
        fieldMap.put("media_url_name", "媒介名称");
        fieldMap.put("opening_hours", "运营时间");
        fieldMap.put("personal_web_url", "个人网址");
        fieldMap.put("photo_album_url", "相册url地址");
        fieldMap.put("photo_wall", "照片墙链接");
        fieldMap.put("position_message", "工作地点");
        fieldMap.put("register_number","签到数");
        fieldMap.put("diff_time", "治理后的注册日期");
        fieldMap.put("related_home_page", "相关主页信息");
        fieldMap.put("shop_content", "商品信息");
        fieldMap.put("family_and_relation_ships", "家庭成员与感情情况");
        fieldMap.put("skill", "技能");
        fieldMap.put("user_birthday", "出生日期");
        fieldMap.put("user_classify", "用户分类");
        fieldMap.put("user_description", "用户描述");
        fieldMap.put("photo_album_collect", "相册信息");
        fieldMap.put("follower_number_int", "人气数");

        /**
         * fq
         */
        fieldMap.put("contact", "社交联系方式");
        fieldMap.put("alg_remark", "算法标签");
        fieldMap.put("alg_result", "预留字段");
        fieldMap.put("checkins_count", "签到数量");
        fieldMap.put("country_region", "地理位置信息");
        fieldMap.put("home_city", "城市编码");
        fieldMap.put("lenses", "镜头");
        fieldMap.put("lists", "列表");
        fieldMap.put("lists_count", "列表数");
        fieldMap.put("local_user_head_url", "本地头像url");
        fieldMap.put("photo", "图片源数据");
        fieldMap.put("photo_prefix", "头像前缀");
        fieldMap.put("photo_suffix", "头像后缀");
        fieldMap.put("real_name", "人工维护真实姓名");
        fieldMap.put("recent_list", "最近列表");
        fieldMap.put("send_state", "发送状态");
        fieldMap.put("sent", "情感正负面");
        fieldMap.put("sent_num", "情感度");
        fieldMap.put("todo", "计划内容");
        fieldMap.put("top_tips", "top评论");
        fieldMap.put("visibility", "可见性");
        fieldMap.put("vpers", "人物名称分词");
        fieldMap.put("source_key", "关键词来源");
        fieldMap.put("source_venue", "位置来源");
        fieldMap.put("source_venue_id", "位置id来源");
        fieldMap.put("friends", "好友数");

        /**
         * link impl or history
         */
        fieldMap.put("birth_date_date", "出生日期");
        fieldMap.put("description_job_title", "当前职业名称");
        fieldMap.put("description_school_id", "毕业院校");
        fieldMap.put("description_school_name", "毕业学校名");
        fieldMap.put("description_school_logo", "毕业学校logo");
        fieldMap.put("description_school_local_logo", "毕业学校本地logo");
        fieldMap.put("description_company_id", "当前就职企业id");
        fieldMap.put("description_company_name", "当前就职企业名");
        fieldMap.put("description_company_logo", "当前就职企业logo");
        fieldMap.put("description_company_local_logo", "当前就职企业本地logo");
        fieldMap.put("description_location", "居住地址");
        fieldMap.put("industry_name", "所属行业");
        fieldMap.put("summary", "用户简介");
        fieldMap.put("experiences_job_title", "工作经历-职位名称");
        fieldMap.put("experiences_company_id", "工作经历-企业id");
        fieldMap.put("experiences_company_name", "工作经历-公司名称");
        fieldMap.put("experiences_company_logo", "工作经历-公司头像");
        fieldMap.put("experiences_company_local_logo", "工作经历-企业logo");
        fieldMap.put("experiences_company_location", "工作经历-地址名称");
        fieldMap.put("experiences_time_period_time", "工作经历-开始时间+结束时间");
        fieldMap.put("experiences_description", "工作经历-描述");
        fieldMap.put("certifications_name", "资格认证");
        fieldMap.put("certifications_authority", "资格认证");
        fieldMap.put("certifications_license", "资格认证");
        fieldMap.put("certifications_time_period_time", "资格认证时间");
        fieldMap.put("cerfitications_url", "资格认证");
        fieldMap.put("volunteer_experiences_role", "志愿者角色");
        fieldMap.put("volunteer_experiences_company_name", "志愿者单位名");
        fieldMap.put("volunteer_experiences_cause", "志愿原因");
        fieldMap.put("volunteer_experiences_time_period_time", "志愿时间段");
        fieldMap.put("volunteer_experiences_description", "志愿描述");
        fieldMap.put("educations_school_id", "教育经历-学校id");
        fieldMap.put("educations_school_name", "教育经历-学校名称");
        fieldMap.put("educations_school_logo", "教育经历-学校头像");
        fieldMap.put("educations_school_local_logo", "教育经历-学校本地头像");
        fieldMap.put("educations_diplomaqq", "教育经历-学位名称");
        fieldMap.put("educations_subjects", "教育经历-学习专业");
        fieldMap.put("educations_grade", "教育经历-级别");
        fieldMap.put("educations_activity", "教育经历-相关活动");
        fieldMap.put("educations_time_period_time", "教育经历-开始时间+结束时间");
        fieldMap.put("educations_description", "教育经历-描述");
        fieldMap.put("skills_name", "技能认可-技能名称");
        fieldMap.put("skills_endorsement_count", "技能认可-数");
        fieldMap.put("languages_name", "语言名");
        fieldMap.put("languages_proficiency", "精通");
        fieldMap.put("honors_title", "荣誉-名称");
        fieldMap.put("honors_issuer", "荣誉");
        fieldMap.put("honors_time", "荣誉-时间");
        fieldMap.put("honors_description", "荣誉-描述");
        fieldMap.put("organizations_name", "荣誉-组织名");
        fieldMap.put("organizations_time_period_time", "荣誉-时间段");
        fieldMap.put("organizations_description", "荣誉-组织描述");
        fieldMap.put("organizations_location", "荣誉-组织所在地");
        fieldMap.put("patents_title", "专利名");
        fieldMap.put("patents_serial_no", "专利号");
        fieldMap.put("patents_issuer_country_name", "发行国家");
        fieldMap.put("patents_time", "专利时间");
        fieldMap.put("patents_issuer", "专利发行人");
        fieldMap.put("patents_url", "专利链接");
        fieldMap.put("patents_description", "专利描述");
        fieldMap.put("projects_name", "项目名");
        fieldMap.put("projects_url", "项目链接");
        fieldMap.put("projects_time_period_time", "项目时间");
        fieldMap.put("projects_description", "项目描述");
        fieldMap.put("publications_title", "出版物标题");
        fieldMap.put("publications_issuer", "出版物发行人");
        fieldMap.put("publications_url", "出版物发行链接");
        fieldMap.put("publications_time", "出版物发行时间");
        fieldMap.put("contacts_title", "联系方式-标题");
        fieldMap.put("contacts_name", "联系方式-名");
        fieldMap.put("contacts_url", "联系方式-链接");
        fieldMap.put("tags", "博主标签");
        fieldMap.put("coureses_name", "课程名称");
        fieldMap.put("coureses_number", "课程数量");
        fieldMap.put("tests_description", "测试-描述");
        fieldMap.put("tests_name", "测试-名称");
        fieldMap.put("tests_score", "测试-得分");
        fieldMap.put("tests_time_period", "测试-时间");

        /**
         * link_school
         */
        fieldMap.put("cover", "背景图片");
        fieldMap.put("local_cover", "本地背景图片");
        fieldMap.put("staffing_company", "是否是上市公司");
        fieldMap.put("industries", "所属行业");
        fieldMap.put("specialities","领域");
        fieldMap.put("apecialities", "专业");
        fieldMap.put("headquarter_post", "邮编");
        fieldMap.put("headquarter_address", "所在地址");
        fieldMap.put("confirmed_locations_contry", "确认地点国家");
        fieldMap.put("confirmed_locations_city", "确认地点城市");
        fieldMap.put("confirmed_locations_post", "确认地点邮编");
        fieldMap.put("confirmed_locations_address", "确认地点地址");
        fieldMap.put("size", "规模");
        fieldMap.put("founded_on", "成立于");
        fieldMap.put("description", "企业描述（简介）");
        fieldMap.put("website", "官网");
        fieldMap.put("employees", "员工数量");
        fieldMap.put("keywords", "关键词");
        fieldMap.put("tags", "标签");
        fieldMap.put("profile_score", "信息完整度");
        fieldMap.put("source_connection", "资料来源");
        fieldMap.put("source", "资源类别");
        fieldMap.put("timestamp", "时间戳");

    }

    public static String getFieldNameFromZh(String key) {
        if (StringUtils.isBlank(key)) {
            return "";
        }

        String fieldName = fieldMap.get(key);
        return StringUtils.isNotBlank(fieldName) ? fieldName : key;
    }
}
