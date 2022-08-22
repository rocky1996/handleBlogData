package com.acat.handleBlogData.domain.esEntityV2;

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
 * email          String     可以为空       邮箱
 * name_userd_before   String   可以为空    曾用名
 * language       String     可以为空       语言编码
 * user_religion  String     可以为空       宗教信仰
 * works          String     可以为空       工作信息
 * location       String     可以为空       位置信息
 * marriage       String     可以为空       婚姻状况
 * home_town      String     可以为空       家乡地址
 * user_summary   String     可以为空       用户简介
 * contact        String     可以为空       社交联系方式
 * alg_remark     String     可以为空       算法标签
 * alg_result     String     可以为空       预留
 * bio            String     可以为空       用户简介
 * checkins_count    String     可以为空    签到数量
 * country_region    String     可以为空    地理位置信息
 * create_time       String     可以为空    创建时间
 * first_name        String     可以为空    名称前缀
 * home_city         String     可以为空    城市编码
 * last_name         String     可以为空    名称后缀
 * lenses            String     可以为空    镜头
 * lists             String     可以为空    列表
 * lists_count       String     可以为空    列表数
 * local_user_head_url     String    可以为空    本地头像url
 * media_title       String     可以为空    附件媒体文字
 * media_type_embeded      String    可以为空    媒介类型
 * media_url         String     可以为空    媒介URL
 * media_url_name    String     可以为空    媒介名称
 * person            String     可以为空    人物标签
 * photo             String     可以为空    图片源数据
 * photo_prefix      String     可以为空    头像前缀
 * photo_suffix      String     可以为空    头像后缀
 * real_name         String     可以为空    人工维护真实姓名（样例数据为空）
 * recent_list       String     可以为空    最近列表
 * send_state        String     可以为空    发送状态
 * send              String     可以为空    情感正负面
 * send_num          String     可以为空    情感度
 * todo              String     可以为空    计划内容
 * top_tips          String     可以为空    top评论
 * visibility        String     可以为空    可见性
 * vpers             String     可以为空    人物名称分词
 * w3_fb_url         String     可以为空    主页（重复）
 * source_key        String     可以为空    关键词来源
 * source_venue      String     可以为空    位置来源
 * source_venue_id   String     可以为空    位置id来源
 * friends           String     可以为空    朋友数量
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
 *     "source_id":"",      #自增id  ->   id
 *     "user_id":"",        #用户id
 *     "screen_name":"",    #昵称（人工标注） ->   nick_name
 *     "use_name":"",       #目前无此属性（通过Firstname和Lastname拼接）      ->      name
 *     "user_url":"",       #主页    ->     canonical_url
 *     "user_avatar":"",    #头像地  ->      photo_url
 *     "local_photo_url":"", #本地头像url（服务器存放地址，仅保留文件名）
 *     "gender":"",      #用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）
 *     "country":"",     #国家编码（示例：TW）   ->    country_code
 *     "city":"",        #城市编码（示例：中山區, 台北市）   ->      home_city
 *     "user_type":"",   #用户类型（个人、公开page、其他）
 *     "verified":"",    #是否认证 1：认证，0：非认证
 *     "followers_count":"",   #追随者数量,    ->      follower_count
 *     "friend_count":"",      #关注者数量     ->      following_count
 *     "post_count":"",        #评论数       ->      tips_count
 *     "like_count":"",        #喜欢数
 *     "source_create_time":"",#采集时间   ->    crawl_time
 *     "mobile":"",            #手机号
 *     "email":"",             #邮箱
 *     "name_userd_before":"", #曾用名
 *     "language":"",          #语言编码
 *     "user_religion":"",     #宗教信仰
 *     "works":"",             #工作信息
 *     "location":"",          #位置信息
 *     "marriage":"",          #婚姻状况
 *     "home_town":"",         #家乡地址
 *     "user_summary":"",      #用户简介    ->     bio
 *     "alg_remark":"",  #算法标签
 *     "alg_result":"",  #预留
 *     "contact":"",     #社交联系方式
 *     "checkins_count":"",    #签到数量
 *     "country_region":"",    #地理位置信息
 *     "create_time":"",       #创建时间
 *     "first_name":"",        #名称前缀
 *     "home_city":"",         #城市编码
 *     "last_name":"",         #名称后缀
 *     "lenses":"",            #镜头
 *     "lists":"",             #列表
 *     "lists_count":"",       #列表数
 *     "local_user_head_url":"",     #本地头像url
 *     "media_title":"",       #附件媒体文字
 *     "media_type_embeded":"",#媒介类型
 *     "media_url":"",         #媒介URL
 *     "media_url_name":"",    #媒介名称
 *     "person":"",            #人物标签
 *     "photo":"",             #图片源数据
 *     "photo_prefix":"",      #头像前缀
 *     "photo_suffix":"",      #头像后缀
 *     "real_name":"",         #人工维护真实姓名（样例数据为空）
 *     "recent_list":"",       #最近列表
 *     "send_state":"",        #发送状态
 *     "send":"",              #情感正负面
 *     "send_num":"",          #情感度
 *     "todo":"",              #计划内容
 *     "top_tips":"",          #top评论
 *     "visibility":"",        #可见性
 *     "vpers":"",             #人物名称分词
 *     "w3_fb_url":"",         #主页（重复）
 *     "source_key":"",        #关键词来源
 *     "source_venue":"",      #位置来源
 *     "source_venue_id":"",   #位置id来源
 *     "friends":"",           #朋友数量
 * }
 */
