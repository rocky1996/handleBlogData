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
 *     "source_id":"",   #名称唯一ID   对应原字段->  public_id
 *     "user_id":"",     #用户id         ->       company_id
 *     "screen_name":"", #用户名称        ->       public_id
 *     "use_name":"",    #用户名,         对应原字段->    name
 *     "user_url":"",    #博主url（若为空需要拼接，格式如下：https://www.facebook.com/profile.php?id=100068526609915)  ->    url
 *     "user_avatar":"", #用户头像链接     对应原字段->    photo
 *     "local_photo_url":"", #用户头像本地路径（若为空需要拼接，格式如下：fb_Info_1614294429_photo.jpg）   ->     local_photo
 *     "gender":"",      #用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）       ->     user_gender
 *     "country":"",     #国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典） ->   headquarter_country
 *     "city":"",        #地址信息（通过currentLocation值抽取城市名称）   ->      headquarter_city
 *     "user_type":"",   #用户类型（个人、公开page、其他）  ->     type
 *     "verified":"",    #是否认证 1：认证，0：非认证   ->    is_attestation
 *     "followers_count":"",  #粉丝人数-个人/ 关注数- 公共page（粉丝总数）,      ->     followers
 *     "location":"",         #地址信息（现居地）
 *     "cover":"",            #背景图片
 *     "local_cover":"",      #本地背景图片
 *     "staffing_company":"", #是否是上市公司
 *     "industries":"",       #所属行业
 *     "specialities":"",     #专注领域
 *     "headquarter_post":"", #总部所在邮编
 *     "headquarter_address:"",     #总部所在地址
 *     "confirmed_locations_contry":"",  #办公所在国家
 *     "confirmed_locations_city":"",    #办公所在城市
 *     "confirmed_locations_post":"",    #办公所在邮编
 *     "confirmed_locations_address":"", #办公所在地址
 *     "size":"",                        #企业规模
 *     "founded_on":"",                  #成立于
 *     "description":"",                 #企业描述（简介）
 *     "website":"",                     #企业官网
 *     "employees":"",                   #员工数量
 *     "keywords":"",                    #关键词
 *     "tags":"",                        #标签
 *     "timestamp":"",                   #时间戳
 *     "source_connection":"",           #资料来源
 *     "person":"",                      #人物
 *     "media_type_embeded":"",          #媒介类型 ‘P’:  图片； ‘M’:  音频； ‘V’:  视频； ‘X’:  附件(文章) ²文章含有媒介时，多个之间用’|’隔开。比如：P|P|V|X media_type 与 media_url 一一对应
 *     "media_url":"",                   #²文章含有媒介时，多个之间用’|’隔开
 *     "media_url_name":"",              #本地媒体url， 多个用“|”分割
 *     "media_title":"",                 #²  文章含有媒介时，多个之间用’|’隔开。比如：头像|banner|位置图集1|…|位置图集60|发帖图集1|…|发帖图集20|评论图1
 * }
 */
@Data
@Builder
@Document(indexName = "link_business")
public class LinkBusinessUserData {

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
     * 名称唯一ID   对应原字段->  public_id
     */
    private String source_id;

    /**
     * 用户id         ->       company_id
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String user_id;

    /**
     * 用户名称        ->       public_id
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String screen_name;

    /**
     * 用户名,         对应原字段->    name
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String use_name;

    /**
     * 博主url（若为空需要拼接，格式如下：https://www.facebook.com/profile.php?id=100068526609915)  ->    url
     */
    private String user_url;

    /**
     * 用户头像链接     对应原字段->    photo
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
     * 国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典） ->   headquarter_country
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String country;

    /**
     * 地址信息（通过currentLocation值抽取城市名称）   ->      headquarter_city
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String city;

    /**
     * 用户类型（个人、公开page、其他）  ->     type
     */
    private String user_type;

    /**
     * 是否认证 1：认证，0：非认证   ->    is_attestation
     */
    private String verified;

