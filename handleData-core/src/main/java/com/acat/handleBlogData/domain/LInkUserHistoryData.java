package com.acat.handleBlogData.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * {
 *     "uuid":"",        #用户唯一ID
 *     "platform":"",    #来源平台：FB、FQ、TW、IS、LI
 *     "data_source":"", #数据来源,数据来源文件名
 *     "create_time":"", #数据入库时间
 *     "importance":"",  #重要等级,0：普通 （默认）1：关注 2：重要 3：特别重要
 *     "remark":"",      #扩展字段，对重要数据手动编辑的批注信息
 *     "language_type":"",  #语种
 *     "source_id":"",   #名称唯一ID   对应原字段->  user_id
 *     "user_id":"",     #用户id         ->       public_id
 *     "screen_name":"", #用户名称        ->       public_id
 *     "use_name":"",    #用户名,         对应原字段->    name
 *     "user_url":"",    #博主url（若为空需要拼接，格式如下：https://www.facebook.com/profile.php?id=100068526609915)  ->    blogger_url
 *     "user_avatar":"", #用户头像链接     对应原字段->    avatar_url
 *     "local_photo_url":"", #用户头像本地路径（若为空需要拼接，格式如下：fb_Info_1614294429_photo.jpg）   ->     local_photo
 *     "gender":"",      #用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）       ->     user_gender
 *     "country":"",     #国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典） ->   country_code
 *     "city":"",        #地址信息（通过currentLocation值抽取城市名称）   ->      current_location
 *     "user_type":"",   #用户类型（个人、公开page、其他）
 *     "verified":"",    #是否认证 1：认证，0：非认证   ->    is_attestation
 *     "followers_count":"",  #粉丝人数-个人/ 关注数- 公共page（粉丝总数）,      ->     followings
 *     "mobile":"",          #联系方式（电话号码“-”连接）,     ->     contact
 *     "email":"",           #联系方式和基本信息（部分有值，例：Facebook:/tobu.hiroshi.takano|邮箱:celicas2245@gmail.com）  ->   contact
 *     "works":"",            #工作信息（数据部分为空）      ->      occupation
 *     "location":"",         #地址信息（现居地）         ->       location
 *     "description_school_name":"",    #毕业学校名    ->      education_schoolName
 *     "description_school_logo":"",    #毕业学校logo ->       education.logo
 *     "description_school_local_logo":"",   #毕业学校本地logo ->  education.logo
 *     "description_company_name":"",   #当前就职企业名   ->    occupation
 *     "description_location":"",       #居住地址          ->   location
 *     "industry_name":"",              #所属行业
 *     "summary":"",                    #用户简介
 *     "experiences_job_title":"",      #工作经历          ->   positionview_title
 *     "experiences_company_name":"",   #工作经历          ->   positionview_companyName
 *     "experiences_company_logo":"",   #工作经历          ->   positionview_logo
 *     "experiences_company_location":"",     #工作经历    ->   positionview_locationName
 *     "experiences_time_period_time":"",     #工作经历    ->   positionview_startDateYear/startDateMonth/startDateDay + positionview_startDateYear/startDateMonth/startDateDay
 *     "experiences_description":"",          #工作经历    ->   positionview_description
 *     "certifications_name":"",              #资格认证    ->   certification
 *     "volunteer_experiences_role":"",       #志愿者角色  ->    volunteer_experience
 *     "educations_school_name":"",           #教育经历    ->    education_schoolName
 *     "educations_school_logo":"",           #教育经历    ->    education_logo
 *     "educations_diplomaqq":"",             #教育经历    ->    education_degreeName
 *     "educations_subjects":"",              #教育经历    ->    education_fieldOfStudy
 *     "educations_grade":"",                 #教育经历    ->    education_grade
 *     "educations_activity":"",              #教育经历    ->    education_activities
 *     "educations_time_period_time":"",      #教育经历    ->    education_startDateYea + education_endDateYear
 *     "educations_description":"",           #教育经历    ->    education_description
 *     "skills_name":"",                      #技能认可 ->     userskill_skill
 *     "languages_name":"",                   #语言名  ->      language
 *     "honors_title":"",                     #荣誉    ->      honor
 *     "contacts_title":"",                   #联系方式 ->      contact
 *     "tags":"",                             #类别    ->      category
 * }
 */
