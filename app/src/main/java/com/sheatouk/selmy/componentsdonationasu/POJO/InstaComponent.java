package com.sheatouk.selmy.componentsdonationasu.POJO;

/**
 * Created by pc on 01/05/2017.
 */

public class InstaComponent {
    String productId,ownerId;
    Boolean available;

    public InstaComponent() {
    }

    public InstaComponent(String productId, String ownerId,Boolean available) {
        this.productId = productId;
        this.ownerId = ownerId;
        this.available = available;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
