package common.bean;

public class Language {

    private String code;
    private String language;
    private String country;

    public Language() {
    }

    public Language(String code, String language, String country) {
        this.code = code;
        this.language = language;
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
