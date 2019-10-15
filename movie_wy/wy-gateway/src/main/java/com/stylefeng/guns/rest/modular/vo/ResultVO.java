package com.stylefeng.guns.rest.modular.vo;

public class ResultVO {
    private Integer status;
    private String imgPre;
    private Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImgPre() {
        return imgPre;
    }

    public void setImgPre(String imgPre) {
        this.imgPre = imgPre;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultVO ok(Object data){
        ResultVO resultVO = new ResultVO();
        resultVO.setData(data);
        resultVO.setStatus(0);
        resultVO.setImgPre("http://img.meetingshop.cn/");
        return resultVO;
    }
}
