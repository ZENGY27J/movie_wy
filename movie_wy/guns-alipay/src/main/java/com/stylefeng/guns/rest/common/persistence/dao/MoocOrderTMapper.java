package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author song
 * @since 2019-10-16
 */
public interface MoocOrderTMapper extends BaseMapper<MoocOrderT> {

//    MoocOrderT queryOrderById(@Param("orderId") String orderId);
}
