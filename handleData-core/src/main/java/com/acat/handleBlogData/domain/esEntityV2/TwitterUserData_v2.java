package com.acat.handleBlogData.domain.esEntityV2;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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
 * local_photo_url   String    可以为空     用户头像本地路径
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
 * name_userd_before    String   可以为空   曾用名,预留字段
 * user_religion  Strign     可以为空       宗教信仰
 * works          String     可以为空       工作信息
 * location       String　　　可以为空　　　　 位置信息
 * marriage       String     可以为空       婚姻情况
 * home_town      String     可以为空       家乡地址
 * user_summary   String     可以为空       用户简介
 * language       String     可以为空       语言编码
 * user_web_url   String     可以为空       个人站点（数据部分为空）
 * born_time      String     可以为空       出生日期（数据部分为空）
 * registered_time       String     可以为空       注册时间（示例数据：2019-07-30 01:39:26）
 * bkgd_url       String     可以为空       背景图片url（示例：https://pbs.twimg.com/profile_banners/1156016417075937281/1564470068）
 * user_flag      String     可以为空       用户描述（示例：wife, mother, avid gardener.）
 * listed         String     可以为空       所属公共列表数（数字）
 * moments        String     可以为空       瞬间（数字）
 * protected      String     可以为空       是否是锁定账号（1：锁定，0：非锁定）
 * tf_effective   String     可以为空       是否有博文（0：代表有效，1：代表无效）
 * time_zone      String     可以为空       用户所在时区（样例数据均为空）
 * com_from       String     可以为空       来源（样例数据都是 other）
 * diff_time      String     可以为空        注册日期(示例：2019-07-30 09:39:26.000000 +08:00)
 * extend         String     可以为空        扩展信息（无属性留空）
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
 *     "source_id":"",      #社交源idMd5   ->   md5_id或者data_id
 *     "user_id":"",  #博主id        ->   blogger_id
 *     "screen_name":"", #用户名称    ->   nick_name
 *     "use_name":"",   #博主全名,      ->      full_name或者user_name
 *     "user_url":"",    #博主url（若为空需要拼接，格式如下：https://www.facebook.com/profile.php?id=100068526609915)
 *     "user_avatar":"",    #用户头像链接
 *     "local_photo_url":"", #用户头像本地路径（若为空需要拼接，格式如下：fb_Info_1614294429_photo.jpg）   ->     local_profile_pic_url
 *     "gender":"",      #用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）
 *     "country":"",     #国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
 *     "city":"",        #地址信息（通过currentLocation值抽取城市名称）   ->      user_addr或者registered_address
 *     "user_type":"",   #用户类型（个人、公开page、其他）
 *     "verified":"",    #是否认证 1：认证，0：非认证
 *     "followers_count":"",   #粉丝数,      ->     followers或者follower_cnt
 *     "friend_count":"",   #博主关数     ->      following或者following_cnt
 *     "post_count":"",      #发文数       ->      tweets或者article_cnt
 *     "like_count":"",        #收藏的推文数  ->      like
 *     "source_create_time":"",  #采集时间   ->    input_time或者crawl_time
 *     "mobile":"",      #手机号
 *     "email":"",       #用户邮箱（数据部分为空）
 *     "name_userd_before":"",     #曾用名
 *     "user_religion"   #宗教信仰
 *     "works":"",       #工作信息
 *     "location":"",    #位置信息
 *     "marriage":"",    #婚姻情况
 *     "home_town":"",   #家乡地址
 *     "user_summary":""     #用户简介     ->      userFlag
 *     "language":"",        #用户语言（数据部分为空）        ->      lang
 *     "user_web_url":"",        #个人站点（数据部分为空）
 *     "born_time":"",           #出生日期（数据部分为空）    ->      born_time或者birth_date
 *     "registered_time":"",     #注册时间（示例数据：2019-07-30 01:39:26）
 *     "bkgd_url":"",            #背景图片url（示例：https://pbs.twimg.com/profile_banners/1156016417075937281/1564470068）
 *     "user_flag":"",           #用户描述（示例：wife, mother, avid gardener.）   ->    user_flag或者user_info
 *     "listed":"",              #所属公共列表数（数字）   ->    listed或者collect_cnt
 *     "moments":"",             #瞬间（数字）
 *     "protected":"",           #是否是锁定账号（1：锁定，0：非锁定）
 *     "tf_effective":"",        #是否有博文（0：代表有效，1：代表无效）
 *     "time_zone:"",            #用户所在时区（样例数据均为空）
 *     "com_from:"",             #来源（样例数据都是 other）  ->   com_from或者source_tag
 *     "diff_time":"",           #注册日期(示例：2019-07-30 09:39:26.000000 +08:00)
 *     "extend":""               #扩展信息（无属性留空）
 * }
 */

