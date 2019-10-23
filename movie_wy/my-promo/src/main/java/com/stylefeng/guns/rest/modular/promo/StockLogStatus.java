package com.stylefeng.guns.rest.modular.promo;


/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 库存状态
 * @Date: 2019-10-21-20:37
 */
public enum StockLogStatus {

    /**
     * 反应状态码
     */
    FAIL("失败",3),SUCCESS("成功",2),INIT("初始化",1);

    private String name;
    private int index;

    StockLogStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }}



