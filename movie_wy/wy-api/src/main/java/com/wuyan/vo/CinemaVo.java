package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CinemaVo implements Serializable {
    private Integer uuid;
    private String cinemaName;
    private String cinemaAddress;
    private Integer minimumPrice;


}
