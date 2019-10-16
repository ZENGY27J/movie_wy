package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeActorT;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 演员表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-10-14
 */
public interface MtimeActorTMapper extends BaseMapper<MtimeActorT> {

    MtimeActorT getActorByName(String name);

    MtimeActorT getActorById(Integer id);
}
