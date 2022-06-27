package com.acat.handleBlogData.domain.esDb;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 *
 "uuid":"",        #用户唯一ID
 "platform":"",    #来源平台：FB、FQ、TW、IS、LI
 "data_source":"", #数据来源,数据来源文件名
 "create_time":"", #数据入库时间
 "importance":"",  #重要等级,0：普通 （默认）1：关注 2：重要 3：特别重要
 "remark":"",      #扩展字段，对重要数据手动编辑的批注信息
 "language_type":"",  #语种
 "source_id":"",      #唯一指纹,对应表里的   ->   md5_id
 "user_id":"",     #用户id
 "screen_name":"", #用户名称
 "use_name":"",        #博主名,      ->      name
 "user_url":"",    #博主url（若为空需要拼接，格式如下：https://www.facebook.com/profile.php?id=100068526609915)
 "user_avatar":"", #用户头像链接
 "local_photo_url":"", #用户头像本地路径（若为空需要拼接，格式如下：fb_Info_1614294429_photo.jpg）   ->     local_profile_pic_url
 "gender":"",      #性别 -1男 1女（样例数据均为-1）
 "country":"",     #国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
 "city":"",        #城市（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
 "user_type":"",   #用户类型（个人、公开page、其他）
 "verified":"",    #是否认证 1是 0否
 "followers_count":"",     #粉丝数,      ->     flw_cnt
 "friend_count":"",     #关注数,      ->     frd_cnt
 "mobile":"",   #联系方式（电话号码“-”连接）,     ->     phone_num
 "email":"",       #邮箱地址（数据部分为空）
 "language":"",     #语言编码（数据部分为空）,  ->     language_code
 "works":"",       #工作信息（数据部分为空）
 "location":"",    #位置信息（数据部分为空）
 "marriage":"",    #婚姻状况（数据部分为空）
 "home_town":"",   #家乡地址（数据部分为空）
 "user_summary":"",      #用户简介(个人账户-Intro)（部分有值，例：["目前就职：長距離トラックドライバー","长野市","所在地：Chikuma-shi, Nagano, Japan"]）
 "user_systent_name":"",   #昵称（采集用的名字，不需要，基本和name相同）   ->     nick_name
 "registration_date":"",      #注册日期（数据部分为空）   ->       regist
 "user_birthday":"",    #生日时间（数据部分为空）   ->       birthday
 "user_classify":"",    #用户标签（数据部分为空）   ->       user_tag
 "verified_reason":"",   #认证原因（数据部分为空）
 }
 */
@Data
@Builder
@Document(indexName = "fb_history")
public class FbUserHistoryData {

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
     * 唯一指纹,对应表里的   ->   md5_id
     */
    private String source_id;

    /**
     * 用户id
     */
    private String user_id;

    /**
     * 用户名称
     */
    private String screen_name;

    /**
     * 博主名,      ->      name
     */
    private String use_name;

    /**
     * 博主url（若为空需要拼接，格式如下：https://www.facebook.com/profile.php?id=100068526609915)
     */
    private String user_url;

    /**
     * 用户头像链接
     */
    private String user_avatar;

    /**
     * 用户头像本地路径（若为空需要拼接，格式如下：fb_Info_1614294429_photo.jpg）   ->     local_profile_pic_url
     */
    private String local_photo_url;

    /**
     * 性别 -1男 1女（样例数据均为-1）
     */
    private String gender;

    /**
     * 国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
     */
    private String country;

    /**
     * 城市（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
     */
    private String city;

    /**
     * 用户类型（个人、公开page、其他）
     */
    private String user_type;

    /**
     * 是否认证 1是 0否
     */
    private String verified;

    /**
     * 粉丝数,      ->     flw_cnt
     */
    private String followers_count;

    /**
     * 关注数,      ->     frd_cnt
     */
    private String friend_count;

    /**
     * 联系方式（电话号码“-”连接）,     ->     phone_num
     */
    private String mobile;

    /**
     * 邮箱地址（数据部分为空）
     */
    private String email;

    /**
     * 语言编码（数据部分为空）,  ->     language_code
     */
    private String language;

    /**
     * 工作信息（数据部分为空）
     */
    private String works;

    /**
     * 位置信息（数据部分为空）
     */
    private String location;

    /**
     * 婚姻状况（数据部分为空）
     */
    private String marriage;

    /**
     * 家乡地址（数据部分为空）
     */
    private String home_town;

    /**
     * 用户简介(个人账户-Intro)（部分有值，例：["目前就职：長距離トラックドライバー","长野市","所在地：Chikuma-shi, Nagano, Japan"]）
     */
    private String user_summary;

    /**
     * 昵称（采集用的名字，不需要，基本和name相同）   ->     nick_name
     */
    private String user_systent_name;

    /**
     * 注册日期（数据部分为空）   ->       regist
     */
    private String registration_date;

    /**
     * 生日时间（数据部分为空）   ->       birthday
     */
    private String user_birthday;

    /**
     * 用户标签（数据部分为空）   ->       user_tag
     */
    private String user_classify;

    /**
     * 认证原因（数据部分为空）
     */
    private String verified_reason;

    public FbUserHistoryData() {}

    public FbUserHistoryData(String uuid, String platform, String data_source, String create_time, String importance, String remark, String language_type, String source_id, String user_id, String screen_name, String use_name, String user_url, String user_avatar, String local_photo_url, String gender, String country, String city, String user_type, String verified, String followers_count, String friend_count, String mobile, String email, String language, String works, String location, String marriage, String home_town, String user_summary, String user_systent_name, String registration_date, String user_birthday, String user_classify, String verified_reason) {
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
        this.friend_count = friend_count;
        this.mobile = mobile;
        this.email = email;
        this.language = language;
        this.works = works;
        this.location = location;
        this.marriage = marriage;
        this.home_town = home_town;
        this.user_summary = user_summary;
        this.user_systent_name = user_systent_name;
        this.registration_date = registration_date;
        this.user_birthday = user_birthday;
        this.user_classify = user_classify;
        this.verified_reason = verified_reason;
    }
}
