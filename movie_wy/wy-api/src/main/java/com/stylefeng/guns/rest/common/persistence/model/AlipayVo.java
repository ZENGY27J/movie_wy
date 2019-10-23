package com.stylefeng.guns.rest.common.persistence.model;

import java.io.Serializable;

public class AlipayVo implements Serializable {

    private static final long serialVersionUID = 6058069635091325102L;
    private String orderId;
    private String qRCodeAddress;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getqRCodeAddress() {
        return qRCodeAddress;
    }

    public void setqRCodeAddress(String qRCodeAddress) {
        this.qRCodeAddress = qRCodeAddress;
    }
}
