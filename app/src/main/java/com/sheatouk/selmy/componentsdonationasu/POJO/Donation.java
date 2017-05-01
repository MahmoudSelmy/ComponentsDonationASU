package com.sheatouk.selmy.componentsdonationasu.POJO;

/**
 * Created by pc on 01/05/2017.
 */

public class Donation {
    String donatorId,reqUserId,productId;
    Date start,end;

    public Donation() {
    }

    public Donation(String donatorId, String reqUserId, String productId, Date start, Date end) {
        this.donatorId = donatorId;
        this.reqUserId = reqUserId;
        this.productId = productId;
        this.start = start;
        this.end = end;
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
