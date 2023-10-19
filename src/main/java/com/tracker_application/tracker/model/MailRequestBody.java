package com.tracker_application.tracker.model;

public class MailRequestBody {
    private String mailTo;
    private String userid;
    private String resp;

    public MailRequestBody() {
    }

    public MailRequestBody(String mailTo, String userId, String resp) {
        this.mailTo = mailTo;
        this.userid = userId;
        this.resp = resp;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
