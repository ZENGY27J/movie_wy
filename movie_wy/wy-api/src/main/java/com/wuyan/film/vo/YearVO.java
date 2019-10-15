package com.wuyan.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class YearVO implements Serializable {

    private static final long serialVersionUID = 7778929295919371753L;
    private Integer yearId;
    private String yearName;
    private Boolean active;

}
