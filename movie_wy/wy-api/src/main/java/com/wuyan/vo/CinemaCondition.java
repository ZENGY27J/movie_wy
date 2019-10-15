package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CinemaCondition implements Serializable {
    List<AreaVo> areaList;
    List<BrandVo> brandList;
    List<HallTypeVo> hallTypeList;
}
