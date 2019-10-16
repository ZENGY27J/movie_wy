package com.wuyan.order.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description:
 * @Date: 2019-10-16-10:53
 */
public class Page<T> implements Serializable {

    // 当前页
    Integer nowPage;

    // 页面大小
    Integer pageSize;

    List<T> list;

    public Integer getNowPage() {
        return nowPage;
    }

    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Page{" +
                "nowPage=" + nowPage +
                ", pageSize=" + pageSize +
                ", list=" + list +
                '}';
    }
}
