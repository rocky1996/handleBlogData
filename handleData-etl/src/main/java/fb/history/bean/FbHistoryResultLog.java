package fb.history.bean;

import common.bean.BaseLog;

public class FbHistoryResultLog extends BaseLog {

    private String user_systent_name;
    private String registration_date;
    private String user_birthday;
    private String user_classify;
    private String verified_reason;

    public String getUser_systent_name() {
        return user_systent_name;
    }

    public void setUser_systent_name(String user_systent_name) {
        this.user_systent_name = user_systent_name;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public void setUser_birthday(String user_birthday) {
        this.user_birthday = user_birthday;
    }

    public String getUser_classify() {
        return user_classify;
    }

    public void setUser_classify(String user_classify) {
        this.user_classify = user_classify;
    }

    public String getVerified_reason() {
        return verified_reason;
    }

    public void setVerified_reason(String verified_reason) {
        this.verified_reason = verified_reason;
    }
}
