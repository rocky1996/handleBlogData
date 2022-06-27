package com.acat.handleBlogData.domain.esDb;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * {
 *     "uuid":"",        #用户唯一ID
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
 *     "email":"",       #用户邮箱（数据部分为空）
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
 *
 * even文件的字段:
 * {
 * "user_web_url":"https://linktr.ee/MamamooCharts",
 * "born_time":"",
 * "registered_time":"2018-06-04 03:24:48",
 * "bkgd_url":"https://pbs.twimg.com/profile_banners/1003477642786541569/1605602083",
 * "user_flag":"Your best source of charts, analytics \u0026 stats for @RBW_MAMAMOO | ENG+한국어+中文 | Not affiliated with MAMAMOO",
 * "listed":"119",
 * "moments":"4126",
 * "protect_ed":"0",
 * "tf_effective":"0",
 * "time_zone":"",
 * "com_from":"forwardingrelationship",
 * "diff_time":"4/6/2018 11:24:48+08",
 * "extend":"",
 * "uuid":"4ec16b89-56f9-475b-9a6f-f904e6994159",
 * "platform":"TW",
 * "data_source":"TW_i_userprofile_69.json.tran_72121.json",
 * "create_time":"17:03:04.432",
 * "importance":"0",
 * "remark":"",
 * "language_type":"en",
 * "source_id":"2460f2b8048b6b706235ae24d3399b48",
 * "user_id":"1003477642786541569",
 * "screen_name":"MamamooCharts",
 * "use_name":"MAMAMOO Charts :tooth:",
 * "user_url":"https://twitter.com/MamamooCharts",
 * "user_avatar":"https://pbs.twimg.com/profile_images/1313845931524059136/Q8yOLjTs_normal.jpg",
 * "local_photo_url":"tw_Info_1003477642786541569_photo.jpg",
 * "gender":"0",
 * "country":"美国",
 * "city":"LINKS ➡️",
 * "user_type":"-1",
 * "verified":"0",
 * "followers_count":"23260",
 * "friend_count":"58",
 * "post_count":"15330",
 * "like_count":"5004",
 * "source_create_time":"7/12/2020 14:07:10+08",
 * "email":"",
 * "language":"en"
 * }
  */
@Document(indexName = "twitter")
@Data
@Builder
public class TwitterUserData{

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
     * 社交源idMd5   ->   md5_id或者data_id
     */
    private String source_id;

    /**
     * 博主id        ->   blogger_id
     */
    private String user_id;

    /**
     * 用户名称    ->   nick_name
     */
    private String screen_name;

    /**
     * 博主全名,      ->      full_name或者user_name
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
     * 用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）
     */
    private String gender;

    /**
     * 国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
     */
    private String country;

    /**
     * 地址信息（通过currentLocation值抽取城市名称）   ->      user_addr或者registered_address
     */
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
     * 用户邮箱（数据部分为空）
     */
    private String email;

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

    public TwitterUserData() {}

    public TwitterUserData(String uuid, String platform, String data_source, String create_time, String importance, String remark, String language_type, String source_id, String user_id, String screen_name, String use_name, String user_url, String user_avatar, String local_photo_url, String gender, String country, String city, String user_type, String verified, String followers_count, String friend_count, String post_count, String like_count, String source_create_time, String email, String language, String user_web_url, String born_time, String registered_time, String bkgd_url, String user_flag, String listed, String moments, String protect_ed, String tf_effective, String time_zone, String com_from, String diff_time, String extend) {
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
        this.email = email;
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
    }
}
