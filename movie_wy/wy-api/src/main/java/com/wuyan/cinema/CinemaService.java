package com.wuyan.cinema;

import com.wuyan.vo.CinemaFieldInfo;
import com.wuyan.vo.CinemaGetFieldsVo;

/**
 * 影院模块的服务接口
 */
public interface CinemaService {
    /**
     * @Description: 获取播放场次的接口
     * @Author: fangbo
     * @Date: 2019/10/14
     */
    CinemaGetFieldsVo getFields(Integer cinemaId);

    CinemaFieldInfo getFieldInfo(Integer cinemaId, Integer fieldId);
}
