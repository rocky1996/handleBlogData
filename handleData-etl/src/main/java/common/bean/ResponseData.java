package common.bean;

public class ResponseData {

    private int confidence;
    private String language;

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }
    public int getConfidence() {
        return confidence;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public String getLanguage() {
        return language;
    }
}
