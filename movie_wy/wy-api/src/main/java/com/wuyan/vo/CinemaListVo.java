package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CinemaListVo implements Serializable {
    private List<CinemaVo> data;
    private String imgPre;
    private String msg;
    private Integer nowPage;
    private Integer status;
    private Integer totalPage;


}
