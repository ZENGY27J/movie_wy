package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wuyan.user.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Program: guns-parent
 * @Author: ZyEthan
 * @Description:
 * @Date: 2019-10-12-20:29
 */
@RestController
public class UserController {

    @Reference(interfaceClass = UserService.class)
    UserService userService;

    @RequestMapping("getUserName")
    public String getUserById(Integer id) {
        String name = userService.getNameById(id);
        return name;
    }

}
