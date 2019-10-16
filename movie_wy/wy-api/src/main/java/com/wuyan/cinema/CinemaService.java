package com.wuyan.cinema;

import com.wuyan.vo.*;

public interface CinemaService {
    //@Description: 获取播放场次的接口
    CinemaGetFieldsVo getFields(Integer cinemaId);

    CinemaFieldInfo getFieldInfo(Integer cinemaId, Integer fieldId);
    CinemaListVo cinemaList(CinemaQueryVo cinemaQueryVo);
    CinemaCoditionVo cinemaCodition(CinemaConditionQueryVo cinemaConditionQueryVo);
}
