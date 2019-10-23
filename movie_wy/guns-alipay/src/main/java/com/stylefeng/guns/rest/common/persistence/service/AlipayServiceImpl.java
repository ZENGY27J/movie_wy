package com.stylefeng.guns.rest.common.persistence.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.alipay.AlipayService;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.AliPayResultVo;
import com.stylefeng.guns.rest.common.persistence.model.AlipayVo;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.persistence.utils.AliyunOSSUtil;
import com.stylefeng.guns.rest.modular.trade.config.Configs;
import com.stylefeng.guns.rest.modular.trade.model.ExtendParams;
import com.stylefeng.guns.rest.modular.trade.model.GoodsDetail;
import com.stylefeng.guns.rest.modular.trade.model.TradeStatus;
import com.stylefeng.guns.rest.modular.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.stylefeng.guns.rest.modular.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.stylefeng.guns.rest.modular.trade.model.result.AlipayF2FPrecreateResult;
import com.stylefeng.guns.rest.modular.trade.model.result.AlipayF2FQueryResult;
import com.stylefeng.guns.rest.modular.trade.service.AlipayMonitorService;
import com.stylefeng.guns.rest.modular.trade.service.AlipayTradeService;
import com.stylefeng.guns.rest.modular.trade.service.impl.AlipayMonitorServiceImpl;
import com.stylefeng.guns.rest.modular.trade.service.impl.AlipayTradeServiceImpl;
import com.stylefeng.guns.rest.modular.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.stylefeng.guns.rest.modular.trade.utils.Utils;
import com.stylefeng.guns.rest.modular.trade.utils.ZxingUtils;
import com.wuyan.film.FilmService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.stylefeng.guns.rest.modular.trade.model.TradeStatus.UNKNOWN;

@Component
@Service(interfaceClass = AlipayService.class)
public class AlipayServiceImpl implements AlipayService{

    private String TradeID;

    private static Log log = LogFactory.getLog(AlipayServiceImpl.class);

    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;

    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
    private static AlipayTradeService   tradeWithHBService;

    // 支付宝交易保障接口服务，供测试接口api使用，请先阅读readme.txt
    private static AlipayMonitorService monitorService;

    static {
        /* 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /* 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();

        // 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 *//*
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
                .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
                .setFormat("json").build();
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

//    @Reference(interfaceClass = MoocOrderTMapper.class,check = false)
    @Autowired
    MoocOrderTMapper moocOrderTMapper;

//    @Reference(interfaceClass = FilmService.class,check = false)
//    FilmService filmService;

    @Override
    public AlipayVo getPayInfo(String orderId) {

        EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper();
        entityWrapper.eq("UUID",orderId);
        List<MoocOrderT> moocOrderTS = moocOrderTMapper.selectList(entityWrapper);
        MoocOrderT moocOrder = moocOrderTS.get(0);

//        MoocOrderT moocOrder = moocOrderTMapper.queryOrderById(orderId);

        String[] split = moocOrder.getSeatsIds().split(",");
        String filmName = "";
        String img = getPayImg(moocOrder,filmName,split.length);

        AlipayVo alipayVo = new AlipayVo();
        alipayVo.setOrderId(orderId);
        alipayVo.setqRCodeAddress(img);
        return alipayVo;
    }

    @Override
    public AliPayResultVo getPayResult(String orderId) {
        AliPayResultVo payResultVo = new AliPayResultVo();
        TradeStatus tradeStatus = test_trade_query();
        String s = tradeStatus.toString();
        if (s.equals("SUCCESS")){
            moocOrderTMapper.updateForSet("order_status=1", new EntityWrapper<MoocOrderT>().eq("UUID", orderId));
        }

        EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper();
        entityWrapper.eq("UUID",orderId);
        List<MoocOrderT> moocOrderTS = moocOrderTMapper.selectList(entityWrapper);
        MoocOrderT moocOrderT = moocOrderTS.get(0);

//        MoocOrderT moocOrderT = moocOrderTMapper.queryOrderById(orderId);
        if (moocOrderT.getOrderStatus()==1){
            payResultVo.setOrderId(moocOrderT.getUuid());
            payResultVo.setOrderMsg("支付成功");
            payResultVo.setOrderStatus(1);
            return payResultVo;
        }else {
            payResultVo.setOrderStatus(3);
            return payResultVo;
        }
    }

    public TradeStatus test_trade_query() {
        // (必填) 商户订单号，通过此商户订单号查询当面付的交易状态
        String outTradeNo = TradeID;

        // 创建查询请求builder，设置请求参数
        AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder()
                .setOutTradeNo(outTradeNo);

        AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);
        TradeStatus resultTradeStatus = result.getTradeStatus();
        System.out.println(resultTradeStatus);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("查询返回该订单支付成功: )");

                AlipayTradeQueryResponse response = result.getResponse();
                dumpResponse(response);

                log.info(response.getTradeStatus());
                if (Utils.isListNotEmpty(response.getFundBillList())) {
                    for (TradeFundBill bill : response.getFundBillList()) {
                        log.info(bill.getFundChannel() + ":" + bill.getAmount());
                    }
                }
                break;

            case FAILED:
                log.error("查询返回该订单支付失败或被关闭!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，订单支付状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return resultTradeStatus;
    }

    private String getPayImg(MoocOrderT moocOrder, String filmName,int len) {
        String outTradeNo = "MT" + System.currentTimeMillis()
                + (long) (Math.random() * 10000000L);
        TradeID = outTradeNo;
        String subject = "meeting院线电影消费";

        filmName = "我不是药神";
        String totalAmount = String.valueOf(moocOrder.getOrderPrice());

        String undiscountableAmount = "0";

        String sellerId = "";


        String body = "购买"+filmName+"的电影票,票价为："+moocOrder.getFilmPrice()+"元一张,购买时间为:"+moocOrder.getOrderTime();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        long price = 100*moocOrder.getFilmPrice().longValue();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance(String.valueOf(moocOrder.getFilmId()), filmName, price, len);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
//        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
//        goodsDetailList.add(goods2);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                //                .setNotifyUrl("http://www.test-notify-url.com")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        File qrCodeImge = null;
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
                String filePath = String.format("C:\\png/qr-%s.png", response.getOutTradeNo());
                log.info("filePath:" + filePath);
                qrCodeImge = ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                AliyunOSSUtil.upLoad(qrCodeImge);
                break;

            case FAILED:
                log.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return qrCodeImge.getName();
    }
}
