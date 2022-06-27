//package com.acat.handleBlogData.domain.esDb;
//
//import lombok.Builder;
//import lombok.Data;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
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
// *     "source_id":"",      #id   ->   account_id
// *     "user_id":"",        #账户id        ->   account_id
// *     "screen_name":"", #用户名      ->      account_name
// *     "use_name":"",    #全名,     ->      account_nick
// *     "user_url":"",    #web端url 如果为空：拼接规则：https://www.instagram.com/+username     ->     w3_fb_url
// *     "user_avatar":"",    #头像url    ->      profile_pic_url
// *     "local_photo_url":"", #头像_本地下载本地后的地址（拼接规则：in_info_6040184411_photo.jpg）   ->     profile_pic_url_local
// *     "gender":"",      #用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）
// *     "country":"",     #国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
// *     "city":"",        #地址信息（通过currentLocation值抽取城市名称）   ->      user_addr
// *     "user_type":"",   #用户类型 是否私有 1：个人账号 0：公众（任何人都可以看到）  ->    is_private
// *     "verified":"",    #是否认证账号 1：认证，0：非认证    ->   is_verified
// *     "followers_count":"",   #粉丝数,      ->     fans_count
// *     "friend_count":"",   #博主关数     ->      follow_count
// *     "post_count":"",      #发文数       ->      media_count
// *     "source_create_time":"",  #采集时间  空 默认接入时间   ->    fetch_time
// *     "email":"",       #用户邮箱（数据部分为空）
// *     "user_summary":"",   #用户简介
// *     "external_url":"",   #社交联系方式，例如fb账号地址
// *     "fetch_day":"",      #更新日期
// *     "profile_pic_url_oss":"",   #头像oss地址
// *     "media_url":"",             #²文章含有媒介时，多个之间用’|’隔开。比如：www.sina.com/1.png | www.baidu.cn/2.doc media_url 与 media_type 一一对应
// *     "person":"",         #人物标签 --算法预留
// *     "sent_num":"",       #情感度 --算法预留
// * }
// */
//@Data
//@Builder
//@Document(indexName = "instagram")
//public class InstagramUserData {
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
//     * id   ->   account_id
//     */
//    private String source_id;
//
//    /**
//     * 账户id        ->   account_id
//     */
//    private String user_id;
//
//    /**
//     * 用户名      ->      account_name
//     */
//    private String screen_name;
//
//    /**
//     * 全名,     ->      account_nick
//     */
//    private String use_name;
//
//    /**
//     * web端url 如果为空：拼接规则：https://www.instagram.com/+username     ->     w3_fb_url
//     */
//    private String user_url;
//
//    /**
//     * 头像url    ->      profile_pic_url
//     */
//    private String user_avatar;
//
//    /**
//     * 头像_本地下载本地后的地址（拼接规则：in_info_6040184411_photo.jpg）   ->     profile_pic_url_local
//     */
//    private String local_photo_url;
//
//    /**
//     * 用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）
//     */
//    private String gender;
//
//    /**
//     * 国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
//     */
//    private String country;
//
//    /**
//     * 地址信息（通过currentLocation值抽取城市名称）   ->      user_addr
//     */
//    private String city;
//
//    /**
//     * 用户类型 是否私有 1：个人账号 0：公众（任何人都可以看到）  ->    is_private
//     */
//    private String user_type;
//
//    /**
//     * 是否认证账号 1：认证，0：非认证    ->   is_verified
//     */
//    private String verified;
//
//    /**
//     * 粉丝数,      ->     fans_count
//     */
//    private String followers_count;
//
//    /**
//     * 博主关数     ->      follow_count
//     */
//    private String friend_count;
//
//    /**
//     * 发文数       ->      media_count
//     */
//    private String post_count;
//
//    /**
//     * 采集时间  空 默认接入时间   ->    fetch_time
//     */
//    private String source_create_time;
//
//    /**
//     * 用户邮箱（数据部分为空）
//     */
//    private String email;
//
//    /**
//     * 用户简介
//     */
//    private String user_summary;
//
//    /**
//     * 社交联系方式，例如fb账号地址
//     */
//    private String external_url;
//
//    /**
//     * 更新日期
//     */
//    private String fetch_day;
//
//    /**
//     * 头像oss地址
//     */
//    private String profile_pic_url_oss;
//
//    /**
//     * ²文章含有媒介时，多个之间用’|’隔开。比如：www.sina.com/1.png | www.baidu.cn/2.doc media_url 与 media_type 一一对应
//     */
//    private String media_url;
//
//    /**
//     * 人物标签 --算法预留
//     */
//    private String person;
//
//    /**
//     * 情感度 --算法预留
//     */
//    private String sent_num;
//
//    public InstagramUserData() {}
//
//    public InstagramUserData(String uuid, String platform, String data_source, String create_time, String importance, String remark, String language_type, String source_id, String user_id, String screen_name, String use_name, String user_url, String user_avatar, String local_photo_url, String gender, String country, String city, String user_type, String verified, String followers_count, String friend_count, String post_count, String source_create_time, String email, String user_summary, String external_url, String fetch_day, String profile_pic_url_oss, String media_url, String person, String sent_num) {
//        this.uuid = uuid;
//        this.platform = platform;
//        this.data_source = data_source;
//        this.create_time = create_time;
//        this.importance = importance;
//        this.remark = remark;
//        this.language_type = language_type;
//        this.source_id = source_id;
//        this.user_id = user_id;
//        this.screen_name = screen_name;
//        this.use_name = use_name;
//        this.user_url = user_url;
//        this.user_avatar = user_avatar;
//        this.local_photo_url = local_photo_url;
//        this.gender = gender;
//        this.country = country;
//        this.city = city;
//        this.user_type = user_type;
//        this.verified = verified;
//        this.followers_count = followers_count;
//        this.friend_count = friend_count;
//        this.post_count = post_count;
//        this.source_create_time = source_create_time;
//        this.email = email;
//        this.user_summary = user_summary;
//        this.external_url = external_url;
//        this.fetch_day = fetch_day;
//        this.profile_pic_url_oss = profile_pic_url_oss;
//        this.media_url = media_url;
//        this.person = person;
//        this.sent_num = sent_num;
//    }
//}
