package com.stylefeng.guns.rest.common.persistence.model;

import java.io.Serializable;
import java.util.List;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: json文件座位信息
 * @Date: 2019-10-16-19:19
 */
public class MySeatsInfo implements Serializable {

    private int limit;
    private String ids;
    private List<List<Single>> single;
    public void setLimit(int limit) {
        this.limit = limit;
    }
    public int getLimit() {
        return limit;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
    public String getIds() {
        return ids;
    }

    public void setSingle(List<List<Single>> single) {
        this.single = single;
    }
    public List<List<Single>> getSingle() {
        return single;
    }
}
