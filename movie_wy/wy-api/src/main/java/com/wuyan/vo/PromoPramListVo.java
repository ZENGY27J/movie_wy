package com.wuyan.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 得到秒杀列表的请求参数类
 * @Date: 2019-10-18-15:45
 */
@Data
public class PromoPramListVo implements Serializable {

    Integer brandId;
    // 影厅类型
    Integer hallType;
    // 区域Id
    Integer areaId;
    // 页面大小
    Integer pageSize;
    // 当前页
    Integer nowPage;

}
