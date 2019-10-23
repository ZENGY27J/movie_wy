package com.stylefeng.guns.rest.modular.alipay;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.alipay.AlipayService;
import com.stylefeng.guns.rest.common.persistence.model.AliPayResultVo;
import com.stylefeng.guns.rest.common.persistence.model.AlipayVo;
import com.stylefeng.guns.rest.common.persistence.model.Vo.ResultVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("order")
public class AlipayController {

    @Reference(interfaceClass = AlipayService.class, check = false)
    AlipayService alipayService;

    @RequestMapping("/getPayInfo")
    public ResultVo getPayInfo(String orderId) {
        AlipayVo alipayVo = alipayService.getPayInfo(orderId);
        ResultVo resultVo = new ResultVo();
        resultVo.setImgPre("https://wymovie.oss-cn-shenzhen.aliyuncs.com/");
        resultVo.setStatus(0);
        resultVo.setData(alipayVo);
        return resultVo;
    }

    @RequestMapping("/getPayResult")
    public ResultVo getPayResult(String orderId, int tryNums) {
        ResultVo resultVo = new ResultVo();
        /*if (tryNums > 3) {
            resultVo.setStatus(1);
            resultVo.setMsg("订单支付失败，请稍后重试");
        }*/
        AliPayResultVo payResultVo = alipayService.getPayResult(orderId);
        if (payResultVo.getOrderStatus() != 1 && tryNums == 5) {
            resultVo.setStatus(0);
            resultVo.setData(payResultVo);
            resultVo.setMsg("订单支付失败,请刷新页面重试!");
            return resultVo;
        } else {
            resultVo.setData(payResultVo);
            resultVo.setStatus(0);
            return resultVo;
        }
    }
}
