package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-10-14
 */
public interface MtimeFilmTMapper extends BaseMapper<MtimeFilmT> {

    MtimeFilmT getFilmById(Integer id);

    MtimeFilmT getFilmByKeyword(String keyword);

    List<MtimeFilmT> getFilmByShowType(Integer showType);

}
