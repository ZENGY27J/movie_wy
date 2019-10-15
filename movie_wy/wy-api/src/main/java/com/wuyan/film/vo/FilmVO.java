package com.wuyan.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilmVO implements Serializable {

    private static final long serialVersionUID = -5950531743779688031L;

    private Integer filmNum;

    private Integer nowPage;

    private Integer totalPage;

    private List<FilmInfoVO> filmInfo;

}
