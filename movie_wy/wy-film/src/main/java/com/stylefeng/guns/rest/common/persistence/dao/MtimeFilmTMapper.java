package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wuyan.film.vo.FilmInfoVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.List;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 * @author lanzhao
 * @since 2019-10-12
 */
public interface MtimeFilmTMapper extends BaseMapper<MtimeFilmT> {

//    List<FilmInfoVO> getFilms(@Param("filmType") String filmType);

    @Select("select * from mtime_film_t where UUID = #{id}")
    MtimeFilmT getFilmById(@Param("id") Integer id);

    @Select("select * from mtime_film_t where film_name = #{keyword}")
    MtimeFilmT getFilmByKeyword(@Param("keyword") String keyword);

    @Select("select * from mtime_film_t where film_type = #{showType}")
    List<MtimeFilmT> getFilmByShowType(@Param("showType") Integer showType);

}
