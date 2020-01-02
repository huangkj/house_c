package com.easylife.house.customer.bean;

public class BankCardOcrResult {

    /**
     * card_num : 9559930210373015416
     * request_id : 20181108163126_52b3a86032096bb2b7742cefce1f9c43
     * success : true
     */

    private String card_num;
    private String request_id;
    private boolean success;

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
