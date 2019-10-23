package com.stylefeng.guns.rest.common.persistence.model.Vo;

public class ResultVo {
    private int status;

    private String msg;

    private Object data;

    private String imgPre;

    public String getImgPre() {
        return imgPre;
    }

    public void setImgPre(String imgPre) {
        this.imgPre = imgPre;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultVo ok(Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setMsg("成功");
        resultVo.setData(data);
        resultVo.setStatus(0);
        return resultVo;
    }
    public static ResultVo fail(Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setMsg("失败");
        resultVo.setData(data);
        resultVo.setStatus(1);
        return resultVo;
    }
    public static ResultVo error(Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setStatus(999);
        resultVo.setMsg("系统出现异常，请联系管理员");
        return resultVo;
    }
}
