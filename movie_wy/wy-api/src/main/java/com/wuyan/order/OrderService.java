package com.wuyan.order;

import com.wuyan.order.vo.OrderVo;
import com.wuyan.order.vo.Page;

import java.io.IOException;

public interface OrderService {

    // 验证售出的票是否为真
    boolean isTrueSeats(String fieldId, String seats) throws IOException;

    // 已经售出的座位里，有没有这些座位
    boolean isNoSoldSeats(String fieldId, String seats);

    // 创建新的订单信息
    OrderVo saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId);

    // 使用当前登录人信息，获取已经购买的订单
    Page<OrderVo> getOrderByUserId(Integer userId, Page<OrderVo> page);

    //根据FieldId 获取所有已经销售的座位编号
    String getSoldSeatsByFieldId(Integer fieldId);
}
