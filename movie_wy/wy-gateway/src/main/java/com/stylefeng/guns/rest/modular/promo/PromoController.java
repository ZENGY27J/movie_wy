package com.stylefeng.guns.rest.modular.promo;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.util.concurrent.RateLimiter;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.modular.auth.util.Json2BeanUtils;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.vo.ReBaseDataVo;
import com.stylefeng.guns.rest.modular.vo.ReBaseMsgVo;
import com.stylefeng.guns.rest.modular.vo.ReBaseVo;
import com.wuyan.promo.PromoService;
import com.wuyan.user.bean.UserInfoModel;
import com.wuyan.vo.PromoListInfo;
import com.wuyan.vo.PromoPramListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 秒杀活动控制层接口
 * @Date: 2019-10-18-16:20
 */
@Slf4j
@RestController
@RequestMapping("promo")
public class PromoController {

    @Reference(interfaceClass = PromoService.class, check = false)
    PromoService promoService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    //redis缓存前缀
    private static final String PROMOVO_CACHE_KEY_IN_REDIS_PREFIX = "promovo_cache_key_prefix_cinemaid_";

    //发布库存到缓存接口前缀
    private static final String PROMOVO_STOCK_KEY_IN_REDIS_PREFIX = "promovo_stock_key_prefix_cinemaid_";

    //库存售罄标记前缀
    private static final String  PROMO_STOCK_CACHE_SELLOUT_PREFIX = "promo_stock_sellout_cache_prefix_";

    //本地缓存前缀
    private static final String PROMOVO_CACHE_KEY_IN_LOCAL_PREFIX = "promovo_local_cache_key_prefix_cinemaid_";

    //秒杀令牌关联key
    private static final String PROMOVO_TOKEN_KEY_PREFIX = "promo_token_key_prefix_";

    //redis缓存过期时间
    private static final Integer REDIS_CACHE_EXPIRE_TIME = 5;

    private ExecutorService executorService;

    private RateLimiter rateLimiter;

    @PostConstruct
    public void init(){
        executorService = Executors.newFixedThreadPool(200);

        rateLimiter = RateLimiter.create(10);

    }
    /**
     * 得到秒杀活动列表
     * @param promoPramListVo
     * @return
     */
    @RequestMapping("/getPromo")
    public ReBaseVo getPromList(PromoPramListVo promoPramListVo) {
        List<PromoListInfo> promoList = promoService.getPromoList(promoPramListVo);
        ReBaseDataVo ok = ReBaseDataVo.ok(promoList);
        return ok;
    }


