package com.wuyan.film.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class SourceVO implements Serializable {

    private static final long serialVersionUID = 4902345575906572488L;

    private Integer sourceId;

    private String sourceName;

    private Boolean active;

}