@Document(indexName = "twitter_v2")
@Data
@Builder
public class TwitterUserData_v2{

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
     * 社交源idMd5   ->   md5_id或者data_id
     */
    private String source_id;

    /**
     * 博主id        ->   blogger_id
     */
    //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    @Id
    private String user_id;

    /**
     * 用户名称    ->   nick_name
     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String screen_name;

    /**
     * 博主全名,      ->      full_name或者user_name
     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
//    @Field(type = FieldType.Text, analyzer = "analysis-auto", searchAnalyzer = "analysis-auto")
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
     * 用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）
     */
    private String gender;

    /**
     * 国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
     */
    //   @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String country;

    /**
     * 地址信息（通过currentLocation值抽取城市名称）   ->      user_addr或者registered_address
     */
    //   @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String city;

    /**
     * 用户类型（个人、公开page、其他）
     */
    private String user_type;

    /**
     * 是否认证 1：认证，0：非认证
     */
    private String verified;

    /**
     * 粉丝数,      ->     followers或者follower_cnt
     */
    private String followers_count;

    /**
     * 博主关数     ->      following或者following_cnt
     */
    private String friend_count;

    /**
     * 发文数       ->      tweets或者article_cnt
     */
    private String post_count;

    /**
     * 收藏的推文数  ->      like
     */
    private String like_count;

    /**
     * 采集时间   ->    input_time或者crawl_time
     */
    private String source_create_time;

    /**
     * 手机号
     */
    private String mobile;

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
     * 用户邮箱（数据部分为空）
     */
    //   @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String email;

    /**
     * 曾用名
     */
    private String name_userd_before;

    /**
     * 用户简介     ->      userFlag
     */
    private String user_summary;

    /**
     * 用户语言（数据部分为空）        ->      lang
     */
    private String language;

    /**
     * 个人站点（数据部分为空）
     */
    private String user_web_url;

    /**
     * 出生日期（数据部分为空）    ->      born_time或者birth_date
     */
    private String born_time;

    /**
     * 注册时间（示例数据：2019-07-30 01:39:26）
     */
    private String registered_time;

    /**
     * 背景图片url（示例：https://pbs.twimg.com/profile_banners/1156016417075937281/1564470068）
     */
    private String bkgd_url;

    /**
     * 用户描述（示例：wife, mother, avid gardener.）   ->    user_flag或者user_info
     */
    private String user_flag;

    /**
     * 所属公共列表数（数字）   ->    listed或者collect_cnt
     */
    private String listed;

    /**
     * 瞬间（数字）
     */
    private String moments;

    /**
     * 是否是锁定账号（1：锁定，0：非锁定）
     */
    private String protect_ed;

    /**
     * 是否有博文（0：代表有效，1：代表无效）
     */
    private String tf_effective;

    /**
     * 用户所在时区（样例数据均为空）
     */
    private String time_zone;

    /**
     * 来源（样例数据都是 other）  ->   com_from或者source_tag
     */
    private String com_from;

    /**
     * 注册日期(示例：2019-07-30 09:39:26.000000 +08:00)
     */
    private String diff_time;

    /**
     * 扩展信息（无属性留空）
     */
    private String extend;

    /**
     * 字段非空值累加
     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private Integer integrity;

    public TwitterUserData_v2() {}

    public TwitterUserData_v2(String uuid, String platform, String data_source, String create_time, String importance, String remark, String language_type, String source_id, String user_id, String screen_name, String use_name, String user_url, String user_avatar, String local_photo_url, String gender, String country, String city, String user_type, String verified, String followers_count, String friend_count, String post_count, String like_count, String source_create_time, String mobile, String user_religion, String works, String location, String marriage, String home_town, String email, String name_userd_before, String user_summary, String language, String user_web_url, String born_time, String registered_time, String bkgd_url, String user_flag, String listed, String moments, String protect_ed, String tf_effective, String time_zone, String com_from, String diff_time, String extend, Integer integrity) {
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
        this.user_religion = user_religion;
        this.works = works;
        this.location = location;
        this.marriage = marriage;
        this.home_town = home_town;
        this.email = email;
        this.name_userd_before = name_userd_before;
        this.user_summary = user_summary;
        this.language = language;
        this.user_web_url = user_web_url;
        this.born_time = born_time;
        this.registered_time = registered_time;
        this.bkgd_url = bkgd_url;
        this.user_flag = user_flag;
        this.listed = listed;
        this.moments = moments;
        this.protect_ed = protect_ed;
        this.tf_effective = tf_effective;
        this.time_zone = time_zone;
        this.com_from = com_from;
        this.diff_time = diff_time;
        this.extend = extend;
        this.integrity = integrity;
    }
}

