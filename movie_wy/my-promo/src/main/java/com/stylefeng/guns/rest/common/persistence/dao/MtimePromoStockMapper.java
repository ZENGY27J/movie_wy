package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-10-18
 */
public interface MtimePromoStockMapper extends BaseMapper<MtimePromoStock> {

    Integer updateStockByPid(@Param("stockNum") Integer stockNum, @Param("promoId") Integer promoId);
}
