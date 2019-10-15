package com.stylefeng.guns.rest.modular.vo;

/**
 * @Program: guns-parent
 * @Author: ZyEthan
 * @Description: 正常应答报文
 * @Date: 2019-10-13-11:16
 */
public class ReBaseDataVo<T> extends ReBaseVo{

    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ReBaseDataVo ok(Object data) {
        ReBaseDataVo reBaseDataVo = new ReBaseDataVo();
        reBaseDataVo.setStatus(0);
        reBaseDataVo.setData(data);
        return reBaseDataVo;
    }
}
