package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    MoocOrderTMapper moocOrderTMapper;
    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;
    @Autowired
    MtimeFilmTMapper mtimeFilmTMapper;
    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;

    @Override
    public boolean isTrueSeats(String fieldId, String seats) {
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
        uuid.replace("", "-");

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
    public Page<OrderVo> getOrderByUserId(Integer UserId, Page<OrderVo> page) {
        return null;
    }

    /**
     * @param fieldId
     * @return 返回所有已购座位
     */
    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        EntityWrapper<MoocOrderT> moocOrderTEntityWrapper = new EntityWrapper<>();
        moocOrderTEntityWrapper.eq("field_id", "fieldId");
        List<MoocOrderT> moocOrderTS = moocOrderTMapper.selectList(moocOrderTEntityWrapper);
        StringBuffer soldSeats = new StringBuffer();

        int count = 0;
        for (MoocOrderT moocOrderT : moocOrderTS) {
            count++;
            String seatsIds = moocOrderT.getSeatsIds();
            if (moocOrderTS.size() == count) {
                soldSeats.append(seatsIds);
                break;
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
