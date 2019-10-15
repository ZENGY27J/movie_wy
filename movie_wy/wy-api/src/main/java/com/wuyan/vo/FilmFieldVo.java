package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class FilmFieldVo implements Serializable {
    private Integer fieldId;
    private String beginTime;
    private String endTime;
    private String language;
    private String hallName;
    private Integer price;
}
