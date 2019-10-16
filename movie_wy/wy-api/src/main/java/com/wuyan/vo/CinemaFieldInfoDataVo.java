package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CinemaFieldInfoDataVo implements Serializable {
    CinemaInfoVo cinemaInfo;

    FilmInfoVo filmInfo;

    HallInfoVo hallInfo;
}
