package com.stylefeng.guns.rest.modular.promo;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.rest.modular.auth.util.RandomCharUtils;
import com.wuyan.promo.PromoService;
import com.wuyan.vo.PromoListInfo;
import com.wuyan.vo.PromoPramListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @Program: movie_wy
 * @Author: ZyEthan
 * @Description: 秒杀活动业务层实现类
 * @Date: 2019-10-18-16:18
 */
@Slf4j
@Component
@Service(interfaceClass = PromoService.class)
public class PromoServiceImpl implements PromoService {

    @Autowired
    MtimePromoMapper mtimePromoMapper;

    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;

    @Autowired
    MtimePromoStockMapper mtimePromoStockMapper;

    @Autowired
    MtimePromoOrderMapper mtimePromoOrderMapper;

    @Autowired
    MtimeStockLogMapper mtimeStockLogMapper;


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    Producer producer;

    //设置兑换码有效期为3个月
    private static final Integer EXCHANGE_CODE_VALID_MONTH = 90;

    @Override
    public List<PromoListInfo> getPromoList(PromoPramListVo promoPramListVo) {
//        Page<Object> objectPage = new Page<>();
//        if (promoPramListVo.getNowPage() != null && promoPramListVo.getPageSize() != null) {
//            objectPage.setCurrent(promoPramListVo.getNowPage());
//            objectPage.setSize(promoPramListVo.getPageSize());
//        }
//        EntityWrapper<MtimePromo> entityWrapper = new EntityWrapper<>();
//        List<MtimePromo> mtimePromos = mtimePromoMapper.selectPage(objectPage, entityWrapper);
        Page<Object> objectPage = new Page<>();
        objectPage.setCurrent(1);
        objectPage.setSize(20);
        EntityWrapper<MtimePromo> entityWrapper = new EntityWrapper<>();
        List<MtimePromo> mtimePromos = mtimePromoMapper.selectPage(objectPage, entityWrapper);
        if (CollectionUtils.isEmpty(mtimePromos)) {
            return null;
        }

        List<PromoListInfo> promoListInfoList = new LinkedList<>();
        for (MtimePromo mtimePromo : mtimePromos) {
            PromoListInfo promoListInfo = new PromoListInfo();
            // 查询 cinemaAddress，cinemaName，imgAddress
            // 查询 stock库存(应该读缓存)
            Integer cinemaId = mtimePromo.getCinemaId();
            MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(cinemaId);

            String s = redisTemplate.opsForValue().get(mtimePromo.getUuid() + "stock");
            Integer stockNum = 0;
            if (!StringUtils.isBlank(s)) {
                stockNum = Integer.valueOf(s);
            }

            promoListInfo.setCinemaAddress(mtimeCinemaT.getCinemaAddress());
            promoListInfo.setCinemaId(cinemaId);
            promoListInfo.setCinemaName(mtimeCinemaT.getCinemaName());
            promoListInfo.setImgAddress(mtimeCinemaT.getImgAddress());
            promoListInfo.setStock(stockNum);
            promoListInfo.setDescription(mtimePromo.getDescription());
            promoListInfo.setStartTime(mtimePromo.getStartTime());
            promoListInfo.setEndTime(mtimePromo.getEndTime());
            promoListInfo.setUuid(mtimePromo.getUuid());
            promoListInfo.setPrice(mtimePromo.getPrice());
            promoListInfo.setStatus(mtimePromo.getStatus());
            promoListInfoList.add(promoListInfo);
        }
        return promoListInfoList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean createOrder(Integer promoId, Integer userId, Integer amount, String stockLogId) {
        // 参数校验
        processParam(promoId, userId, amount);

        // 3.生成订单(生成兑换码)写入订单表(订单入库)
        EntityWrapper<MtimePromo> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("uuid", promoId);
        List<MtimePromo> mtimePromos = mtimePromoMapper.selectList(entityWrapper);
        if (CollectionUtils.isEmpty(mtimePromos)) {
            return false;
        }
        MtimePromo mtimePromo = mtimePromos.get(0);

        MtimePromoOrder promoOrder = savePromoOrder(mtimePromo, userId, amount);
        if (promoOrder == null) {
            // 修改库存流失库的status字段为失败
            mtimeStockLogMapper.updateStatusById(stockLogId, StockLogStatus.FAIL.getIndex());
            throw new GunsException(GunsExceptionEnum.DATABASE_ERROR);
        }

        // 2.扣库存(修改redis,然后异步消息修改数据库)
        Boolean ret = decreaseStock(promoId, amount);
        if (!ret) {
            // 扣减库存失败，补回redis里面的库存
            String key = promoId + "stock";
            Long increment = redisTemplate.opsForValue().increment(key, amount);
            // 修改库存流水状态为失败
            mtimeStockLogMapper.updateStatusById(stockLogId, StockLogStatus.FAIL.getIndex());
            throw new GunsException(GunsExceptionEnum.STOCK_ERROR);
        }

        // 修改库存流水状态为成功
        mtimeStockLogMapper.updateStatusById(stockLogId, StockLogStatus.SUCCESS.getIndex());
        return true;
    }

    private Boolean decreaseStock(Integer promoId, Integer amount) {
        String key = promoId + "stock";
        Long increment = redisTemplate.opsForValue().increment(key, amount * -1);
        // 打上库存售罄的标记
        if (increment == 0) {
            String stockKey = "PROMO_STOCK_NULL_PROMOID" + promoId;
            redisTemplate.opsForValue().set(stockKey,"success");
        }
        if (increment < 0) {
            // 执行失败后要将减去的库存加回去
            log.info("库存已经售完,promoId:{}", promoId);
            redisTemplate.opsForValue().increment(key, amount);
            return false;
        }
        // 事务性消息，不需要再这里发送了
        return true;
    }

    //参数校验
    private void processParam(Integer promoId, Integer userId, Integer amount) {
        if (promoId == null) {
            log.info("promoId不能为空！");
            throw new GunsException(GunsExceptionEnum.REQUEST_NULL);
        }
        if (userId == null) {
            log.info("userId不能为空！");
            throw new GunsException(GunsExceptionEnum.REQUEST_NULL);
        }
        if (amount == null) {
            log.info("amount不能为空！");
            throw new GunsException(GunsExceptionEnum.REQUEST_NULL);
        }
    }

    /**
     * 生成秒杀订单
     * @param mtimePromo
     * @param userId
     * @param amount
     * @return
     */
    private MtimePromoOrder savePromoOrder(MtimePromo mtimePromo, Integer userId, Integer amount) {
        MtimePromoOrder mtimePromoOrder = buidPromoOrder(mtimePromo, userId, amount);
        Integer insertRet = mtimePromoOrderMapper.insert(mtimePromoOrder);
        if (insertRet < 1) {
            log.info("生成秒杀订单失败！promoOrder:{}", JSON.toJSONString(mtimePromoOrder));
            return null;
        }
        return mtimePromoOrder;
    }

    private MtimePromoOrder buidPromoOrder(MtimePromo mtimePromo, Integer userId, Integer amount) {
        MtimePromoOrder mtimePromoOrder = new MtimePromoOrder();
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", "");
        // 设置兑换开始时间和结束时间，从现在开始到未来三个月
        String exchangeCode = RandomCharUtils.getRandomNum(10);
        //兑换开始时间和兑换结束时间 为从现在开始，到未来三个月之内
        Date startTime = new Date();
        Date endTime = DateUtil.parseTime(DateUtil.getAfterDayDate(EXCHANGE_CODE_VALID_MONTH.toString()));
        mtimePromoOrder.setUuid(uuidStr);
        mtimePromoOrder.setUserId(userId);
        mtimePromoOrder.setAmount(amount);
        mtimePromoOrder.setStartTime(startTime);
        mtimePromoOrder.setEndTime(endTime);
        mtimePromoOrder.setCinemaId(mtimePromo.getCinemaId());
        mtimePromoOrder.setPrice(mtimePromo.getPrice().multiply(new BigDecimal(amount)));
        mtimePromoOrder.setExchangeCode(exchangeCode);
        mtimePromoOrder.setCreateTime(new Date());
        return mtimePromoOrder;
    }

    @Override
    public boolean publishPromoStock(Integer cinemaId) {
        // 更新该影院的活动库存到缓存
        if (cinemaId != null) {
            EntityWrapper<MtimePromo> promoEntityWrapper = new EntityWrapper<>();
            promoEntityWrapper.eq("cinema_id", cinemaId);
            List<MtimePromo> mtimePromos = mtimePromoMapper.selectList(promoEntityWrapper);
            if (CollectionUtils.isEmpty(mtimePromos)) {
                return false;
            }
            MtimePromo mtimePromo = mtimePromos.get(0);
            Integer promoId = mtimePromo.getUuid();
            MtimePromoStock mtimePromoStock = mtimePromoStockMapper.selectById(promoId);
            Integer stock = mtimePromoStock.getStock();
            redisTemplate.opsForValue().set(promoId + "stock", stock.toString());
            String tokenAmountkey = "PROMO_STOCK_AMOUNT_LIMIT" + mtimePromo.getUuid();
            Integer amount = stock * 5;
            redisTemplate.opsForValue().set(tokenAmountkey,amount+"");
        } else {
            // 把所有库存信息存到Redis缓存
            EntityWrapper<MtimePromoStock> promoStockEntityWrapper = new EntityWrapper<>();
            promoStockEntityWrapper.where("1=1");
            List<MtimePromoStock> mtimePromoStocks = mtimePromoStockMapper.selectList(promoStockEntityWrapper);
            for (MtimePromoStock mtimePromoStock : mtimePromoStocks) {
                redisTemplate.opsForValue().set(mtimePromoStock.getPromoId() + "stock", mtimePromoStock.getStock().toString());
                // 秒杀令牌的数量
                String tokenAmountkey = "PROMO_STOCK_AMOUNT_LIMIT" + mtimePromoStock.getPromoId();
                Integer amount = mtimePromoStock.getStock() * 5;
                redisTemplate.opsForValue().set(tokenAmountkey,amount+"");
            }
        }
        return true;
    }

    @Override
    public String getGenerateToken(Integer userId, Integer promoId) {
        // 根据promoId生成token
        UUID uuid = UUID.randomUUID();
        String generateToken = uuid.toString().replace("-","");
        // 扣减秒杀令牌的数量
        String promoAmountKey = "PROMO_STOCK_AMOUNT_LIMIT" + promoId;
        Long result = redisTemplate.opsForValue().increment(promoAmountKey, -1);
        if (result < 0) {
            log.info("秒杀令牌已经发放完！promoId:{}",promoId);
            return null;
        }
        // 存放token到redis里面
        String key = "USER_PROMO_TOKEN_PREFIX" + promoId;
        redisTemplate.opsForValue().set(key,generateToken);
        return generateToken;
    }

    /**
     * 包装MqProducer中的transactionCreateOrder方法
     * @param promoId
     * @param userId
     * @param amount
     * @param stockLogId
     * @return
     */
    @Override
    public Boolean transactionSaveOrder(Integer promoId, Integer userId, Integer amount, String stockLogId) {
        Boolean result = producer.transactionCreateOrder(promoId, amount, userId, stockLogId);
        return result;
    }

    /**
     * 初始化订单库存表
     * @param promoId
     * @param amount
     * @return
     */
    @Override
    public String initPromoStockLog(Integer promoId, Integer amount) {
        MtimeStockLog mtimeStockLog = new MtimeStockLog();
        mtimeStockLog.setPromoId(promoId);
        mtimeStockLog.setAmount(amount);
        String uuid = UUID.randomUUID().toString().replace("-","");
        mtimeStockLog.setUuid(uuid);
        mtimeStockLog.setStatus(StockLogStatus.INIT.getIndex());
        Integer insert = mtimeStockLogMapper.insert(mtimeStockLog);
        if(insert > 0) {
            return uuid;
        }else {
            return null;
        }
    }
}
