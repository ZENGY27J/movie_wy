package com.wuyan.promo;

import com.wuyan.vo.PromoListInfo;
import com.wuyan.vo.PromoOrderPram;
import com.wuyan.vo.PromoPramListVo;

import java.util.List;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 秒杀活动业务层接口
 * @Date: 2019-10-18-15:55
 */
public interface PromoService {

    // 获得所有的秒杀活动
    List<PromoListInfo> getPromoList(PromoPramListVo promoPramListVo);

    //秒杀下单接口
    boolean createOrder(Integer promoId, Integer userId, Integer amount,String stockLogId);

    //将库存信息发布到缓存
    boolean publishPromoStock(Integer cinemaId);

    //获取秒杀令牌接口
    String getGenerateToken(Integer userId,Integer promoId);

    /**
     * 事务处理订单
     * @param promoId
     * @param userId
     * @param amount
     * @param stockLogId
     * @return
     */
    Boolean transactionSaveOrder(Integer promoId, Integer userId, Integer amount,String stockLogId);


    String initPromoStockLog(Integer promoId, Integer amount);
}
