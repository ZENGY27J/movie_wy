package com.wuyan.pay;

import com.wuyan.pay.bean.PayInfo;
import com.wuyan.pay.bean.PayResult;

public interface PayService {
    PayInfo getPayInfo(String orderId);

    PayResult getPayResult(String orderId, Integer tryNums);
}