    /**
     * 粉丝人数-个人/ 关注数- 公共page（粉丝总数）,      ->     followers
     */
    private String followers_count;

    /**
     * 地址信息（现居地）
     */
    private String location;

    /**
     * 地址信息（现居地）
     */
    private String cover;

    /**
     * 本地背景图片
     */
    private String local_cover;

    /**
     * 是否是上市公司
     */
    private String staffing_company;

    /**
     * 所属行业
     */
    private String industries;

    /**
     * 所属行业
     */
    private String specialities;

    /**
     * 总部所在邮编
     */
    private String headquarter_post;

    /**
     * 总部所在地址
     */
    private String headquarter_address;

    /**
     * 办公所在国家
     */
    private String confirmed_locations_contry;

    /**
     * 办公所在城市
     */
    private String confirmed_locations_city;

    /**
     * 办公所在邮编
     */
    private String confirmed_locations_post;

    /**
     * 办公所在邮编
     */
    private String confirmed_locations_address;

    /**
     * 企业规模
     */
    private String size;

    /**
     * 企业规模
     */
    private String founded_on;

    /**
     * 企业描述（简介）
     */
    private String description;

    /**
     * 企业官网
     */
    private String website;

    /**
     * 企业官网
     */
    private String employees;

    /**
     * 关键词
     */
    private String keywords;

    /**
     * 标签
     */
    private String tags;

    /**
     * 时间
     */
    private String timestamp;

    /**
     * 资料来源
     */
    private String source_connection;

    /**
     * 人物
     */
    private String person;

    /**
     * 媒介类型 ‘P’:  图片； ‘M’:  音频； ‘V’:  视频； ‘X’:  附件(文章) ²文章含有媒介时，多个之间用’|’隔开。比如：P|P|V|X media_type 与 media_url 一一对应
     */
    private String media_type_embeded;

    /**
     * ²文章含有媒介时，多个之间用’|’隔开
     */
    private String media_url;

    /**
     * 本地媒体url， 多个用“|”分割
     */
    private String media_url_name;

    /**
     * ²  文章含有媒介时，多个之间用’|’隔开。比如：头像|banner|位置图集1|…|位置图集60|发帖图集1|…|发帖图集20|评论图1
     */
    private String media_title;

    public LinkBusinessUserData() {}

    public LinkBusinessUserData(String uuid, String platform, String data_source, String create_time, String importance, String remark, String language_type, String source_id, String user_id, String screen_name, String use_name, String user_url, String user_avatar, String local_photo_url, String gender, String country, String city, String user_type, String verified, String followers_count, String location, String cover, String local_cover, String staffing_company, String industries, String specialities, String headquarter_post, String headquarter_address, String confirmed_locations_contry, String confirmed_locations_city, String confirmed_locations_post, String confirmed_locations_address, String size, String founded_on, String description, String website, String employees, String keywords, String tags, String timestamp, String source_connection, String person, String media_type_embeded, String media_url, String media_url_name, String media_title) {
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
        this.location = location;
        this.cover = cover;
        this.local_cover = local_cover;
        this.staffing_company = staffing_company;
        this.industries = industries;
        this.specialities = specialities;
        this.headquarter_post = headquarter_post;
        this.headquarter_address = headquarter_address;
        this.confirmed_locations_contry = confirmed_locations_contry;
        this.confirmed_locations_city = confirmed_locations_city;
        this.confirmed_locations_post = confirmed_locations_post;
        this.confirmed_locations_address = confirmed_locations_address;
        this.size = size;
        this.founded_on = founded_on;
        this.description = description;
        this.website = website;
        this.employees = employees;
        this.keywords = keywords;
        this.tags = tags;
        this.timestamp = timestamp;
        this.source_connection = source_connection;
        this.person = person;
        this.media_type_embeded = media_type_embeded;
        this.media_url = media_url;
        this.media_url_name = media_url_name;
        this.media_title = media_title;
    }
}
