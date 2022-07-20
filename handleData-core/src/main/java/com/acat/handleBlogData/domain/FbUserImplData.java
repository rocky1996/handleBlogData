package com.acat.handleBlogData.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
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
 *     "source_id":"",   #id【业务字段】   对应原字段->    id
 *     "user_id":"",     #用户id
 *     "screen_name":"", #用户名称
 *     "use_name":"",    #用户名,         对应原字段->    user_name
 *     "user_url":"",    #博主url（若为空需要拼接，格式如下：https://www.facebook.com/profile.php?id=100068526609915)
 *     "user_avatar":"", #用户头像链接     对应原字段->    user_head_url
 *     "local_photo_url":"", #用户头像本地路径（若为空需要拼接，格式如下：fb_Info_1614294429_photo.jpg）   ->     local_profile_pic_url
 *     "gender":"",      #用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）       ->     user_gender
 *     "country":"",     #国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
 *     "city":"",        #地址信息（通过currentLocation值抽取城市名称）   ->      current_location
 *     "user_type":"",   #用户类型（个人、公开page、其他）
 *     "verified":"",    #是否认证 1：认证，0：非认证   ->    is_attestation
 *     "followers_count":"",  #粉丝人数-个人/ 关注数- 公共page（粉丝总数）,      ->     follower_number
 *     "friend_count":"",     #关注数
 *     "post_count":"",       #发文数
 *     "like_count":"",      #喜欢人数（点赞人数）  -> like_number
 *     "source_create_time":"",      #数据产生的时间，如果没有产生时间，则填写入库时间   ->    create_time
 *     "mobile":"",          #联系方式（电话号码“-”连接）,     ->     user_phone
 *     "email":"",           #联系方式和基本信息（部分有值，例：Facebook:/tobu.hiroshi.takano|邮箱:celicas2245@gmail.com）  ->    contact_and_basic_info
 *     "name_userd_before":"",#用户曾用名（数据部分为空）      ->      name_used_before
 *     "language":"",         #用户语言（数据部分为空）        ->      user_language
 *     "user_religion":"",    #宗教信仰（数据部分为空）
 *     "works":"",            #工作信息（数据部分为空）      ->      work_message
 *     "location":"",         #地址信息（现居地）         ->       current_location
 *     "marriage":"",         #夫妻关系（数据部分为空）   ->       significant_other
 *     "home_town":"",   #家乡信息、生活过的地方（部分有值，例：来自：Carolina Beach, North Carolina）   ->   hometown_message
 *     "user_political_views":"", #政治观点（数据部分为空）
 *     "user_summary":"",      #用户简介(个人账户-Intro)（部分有值，例：["目前就职：長距離トラックドライバー","长野市","所在地：Chikuma-shi, Nagano, Japan"]）
 *     "user_systent_name":"", #用户系统名（数据部分为空）
 *     "w3_fb_url":"",         #采集层统一的博主地址（样例数据全部为空，新数据都有值）
 *     "institution_id":"",    #隶属机构ID（数据部分为空）
 *     "is_community_page":"", #是否社区page页，接口API返回，1：是，0：否（数据部分为空）
 *     "communication_philosophy":"",   #传播理念（数据部分为空）
 *     "have_product":"",         #拥有产品，企业账号（数据部分为空）
 *     "exchange_number":"",      #交流人数，企业账号（数据部分为空）
 *     "visit_number":"",         #访问人数，企业账号（数据部分为空）
 *     "first_name":"",           #姓名-姓（预留）（数据部分为空）
 *     "last_name":"",            #姓名-名（预留）（数据部分为空）
 *     "teach_message":"",        #教育信息（部分有值，例：目前就职：Gustavo, Asoc. Ind. Herbalife|2017年6月11日至今|曾经就读：UNC Charlotte）
 *     "acquisition_time":"",     #采集时间
 *     "affective_state":"",      #情感状态（数据部分为空）
 *     "background_picture_url":"",     #背景图片
 *     "business_story":"",       #商家故事* 'name': 商家故事标题名称,* 'id': 商家故事id,* 'photoUrl': 商家故事图片url,* 'localProfilePicUrl': 商家故事图片本地存储路径，采集存储结果类型list
 *     "classify_message":"",     #分类信息（例：政府公职服务机构）
 *     "com_from":"",             #comFrom：数据来源  blogger-博主 keyword-关键词 friendships-博主关系  Forwardingrelationship-转发关系  commentrelationship-评论关系 oher-其他 等（数据部分为空）
 *     "dm_tag1":"",              #人物标签-算法（例：军政人物）
 *     "company_profile":"",      #公司简介（数据部分为空）
 *     "country_region":"",       #地理位置信息  --算法预留（例：USA|美国）
 *     "country_region_city":"",  #地理位置信息-城市  --算法预留（数据部分为空)
 *     "detailed_summary":"",     #详细简介(大部分数据为空，样例见备注)
 *     "favorite_quotes":"",      #格言信息（数据部分为空）
 *     "found":"",                #成立时间（数据部分为空）
 *     "gender_orientation":"",   #性取向（部分有值，例：女性）
 *     "go_through":"",           #经历 生活纪事（部分有值，例：2021年:長野県 上田市出身）
 *     "like_number_int":"",      #喜欢人数（转化为数字）（样例数据全部为0）
 *     "local_profile_pic_background_url":"",  #本地背景图路径（数据部分为空）
 *     "media_title":"",          #媒体文字 ²文章含有媒介时，多个之间用’|’隔开。（部分有值，例：头像|背景图）
 *     "media_type_embeded":"",   #媒介类型 媒介类型 ‘P’:  图片； ‘M’:  音频； ‘V’:  视频； ‘X’:  附件(文章) （部分有值，例：P|P）
 *     "media_url":"",            #媒体URL ²文章含有媒介时，多个之间用’|’隔开。（部分有值，示例见备注）
 *     "media_url_name":"",       #文章含有媒介系统生成文件名 文章含有媒介系统生成文件名，用于下载文件后关联及内容模型识别结果回更，多个文件名之间用’|’隔开。（部分有值，示例见备注）
 *     "opening_hours":"",        #运营时间（数据部分为空）
 *     "personal_web_url":"",     #个人网址（部分为空，例：http://www.dragospislaru.eu/）
 *     "photo_album_url":"",      #相册url地址（数据部分为空）
 *     "photo_wall":"",           #照片墙链接 采集存储结果多个以| 分割（数据部分为空）
 *     "position_message":"",     #位置信息（部分有值，例：{"latitude":"59.914375228846","longitude":"10.743255615234"}）
 *     "register_number":"",      #签到数（样例数据全部为0）
 *     "registration_date":"",    #注册日期（数据部分为空）
 *     "related_home_page":"",    #相关主页信息（数据部分为空）
 *     "shop_content":"",         #商品信息；（数据部分为空）
 *     "family_and_relation_ships":"",    #家庭成员与感情情况（数据部分为空）
 *     "skill":"",                #技能 技能列表（部分为空，示例见备注）
 *     "user_birthday":"",        #用户生日（部分有值，例：10月29日）
 *     "user_classify":"",        #用户分类（示例：["政府机构"]）
 *     "user_description":"",     #用户描述（部分有值，示例见备注）
 *     "user_facebook_url":"",    #用户主页地址，带名称的URL（部分有值，例：https://www.facebook.com/travelGoC/）
 *     "verified_reason":"",      #认证原因（数据部分为空）
 * }
 */
