package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: getFields 请求的返回报文的封装对象
 * @Author: fangbo
 * @Date: 2019/10/14
 */
@Data
public class CinemaGetFieldsVo implements Serializable {
    CinemaGetFieldsDataVo data;

    String imgPre;

    Integer status;
}
