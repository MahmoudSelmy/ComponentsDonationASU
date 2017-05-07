package com.sheatouk.selmy.componentsdonationasu.POJO;

/**
 * Created by pc on 01/05/2017.
 */

public class Donation {
    private String donatorId,donatorName,reqUserId,productId,reqUserName,productName;
    private Date start,end;
    private long time;
    private Boolean waiting;

    public Donation() {
    }

    public Donation(String donatorId,String donatorName, String reqUserId,String reqUserName, String productId,String productName, Date start, Date end,Long time,Boolean waiting) {
        this.donatorId = donatorId;
        this.reqUserId = reqUserId;
        this.productId = productId;
        this.start = start;
        this.end = end;
        this.time = time;
        this.waiting = waiting;
        this.reqUserName = reqUserName;
        this.productName = productName;
        this.donatorName = donatorName;
    }

    public String getDonatorName() {
        return donatorName;
    }

    public void setDonatorName(String donatorName) {
        this.donatorName = donatorName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getReqUserName() {
        return reqUserName;
    }

    public void setReqUserName(String reqUserName) {
        this.reqUserName = reqUserName;
    }

    public Boolean getWaiting() {
        return waiting;
    }

    public void setWaiting(Boolean waiting) {
        this.waiting = waiting;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDonatorId() {
        return donatorId;
    }

    public void setDonatorId(String donatorId) {
        this.donatorId = donatorId;
    }

    public String getReqUserId() {
        return reqUserId;
    }

    public void setReqUserId(String reqUserId) {
        this.reqUserId = reqUserId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
