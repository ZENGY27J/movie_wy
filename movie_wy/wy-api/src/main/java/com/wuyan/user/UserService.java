package com.wuyan.user;

import com.wuyan.user.bean.UserInfoModel;
import com.wuyan.user.bean.UserModel;

public interface UserService {

    String getNameById(Integer id);

    /**
     * 用户注册接口
     * @param userModel
     * @return
     */
    boolean register(UserModel userModel);

    /**
     * 用户名验证接口
     * @param username
     * @return
     */
    boolean checkUserName(String username);

    /**
     * 用户登录接口
     * @param username
     * @param password
     * @return 返回id表示登录成功，返回null表示登录失败
     */
    Integer login(String username, String password);

    /**
     * 用户退出接口
     * @param id
     * @return
     */
    int logout(Integer id);

    /**
     * 用户信息查询接口
     * @param id
     * @return
     */
    UserInfoModel queryUserInfo(Integer id);

    /**
     * 修改用户信息的接口
     * @param userInfoModel
     * @return
     */
    UserInfoModel updateUserInfo(UserInfoModel userInfoModel);
}
