package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.UserMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
import com.stylefeng.guns.rest.common.persistence.model.User;
import com.wuyan.user.UserService;
import com.wuyan.user.bean.UserInfoModel;
import com.wuyan.user.bean.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    public boolean register(UserModel userModel) {
        int i = mtimeUserTMapper.selectByUsername(userModel.getUsername());
        if (i != 0) {
            return false;
        }
        MtimeUserT mtimeUserT = new MtimeUserT();
        mtimeUserT.setUserName(userModel.getUsername());
        mtimeUserT.setUserPwd(userModel.getPassword());
        mtimeUserT.setEmail(userModel.getEmail());
        mtimeUserT.setBeginTime(new Date());
        mtimeUserT.setAddress(userModel.getAddress());
        Integer insert = mtimeUserTMapper.insert(mtimeUserT);
        return true;
    }

    @Override
    public boolean checkUserName(String username) {
        int i = mtimeUserTMapper.selectByUsername(username);
        if (i == 0) {
            return true;
        }
        return false;
    }

    @Override
    public Integer login(String username, String password) {
        int i = mtimeUserTMapper.selectByUsernamePwd(username,password);
        return i;
    }

    @Override
    public int logout(Integer id) {
        return 0;
    }

    @Override
    public UserInfoModel getUserInfo(String username) {
        MtimeUserT mtimeUserT = mtimeUserTMapper.selectUserInfoByUsername(username);
//        EntityWrapper<MtimeUserT> entityWrapper = new EntityWrapper<>();
//        entityWrapper.eq("user_name",username);
//        List<MtimeUserT> mtimeUserTS = mtimeUserTMapper.selectList(entityWrapper);
//        if (CollectionUtils.isEmpty(mtimeUserTS)){
//            return null;
//        }
//        MtimeUserT mtimeUserT = mtimeUserTS.get(0);
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUuid(mtimeUserT.getUuid());
        userInfoModel.setUsername(mtimeUserT.getUserName());
        userInfoModel.setNickname(mtimeUserT.getNickName());
        userInfoModel.setEmail(mtimeUserT.getEmail());
        userInfoModel.setPhone(mtimeUserT.getUserPhone());
        userInfoModel.setSex(mtimeUserT.getUserSex());
        userInfoModel.setBirthday(mtimeUserT.getBirthday());
        userInfoModel.setBiography(mtimeUserT.getBiography());
        userInfoModel.setAddress(mtimeUserT.getAddress());
        userInfoModel.setLifeState(mtimeUserT.getLifeState());
        userInfoModel.setHeadAddress(mtimeUserT.getHeadUrl());
        userInfoModel.setBeginTime(mtimeUserT.getBeginTime());
        userInfoModel.setUpdateTime(mtimeUserT.getUpdateTime());
        return userInfoModel;
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        MtimeUserT mtimeUserT = new MtimeUserT();
        mtimeUserT.setUuid(userInfoModel.getUuid());
        mtimeUserT.setUserName(userInfoModel.getUsername());
        mtimeUserT.setNickName(userInfoModel.getNickname());
        mtimeUserT.setEmail(userInfoModel.getEmail());
        mtimeUserT.setUserPhone(userInfoModel.getPhone());
        mtimeUserT.setUserSex(userInfoModel.getSex());
        mtimeUserT.setBirthday(userInfoModel.getBirthday());
        mtimeUserT.setBiography(userInfoModel.getBiography());
        mtimeUserT.setLifeState(userInfoModel.getLifeState());
        mtimeUserT.setAddress(userInfoModel.getAddress());
        mtimeUserT.setHeadUrl(userInfoModel.getHeadAddress());
        mtimeUserT.setBeginTime(userInfoModel.getBeginTime());
        mtimeUserT.setUpdateTime(userInfoModel.getUpdateTime());
        mtimeUserTMapper.updateById(mtimeUserT);
        userInfoModel.setUpdateTime(new Date());
        return userInfoModel;
    }
}
