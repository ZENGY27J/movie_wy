package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.rest.modular.auth.util.Json2BeanUtils;
import com.stylefeng.guns.rest.modular.auth.util.SeatsInfoUtils;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFilmTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.wuyan.order.OrderService;
import com.wuyan.order.vo.Page;
import com.wuyan.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

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
        String s1 = jedis.get(seatAddress);
        if (StringUtils.isBlank(jedis.get(seatAddress))) {
            String jsonString = SeatsInfoUtils.getJsonString(seatAddress);
            jedis.set(seatAddress,jsonString);
            jedis.expire(seatAddress, 3600);
        }
        String myseats = jedis.get(seatAddress);
        MySeatsInfo mySeatsInfo = new MySeatsInfo();
//        MySeatsInfo mySeatsInfoforJson = (MySeatsInfo) Json2BeanUtils.jsonToObj(mySeatsInfo, myseats);
        MySeatsInfo mySeatsInfoforJson = (MySeatsInfo) Json2BeanUtils.json2Bean(mySeatsInfo,myseats);
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
        String soldSeats = getSoldSeatsByFieldId(Integer.parseInt(fieldId));
        String[] soldSeatsArray = soldSeats.split(",");
        String[] seatsArray = seats.split(",");

        List<String> soldSeatsList = Arrays.asList(soldSeatsArray);
        List<String> seatsList = Arrays.asList(seatsArray);

        for (String s : seatsList) {
            if (soldSeatsList.contains(s)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public OrderVo saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        MoocOrderT moocOrderT = new MoocOrderT();
        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);
        Integer cinemaId = mtimeFieldT.getCinemaId();
        Integer filmId = mtimeFieldT.getFilmId();
        String[] split = soldSeats.split(",");
        String uuid =  UUID.randomUUID().toString();
        uuid.replace("-", "");

        MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(cinemaId);
        MtimeFilmT mtimeFilmT = mtimeFilmTMapper.selectById(filmId);
        OrderVo orderVo = new OrderVo();
        orderVo.setCinemaName(mtimeCinemaT.getCinemaName());
        orderVo.setFilmName(mtimeFilmT.getFilmName());
        orderVo.setFieldTime(mtimeFieldT.getBeginTime());
        orderVo.setSeatsName(seatsName);
        orderVo.setOrderPrice( String.valueOf(mtimeFieldT.getPrice() * (split.length)));
        orderVo.setOrderStatus("未支付");
        orderVo.setOrderId(uuid);

        moocOrderT.setUuid(uuid);
        moocOrderT.setCinemaId(cinemaId);
        moocOrderT.setFieldId(fieldId);
        moocOrderT.setFilmId(mtimeFieldT.getFilmId());
        moocOrderT.setSeatsIds(soldSeats);
        moocOrderT.setSeatsName(seatsName);
        moocOrderT.setFilmPrice((double) mtimeFieldT.getPrice());
        moocOrderT.setOrderPrice((double) (mtimeFieldT.getPrice() * (split.length)));
        moocOrderT.setOrderTime(new Date());
        moocOrderT.setOrderUser(userId);
        moocOrderT.setOrderStatus(0);
        moocOrderTMapper.insert(moocOrderT);

        return orderVo;
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

    /**
     * @param fieldId
     * @return 返回所有已购座位
     */
    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        EntityWrapper<MoocOrderT> moocOrderTEntityWrapper = new EntityWrapper<>();
        moocOrderTEntityWrapper.eq("field_id", fieldId);
        List<MoocOrderT> moocOrderTS = moocOrderTMapper.selectList(moocOrderTEntityWrapper);
        StringBuffer soldSeats = new StringBuffer();

        int count = 0;
        for (MoocOrderT moocOrderT : moocOrderTS) {
            count++;
            String seatsIds = moocOrderT.getSeatsIds();
            if (moocOrderTS.size() == count) {
                return soldSeats.append(seatsIds).toString();
            }
            soldSeats.append(seatsIds).append(",");
        }

        return soldSeats.toString();
    }

    @Override
    public void updateOrderStatus(String orderId, Integer status) {
        MoocOrderT moocOrderT = moocOrderTMapper.selectById(orderId);
        moocOrderT.setOrderStatus(status);
        moocOrderTMapper.updateById(moocOrderT);
    }

}
