package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmActorT;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 影片与演员映射表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-10-14
 */
public interface MtimeFilmActorTMapper extends BaseMapper<MtimeFilmActorT> {

    MtimeFilmActorT getActorByActorId(Integer actorId,Integer filmId);
}
