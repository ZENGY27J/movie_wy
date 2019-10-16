package com.stylefeng.guns.rest.modular.order;

import com.stylefeng.guns.rest.modular.vo.ReBaseVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 订单服务控制层
 * @Date: 2019-10-16-09:28
 */
@RestController
@RequestMapping("order")
public class OrderController {

    /**
     * 用户下单购票
     * @param request
     * @return
     */
    @RequestMapping("/buyTickets")
    public ReBaseVo buyTickets(HttpServletRequest request) {

        return null;
    }

    /**
     * 获取用户订单信息接口
     * @param request
     * @return
     */
    @RequestMapping("/getOrderInfo")
    public ReBaseVo getOrderInfo(HttpServletRequest request) {

        return null;
    }

}
