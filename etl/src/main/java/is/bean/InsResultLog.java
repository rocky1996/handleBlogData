package is.bean;

import common.bean.BaseLog;

public class InsResultLog extends BaseLog {

    private String external_url;
    private String fetch_day;
    private String profile_pic_url_oss;
    private String media_url;
    private String person;
    private String sent_num;

    public String getExternal_url() {
        return external_url;
    }

    public void setExternal_url(String external_url) {
        this.external_url = external_url;
    }

    public String getFetch_day() {
        return fetch_day;
    }

    public void setFetch_day(String fetch_day) {
        this.fetch_day = fetch_day;
    }

    public String getProfile_pic_url_oss() {
        return profile_pic_url_oss;
    }

    public void setProfile_pic_url_oss(String profile_pic_url_oss) {
        this.profile_pic_url_oss = profile_pic_url_oss;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getSent_num() {
        return sent_num;
    }

    public void setSent_num(String sent_num) {
        this.sent_num = sent_num;
    }
}
