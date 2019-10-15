package com.wuyan.film.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class FilmRankingVO implements Serializable {

    private static final long serialVersionUID = -1129206261388789515L;

    private Integer filmId;
    private String imgAddress;
    private String filmName;

    private Integer boxNum;
    private Integer expectNum;
    private String score;

}
