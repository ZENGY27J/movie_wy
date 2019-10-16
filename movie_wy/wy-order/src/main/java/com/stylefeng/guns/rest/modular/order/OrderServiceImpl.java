package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Service;
import com.wuyan.order.OrderService;
import com.wuyan.order.vo.OrderVo;
import com.wuyan.order.vo.Page;
import org.springframework.stereotype.Component;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 订单业务接口实现类
 * @Date: 2019-10-16-11:45
 */
@Component
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Override
    public boolean isTrueSeats(String fieldId, String seats) {
        return false;
    }

    @Override
    public boolean isNoSoldSeats(String fieldId, String seats) {
        return false;
    }

    @Override
    public OrderVo saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        return null;
    }

    @Override
    public Page<OrderVo> getOrderByUserId(Integer UserId, Page<OrderVo> page) {
        return null;
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        return null;
    }
}
