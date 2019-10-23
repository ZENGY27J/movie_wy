package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 秒杀下单参数
 * @Date: 2019-10-18-16:09
 */
@Data
public class PromoOrderPram implements Serializable {

    // 秒杀活动ID
    Integer promoId;
    // 购买数量
    Integer amount;
    // 秒杀令牌
    String promoToken;
}
