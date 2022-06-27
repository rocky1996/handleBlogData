package common.bean;

public class TranslateResponse {

    private ResponseData responseData;
    private String responseDetails;
    private int responseStatus;

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }
    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseDetails(String responseDetails) {
        this.responseDetails = responseDetails;
    }
    public String getResponseDetails() {
        return responseDetails;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }
    public int getResponseStatus() {
        return responseStatus;
    }
}
