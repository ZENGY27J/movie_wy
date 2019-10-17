package com.stylefeng.guns.rest.common.persistence.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alipay.demo.trade.Main;
import com.stylefeng.guns.rest.modular.order.OrderServiceImpl;
import com.wuyan.order.OrderService;
import com.wuyan.pay.PayService;
import com.wuyan.pay.bean.PayInfo;
import com.wuyan.pay.bean.PayResult;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = PayService.class)
public class PayServiceImpl implements PayService{

    @Reference(interfaceClass = OrderService.class)
    OrderService orderService;

    @Override
    public PayInfo getPayInfo(String orderId) {
        PayInfo payInfo = new PayInfo();
        Main main = new Main();
        payInfo.setOrderId(orderId);
        String s = orderId + ".png";
        payInfo.setORCodeAddress(s);
        try {
            main.test_trade_precreate(orderId);
            return payInfo;
        }catch (Throwable e){
            return null;
        }
    }

    @Override
    public PayResult getPayResult(String orderId, Integer tryNums) {
        Main main = new Main();
        Boolean flag = main.test_trade_query(orderId);
        if (tryNums > 3){
            orderService.updateOrderStatus(orderId,2);
            return null;
        }else if (!flag){
            return null;
        }
        orderService.updateOrderStatus(orderId,1);
        PayResult payResult = new PayResult();
        payResult.setOrderId(orderId);
        payResult.setOrderStatus(1);
        payResult.setOrderMsg("支付成功");
        return payResult;
    }
}
