package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CinemaCoditionVo implements Serializable {
    CinemaCondition data;
    String imgPro;
    String msg;
    String nowPage;
    Integer status;
    Integer totalPage;
}
