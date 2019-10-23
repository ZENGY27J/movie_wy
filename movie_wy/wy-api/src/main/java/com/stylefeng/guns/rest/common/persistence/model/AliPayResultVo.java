package com.stylefeng.guns.rest.common.persistence.model;

import java.io.Serializable;

public class AliPayResultVo implements Serializable {
    private static final long serialVersionUID = 110198775729349014L;
    private String orderId;
    private String orderMsg;
    private int orderStatus;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderMsg() {
        return orderMsg;
    }

    public void setOrderMsg(String orderMsg) {
        this.orderMsg = orderMsg;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