@Data
@Builder
@Document(indexName = "fb_impl")
public class FbUserImplData {

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
     * id【业务字段】   对应原字段->    id
     */
    private String source_id;

    /**
     * 用户id
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    @Id
    private String user_id;

    /**
     * 用户名称
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String screen_name;

    /**
     * 用户名,         对应原字段->    user_name
     */
   // @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String use_name;

    /**
     * 博主url（若为空需要拼接，格式如下：https://www.facebook.com/profile.php?id=100068526609915)
     */
    private String user_url;

    /**
     * 用户头像链接     对应原字段->    user_head_url
     */
    private String user_avatar;

    /**
     * 用户头像本地路径（若为空需要拼接，格式如下：fb_Info_1614294429_photo.jpg）   ->     local_profile_pic_url
     */
    private String local_photo_url;

    /**
     * 用户性别，样例数据既有男，也有FEMALE，需统一编码（若为空，设置为0，-1是男，1是女）       ->     user_gender
     */
    private String gender;

    /**
     * 国家（样例数据有国家中文名也有英文名；英文的统一治理中文，非标准的 原样存储 检索条件增加字典）
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String country;

    /**
     * 地址信息（通过currentLocation值抽取城市名称）   ->      current_location
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
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
     * 粉丝人数-个人/ 关注数- 公共page（粉丝总数）,      ->     follower_number
     */
    private String followers_count;

