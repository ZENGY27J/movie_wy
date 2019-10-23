package com.stylefeng.guns.rest.modular.auth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthRequest;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthResponse;
import com.stylefeng.guns.rest.modular.auth.util.Json2BeanUtils;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.auth.util.Md5Utils;
import com.stylefeng.guns.rest.modular.auth.validator.IReqValidator;
import com.stylefeng.guns.rest.modular.vo.ReBaseDataVo;
import com.stylefeng.guns.rest.modular.vo.ReBaseVo;
import com.wuyan.user.UserService;
import com.wuyan.user.bean.UserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 请求验证的
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@RestController
public class AuthController {

    @Reference(interfaceClass = UserService.class)
    UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Resource(name = "simpleValidator")
    private IReqValidator reqValidator;

    @RequestMapping(value = "${jwt.auth-path}")
    public ReBaseVo createAuthenticationToken(AuthRequest authRequest) {

//        boolean validate = reqValidator.validate(authRequest);
        // 自己的逻辑
        String userName = authRequest.getUserName();
        String password = authRequest.getPassword();
        String md5 = Md5Utils.getMd5(password);
        Integer uid = userService.login(userName, md5);
        UserInfoModel userInfo = userService.getUserInfo(userName);
        String objToJson = null;
        try {
            objToJson = Json2BeanUtils.objToJson(userInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (uid == 1) {
            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(objToJson, randomKey);
            String key = "token_key_prefix_"+userName;
            redisTemplate.opsForValue().set(key, token);
            redisTemplate.expire(token, 3, TimeUnit.HOURS);
//            return ResponseEntity.ok(new AuthResponse(token, randomKey));
            ReBaseDataVo ok = ReBaseDataVo.ok(new AuthResponse(token, randomKey));
            return ok;

        } else {
            throw new GunsException(BizExceptionEnum.AUTH_REQUEST_ERROR);
        }
    }
}
