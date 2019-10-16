package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrandVo implements Serializable {
    private Integer brandId;
    private String brandName;
    private boolean isActive;

}