    /**
     * 关注数
     */
    private String friend_count;

    /**
     * 发文数
     */
    private String post_count;

    /**
     * 喜欢人数（点赞人数）  -> like_number
     */
    private String like_count;

    /**
     * 数据产生的时间，如果没有产生时间，则填写入库时间   ->    create_time
     */
    private String source_create_time;

    /**
     * 联系方式（电话号码“-”连接）,     ->     user_phone
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String mobile;

    /**
     * 联系方式和基本信息（部分有值，例：Facebook:/tobu.hiroshi.takano|邮箱:celicas2245@gmail.com）  ->    contact_and_basic_info
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String email;

    /**
     * 用户曾用名（数据部分为空）      ->      name_used_before
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String name_userd_before;

    /**
     * 用户语言（数据部分为空）        ->      user_language
     */
    private String language;

    /**
     * 宗教信仰（数据部分为空）
     */
    private String user_religion;

    /**
     * 工作信息（数据部分为空）      ->      work_message
     */
    private String works;

    /**
     * 地址信息（现居地）         ->       current_location
     */
    private String location;

    /**
     * 夫妻关系（数据部分为空）   ->       significant_other
     */
    private String marriage;

    /**
     * 家乡信息、生活过的地方（部分有值，例：来自：Carolina Beach, North Carolina）   ->   hometown_message
     */
    private String home_town;

    /**
     * 政治观点（数据部分为空）
     */
    private String user_political_views;

    /**
     * 用户简介(个人账户-Intro)（部分有值，例：["目前就职：長距離トラックドライバー","长野市","所在地：Chikuma-shi, Nagano, Japan"]）
     */
  //  @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String user_summary;

    /**
     * 用户系统名（数据部分为空）
     */
    private String user_systent_name;

    /**
     * 采集层统一的博主地址（样例数据全部为空，新数据都有值）
     */
    private String w3_fb_url;

    /**
     * 隶属机构ID（数据部分为空）
     */
    private String institution_id;

    /**
     * 是否社区page页，接口API返回，1：是，0：否（数据部分为空）
     */
    private String is_community_page;

    /**
     * 传播理念（数据部分为空）
     */
    private String communication_philosophy;

    /**
     * 拥有产品，企业账号（数据部分为空）
     */
    private String have_product;

    /**
     * 交流人数，企业账号（数据部分为空）
     */
    private String exchange_number;

    /**
     * 访问人数，企业账号（数据部分为空）
     */
    private String visit_number;

    /**
     * 姓名-姓（预留）（数据部分为空）
     */
    private String first_name;

    /**
     * 姓名-名（预留）（数据部分为空）
     */
    private String last_name;