    /**
     * 秒杀活动下单
     * @param
     * @return
     */
    @RequestMapping("/createOrder")
    public ReBaseVo createOrder(@RequestParam(required = true, name = "promoId") Integer promoId,
                                @RequestParam(required = true, name = "amount") Integer amount,
                                @RequestParam(required = true,name = "promoToken")String promoToken,
                                HttpServletRequest request, HttpServletResponse response) {
        final long startTime = System.currentTimeMillis();

        double rateAmont = rateLimiter.acquire();
        if (rateAmont < 0) {
            ReBaseMsgVo reBaseMsgVo = ReBaseMsgVo.serviceEx();
            reBaseMsgVo.setMsg("下单失败");
            return reBaseMsgVo;
        }

        String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken;
        try {
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                authToken = requestHeader.substring(7);
                String userInfoFromToken = jwtTokenUtil.getUsernameFromToken(authToken);
                UserInfoModel userInfo = new UserInfoModel();
                userInfo = (UserInfoModel) Json2BeanUtils.jsonToObj(userInfo, userInfoFromToken);
                Integer userId = userInfo.getUuid();

                // 检验秒杀令牌
                String promoTokenKey = "USER_PROMO_TOKEN_PREFIX" + promoId;
                Object promoTokenInredis = redisTemplate.opsForValue().get(promoTokenKey);
                if (promoTokenInredis == null) {
                    ReBaseMsgVo reBaseMsgVo = ReBaseMsgVo.serviceEx();
                    reBaseMsgVo.setMsg("令牌校验失败！");
                    return reBaseMsgVo;
                }
                String promoTokenRedis = (String) promoTokenInredis;
                if (!promoToken.equals(promoTokenRedis)) {
                    ReBaseMsgVo reBaseMsgVo = ReBaseMsgVo.serviceEx();
                    reBaseMsgVo.setMsg("令牌校验失败！");
                    return reBaseMsgVo;
                }

                Future future = executorService.submit(() -> {
                    //初始化库存流水 ----状态是初始化 返回UUID
                    Boolean result = false;
                    final String stockLogId = promoService.initPromoStockLog(promoId, amount);
                    if (org.apache.commons.lang3.StringUtils.isBlank(stockLogId)) {
                        log.info("初始化库存流水失败！promoId:{},amount:{}",promoId,amount);
//                return ResponseVO.fail("下单失败！");
                        throw  new GunsException(GunsExceptionEnum.STOCK_LOG_INIT_ERROR);
                    }

                    try {
                        result = promoService.transactionSaveOrder(promoId, Integer.valueOf(userId), amount,stockLogId);
                    } catch (Exception e) {
                        log.info("下单失败！promoId:{},userId:{},amount:{}",promoId,userId,amount);
//                return ResponseVO.fail("下单失败！");
                        throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
                    }
                    if (!result) {
                        throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
                    }
                });
                future.get();
                long endTime = System.currentTimeMillis();
                log.info("秒杀下单接口请求耗时 -> {} ms",endTime-startTime);

                ReBaseMsgVo ok = ReBaseMsgVo.ok();
                ok.setMsg("下单成功");
                return ok;
            }
            return ReBaseMsgVo.systemEx();
        } catch (Exception e) {
            return ReBaseMsgVo.systemEx();
        }
    }

    /**
     * 将库存信息发布到缓存
     * @param cinemaId
     * @return
     */
    @RequestMapping("/publishPromoStock")
    public ReBaseVo publishPromoStock(Integer cinemaId) {
        // 该接口只调用一次,检查该接口是否被访问
        String object = (String) redisTemplate.opsForValue().get(PROMOVO_CACHE_KEY_IN_REDIS_PREFIX);
        ReBaseMsgVo ok = ReBaseMsgVo.ok();
        if (StringUtils.isBlank(object)) {
            boolean b = promoService.publishPromoStock(cinemaId);
            if (b) {
                log.info("发布库存到缓存中成功~");
                redisTemplate.opsForValue().set(PROMOVO_CACHE_KEY_IN_REDIS_PREFIX,"success");
                redisTemplate.expire(PROMOVO_CACHE_KEY_IN_REDIS_PREFIX,6, TimeUnit.HOURS);

                ok.setMsg("发布成功");
                return ok;
            } else {
                ReBaseMsgVo reBaseMsgVo = ReBaseMsgVo.serviceEx();
                reBaseMsgVo.setMsg("发布失败");
                return  reBaseMsgVo;
            }
        } else {
            ok.setMsg("已经操作过了");
            return ok;
        }
    }

    @RequestMapping("/generateToken")
    public ReBaseVo getGenerateToken(Integer promoId, HttpServletRequest request) {
        String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken;
        Integer userId = 0;
        try {
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                authToken = requestHeader.substring(7);
                String userInfoFromToken = jwtTokenUtil.getUsernameFromToken(authToken);
                UserInfoModel userInfo = new UserInfoModel();
                userInfo = (UserInfoModel) Json2BeanUtils.jsonToObj(userInfo, userInfoFromToken);
                userId = userInfo.getUuid();
            }
        } catch (Exception e) {
            return ReBaseMsgVo.systemEx();
        }

        // 判断令牌是否售罄
        String stockKey = "PROMO_STOCK_NULL_PROMOID" + promoId;
        Object object = redisTemplate.opsForValue().get(stockKey);
        if (object != null) {
            ReBaseMsgVo reBaseMsgVo = ReBaseMsgVo.serviceEx();
            reBaseMsgVo.setMsg("令牌校验失败！");
            return reBaseMsgVo;
        }

        // 生成秒杀令牌
        String token = promoService.getGenerateToken(userId,promoId);
        if (StringUtils.isBlank(token)) {
            ReBaseMsgVo reBaseMsgVo = ReBaseMsgVo.serviceEx();
            reBaseMsgVo.setMsg("获取失败");
        }
        ReBaseMsgVo ok = ReBaseMsgVo.ok();
        ok.setMsg(token);
        return ok;
    }
}