@Data
@Builder
@Document(indexName = "fq_v2")
public class FqUserData_v2 {

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
     * 自增id  ->   id
     */
    private String source_id;

    /**
     * 用户id
     */
    //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    @Id
    private String user_id;

    /**
     * 昵称（人工标注） ->   nick_name
     */
    //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String screen_name;

    /**
     * 目前无此属性（通过Firstname和Lastname拼接）      ->      name
     */
    // @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String use_name;

    /**
     * 主页    ->     canonical_url
     */
    private String user_url;

    /**
     * 头像地  ->      photo_url
     */
    private String user_avatar;

    /**
     * 本地头像url（服务器存放地址，仅保留文件名）
     */
    private String local_photo_url;

    /**
     * 用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）
     */
    private String gender;

    /**
     * 国家编码（示例：TW）   ->    country_code
     */
    //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String country;

    /**
     * 城市编码（示例：中山區, 台北市）   ->      home_city
     */
    //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
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
     * 追随者数量,    ->      follower_count
     */
    private String followers_count;

    /**
     * 关注者数量     ->      following_count
     */
    private String friend_count;

    /**
     * 评论数       ->      tips_count
     */
    private String post_count;

    /**
     * 采集时间   ->    crawl_time
     */
    private String source_create_time;

    /**
     * 手机号
     */
     private String mobile;

    /**
     * 邮箱
     */
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
     * 婚姻状况
     */
    private String marriage;

    /**
     * 家乡地址
     */
    private String home_town;

    /**
     * 用户简介    ->     bio
     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String user_summary;

    /**
     * impl or history
     */
    private String impl_or_history_type;


    /**
     * 算法标签
     */
    private String alg_remark;

    /**
     * 预留
     */
    private String alg_result;

    /**
     * 社交联系方式
     */
    private String contact;

    /**
     * 签到数量
     */
    private String checkins_count;

    /**
     * 地理位置信息
     */
    private String country_region;

//    /**
//     * 创建时间
//     */
//    private String create_time;

    /**
     * 名称前缀
     */
    private String first_name;

    /**
     * 城市编码
     */
    private String home_city;

    /**
     * 名称后缀
     */
    private String last_name;

    /**
     * 镜头
     */
    private String lenses;

