package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wuyan.cinema.CinemaService;
import com.wuyan.vo.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cinema")
public class CinemaController {

    @Reference(interfaceClass = CinemaService.class)
    CinemaService cinemaService;

    @RequestMapping("getFields")
    public CinemaGetFieldsVo getFields(String cinemaId) {
        CinemaGetFieldsVo cinemaGetFieldsVo = cinemaService.getFields(Integer.parseInt(cinemaId));
        return cinemaGetFieldsVo;
    }

    @RequestMapping("getFieldInfo")
    public CinemaFieldInfo getFieldInfo(String cinemaId, String fieldId) {
        CinemaFieldInfo cinemaFieldInfo = cinemaService.getFieldInfo(Integer.parseInt(cinemaId), Integer.parseInt(fieldId));

        return cinemaFieldInfo;
    }

    @RequestMapping("getCondition")
    public CinemaCoditionVo getCondition(CinemaConditionQueryVo cinemaConditionQueryVo){
        CinemaCoditionVo cinemaCoditionVo = cinemaService.cinemaCodition(cinemaConditionQueryVo);
        return cinemaCoditionVo;
    }

    @RequestMapping("getCinemas")
    public CinemaListVo getCinemas(CinemaQueryVo cinemaQueryVo){
        CinemaListVo cinemaListVo = cinemaService.cinemaList(cinemaQueryVo);
        return cinemaListVo;
    }


}
