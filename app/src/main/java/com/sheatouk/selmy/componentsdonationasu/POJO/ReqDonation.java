package com.sheatouk.selmy.componentsdonationasu.POJO;

/**
 * Created by pc on 06/05/2017.
 */

public class ReqDonation {
    private String reqId;
    private Long time;

    public ReqDonation() {

    }

    public ReqDonation(String reqId, Long time) {
        this.reqId = reqId;
        this.time = time;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
