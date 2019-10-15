package com.wuyan.film.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class CatVO implements Serializable {
    private static final long serialVersionUID = 1937216291763071123L;

    private Integer catId;
    private String catName;
    private Boolean active;

}
