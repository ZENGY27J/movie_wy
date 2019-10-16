package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.rest.modular.auth.util.Json2BeanUtils;
import com.stylefeng.guns.rest.modular.auth.util.SeatsInfoUtils;
import com.wuyan.order.OrderService;
import com.wuyan.order.vo.OrderVo;
import com.wuyan.order.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 订单业务接口实现类
 * @Date: 2019-10-16-11:45
 */
@Component
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private Jedis jedis;

    @Autowired
    MoocOrderTMapper moocOrderTMapper;

    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;

    @Autowired
    MtimeHallDictTMapper mtimeHallDictTMapper;

    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;

    @Autowired
    MtimeFilmTMapper mtimeFilmTMapper;

    @Override
    public boolean isTrueSeats(String fieldId, String seats) throws IOException {
        EntityWrapper<MtimeFieldT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("UUID",fieldId);
        List<MtimeFieldT> mtimeFieldTS = mtimeFieldTMapper.selectList(entityWrapper);
        if (CollectionUtils.isEmpty(mtimeFieldTS)) {
            return false;
        }
        Integer hallId = mtimeFieldTS.get(0).getHallId();
        MtimeHallDictT mtimeHallDictT = mtimeHallDictTMapper.selectById(hallId);
        String seatAddress = mtimeHallDictT.getSeatAddress();
        if (StringUtils.isBlank(jedis.get(seatAddress))) {
            String jsonString = SeatsInfoUtils.getJsonString(seatAddress);
            jedis.set(seatAddress,jsonString);
            jedis.expire(seatAddress, 3600);
        }
        String myseats = jedis.get(seatAddress);
        MySeatsInfo mySeatsInfo = new MySeatsInfo();
        MySeatsInfo mySeatsInfoforJson = (MySeatsInfo) Json2BeanUtils.jsonToObj(mySeatsInfo, seatAddress);
        String[] split = mySeatsInfoforJson.getIds().split(",");
        for (String s : split) {
            if (seats.equals(s)) {
                return true;
            }
        }
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
    public Page<OrderVo> getOrderByUserId(Integer userId, Page<OrderVo> page) {

        com.baomidou.mybatisplus.plugins.Page<Object> objectPage = new com.baomidou.mybatisplus.plugins.Page<>();
        objectPage.setCurrent(page.getNowPage());
        objectPage.setSize(page.getPageSize());

        OrderVo orderVo = new OrderVo();
        List<OrderVo> orderVoList = new LinkedList<>();
        EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("order_user",userId);
        List<MoocOrderT> moocOrderTS = moocOrderTMapper.selectPage(objectPage,entityWrapper);
        for (MoocOrderT moocOrderT : moocOrderTS) {
            EntityWrapper<MtimeCinemaT> entityWrapper1 = new EntityWrapper<>();
            entityWrapper.eq("UUID",moocOrderT.getCinemaId());
            List<MtimeCinemaT> mtimeCinemaTS = mtimeCinemaTMapper.selectList(entityWrapper1);
            String cinemaName = mtimeCinemaTS.get(0).getCinemaName();

            EntityWrapper<MtimeFilmT> entityWrapper2 = new EntityWrapper<>();
            entityWrapper.eq("UUID",moocOrderT.getFilmId());
            List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectList(entityWrapper2);
            String filmName = mtimeFilmTS.get(0).getFilmName();

            EntityWrapper<MtimeFieldT> entityWrapper3 = new EntityWrapper<>();
            entityWrapper.eq("UUID",moocOrderT.getFieldId());
            List<MtimeFieldT> mtimeFieldTS = mtimeFieldTMapper.selectList(entityWrapper3);
            String beginTime = mtimeFieldTS.get(0).getBeginTime();

            orderVo.setOrderId(moocOrderT.getUuid());
            orderVo.setFilmName(filmName);
            orderVo.setFieldTime(beginTime);
            orderVo.setCinemaName(cinemaName);
            orderVo.setSeatsName(moocOrderT.getSeatsName());
            orderVo.setOrderPrice(moocOrderT.getOrderPrice().toString());
            orderVo.setOrderTimestamp(moocOrderT.getOrderTime().toString());
            orderVo.setOrderStatus(moocOrderT.getOrderStatus().toString());
            orderVoList.add(orderVo);
        }
        page.setList(orderVoList);
        return page;
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        return null;
    }
}
