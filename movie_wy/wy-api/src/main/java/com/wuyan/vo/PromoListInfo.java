package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 返回秒杀活动影院影片信息
 * @Date: 2019-10-18-15:49
 */
@Data
public class PromoListInfo implements Serializable {
    String  cinemaAddress;
    Integer  cinemaId;
    String  cinemaName;
    // 详情描述
    String  description;
    Date endTime;
    String  imgAddress;
    BigDecimal price;
    Date  startTime;

    Integer  status;
    // 库存
    Integer  stock;

    Integer  uuid;
}
