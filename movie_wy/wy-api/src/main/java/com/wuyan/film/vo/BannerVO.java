package com.wuyan.film.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class BannerVO implements Serializable {
    private static final long serialVersionUID = 3053230322732248878L;
    private Integer bannerId;

    private String bannerAddress;

    private String bannerUrl;

}
