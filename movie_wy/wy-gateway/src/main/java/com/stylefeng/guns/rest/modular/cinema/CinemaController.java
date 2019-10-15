package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wuyan.cinema.CinemaService;
import com.wuyan.vo.CinemaFieldInfo;
import com.wuyan.vo.CinemaGetFieldsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 影院页面的显示
 * @Author: fangbo
 * @Date: 2019/10/13
 */
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

}
