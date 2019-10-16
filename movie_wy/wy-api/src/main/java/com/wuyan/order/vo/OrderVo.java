package com.wuyan.order.vo;

import java.io.Serializable;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 订单Vo
 * @Date: 2019-10-16-10:49
 */
public class OrderVo implements Serializable {
    private String orderId;
    private String filmName;
    private String fieldTime;
    private String cinemaName;
    private String seatsName;
    private String orderPrice;
    private String orderTimestamp;
    private String orderStatus;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getFieldTime() {
        return fieldTime;
    }

    public void setFieldTime(String fieldTime) {
        this.fieldTime = fieldTime;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getSeatsName() {
        return seatsName;
    }

    public void setSeatsName(String seatsName) {
        this.seatsName = seatsName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(String orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "OrderVo{" +
                "orderId='" + orderId + '\'' +
                ", filmName='" + filmName + '\'' +
                ", fieldTime='" + fieldTime + '\'' +
                ", cinemaName='" + cinemaName + '\'' +
                ", seatsName='" + seatsName + '\'' +
                ", orderPrice='" + orderPrice + '\'' +
                ", orderTimestamp='" + orderTimestamp + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
