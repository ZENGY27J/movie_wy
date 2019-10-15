package com.wuyan.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class FilmInfoVO implements Serializable {
    private static final long serialVersionUID = 2675186264904988623L;

    private String filmId;

    private Integer filmType;

    private String imgAddress;

    private String filmName;

    private String filmScore;

    private Integer expectNum;

    private Date showTime;

    private Integer boxNum;

    private String score;

}
