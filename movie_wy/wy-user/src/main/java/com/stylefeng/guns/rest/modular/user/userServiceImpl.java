package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.UserMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
import com.stylefeng.guns.rest.common.persistence.model.User;
import com.wuyan.user.UserService;
import com.wuyan.user.bean.UserInfoModel;
import com.wuyan.user.bean.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Program: guns-parent
 * @Author: ZyEthan
 * @Description:
 * @Date: 2019-10-12-20:22
 */
@Component
@Service(interfaceClass = UserService.class)
public class userServiceImpl implements UserService {

    @Autowired
    MtimeUserTMapper mtimeUserTMapper;

    @Override
    public String getNameById(Integer id) {
        MtimeUserT mtimeUserT = mtimeUserTMapper.selectById(id);
        String userName = mtimeUserT.getUserName();
        return userName;
    }

    @Override
    public boolean register(UserModel userModel) {
        return false;
    }

    @Override
    public boolean checkUserName(String username) {
        return false;
    }

    @Override
    public Integer login(String username, String password) {
        return null;
    }

    @Override
    public int logout(Integer id) {
        return 0;
    }

    @Override
    public UserInfoModel queryUserInfo(Integer id) {
        return null;
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        return null;
    }
}
