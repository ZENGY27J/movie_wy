package com.wuyan.pay.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayInfo implements Serializable {

    private static final long serialVersionUID = 8588522818178293453L;
    String orderId;
    String ORCodeAddress;
}
