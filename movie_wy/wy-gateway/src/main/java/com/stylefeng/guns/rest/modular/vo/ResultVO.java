package com.stylefeng.guns.rest.modular.vo;

public class ResultVO extends ReBaseDataVo{
    private String imgPre;

    public String getImgPre() {
        return imgPre;
    }

    public void setImgPre(String imgPre) {
        this.imgPre = imgPre;
    }

    public static ResultVO ok(String imgPre,Object data){
        ResultVO resultVO = new ResultVO();
        resultVO.setStatus(0);
        resultVO.setData(data);
        resultVO.setImgPre(imgPre);
        return resultVO;
    }
}