    /**
     * 列表
     */
    private String lists;

    /**
     * 列表数
     */
    private String lists_count;

    /**
     * 本地头像url
     */
    private String local_user_head_url;

    /**
     * 附件媒体文字
     */
    private String media_title;

    /**
     * 媒介类型
     */
    private String media_type_embeded;

    /**
     * 媒介URL
     */
    private String media_url;

    /**
     * 媒介名称
     */
    private String media_url_name;

    /**
     * 人物标签
     */
    private String person;

    /**
     * 图片源数据
     */
    private String photo;

    /**
     * 头像前缀
     */
    private String photo_prefix;

    /**
     * 头像后缀
     */
    private String photo_suffix;

    /**
     * 人工维护真实姓名（样例数据为空）
     */
    private String real_name;

    /**
     * 最近列表
     */
    private String recent_list;

    /**
     * 发送状态
     */
    private String send_state;

    /**
     * 情感正负面
     */
    private String send;

    /**
     * 情感度
     */
    private String send_num;

    /**
     * 计划内容
     */
    private String todo;

    /**
     * top评论
     */
    private String top_tips;

    /**
     * 可见性
     */
    private String visibility;

    /**
     * 人物名称分词
     */
    private String vpers;

    /**
     * 主页（重复）
     */
    private String w3_fb_url;

    /**
     * 关键词来源
     */
    private String source_key;

    /**
     * 位置来源
     */
    private String source_venue;

    /**
     * 位置id来源
     */
    private String source_venue_id;

    /**
     * 朋友数量
     */
    private String friends;

    /**
     * 字段非空值累加
     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private Integer integrity;

    public FqUserData_v2() {}

    public FqUserData_v2(String uuid, String platform, String data_source, String create_time, String importance, String remark, String language_type, String source_id, String user_id, String screen_name, String use_name, String user_url, String user_avatar, String local_photo_url, String gender, String country, String city, String user_type, String verified, String followers_count, String friend_count, String post_count, String source_create_time, String mobile, String email, String name_userd_before, String language, String user_religion, String works, String location, String marriage, String home_town, String user_summary, String impl_or_history_type, String alg_remark, String alg_result, String contact, String checkins_count, String country_region, String first_name, String home_city, String last_name, String lenses, String lists, String lists_count, String local_user_head_url, String media_title, String media_type_embeded, String media_url, String media_url_name, String person, String photo, String photo_prefix, String photo_suffix, String real_name, String recent_list, String send_state, String send, String send_num, String todo, String top_tips, String visibility, String vpers, String w3_fb_url, String source_key, String source_venue, String source_venue_id, String friends, Integer integrity) {
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
        this.impl_or_history_type = impl_or_history_type;
        this.alg_remark = alg_remark;
        this.alg_result = alg_result;
        this.contact = contact;
        this.checkins_count = checkins_count;
        this.country_region = country_region;
        this.first_name = first_name;
        this.home_city = home_city;
        this.last_name = last_name;
        this.lenses = lenses;
        this.lists = lists;
        this.lists_count = lists_count;
        this.local_user_head_url = local_user_head_url;
        this.media_title = media_title;
        this.media_type_embeded = media_type_embeded;
        this.media_url = media_url;
        this.media_url_name = media_url_name;
        this.person = person;
        this.photo = photo;
        this.photo_prefix = photo_prefix;
        this.photo_suffix = photo_suffix;
        this.real_name = real_name;
        this.recent_list = recent_list;
        this.send_state = send_state;
        this.send = send;
        this.send_num = send_num;
        this.todo = todo;
        this.top_tips = top_tips;
        this.visibility = visibility;
        this.vpers = vpers;
        this.w3_fb_url = w3_fb_url;
        this.source_key = source_key;
        this.source_venue = source_venue;
        this.source_venue_id = source_venue_id;
        this.friends = friends;
        this.integrity = integrity;
    }
}
