package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MtimeFieldTMapper extends BaseMapper<MtimeFieldT> {

    List<MtimeFieldT> selectByCinemaId(@Param("cinemaId") Integer cinemaId);
}
