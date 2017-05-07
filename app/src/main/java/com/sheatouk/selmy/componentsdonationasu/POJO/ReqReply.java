package com.sheatouk.selmy.componentsdonationasu.POJO;

/**
 * Created by pc on 06/05/2017.
 */

public class ReqReply {
    private String productName,donatorName;
    private long time;
    private Boolean accepted,seen;

    public ReqReply() {
    }

    public ReqReply(String productName, String donatorName, long time, Boolean accepted,Boolean seen) {
        this.productName = productName;
        this.donatorName = donatorName;
        this.time = time;
        this.accepted = accepted;
        this.seen = seen;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDonatorName() {
        return donatorName;
    }

    public void setDonatorName(String donatorName) {
        this.donatorName = donatorName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
