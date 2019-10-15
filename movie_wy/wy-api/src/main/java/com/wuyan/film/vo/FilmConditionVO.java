package com.wuyan.film.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class FilmConditionVO implements Serializable {
    private static final long serialVersionUID = -6049390873021210357L;
    Integer catId;
    Integer sourceId;
    Integer yearId;

}
