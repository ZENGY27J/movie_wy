package com.wuyan.cinemaService;

import com.wuyan.vo.*;

import java.util.List;

public interface CinemaService {
    CinemaListVo cinemaList(CinemaQueryVo cinemaQueryVo);
    CinemaCoditionVo cinemaCodition(CinemaConditionQueryVo cinemaConditionQueryVo);
}
