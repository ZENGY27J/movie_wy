package com.wuyan.film.vo;

import java.io.Serializable;

public class BaseRespVO<T> implements Serializable {
    private T data;
    private String imgPre;
    private String msg;
    private Integer nowPage;
    private int status;
    private Long totalPage;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getImgPre() {
        return imgPre;
    }

    public void setImgPre(String imgPre) {
        this.imgPre = imgPre;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getNowPage() {
        return nowPage;
    }

    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public static BaseRespVO ok(Object data){
        BaseRespVO baseRespVO = new BaseRespVO();
        baseRespVO.setData(data);
        baseRespVO.setImgPre("http://img.meetingshop.cn/");
        baseRespVO.setMsg("");
        baseRespVO.setStatus(0);
        return baseRespVO;
    }

    public static BaseRespVO serviceErr(){
        BaseRespVO baseRespVO = new BaseRespVO();
        baseRespVO.setStatus(1);
        baseRespVO.setMsg("查询失败，五影片可加载");
        return baseRespVO;
    }

    public static BaseRespVO systemErr(){
        BaseRespVO baseRespVO = new BaseRespVO();
        baseRespVO.setStatus(999);
        baseRespVO.setMsg("系统出现异常，请联系管理员");
        return baseRespVO;
    }
}
