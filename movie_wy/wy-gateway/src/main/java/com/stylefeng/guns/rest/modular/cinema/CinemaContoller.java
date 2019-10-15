package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wuyan.cinemaService.CinemaService;
import com.wuyan.vo.CinemaCoditionVo;
import com.wuyan.vo.CinemaConditionQueryVo;
import com.wuyan.vo.CinemaListVo;
import com.wuyan.vo.CinemaQueryVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CinemaContoller {

    @Reference(interfaceClass = CinemaService.class)
    CinemaService cinemaService;

    @RequestMapping("/cinema/getCondition")
    public CinemaCoditionVo getCondition(CinemaConditionQueryVo cinemaConditionQueryVo){
        CinemaCoditionVo cinemaCoditionVo = cinemaService.cinemaCodition(cinemaConditionQueryVo);
        return cinemaCoditionVo;
    }

    @RequestMapping("/cinema/getCinemas")
    public CinemaListVo getCinemas(CinemaQueryVo cinemaQueryVo){
        CinemaListVo cinemaListVo = cinemaService.cinemaList(cinemaQueryVo);
        return cinemaListVo;
    }


}
