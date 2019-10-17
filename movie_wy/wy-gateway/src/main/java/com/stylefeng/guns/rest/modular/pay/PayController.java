package com.stylefeng.guns.rest.modular.pay;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.modular.vo.ReBaseDataVo;
import com.stylefeng.guns.rest.modular.vo.ReBaseMsgVo;
import com.stylefeng.guns.rest.modular.vo.ReBaseVo;
import com.stylefeng.guns.rest.modular.vo.ResultVO;
import com.wuyan.pay.PayService;
import com.wuyan.pay.bean.PayInfo;
import com.wuyan.pay.bean.PayResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("order")
public class PayController {

    @Reference(interfaceClass = PayService.class,check = false)
    PayService payService;

    @RequestMapping("getPayInfo")
    public ReBaseVo getPayInfo(String orderId){
        PayInfo payInfo;
        try {
            payInfo = payService.getPayInfo(orderId);
        }catch (Throwable e){
            return ReBaseMsgVo.systemEx();
        }
        if (payInfo == null){
            return ReBaseMsgVo.error(1,"订单支付失败,请稍后重试");
        }
        return ResultVO.ok("C:\\png",payInfo);
    }

    @RequestMapping("getPayResult")
    public ReBaseVo getPayResult(@RequestBody Map map){
        String orderId = (String) map.get("orderId");
        Integer tryNums = (Integer) map.get("tryNums");
        PayResult payResult;
        try {
            payResult = payService.getPayResult(orderId, tryNums);
        }catch (Throwable e){
            return ReBaseMsgVo.error(999,"系统出现异常，请联系管理员");
        }
        if (payResult == null){
            return ReBaseMsgVo.error(1,"订单支付失败,请稍后重试");
        }
        return ReBaseDataVo.ok(payResult);
    }
}
