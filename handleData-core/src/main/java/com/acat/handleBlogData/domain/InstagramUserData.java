package com.acat.handleBlogData.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 字段            类型       是否允许为空       备注
 * uuid           String     不能为空       系统自动生成ID
 * integrity      String     不能为空       完整度
 * platform       String     可以为空       来源平台：FB、FQ、TW、IS、LI
 * data_source    String     可以为空       数据来源,数据来源文件名
 * importance     String     可以为空       重要等级,0：普通 （默认）1：关注 2：重要 3：特别重要
 * remark         String     可以为空       批注
 * language_type  String     可以为空       语种
 * source_id      String     不能为空       数据来源id
 * user_id        String     不能为空       用户唯一标识ID
 * screen_name    String     可以为空       昵称
 * use_name       String     可以为空       用户全名
 * user_url       String     可以为空       用户主页URL
 * user_avatar    String     可以为空       用户头像
 * local_photo_url   String  可以为空       用户头像本地路径
 * gender         String     可以为空       性别: -1：男,1：女, 0：未知
 * country        String     可以为空       国家
 * city           String     可以为空       城市
 * user_type      String     可以为空       用户类型,用户类型：1：个人账号people  0：公众 pages  -1：其他
 * verified       String     可以为空       用户认证,1：认证，0：非认证
 * followers_count   String  可以为空       粉丝数
 * friend_count   String     可以为空       关注数
 * post_count     String     可以为空       发文数
 * like_count     String     可以为空       喜欢数
 * source_create_time   String   可以为空   数据产生时间，数据产生的时间，如果没有产生时间，则填写入库时间
 * mobile         String     可以为空       手机号
 * email          String     可以为空       邮箱地址,预留字段
 * language       String     可以为空       语言编码
 * user_religion  String     可以为空       宗教信仰
 * works          String     可以为空       工作信息
 * location       String     可以为空       位置信息
 * marriage       String     可以为空       婚姻情况
 * home_town      String     可以为空       家乡地址
 * user_summary   String     可以为空       用户简介
 * name_userd_before   String    可以为空   曾用名
 * external_url   String     可以为空       社交联系方式，例如fb账号地址
 * fetch_day      String     可以为空       更新日期
 * profile_pic_url_oss   String   可以为空      头像oss地址
 * media_url             String   可以为空      ²文章含有媒介时，多个之间用’|’隔开。比如：www.sina.com/1.png | www.baidu.cn/2.doc media_url 与 media_type 一一对应
 * person         String     可以为空       人物标签 --算法预留
 * sent_num       String     可以为空       情感度 --算法预留
 *
 * 入参格式:
 * {
 *     "uuid":"",        #用户唯一ID
 *     "integrity":"",   #完整度
 *     "platform":"",    #来源平台：FB、FQ、TW、IS、LI
 *     "data_source":"", #数据来源,数据来源文件名
 *     "create_time":"", #数据入库时间
 *     "importance":"",  #重要等级,0：普通 （默认）1：关注 2：重要 3：特别重要
 *     "remark":"",      #扩展字段，对重要数据手动编辑的批注信息
 *     "language_type":"",  #语种
 *     "source_id":"",      #id   ->   account_id
 *     "user_id":"",        #账户id        ->   account_id
 *     "screen_name":"", #用户名      ->      account_name
 *     "use_name":"",    #全名,     ->      account_nick
 *     "user_url":"",    #web端url 如果为空：拼接规则：https://www.instagram.com/+username     ->     w3_fb_url
 *     "user_avatar":"",    #头像url    ->      profile_pic_url
 *     "local_photo_url":"", #头像_本地下载本地后的地址（拼接规则：in_info_6040184411_photo.jpg）   ->     profile_pic_url_local
 *     "gender":"",      #用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）
 *     "country":"",     #国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
 *     "city":"",        #地址信息（通过currentLocation值抽取城市名称）   ->      user_addr
 *     "user_type":"",   #用户类型 是否私有 1：个人账号 0：公众（任何人都可以看到）  ->    is_private
 *     "verified":"",    #是否认证账号 1：认证，0：非认证    ->   is_verified
 *     "followers_count":"",   #粉丝数,      ->     fans_count
 *     "friend_count":"",   #博主关数     ->      follow_count
 *     "post_count":"",      #发文数       ->      media_count
 *     "like_count":"",      #喜欢数
 *     "source_create_time":"",  #采集时间  空 默认接入时间   ->    fetch_time
 *     "mobile":"",      #手机号
 *     "email":"",       #用户邮箱（数据部分为空）
 *     "language":"",    #语言编码
 *     "user_religion",  #宗教信仰
 *     "works":"",       #工作信息
 *     "location",       #位置信息
 *     "marriage":"",    #婚姻情况
 *     "home_town":"",   #家乡地址
 *     "user_summary":"",   #用户简介    ->      biography
 *     "name_userd_before", #曾用名
 *     "external_url":"",   #社交联系方式，例如fb账号地址
 *     "fetch_day":"",      #更新日期
 *     "profile_pic_url_oss":"",   #头像oss地址
 *     "media_url":"",             #²文章含有媒介时，多个之间用’|’隔开。比如：www.sina.com/1.png | www.baidu.cn/2.doc media_url 与 media_type 一一对应
 *     "person":"",         #人物标签 --算法预留
 *     "sent_num":"",       #情感度 --算法预留
 * }
 */
