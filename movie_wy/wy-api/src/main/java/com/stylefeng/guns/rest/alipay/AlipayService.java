package com.stylefeng.guns.rest.alipay;

import com.stylefeng.guns.rest.common.persistence.model.AliPayResultVo;
import com.stylefeng.guns.rest.common.persistence.model.AlipayVo;

public interface AlipayService {
    AlipayVo getPayInfo(String orderId);

    AliPayResultVo getPayResult(String orderId);
}
