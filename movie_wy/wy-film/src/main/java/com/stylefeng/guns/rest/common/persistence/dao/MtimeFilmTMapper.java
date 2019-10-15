package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wuyan.film.vo.FilmInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author lanzhao
 * @since 2019-10-12
 */
public interface MtimeFilmTMapper extends BaseMapper<MtimeFilmT> {

    List<FilmInfoVO> getFilms(@Param("filmType") String filmType);

}
