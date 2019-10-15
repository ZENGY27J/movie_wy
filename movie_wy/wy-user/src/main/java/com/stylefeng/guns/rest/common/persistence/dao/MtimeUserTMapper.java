package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wuyan.user.bean.UserInfoModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-10-12
 */
public interface MtimeUserTMapper extends BaseMapper<MtimeUserT> {

    @Select("select count(UUID) from mtime_user_t where user_name = #{username}")
    int selectByUsername(@Param("username") String username);

    @Select("select count(UUID) from mtime_user_t where user_name = #{username} and user_pwd = #{password}")
    int selectByUsernamePwd(@Param("username") String username, @Param("password") String password);

    MtimeUserT selectUserInfoByUsername(@Param("username") String username);
}
