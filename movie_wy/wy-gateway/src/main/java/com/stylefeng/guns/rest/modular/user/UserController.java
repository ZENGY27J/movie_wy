package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.modular.vo.ReBaseMsgVo;
import com.stylefeng.guns.rest.modular.vo.ReBaseVo;
import com.wuyan.user.UserService;
import com.wuyan.user.bean.UserModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Program: guns-parent
 * @Author: ZyEthan
 * @Description:
 * @Date: 2019-10-12-20:29
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Reference(interfaceClass = UserService.class)
    UserService userService;

    @RequestMapping("getUserName")
    public String getUserById(Integer id) {
        String name = userService.getNameById(id);
        return name;
    }

    /**
     * 用户登录
     * @param userModel
     * @return
     */
    @RequestMapping("/register")
    public ReBaseVo register(UserModel userModel) {
        boolean register = false;
        try {
            register = userService.register(userModel);
            if (register) {
                ReBaseMsgVo ok = ReBaseMsgVo.ok();
                ok.setMsg("注册成功");
                return ok;
            }
            ReBaseMsgVo serviceEx = ReBaseMsgVo.serviceEx();
            serviceEx.setMsg("用户已存在");
            return serviceEx;
        } catch (Exception e) {
            return ReBaseMsgVo.systemEx();
        }
    }

    /**
     * 用户名验证
     * @param username
     * @return
     */
    @RequestMapping("/check")
    public ReBaseVo check(String username) {
        boolean checkUserName = false;
        try {
            checkUserName = userService.checkUserName(username);
            if (checkUserName) {
                ReBaseMsgVo ok = ReBaseMsgVo.ok();
                ok.setMsg("用户名不存在");
                return ok;
            }
            ReBaseMsgVo serviceEx = ReBaseMsgVo.serviceEx();
            serviceEx.setMsg("用户已注册");
            return serviceEx;
        } catch (Exception e) {
            return ReBaseMsgVo.systemEx();
        }
    }
}
