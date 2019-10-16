package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
<<<<<<< HEAD
import com.wuyan.film.vo.FilmInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


=======

import java.util.List;

>>>>>>> 702e1656fffbe6fee26e4ae0bc3d2f75bed44f80
/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
<<<<<<< HEAD
 * @author lanzhao
 * @since 2019-10-12
 */
public interface MtimeFilmTMapper extends BaseMapper<MtimeFilmT> {

    List<FilmInfoVO> getFilms(@Param("filmType") String filmType);
=======
 * @author stylefeng
 * @since 2019-10-14
 */
public interface MtimeFilmTMapper extends BaseMapper<MtimeFilmT> {

    MtimeFilmT getFilmById(Integer id);

    MtimeFilmT getFilmByKeyword(String keyword);

    List<MtimeFilmT> getFilmByShowType(Integer showType);
>>>>>>> 702e1656fffbe6fee26e4ae0bc3d2f75bed44f80

}