@Data
@Builder
@Document(indexName = "link_history")
public class LInkUserHistoryData {

    /**
     * 用户唯一ID
     */
    @Id
    private String uuid;

    /**
     * 来源平台：FB、FQ、TW、IS、LI
     */
    private String platform;

    /**
     * 数据来源,数据来源文件名
     */
    private String data_source;

    /**
     * 数据入库时间
     */
    private String create_time;

    /**
     * 重要等级,0：普通 （默认）1：关注 2：重要 3：特别重要
     */
    private String importance;

    /**
     * 扩展字段，对重要数据手动编辑的批注信息
     */
    private String remark;

    /**
     * 语种
     */
    private String language_type;

    /**
     * 名称唯一ID   对应原字段->  user_id
     */
    private String source_id;

    /**
     * 用户id         ->       public_id
     */
 //   @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String user_id;

    /**
     * 用户名称        ->       public_id
     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String screen_name;

    /**
     * 用户名,         对应原字段->    name
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String use_name;

    /**
     * 博主url（若为空需要拼接，格式如下：https://www.facebook.com/profile.php?id=100068526609915)  ->    blogger_url
     */
    private String user_url;

    /**
     * 用户头像链接     对应原字段->    avatar_url
     */
    private String user_avatar;

    /**
     * 用户头像本地路径（若为空需要拼接，格式如下：fb_Info_1614294429_photo.jpg）   ->     local_photo
     */
    private String local_photo_url;

    /**
     * 用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）       ->     user_gender
     */
    private String gender;

    /**
     * 国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典） ->   country_code
     */
 //   @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String country;

    /**
     * 地址信息（通过currentLocation值抽取城市名称）   ->      current_location
     */
 //   @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String city;

    /**
     * 用户类型（个人、公开page、其他）
     */
    private String user_type;

    /**
     * 是否认证 1：认证，0：非认证   ->    is_attestation
     */
    private String verified;

    /**
     * 粉丝人数-个人/ 关注数- 公共page（粉丝总数）,      ->     followings
     */
    private String followers_count;

    /**
     * 联系方式（电话号码“-”连接）,     ->     contact
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String mobile;

    /**
     * 联系方式和基本信息（部分有值，例：Facebook:/tobu.hiroshi.takano|邮箱:celicas2245@gmail.com）  ->   contact
     */
 //   @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String email;

    /**
     * 工作信息（数据部分为空）      ->      occupation
     */
    private String works;

    /**
     * 地址信息（现居地）         ->       location
     */
    private String location;

    /**
     * 毕业学校名    ->      education_schoolName
     */
    private String description_school_name;

    /**
     * 毕业学校logo ->       education.logo
     */
    private String description_school_logo;

    /**
     * 毕业学校本地logo ->  education.logo
     */
    private String description_school_local_logo;

    /**
     * 当前就职企业名   ->    occupation
     */
    private String description_company_name;

    /**
     * 居住地址          ->   location
     */
    private String description_location;

    /**
     * 所属行业
     */
    private String industry_name;

    /**
     * 用户简介
     */
    private String summary;

    /**
     * 工作经历          ->   positionview_title
     */
    private String experiences_job_title;

    /**
     * 工作经历          ->   positionview_companyName
     */
    private String experiences_company_name;

    /**
     * 工作经历          ->   positionview_logo
     */
    private String experiences_company_logo;

    /**
     * 工作经历    ->   positionview_locationName
     */
    private String experiences_company_location;

    /**
     * 工作经历    ->   positionview_startDateYear/startDateMonth/startDateDay + positionview_startDateYear/startDateMonth/startDateDay
     */
    private String experiences_time_period_time;

    /**
     * 工作经历    ->   positionview_description
     */
    private String experiences_description;

    /**
     * 资格认证    ->   certification
     */
    private String certifications_name;

