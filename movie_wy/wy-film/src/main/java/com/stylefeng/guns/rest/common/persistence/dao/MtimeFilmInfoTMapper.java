package com.stylefeng.guns.rest.common.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmInfoT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmInfoT;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author wei
 * @since 2019-10-14
 */
public interface MtimeFilmInfoTMapper extends BaseMapper<MtimeFilmInfoT> {

    MtimeFilmInfoT getFilmInfoById(Integer id);
}
