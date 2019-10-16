package com.stylefeng.guns.rest.common.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MtimeFieldTMapper extends BaseMapper<MtimeFieldT> {

    List<MtimeFieldT> selectByCinemaId(@Param("cinemaId") Integer cinemaId);
}
