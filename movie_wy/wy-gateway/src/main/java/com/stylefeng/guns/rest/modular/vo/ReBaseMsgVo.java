package com.stylefeng.guns.rest.modular.vo;

/**
 * @Program: guns-parent
 * @Author: ZyEthan
 * @Description: 有异常的返回
 * @Date: 2019-10-13-11:13
 */
public class ReBaseMsgVo extends ReBaseVo{

    String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ReBaseMsgVo ok() {
        ReBaseMsgVo reBaseMsgVo = new ReBaseMsgVo();
        reBaseMsgVo.setStatus(0);
        return reBaseMsgVo;
    }

    public static ReBaseMsgVo serviceEx() {
        ReBaseMsgVo reBaseMsgVo = new ReBaseMsgVo();
        reBaseMsgVo.setStatus(1);
        return reBaseMsgVo;
    }

    public static ReBaseMsgVo systemEx() {
        ReBaseMsgVo reBaseMsgVo = new ReBaseMsgVo();
        reBaseMsgVo.setStatus(999);
        reBaseMsgVo.setMsg("系统出现异常，请联系管理员");
        return reBaseMsgVo;
    }
}
