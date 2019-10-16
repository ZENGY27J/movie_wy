package com.wuyan.order.vo;

import java.io.Serializable;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 订单座位信息
 * @Date: 2019-10-16-16:04
 */
public class SeatsInfo implements Serializable {
    // 场次id
    String fieldId;
    // 要下单的座位号
    String  soldSeats;
    // 座位编号
    String seatsName;

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getSoldSeats() {
        return soldSeats;
    }

    public void setSoldSeats(String soldSeats) {
        this.soldSeats = soldSeats;
    }

    public String getSeatsName() {
        return seatsName;
    }

    public void setSeatsName(String seatsName) {
        this.seatsName = seatsName;
    }

    @Override
    public String toString() {
        return "SeatsInfo{" +
                "fieldId=" + fieldId +
                ", soldSeats=" + soldSeats +
                ", seatsName='" + seatsName + '\'' +
                '}';
    }
}
