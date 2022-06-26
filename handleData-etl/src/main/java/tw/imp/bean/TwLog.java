package tw.imp.bean;

import com.google.gson.annotations.SerializedName;

public class TwLog {

    private int id;
    private String md5id;
    private String input_time;
    private String user_url;
    private String user_web_url;
    private String screen_name;
    private String blogger_id;
    private String fullname;
    private String user_addr;
    private String born_time;
    private String registered_time;
    private String useravatar;
    private String useravatar_md5;
    private String useravatar_state;
    private String bkgdurl;
    private String bkgdurl_md5;
    private String bkgdurl_state;
    private String userflag;
    private int tweets;
    private int following;
    private String followers;
    private int likes;
    private String listed;
    private String moments;
    private String verified;
    @SerializedName("protected")
    private String protect;
    private String tf_effective;
    private int everyday_tweets;
    private String update_clr_time;
    private String spider_task_id;
    private String is_full;
    private String source_tag;
    private String comfrom;
    private String remarks;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private String spidertype;
    private String lang;
    private String email;
    private String time_zone;
    private String level;
    private String diff_time;
    private String country;
    private String relation;

    private String gender;
    private String userType = "-1";
    private String extend;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setMd5id(String md5id) {
        this.md5id = md5id;
    }
    public String getMd5id() {
        return md5id;
    }

    public void setInput_time(String input_time) {
        this.input_time = input_time;
    }
    public String getInput_time() {
        return input_time;
    }

    public void setUser_url(String user_url) {
        this.user_url = user_url;
    }
    public String getUser_url() {
        return user_url;
    }

    public void setUser_web_url(String user_web_url) {
        this.user_web_url = user_web_url;
    }
    public String getUser_web_url() {
        return user_web_url;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }
    public String getScreen_name() {
        return screen_name;
    }

    public void setBlogger_id(String blogger_id) {
        this.blogger_id = blogger_id;
    }
    public String getBlogger_id() {
        return blogger_id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getFullname() {
        return fullname;
    }

    public void setUser_addr(String user_addr) {
        this.user_addr = user_addr;
    }
    public String getUser_addr() {
        return user_addr;
    }

    public void setBorn_time(String born_time) {
        this.born_time = born_time;
    }
    public String getBorn_time() {
        return born_time;
    }

    public String getRegistered_time() {
        return registered_time;
    }

    public void setRegistered_time(String registered_time) {
        this.registered_time = registered_time;
    }

    public void setUseravatar(String useravatar) {
        this.useravatar = useravatar;
    }
    public String getUseravatar() {
        return useravatar;
    }

    public void setUseravatar_md5(String useravatar_md5) {
        this.useravatar_md5 = useravatar_md5;
    }
    public String getUseravatar_md5() {
        return useravatar_md5;
    }

    public void setUseravatar_state(String useravatar_state) {
        this.useravatar_state = useravatar_state;
    }
    public String getUseravatar_state() {
        return useravatar_state;
    }

    public void setBkgdurl(String bkgdurl) {
        this.bkgdurl = bkgdurl;
    }
    public String getBkgdurl() {
        return bkgdurl;
    }

    public void setBkgdurl_md5(String bkgdurl_md5) {
        this.bkgdurl_md5 = bkgdurl_md5;
    }
    public String getBkgdurl_md5() {
        return bkgdurl_md5;
    }

    public void setBkgdurl_state(String bkgdurl_state) {
        this.bkgdurl_state = bkgdurl_state;
    }
    public String getBkgdurl_state() {
        return bkgdurl_state;
    }

    public void setUserflag(String userflag) {
        this.userflag = userflag;
    }
    public String getUserflag() {
        return userflag;
    }

    public void setTweets(int tweets) {
        this.tweets = tweets;
    }
    public int getTweets() {
        return tweets;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
    public int getFollowing() {
        return following;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }
    public String getFollowers() {
        return followers;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
    public int getLikes() {
        return likes;
    }

    public void setListed(String listed) {
        this.listed = listed;
    }
    public String getListed() {
        return listed;
    }

    public void setMoments(String moments) {
        this.moments = moments;
    }
    public String getMoments() {
        return moments;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }
    public String getVerified() {
        return verified;
    }

    public void setProtect(String protect) {
        this.protect = protect;
    }
    public String getProtect() {
        return protect;
    }

    public void setTf_effective(String tf_effective) {
        this.tf_effective = tf_effective;
    }
    public String getTf_effective() {
        return tf_effective;
    }

    public void setEveryday_tweets(int everyday_tweets) {
        this.everyday_tweets = everyday_tweets;
    }
    public int getEveryday_tweets() {
        return everyday_tweets;
    }

    public void setUpdate_clr_time(String update_clr_time) {
        this.update_clr_time = update_clr_time;
    }
    public String getUpdate_clr_time() {
        return update_clr_time;
    }

    public void setSpider_task_id(String spider_task_id) {
        this.spider_task_id = spider_task_id;
    }
    public String getSpider_task_id() {
        return spider_task_id;
    }

    public void setIs_full(String is_full) {
        this.is_full = is_full;
    }
    public String getIs_full() {
        return is_full;
    }

    public void setSource_tag(String source_tag) {
        this.source_tag = source_tag;
    }
    public String getSource_tag() {
        return source_tag;
    }

    public void setComfrom(String comfrom) {
        this.comfrom = comfrom;
    }
    public String getComfrom() {
        return comfrom;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getRemarks() {
        return remarks;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }
    public String getReserved1() {
        return reserved1;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }
    public String getReserved2() {
        return reserved2;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }
    public String getReserved3() {
        return reserved3;
    }

    public void setSpidertype(String spidertype) {
        this.spidertype = spidertype;
    }
    public String getSpidertype() {
        return spidertype;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    public String getLang() {
        return lang;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }
    public String getTime_zone() {
        return time_zone;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String getLevel() {
        return level;
    }

    public void setDiff_time(String diff_time) {
        this.diff_time = diff_time;
    }
    public String getDiff_time() {
        return diff_time;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountry() {
        return country;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
    public String getRelation() {
        return relation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
