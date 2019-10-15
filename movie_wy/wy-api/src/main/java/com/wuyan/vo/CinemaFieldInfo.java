package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class CinemaFieldInfo implements Serializable {
    CinemaFieldInfoDataVo data;

    String imgPre;

    String msg;

    String nowPage;

    Integer status;

    Integer totalPage;
}
