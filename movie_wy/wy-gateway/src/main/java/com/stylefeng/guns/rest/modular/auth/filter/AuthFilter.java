package com.stylefeng.guns.rest.modular.auth.filter;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.core.util.RenderUtil;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.modular.auth.util.Json2BeanUtils;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.wuyan.user.bean.UserInfoModel;
import io.jsonwebtoken.JwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 对客户端请求的jwt token验证过滤器
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:04
 */
public class AuthFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        if (request.getServletPath().equals("/" + jwtProperties.getAuthPath())) {
//            chain.doFilter(request, response);
//            return;
//        }
        String[] split = jwtProperties.getIgnorelUrl().split(",");
        String servletPath = request.getServletPath();
        String substring = "";
        if (servletPath.length() > 12 ) {
            substring = servletPath.substring(0, 11);
        }
        for (String s : split) {
            if (request.getServletPath().equals(s) || substring.equals(s)) {
            chain.doFilter(request, response);
            return;
            }
        }
        String header = jwtProperties.getHeader();
        final String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);

            String userInfoFromToken = jwtTokenUtil.getUsernameFromToken(authToken);
            UserInfoModel userInfo = new UserInfoModel();
            userInfo = (UserInfoModel) Json2BeanUtils.jsonToObj(userInfo, userInfoFromToken);
            String username = userInfo.getUsername();
            String key = "token_key_prefix_"+username;
            //验证token是否过期,包含了验证jwt是否正确
            try {
                String token = (String) redisTemplate.opsForValue().get(key);
                if (StringUtils.isBlank(token)) {
                    RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_EXPIRED.getCode(), BizExceptionEnum.TOKEN_EXPIRED.getMessage()));
                    return;
                } else {
                    // 刷新用户token缓存时间
                    redisTemplate.expire(key,3, TimeUnit.HOURS);
                }
            } catch (JwtException e) {
                //有异常就是token解析失败
                RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage()));
                return;
            }
        } else {
            //header没有带Bearer字段
            RenderUtil.renderJson(response, new ErrorTip(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage()));
            return;
        }
        chain.doFilter(request, response);
    }
}