@Data
@Builder
//@Document(indexName = "instagram")
@Document(indexName = "instagram_v2")
public class InstagramUserData {

    /**
     * 用户唯一ID
     */
//    @Id
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
     * id   ->   account_id
     */
    private String source_id;

    /**
     * 账户id        ->   account_id
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    @Id
    private String user_id;

    /**
     * 用户名      ->      account_name
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String screen_name;

    /**
     * 全名,     ->      account_nick
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String use_name;

    /**
     * web端url 如果为空：拼接规则：https://www.instagram.com/+username     ->     w3_fb_url
     */
    private String user_url;

    /**
     * 头像url    ->      profile_pic_url
     */
    private String user_avatar;

    /**
     * 头像_本地下载本地后的地址（拼接规则：in_info_6040184411_photo.jpg）   ->     profile_pic_url_local
     */
    private String local_photo_url;

    /**
     * 用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）
     */
    private String gender;

    /**
     * 国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String country;

    /**
     * 地址信息（通过currentLocation值抽取城市名称）   ->      user_addr
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String city;

    /**
     * 用户类型 是否私有 1：个人账号 0：公众（任何人都可以看到）  ->    is_private
     */
    private String user_type;

    /**
     * 是否认证账号 1：认证，0：非认证    ->   is_verified
     */
    private String verified;

    /**
     * 粉丝数,      ->     fans_count
     */
    private String followers_count;

    /**
     * 博主关数     ->      follow_count
     */
    private String friend_count;

    /**
     * 发文数       ->      media_count
     */
    private String post_count;

    /**
     * 喜欢数
     */
    private String like_count;

    /**
     * 采集时间  空 默认接入时间   ->    fetch_time
     */
    private String source_create_time;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 用户邮箱（数据部分为空）
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String email;

    /**
     * 曾用名
     */
    private String name_userd_before;

    /**
     * 语言编码
     */
    private String language;

    /**
     * 宗教信仰
     */
    private String user_religion;

    /**
     * 工作信息
     */
    private String works;

    /**
     * 位置信息
     */
    private String location;

    /**
     * 婚姻情况
     */
    private String marriage;

    /**
     * 家乡地址
     */
    private String home_town;

    /**
     * 用户简介
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String user_summary;

    /**
     * 社交联系方式，例如fb账号地址
     */
    private String external_url;

    /**
     * 更新日期
     */
    private String fetch_day;

    /**
     * 头像oss地址
     */
    private String profile_pic_url_oss;

    /**
     * ²文章含有媒介时，多个之间用’|’隔开。比如：www.sina.com/1.png | www.baidu.cn/2.doc media_url 与 media_type 一一对应
     */
    private String media_url;

    /**
     * 人物标签 --算法预留
     */
    private String person;

    /**
     * 情感度 --算法预留
     */
    private String sent_num;

    /**
     * 字段非空值累加
     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private Integer integrity;

    public InstagramUserData() {}

    public InstagramUserData(String uuid, String platform, String data_source, String create_time, String importance, String remark, String language_type, String source_id, String user_id, String screen_name, String use_name, String user_url, String user_avatar, String local_photo_url, String gender, String country, String city, String user_type, String verified, String followers_count, String friend_count, String post_count, String like_count, String source_create_time, String mobile, String email, String name_userd_before, String language, String user_religion, String works, String location, String marriage, String home_town, String user_summary, String external_url, String fetch_day, String profile_pic_url_oss, String media_url, String person, String sent_num, Integer integrity) {
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
        this.post_count = post_count;
        this.like_count = like_count;
        this.source_create_time = source_create_time;
        this.mobile = mobile;
        this.email = email;
        this.name_userd_before = name_userd_before;
        this.language = language;
        this.user_religion = user_religion;
        this.works = works;
        this.location = location;
        this.marriage = marriage;
        this.home_town = home_town;
        this.user_summary = user_summary;
        this.external_url = external_url;
        this.fetch_day = fetch_day;
        this.profile_pic_url_oss = profile_pic_url_oss;
        this.media_url = media_url;
        this.person = person;
        this.sent_num = sent_num;
        this.integrity = integrity;
    }
}
