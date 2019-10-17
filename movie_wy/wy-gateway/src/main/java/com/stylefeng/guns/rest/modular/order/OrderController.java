package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.modular.auth.util.Json2BeanUtils;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.vo.ReBaseDataVo;
import com.stylefeng.guns.rest.modular.vo.ReBaseMsgVo;
import com.stylefeng.guns.rest.modular.vo.ReBaseVo;
import com.wuyan.order.OrderService;
import com.wuyan.order.vo.Page;
import com.wuyan.order.vo.SeatsInfo;
import com.wuyan.user.bean.UserInfoModel;
import com.wuyan.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 订单服务控制层
 * @Date: 2019-10-16-09:28
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Reference(interfaceClass = OrderService.class)
    OrderService orderService;

    /**
     * 用户下单购票
     *
     * @param request
     * @return
     */
    @RequestMapping("/buyTickets")
    public ReBaseVo buyTickets(SeatsInfo seatsInfo, HttpServletRequest request) {
        // 1.验证售出的票是否为真
        boolean trueSeats = false;
        try {
            trueSeats = orderService.isTrueSeats(seatsInfo.getFieldId(), seatsInfo.getSoldSeats());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!trueSeats) {
            ReBaseMsgVo reBaseMsgVo = ReBaseMsgVo.serviceEx();
            reBaseMsgVo.setMsg("座位信息错误，请重新选择");
        }
        // 2.已经售出的座位里，有没有这些座位
        boolean noSoldSeats = orderService.isNoSoldSeats(seatsInfo.getFieldId(), seatsInfo.getSoldSeats());
        if (!noSoldSeats) {
            ReBaseMsgVo reBaseMsgVo = ReBaseMsgVo.serviceEx();
            reBaseMsgVo.setMsg("该座位已经被购买");
            return reBaseMsgVo;
        }
        // 3.下单，生成订单信息
        String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken;
        try {
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                authToken = requestHeader.substring(7);
                String userInfoFromToken = jwtTokenUtil.getUsernameFromToken(authToken);
                UserInfoModel userInfo = new UserInfoModel();
                userInfo = (UserInfoModel) Json2BeanUtils.jsonToObj(userInfo, userInfoFromToken);
                OrderVo orderVo = orderService.saveOrderInfo(Integer.valueOf(seatsInfo.getFieldId()), seatsInfo.getSoldSeats(), seatsInfo.getSeatsName(), userInfo.getUuid());
                ReBaseDataVo ok = ReBaseDataVo.ok(orderVo);
                return ok;
            }
            return ReBaseMsgVo.systemEx();
        } catch (Exception e) {
            return ReBaseMsgVo.systemEx();
        }
    }

    /**
     * 获取用户订单信息接口
     *
     * @param request
     * @return
     */
    @RequestMapping("/getOrderInfo")
    public ReBaseVo getOrderInfo(Page<OrderVo> orderVoPage, HttpServletRequest request) {
        String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken;
        try {
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                authToken = requestHeader.substring(7);
                String userInfoFromToken = jwtTokenUtil.getUsernameFromToken(authToken);
                UserInfoModel userInfo = new UserInfoModel();
                userInfo = (UserInfoModel) Json2BeanUtils.jsonToObj(userInfo, userInfoFromToken);

                Page<OrderVo> orderByUserId = orderService.getOrderByUserId(userInfo.getUuid(), orderVoPage);
                ReBaseDataVo ok = ReBaseDataVo.ok(orderByUserId.getList());
                return ok;
            }
            return ReBaseMsgVo.systemEx();
        } catch (Exception e) {
            return ReBaseMsgVo.systemEx();
        }
    }
}
