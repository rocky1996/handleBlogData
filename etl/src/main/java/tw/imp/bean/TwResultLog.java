package tw.imp.bean;

import com.google.gson.annotations.SerializedName;
import common.bean.BaseLog;

public class TwResultLog extends BaseLog {

    private String user_web_url;
    private String born_time;
    private String registered_time;
    private String bkgd_url;
    private String listed;
    private String moments;
    @SerializedName("protect_ed")
    private String protect;
    private String tf_effective;
    private String time_zone;
    private String com_from;
    private String diff_time;

    public String getUser_web_url() {
        return user_web_url;
    }

    public void setUser_web_url(String user_web_url) {
        this.user_web_url = user_web_url;
    }

    public String getBorn_time() {
        return born_time;
    }

    public void setBorn_time(String born_time) {
        this.born_time = born_time;
    }

    public String getRegistered_time() {
        return registered_time;
    }

    public void setRegistered_time(String registered_time) {
        this.registered_time = registered_time;
    }

    public String getBkgd_url() {
        return bkgd_url;
    }

    public void setBkgd_url(String bkgd_url) {
        this.bkgd_url = bkgd_url;
    }

    public String getListed() {
        return listed;
    }

    public void setListed(String listed) {
        this.listed = listed;
    }

    public String getMoments() {
        return moments;
    }

    public void setMoments(String moments) {
        this.moments = moments;
    }

    public String getProtect() {
        return protect;
    }

    public void setProtect(String protect) {
        this.protect = protect;
    }

    public String getTf_effective() {
        return tf_effective;
    }

    public void setTf_effective(String tf_effective) {
        this.tf_effective = tf_effective;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getCom_from() {
        return com_from;
    }

    public void setCom_from(String com_from) {
        this.com_from = com_from;
    }

    public String getDiff_time() {
        return diff_time;
    }

    public void setDiff_time(String diff_time) {
        this.diff_time = diff_time;
    }

}