    /**
     * 教育信息（部分有值，例：目前就职：Gustavo, Asoc. Ind. Herbalife|2017年6月11日至今|曾经就读：UNC Charlotte）
     */
    private String teach_message;

    /**
     * 采集时间
     */
    private String acquisition_time;

    /**
     * 情感状态（数据部分为空）
     */
    private String affective_state;

    /**
     * 背景图片
     */
    private String background_picture_url;

    /**
     * 商家故事* 'name': 商家故事标题名称,* 'id': 商家故事id,* 'photoUrl': 商家故事图片url,* 'localProfilePicUrl': 商家故事图片本地存储路径，采集存储结果类型list
     */
    private String business_story;

    /**
     * 分类信息（例：政府公职服务机构）
     */
    private String classify_message;

    /**
     * comFrom：数据来源  blogger-博主 keyword-关键词 friendships-博主关系  Forwardingrelationship-转发关系  commentrelationship-评论关系 oher-其他 等（数据部分为空）
     */
    private String com_from;

    /**
     * 人物标签-算法（例：军政人物）
     */
    private String dm_tag1;

    /**
     * 公司简介（数据部分为空）
     */
    private String company_profile;

    /**
     * 地理位置信息  --算法预留（例：USA|美国）
     */
    private String country_region;

    /**
     * 地理位置信息-城市  --算法预留（数据部分为空)
     */
    private String country_region_city;

    /**
     * 详细简介(大部分数据为空，样例见备注)
     */
    private String detailed_summary;

    /**
     * 格言信息（数据部分为空）
     */
    private String favorite_quotes;

    /**
     * 成立时间（数据部分为空）
     */
    private String found;

    /**
     * 性取向（部分有值，例：女性）
     */
    private String gender_orientation;

    /**
     * 经历 生活纪事（部分有值，例：2021年:長野県 上田市出身）
     */
    private String go_through;

    /**
     * 喜欢人数（转化为数字）（样例数据全部为0）
     */
    private String like_number_int;

    /**
     * 本地背景图路径（数据部分为空）
     */
    private String local_profile_pic_background_url;

    /**
     * 媒体文字 ²文章含有媒介时，多个之间用’|’隔开。（部分有值，例：头像|背景图）
     */
    private String media_title;

    /**
     * 媒介类型 媒介类型 ‘P’:  图片； ‘M’:  音频； ‘V’:  视频； ‘X’:  附件(文章) （部分有值，例：P|P）
     */
    private String media_type_embeded;

    /**
     * 媒体URL ²文章含有媒介时，多个之间用’|’隔开。（部分有值，示例见备注）
     */
    private String media_url;

    /**
     * 文章含有媒介系统生成文件名 文章含有媒介系统生成文件名，用于下载文件后关联及内容模型识别结果回更，多个文件名之间用’|’隔开。（部分有值，示例见备注）
     */
    private String media_url_name;

    /**
     * 运营时间（数据部分为空）
     */
    private String opening_hours;

    /**
     * 个人网址（部分为空，例：http://www.dragospislaru.eu/）
     */
    private String personal_web_url;

    /**
     * 相册url地址（数据部分为空）
     */
    private String photo_album_url;

    /**
     * 照片墙链接 采集存储结果多个以| 分割（数据部分为空）
     */
    private String photo_wall;

    /**
     * 位置信息（部分有值，例：{"latitude":"59.914375228846","longitude":"10.743255615234"}）
     */
    private String position_message;

    /**
     * 签到数（样例数据全部为0）
     */
    private String register_number;

    /**
     * 注册日期（数据部分为空）
     */
    private String registration_date;

    /**
     * 相关主页信息（数据部分为空）
     */
    private String related_home_page;

    /**
     * 商品信息；（数据部分为空）
     */
    private String shop_content;

    /**
     * 家庭成员与感情情况（数据部分为空）
     */
    private String family_and_relation_ships;

    /**
     * 技能 技能列表（部分为空，示例见备注）
     */
    private String skill;

