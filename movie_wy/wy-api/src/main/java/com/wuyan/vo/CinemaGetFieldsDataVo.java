package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: getFields 接口返回报文中的 data 对象
 * @Author: fangbo
 * @Date: 2019/10/14
 */
@Data
public class CinemaGetFieldsDataVo implements Serializable {
    CinemaInfoVo cinemaInfo;

    List<FilmInfoVo> filmList;
}
