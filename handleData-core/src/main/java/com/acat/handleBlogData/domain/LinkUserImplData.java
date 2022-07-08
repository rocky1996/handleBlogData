//package com.acat.handleBlogData.domain;
//
//import lombok.Builder;
//import lombok.Data;
//import org.springframework.data.elasticsearch.annotations.Document;
//
//import javax.persistence.Id;
//
///**
// * {
// *     "uuid":"",        #用户唯一ID
// *     "platform":"",    #来源平台：FB、FQ、TW、IS、LI
// *     "data_source":"", #数据来源,数据来源文件名
// *     "create_time":"", #数据入库时间
// *     "importance":"",  #重要等级,0：普通 （默认）1：关注 2：重要 3：特别重要
// *     "remark":"",      #扩展字段，对重要数据手动编辑的批注信息
// *     "language_type":"",  #语种
// *     "source_id":"",   #名称唯一ID   对应原字段->  data_id
// *     "user_id":"",     #用户id         ->       profile_id
// *     "screen_name":"", #用户名称        ->       public_id
// *     "use_name":"",    #用户名,         对应原字段->    name
// *     "user_url":"",    #博主url（若为空需要拼接，格式如下：https://www.facebook.com/profile.php?id=100068526609915)  ->    blogger_url
// *     "user_avatar":"", #用户头像链接     对应原字段->    photo
// *     "local_photo_url":"", #用户头像本地路径（若为空需要拼接，格式如下：fb_Info_1614294429_photo.jpg）   ->     local_photo
// *     "gender":"",      #用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）       ->     user_gender
// *     "country":"",     #国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典） ->   country_code
// *     "city":"",        #地址信息（通过currentLocation值抽取城市名称）   ->      current_location
// *     "user_type":"",   #用户类型（个人、公开page、其他）
// *     "verified":"",    #是否认证 1：认证，0：非认证   ->    is_attestation
// *     "followers_count":"",  #粉丝人数-个人/ 关注数- 公共page（粉丝总数）,      ->     description_followers
// *     "friend_count":"",     #关注数      ->      description_connections
// *     "source_create_time":"",      #数据产生的时间，如果没有产生时间，则填写入库时间   ->    timestamp
// *     "mobile":"",          #联系方式（电话号码“-”连接）,     ->     contacts_title_phoneNumbers
// *     "email":"",           #联系方式和基本信息（部分有值，例：Facebook:/tobu.hiroshi.takano|邮箱:celicas2245@gmail.com）  ->    contacts_title_email
// *     "works":"",            #工作信息（数据部分为空）      ->      description_company_name
// *     "location":"",         #地址信息（现居地）         ->       description_location
// *     "last_name":"",       #姓氏
// *     "first_name":"",      #名
// *     "maidern_name":"",    #曾用名
// *     "birth_date_date":"", #出生日月
// *     "cover":"",           #背景
// *     "local_cover":"",     #本地化背景
// *     "description_job_title":"",      #当前职业名称
// *     "description_school_id":"",      #毕业院校
// *     "description_school_name":"",    #毕业学校名
// *     "description_school_logo":"",    #毕业学校logo
// *     "description_school_local_logo":"",   #毕业学校本地logo
// *     "description_company_id":"",     #当前就职企业id
// *     "description_company_name":"",   #当前就职企业名
// *     "description_company_logo":"",   #当前就职企业logo
// *     "description_company_local_logo":"",   #当前就职企业本地logo
// *     "description_location":"",       #居住地址
// *     "industry_name":"",              #所属行业
// *     "summary":"",                    #用户简介
// *     "experiences_job_title":"",      #工作经历
// *     "experiences_company_id":"",     #工作经历
// *     "experiences_company_name":"",   #工作经历
// *     "experiences_company_logo":"",   #工作经历
// *     "experiences_company_local_logo":"",   #工作经历
// *     "experiences_company_location":"",     #工作经历
// *     "experiences_time_period_time":"",     #工作经历
// *     "experiences_description":"",          #工作经历
// *     "certifications_name":"",              #资格认证
// *     "certifications_authority":"",         #资格认证
// *     "certifications_license":"",           #资格认证
// *     "certifications_time_period_time":"",  #资格认证
// *     "cerfitications_url":"",               #资格认证
// *     "volunteer_experiences_role":"",       #志愿者角色
// *     "volunteer_experiences_company_name":"",     #志愿者单位名
// *     "volunteer_experiences_cause":"",      #志愿原因
// *     "volunteer_experiences_time_period_time":"", #志愿时间段
// *     "volunteer_experiences_description":"",#志愿描述
// *     "educations_school_id":"",             #教育经历
// *     "educations_school_name":"",           #教育经历
// *     "educations_school_logo":"",           #教育经历
// *     "educations_school_local_logo":"",     #教育经历
// *     "educations_diplomaqq":"",             #教育经历
// *     "educations_subjects":"",              #教育经历
// *     "educations_grade":"",                 #教育经历
// *     "educations_activity":"",              #教育经历
// *     "educations_time_period_time":"",      #教育经历
// *     "educations_description":"",           #教育经历
// *     "skills_name":"",                      #技能认可
// *     "skills_endorsement_count":"",         #技能认可
// *     "languages_name":"",                   #语言名
// *     "languages_proficiency":"",            #精通
// *     "honors_title":"",                     #荣誉
// *     "honors_issuer":"",                    #荣誉
// *     "honors_time":"",                      #荣誉
// *     "honors_description":"",               #荣誉
// *     "organizations_name":"",               #组织名
// *     "organizations_time_period_time":"",   #时间段
// *     "organizations_description":"",        #组织描述
// *     "organizations_location":"",           #组织所在地
// *     "patents_title":"",                    #专利名
// *     "patents_serial_no":"",                #专利号
// *     "patents_issuer_country_name":"",      #发行国家
// *     "patents_time":"",                     #时间
// *     "patents_issuer":"",                   #发行人
// *     "patents_url":"",                      #专利链接
// *     "patents_description":"",              #专利描述
// *     "projects_name":"",                    #项目名
// *     "projects_url":"",                     #项目链接
// *     "projects_time_period_time":"",        #项目时间
// *     "projects_description":"",             #项目描述
// *     "publications_title":"",               #出版物标题
// *     "publications_issuer":"",              #发行人
// *     "publications_url":"",                 #发行链接
// *     "publications_time":"",                #发行时间
// *     "contacts_title":"",                   #联系方式
// *     "contacts_name":"",                    #联系方式
// *     "contacts_url":"",                     #联系方式
// *     "tags":"",                             #博主标签
// *     "profile_score":"",                    #信息完成度
// *     "coureses_name":"",                    #课程名称
// *     "coureses_number":"",                  #课程数量
// *     "tests_description":"",                #描述
// *     "tests_name":"",                       #名称
// *     "tests_score":"",                      #得分
// *     "tests_time_period":"",                #时间
// *     "person":"",                           #人物
// *     "media_type_embeded":"",               #媒介类型 ‘P’:  图片； ‘M’:  音频； ‘V’:  视频； ‘X’:  附件(文章) ²文章含有媒介时，多个之间用’|’隔开。比如：P|P|V|X media_type 与 media_url 一一对应
// *     "media_url":"",                        #²文章含有媒介时，多个之间用’|’隔开
// *     "media_url_name":"",                   #本地媒体url， 多个用“|”分割
// *     "media_title":"",                      #²  文章含有媒介时，多个之间用’|’隔开。比如：头像|banner|位置图集1|…|位置图集60|发帖图集1|…|发帖图集20|评论图1
// * }
// */
//@Data
//@Builder
//@Document(indexName = "link_impl")
//public class LinkUserImplData {
//
//    /**
//     * 用户唯一ID
//     */
//    @Id
//    private String uuid;
//
//    /**
//     * 来源平台：FB、FQ、TW、IS、LI
//     */
//    private String platform;
//
//    /**
//     * 数据来源,数据来源文件名
//     */
//    private String data_source;
//
//    /**
//     * 数据入库时间
//     */
//    private String create_time;
//
//    /**
//     * 重要等级,0：普通 （默认）1：关注 2：重要 3：特别重要
//     */
//    private String importance;
//
//    /**
//     * 扩展字段，对重要数据手动编辑的批注信息
//     */
//    private String remark;
//
//    /**
//     * 语种
//     */
//    private String language_type;
//
//    /**
//     * 名称唯一ID   对应原字段->  data_id
//     */
//    private String source_id;
//
//    /**
//     * 用户id         ->       profile_id
//     */
//    private String user_id;
//
//    /**
//     * 用户名称        ->       public_id
//     */
//    private String screen_name;
//
//    /**
//     * 用户名,         对应原字段->    name
//     */
//    private String use_name;
//        "user_url":"",    #博主url（若为空需要拼接，格式如下：https://www.facebook.com/profile.php?id=100068526609915)  ->    blogger_url
//    "user_avatar":"", #用户头像链接     对应原字段->    photo
//        "local_photo_url":"", #用户头像本地路径（若为空需要拼接，格式如下：fb_Info_1614294429_photo.jpg）   ->     local_photo
//        "gender":"",      #用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）       ->     user_gender
//        "country":"",     #国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典） ->   country_code
//        "city":"",        #地址信息（通过currentLocation值抽取城市名称）   ->      current_location
//        "user_type":"",   #用户类型（个人、公开page、其他）
//        "verified":"",    #是否认证 1：认证，0：非认证   ->    is_attestation
//        "followers_count":"",  #粉丝人数-个人/ 关注数- 公共page（粉丝总数）,      ->     description_followers
//        "friend_count":"",     #关注数      ->      description_connections
//        "source_create_time":"",      #数据产生的时间，如果没有产生时间，则填写入库时间   ->    timestamp
//        "mobile":"",          #联系方式（电话号码“-”连接）,     ->     contacts_title_phoneNumbers
//        "email":"",           #联系方式和基本信息（部分有值，例：Facebook:/tobu.hiroshi.takano|邮箱:celicas2245@gmail.com）  ->    contacts_title_email
//        "works":"",            #工作信息（数据部分为空）      ->      description_company_name
//        "location":"",         #地址信息（现居地）         ->       description_location
//        "last_name":"",       #姓氏
//        "first_name":"",      #名
//        "maidern_name":"",    #曾用名
//        "birth_date_date":"", #出生日月
//        "cover":"",           #背景
//        "local_cover":"",     #本地化背景
//        "description_job_title":"",      #当前职业名称
//        "description_school_id":"",      #毕业院校
//        "description_school_name":"",    #毕业学校名
//        "description_school_logo":"",    #毕业学校logo
//        "description_school_local_logo":"",   #毕业学校本地logo
//        "description_company_id":"",     #当前就职企业id
//        "description_company_name":"",   #当前就职企业名
//        "description_company_logo":"",   #当前就职企业logo
//        "description_company_local_logo":"",   #当前就职企业本地logo
//        "description_location":"",       #居住地址
//        "industry_name":"",              #所属行业
//        "summary":"",                    #用户简介
//        "experiences_job_title":"",      #工作经历
//        "experiences_company_id":"",     #工作经历
//        "experiences_company_name":"",   #工作经历
//        "experiences_company_logo":"",   #工作经历
//        "experiences_company_local_logo":"",   #工作经历
//        "experiences_company_location":"",     #工作经历
//        "experiences_time_period_time":"",     #工作经历
//        "experiences_description":"",          #工作经历
//        "certifications_name":"",              #资格认证
//        "certifications_authority":"",         #资格认证
//        "certifications_license":"",           #资格认证
//        "certifications_time_period_time":"",  #资格认证
//        "cerfitications_url":"",               #资格认证
//        "volunteer_experiences_role":"",       #志愿者角色
//        "volunteer_experiences_company_name":"",     #志愿者单位名
//        "volunteer_experiences_cause":"",      #志愿原因
//        "volunteer_experiences_time_period_time":"", #志愿时间段
//        "volunteer_experiences_description":"",#志愿描述
//        "educations_school_id":"",             #教育经历
//        "educations_school_name":"",           #教育经历
//        "educations_school_logo":"",           #教育经历
//        "educations_school_local_logo":"",     #教育经历
//        "educations_diplomaqq":"",             #教育经历
//        "educations_subjects":"",              #教育经历
//        "educations_grade":"",                 #教育经历
//        "educations_activity":"",              #教育经历
//        "educations_time_period_time":"",      #教育经历
//        "educations_description":"",           #教育经历
//        "skills_name":"",                      #技能认可
//        "skills_endorsement_count":"",         #技能认可
//        "languages_name":"",                   #语言名
//        "languages_proficiency":"",            #精通
//        "honors_title":"",                     #荣誉
//        "honors_issuer":"",                    #荣誉
//        "honors_time":"",                      #荣誉
//        "honors_description":"",               #荣誉
//        "organizations_name":"",               #组织名
//        "organizations_time_period_time":"",   #时间段
//        "organizations_description":"",        #组织描述
//        "organizations_location":"",           #组织所在地
//        "patents_title":"",                    #专利名
//        "patents_serial_no":"",                #专利号
//        "patents_issuer_country_name":"",      #发行国家
//        "patents_time":"",                     #时间
//        "patents_issuer":"",                   #发行人
//        "patents_url":"",                      #专利链接
//        "patents_description":"",              #专利描述
//        "projects_name":"",                    #项目名
//        "projects_url":"",                     #项目链接
//        "projects_time_period_time":"",        #项目时间
//        "projects_description":"",             #项目描述
//        "publications_title":"",               #出版物标题
//        "publications_issuer":"",              #发行人
//        "publications_url":"",                 #发行链接
//        "publications_time":"",                #发行时间
//        "contacts_title":"",                   #联系方式
//        "contacts_name":"",                    #联系方式
//        "contacts_url":"",                     #联系方式
//        "tags":"",                             #博主标签
//        "profile_score":"",                    #信息完成度
//        "coureses_name":"",                    #课程名称
//        "coureses_number":"",                  #课程数量
//        "tests_description":"",                #描述
//        "tests_name":"",                       #名称
//        "tests_score":"",                      #得分
//        "tests_time_period":"",                #时间
//        "person":"",                           #人物
//        "media_type_embeded":"",               #媒介类型 ‘P’:  图片； ‘M’:  音频； ‘V’:  视频； ‘X’:  附件(文章) ²文章含有媒介时，多个之间用’|’隔开。比如：P|P|V|X media_type 与 media_url 一一对应
//        "media_url":"",                        #²文章含有媒介时，多个之间用’|’隔开
//        "media_url_name":"",                   #本地媒体url， 多个用“|”分割
//        "media_title":"",                      #²  文章含有媒介时，多个之间用’|’隔开。比如：头像|banner|位置图集1|…|位置图集60|发帖图集1|…|发帖图集20|评论图1
//    }
//}