    /**
     * 用户生日（部分有值，例：10月29日）
     */
    private String user_birthday;

    /**
     * 用户分类（示例：["政府机构"]）
     */
    private String user_classify;

    /**
     * 用户描述（部分有值，示例见备注）
     */
    private String user_description;

    /**
     * 用户主页地址，带名称的URL（部分有值，例：https://www.facebook.com/travelGoC/）
     */
    private String user_facebook_url;

    /**
     * 认证原因（数据部分为空）
     */
    private String verified_reason;

    /**
     * 字段非空值累加
     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private Integer integrity;

    public FbUserImplData() {}

    public FbUserImplData(String uuid, String platform, String data_source, String create_time, String importance, String remark, String language_type, String source_id, String user_id, String screen_name, String use_name, String user_url, String user_avatar, String local_photo_url, String gender, String country, String city, String user_type, String verified, String followers_count, String friend_count, String post_count, String like_count, String source_create_time, String mobile, String email, String name_userd_before, String language, String user_religion, String works, String location, String marriage, String home_town, String user_political_views, String user_summary, String user_systent_name, String w3_fb_url, String institution_id, String is_community_page, String communication_philosophy, String have_product, String exchange_number, String visit_number, String first_name, String last_name, String teach_message, String acquisition_time, String affective_state, String background_picture_url, String business_story, String classify_message, String com_from, String dm_tag1, String company_profile, String country_region, String country_region_city, String detailed_summary, String favorite_quotes, String found, String gender_orientation, String go_through, String like_number_int, String local_profile_pic_background_url, String media_title, String media_type_embeded, String media_url, String media_url_name, String opening_hours, String personal_web_url, String photo_album_url, String photo_wall, String position_message, String register_number, String registration_date, String related_home_page, String shop_content, String family_and_relation_ships, String skill, String user_birthday, String user_classify, String user_description, String user_facebook_url, String verified_reason, Integer integrity) {
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
        this.user_political_views = user_political_views;
        this.user_summary = user_summary;
        this.user_systent_name = user_systent_name;
        this.w3_fb_url = w3_fb_url;
        this.institution_id = institution_id;
        this.is_community_page = is_community_page;
        this.communication_philosophy = communication_philosophy;
        this.have_product = have_product;
        this.exchange_number = exchange_number;
        this.visit_number = visit_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.teach_message = teach_message;
        this.acquisition_time = acquisition_time;
        this.affective_state = affective_state;
        this.background_picture_url = background_picture_url;
        this.business_story = business_story;
        this.classify_message = classify_message;
        this.com_from = com_from;
        this.dm_tag1 = dm_tag1;
        this.company_profile = company_profile;
        this.country_region = country_region;
        this.country_region_city = country_region_city;
        this.detailed_summary = detailed_summary;
        this.favorite_quotes = favorite_quotes;
        this.found = found;
        this.gender_orientation = gender_orientation;
        this.go_through = go_through;
        this.like_number_int = like_number_int;
        this.local_profile_pic_background_url = local_profile_pic_background_url;
        this.media_title = media_title;
        this.media_type_embeded = media_type_embeded;
        this.media_url = media_url;
        this.media_url_name = media_url_name;
        this.opening_hours = opening_hours;
        this.personal_web_url = personal_web_url;
        this.photo_album_url = photo_album_url;
        this.photo_wall = photo_wall;
        this.position_message = position_message;
        this.register_number = register_number;
        this.registration_date = registration_date;
        this.related_home_page = related_home_page;
        this.shop_content = shop_content;
        this.family_and_relation_ships = family_and_relation_ships;
        this.skill = skill;
        this.user_birthday = user_birthday;
        this.user_classify = user_classify;
        this.user_description = user_description;
        this.user_facebook_url = user_facebook_url;
        this.verified_reason = verified_reason;
        this.integrity = integrity;
    }
}
