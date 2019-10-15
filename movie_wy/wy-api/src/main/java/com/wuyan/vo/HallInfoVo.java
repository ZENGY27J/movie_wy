package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HallInfoVo implements Serializable {
    String discountPrice;
    Integer hallFieldId;
    String hallName;
    Integer price;
    String seatFile;
    String soldSeats;
}
