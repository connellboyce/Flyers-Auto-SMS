package com.connellboyce.flyersautosms.payload.request;

public class SmsRequest {
    private String smsSubject;

    private String smsContent;

    public SmsRequest(String smsSubject, String smsContent) {
        this.smsContent = smsContent;
        this.smsSubject = smsSubject;
    }

    public String getSmsSubject() { return smsSubject; }

    public void setSmsSubject(String smsSubject) {
        this.smsSubject = smsSubject;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }
}
