package com.stylefeng.guns.rest.common.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeSourceDictT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeSourceDictT;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 区域信息表 Mapper 接口
 * </p>
 *
 * @author wei
 * @since 2019-10-14
 */
public interface MtimeSourceDictTMapper extends BaseMapper<MtimeSourceDictT> {

    MtimeSourceDictT getAreaById(Integer id);
}
