package com.acat.handleBlogData.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;
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
 * local_photo_url   String  可以为空     用户头像本地路径
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
 * mobile         String     可以为空       手机号,预留字段，考虑如何模糊查询
 * email          String     可以为空       邮箱地址,预留字段
 * name_userd_before   String　　可以为空　　曾用名
 * language       String　　　可以为空　　　　语言编码
 * user_religion　 String    可以为空       宗教信仰　　
 * works          String     可以为空       工作信息
 * location       String     可以为空       位置信息
 * marriage       String　　　可以为空　　　　　婚姻状况
 * home_town      String　　　可以为空　　　　　家乡地址
 * user_summary   String　　　可以为空　　　　　用户简介
 * cover           String     可以为空       背景图片
 * local_cover     String     可以为空       本地背景图片
 * staffing_company    String     可以为空       是否是上市公司
 * industries      String     可以为空       所属行业
 * specialities    String     可以为空       专注领域
 * headquarter_post        String     可以为空       总部所在邮编
 * headquarter_address     String     可以为空       总部所在地址
 * confirmed_locations_contry  String 可以为空       办公所在国家
 * confirmed_locations_city    String 可以为空       办公所在城市
 * confirmed_locations_post    String 可以为空       办公所在邮编
 * confirmed_locations_address   String    可以为空  办公所在地址
 * size            String    可以为空        企业规模
 * founded_on      String    可以为空        成立于
 * description     String    可以为空        企业描述（简介）
 * website         String    可以为空        企业官网
 * employees       String    可以为空        员工数量
 * keywords        String    可以为空        关键词
 * tags            String    可以为空        标签
 * timestamp       String    可以为空        时间戳
 * source_connection          String    可以为空        资料来源
 * person          String     可以为空    人物
 * media_type_embeded    String     可以为空        媒介类型 ‘P’:  图片； ‘M’:  音频； ‘V’:  视频； ‘X’:  附件(文章) ²文章含有媒介时，多个之间用’|’隔开。比如：P|P|V|X media_type 与 media_url 一一对应
 * media_url             String     可以为空        ²文章含有媒介时，多个之间用’|’隔开。
 * media_url_name        String     可以为空        本地媒体url， 多个用“|”分割
 * media_title           String     可以为空        ²  文章含有媒介时，多个之间用’|’隔开。比如：头像|banner|位置图集1|…|位置图集60|发帖图集1|…|发帖图集20|评论图1
 *
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
 *     "friend_count":"",     #关注数      ->      description_connections
 *    "post_count":"",  #发文数
 *    "like_count":"",  #喜欢数
 *    "source_create_time":"",      #数据产生的时间，如果没有产生时间，则填写入库时间   ->    timestamp
 *    "mobile":"",          #联系方式（电话号码“-”连接）,     ->     contacts_title_phoneNumbers
 *    "email":"",           #联系方式和基本信息（部分有值，例：Facebook:/tobu.hiroshi.takano|邮箱:celicas2245@gmail.com）  ->    contacts_title_email
 *    "name_userd_before":"",   #曾用名     ->     maidern_name
 *    "language":"",        #语言编码
 *    "user_religion":"",   #宗教信仰　
 *    "works":"",            #工作信息（数据部分为空）      ->      description_company_name
 *    "location":"",         #地址信息（现居地）
 *    "marriage":"",         #婚姻状况
 *    "home_town":"",        #家乡地址
 *    "user_summary":"",     #用户简介   ->     description
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
@Document(indexName = "link_business_v2")
public class LinkBusinessUserData {

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
     * 名称唯一ID   对应原字段->  public_id
     */
    private String source_id;

    /**
     * 用户id         ->       company_id
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    @Id
    private String user_id;

    /**
     * 用户名称        ->       public_id
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String screen_name;

    /**
     * 用户名,         对应原字段->    name
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
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
 //   @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String country;

    /**
     * 地址信息（通过currentLocation值抽取城市名称）   ->      headquarter_city
     */
 //   @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
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
     * 关注数      ->      description_connections
     */
    private String friend_count;

    /**
     * 发文数
     */
    private String post_count;

    /**
     * 喜欢数
     */
    private String like_count;

    /**
     * 数据产生的时间，如果没有产生时间，则填写入库时间   ->    timestamp
     */
    private String source_create_time;

    /**
     * 联系方式（电话号码“-”连接）,     ->     contacts_title_phoneNumbers
     */
    //   @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String mobile;

    /**
     * 联系方式和基本信息（部分有值，例：Facebook:/tobu.hiroshi.takano|邮箱:celicas2245@gmail.com）  ->    contacts_title_email
     */
    //   @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String email;

    /**
     * 曾用名     ->     maidern_name
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
     * 工作信息（数据部分为空）      ->      description_company_name
     */
    private String works;

    /**
     * 地址信息（现居地）
     */
    private String location;

    /**
     * 婚姻状况
     */
    private String marriage;

    /**
     * 家乡地址
     */
    private String home_town;

    /**
     * 用户简介   ->     description
     */
    private String user_summary;

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

    /**
     * 字段非空值累加
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private Integer integrity;

    public LinkBusinessUserData() {}

    public LinkBusinessUserData(String uuid, String platform, String data_source, String create_time, String importance, String remark, String language_type, String source_id, String user_id, String screen_name, String use_name, String user_url, String user_avatar, String local_photo_url, String gender, String country, String city, String user_type, String verified, String followers_count, String friend_count, String post_count, String like_count, String source_create_time, String mobile, String email, String name_userd_before, String language, String user_religion, String works, String location, String marriage, String home_town, String user_summary, String cover, String local_cover, String staffing_company, String industries, String specialities, String headquarter_post, String headquarter_address, String confirmed_locations_contry, String confirmed_locations_city, String confirmed_locations_post, String confirmed_locations_address, String size, String founded_on, String description, String website, String employees, String keywords, String tags, String timestamp, String source_connection, String person, String media_type_embeded, String media_url, String media_url_name, String media_title, Integer integrity) {
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
        this.integrity = integrity;
    }
}
