package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.modular.auth.util.Json2BeanUtils;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.auth.util.Md5Utils;
import com.stylefeng.guns.rest.modular.vo.ReBaseDataVo;
import com.stylefeng.guns.rest.modular.vo.ReBaseMsgVo;
import com.stylefeng.guns.rest.modular.vo.ReBaseVo;
import com.wuyan.user.UserService;
import com.wuyan.user.bean.UserInfoModel;
import com.wuyan.user.bean.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

/**
 * @Program: guns-parent
 * @Author: ZyEthan
 * @Description:
 * @Date: 2019-10-12-20:29
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private Jedis jedis;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Reference(interfaceClass = UserService.class,check = false)
    UserService userService;

    /**
     * 用户登录
     * @param userModel
     * @return
     */
    @RequestMapping("/register")
    public ReBaseVo register(UserModel userModel) {
        boolean register = false;
        String md5 = Md5Utils.getMd5(userModel.getPassword());
        userModel.setPassword(md5);
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
            ReBaseMsgVo serviceEx = ReBaseMsgVo.serviceEx();
            if (username == null || "".equals(username)) {
                serviceEx.setMsg("用户名不能为空");
                return serviceEx;
            }
            checkUserName = userService.checkUserName(username);
            if (checkUserName) {
                ReBaseMsgVo ok = ReBaseMsgVo.ok();
                ok.setMsg("用户名不存在");
                return ok;
            }
            serviceEx.setMsg("用户名已注册");
            return serviceEx;
        } catch (Exception e) {
            return ReBaseMsgVo.systemEx();
        }
    }

    /**
     * 得到用户个人信息
     * @param request
     * @return
     */
    @RequestMapping("/getUserInfo")
    public ReBaseVo getUserInfo(HttpServletRequest request) {
        // 先得到token信息
        String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken;
        try {
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                authToken = requestHeader.substring(7);
                String userInfoFromToken = jwtTokenUtil.getUsernameFromToken(authToken);
                UserInfoModel userInfo = new UserInfoModel();
                userInfo = (UserInfoModel) Json2BeanUtils.jsonToObj(userInfo, userInfoFromToken);
                UserInfoModel userInfoModel = userService.getUserInfo(userInfo.getUsername());
                ReBaseDataVo ok = ReBaseDataVo.ok(userInfoModel);
                return ok;
            }
            return ReBaseMsgVo.systemEx();
        } catch (Exception e) {
            return ReBaseMsgVo.systemEx();
        }
    }

    /**
     * 修改更新用户个人信息
     * @param userInfoModel
     * @return
     */
    @RequestMapping("/updateUserInfo")
    public ReBaseVo updateUserInfo(UserInfoModel userInfoModel) {
        UserInfoModel reUserInfo = userService.updateUserInfo(userInfoModel);
        ReBaseDataVo ok = ReBaseDataVo.ok(reUserInfo);
        return ok;
    }

    @RequestMapping("/logout")
    public ReBaseVo logout(HttpServletRequest request) {
        // token 放在redis中,直接在redis中删除
        String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken;
        try {
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                authToken = requestHeader.substring(7);
                // 在redis中删除该token
                jedis.del(authToken);
                ReBaseMsgVo ok = ReBaseMsgVo.ok();
                ok.setMsg("注销成功！");
                return ok;
            }
            return ReBaseMsgVo.systemEx();
        } catch (Exception e) {
            return ReBaseMsgVo.systemEx();
        }
    }
}