    /**
     * 志愿者角色  ->    volunteer_experience
     */
    private String volunteer_experiences_role;

    /**
     * 教育经历    ->    education_schoolName
     */
    private String educations_school_name;

    /**
     * 教育经历    ->    education_logo
     */
    private String educations_school_logo;

    /**
     * 教育经历    ->    education_degreeName
     */
    private String educations_diplomaqq;

    /**
     * 教育经历    ->    education_fieldOfStudy
     */
    private String educations_subjects;

    /**
     * 教育经历    ->    education_grade
     */
    private String educations_grade;

    /**
     * 教育经历    ->    education_activities
     */
    private String educations_activity;

    /**
     * 教育经历    ->    education_startDateYea + education_endDateYear
     */
    private String educations_time_period_time;

    /**
     * 教育经历    ->    education_description
     */
    private String educations_description;

    /**
     * 技能认可 ->     userskill_skill
     */
    private String skills_name;

    /**
     * 语言名  ->      language
     */
    private String languages_name;

    /**
     * 荣誉    ->      honor
     */
    private String honors_title;

    /**
     * 联系方式 ->      contact
     */
    private String contacts_title;

    /**
     * 类别    ->      category
     */
    private String tags;

    public LInkUserHistoryData() {}

    public LInkUserHistoryData(String uuid, String platform, String data_source, String create_time, String importance, String remark, String language_type, String source_id, String user_id, String screen_name, String use_name, String user_url, String user_avatar, String local_photo_url, String gender, String country, String city, String user_type, String verified, String followers_count, String mobile, String email, String works, String location, String description_school_name, String description_school_logo, String description_school_local_logo, String description_company_name, String description_location, String industry_name, String summary, String experiences_job_title, String experiences_company_name, String experiences_company_logo, String experiences_company_location, String experiences_time_period_time, String experiences_description, String certifications_name, String volunteer_experiences_role, String educations_school_name, String educations_school_logo, String educations_diplomaqq, String educations_subjects, String educations_grade, String educations_activity, String educations_time_period_time, String educations_description, String skills_name, String languages_name, String honors_title, String contacts_title, String tags) {
        this.uuid = uuid;
        this.platform = platform;
        this.data_source = data_source;
        this.create_time = create_time;
        this.importance = importance;
        this.remark = remark;
        this.language_type = language_type;
        this.source_id = source_id;
        this.user_id = user_id;
        this.screen_name = screen_name;
        this.use_name = use_name;
        this.user_url = user_url;
        this.user_avatar = user_avatar;
        this.local_photo_url = local_photo_url;
        this.gender = gender;
        this.country = country;
        this.city = city;
        this.user_type = user_type;
        this.verified = verified;
        this.followers_count = followers_count;
        this.mobile = mobile;
        this.email = email;
        this.works = works;
        this.location = location;
        this.description_school_name = description_school_name;
        this.description_school_logo = description_school_logo;
        this.description_school_local_logo = description_school_local_logo;
        this.description_company_name = description_company_name;
        this.description_location = description_location;
        this.industry_name = industry_name;
        this.summary = summary;
        this.experiences_job_title = experiences_job_title;
        this.experiences_company_name = experiences_company_name;
        this.experiences_company_logo = experiences_company_logo;
        this.experiences_company_location = experiences_company_location;
        this.experiences_time_period_time = experiences_time_period_time;
        this.experiences_description = experiences_description;
        this.certifications_name = certifications_name;
        this.volunteer_experiences_role = volunteer_experiences_role;
        this.educations_school_name = educations_school_name;
        this.educations_school_logo = educations_school_logo;
        this.educations_diplomaqq = educations_diplomaqq;
        this.educations_subjects = educations_subjects;
        this.educations_grade = educations_grade;
        this.educations_activity = educations_activity;
        this.educations_time_period_time = educations_time_period_time;
        this.educations_description = educations_description;
        this.skills_name = skills_name;
        this.languages_name = languages_name;
        this.honors_title = honors_title;
        this.contacts_title = contacts_title;
        this.tags = tags;
    }
}
