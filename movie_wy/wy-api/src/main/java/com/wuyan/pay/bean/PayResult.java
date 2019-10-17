package com.wuyan.pay.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayResult implements Serializable {

    private static final long serialVersionUID = -3861475848112627182L;
    String orderId;
    Integer orderStatus;
    String orderMsg;
}
