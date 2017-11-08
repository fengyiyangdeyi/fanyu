package cn.com.fanyu.service;

import cn.com.fanyu.dao.*;
import cn.com.fanyu.domain.*;
import cn.com.fanyu.utils.*;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.github.wxpay.sdk.WXPay;
import freemarker.core.BugException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Service
public class PayService {

    @Autowired
    private FyOrderRepository fyOrderRepository;
    @Autowired
    private FyUserRepository fyUserRepository;
    @Autowired
    private FyDiamondAccountsRepository fyDiamondAccountsRepository;
    @Autowired
    private FyGoodsRepository fyGoodsRepository;
    @Value("${wx_notify_url}")
    private String wx_return_url;

    public String alipay(FyOrder order) throws Exception {
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = order.getCode();
        // 订单名称，必填
        String subject = order.getCode();
        System.out.println(subject);
        // 付款金额，必填
        String total_amount = order.getMoney().toString();
        // 商品描述，可空
        String body = order.getCode();
        ;
        // 超时时间 可空
        String timeout_express = "2m";
        // 销售产品码 必填
        String product_code = "QUICK_WAP_PAY";
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(AlipayConfig.notify_url);
        // 设置同步地址
        alipay_request.setReturnUrl(AlipayConfig.return_url);

        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = client.pageExecute(alipay_request).getBody();
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return form;
    }

    public String createOrderAndAlipay(String jsessionId, Long goodid) throws Exception {
        FyUser user = fyUserRepository.findByUuid(jsessionId);
        if (user == null) {
            throw new BugException("找不到用户");
        }
        FyOrder order = createOrder(user.getUsername(), goodid);
        return alipay(order);
    }

    private FyOrder createOrder(String username, Long goodid) {
        FyGoods goods = fyGoodsRepository.findOne(goodid);
        FyOrder order = new FyOrder();
        order.setUsername(username);
        order.setMoney(goods.getPrice());
        order.setCode(MakeOrderNum.makeOrderNum(""));
        order.setGivingDiamondNum(goods.getGivingDiamondNum());
        order.setDiamondNum(goods.getDiamondNum());
        order.setGoodsId(goodid);
        fyOrderRepository.saveAndFlush(order);
        return order;
    }


    /**
     *
     * @param code 订单号
     * @param trade_no 交易号
     * @param paymoney
     * @param payType
     */
    @Transactional
    public void updateOrderStatus(String code, String trade_no, BigDecimal paymoney,String payType) {
        FyOrder order = fyOrderRepository.findByCode(code);
        if (order == null) {
            throw new BusinessException("找不到订单" + code);
        }
        if (order.getStatus().intValue() != 0) {
            throw new BusinessException("订单状态必须为未支付" + code);
        }
        order.setTrade_no(trade_no);
        order.setPaymoney(paymoney);
        order.setPayTime(new Date());
        order.setStatus(1);
        order.setPayType(payType);
        fyOrderRepository.saveAndFlush(order);
        addDiamond(order);
    }

    private void addDiamond(FyOrder order) {
        FyUser user = fyUserRepository.getIMUserByUserName(order.getUsername());
        BigDecimal total = order.getDiamondNum().add(order.getGivingDiamondNum());
        createFyDiamondAccounts(user, total, order.getPayType()+"充值:orderid" + order.getId());
    }

    public void createFyDiamondAccounts(FyUser user, BigDecimal diamondNum, String remark) throws BusinessException {
        BigDecimal add = user.getDiamondNum().add(diamondNum);
        if (add.doubleValue() < 0) {
            throw new BusinessException("钻石数量不足！");
        }
        FyDiamondAccounts a = new FyDiamondAccounts();
        a.setDiamondNum(diamondNum);
        a.setRemark(remark);
        a.setUsername(user.getUsername());
        a.setUserBeforeNum(user.getDiamondNum());
        user.setDiamondNum(add);
        a.setUserAfterNum(add);
        fyUserRepository.saveAndFlush(user);
        fyDiamondAccountsRepository.saveAndFlush(a);
    }

    @Transactional
    public Map createOrderAndWXpay(String jsessionId, Long goodid,String spbill_create_ip) throws Exception {
        FyUser user = fyUserRepository.findByUuid(jsessionId);
        if (user == null) {
            throw new BugException("找不到用户");
        }
        FyOrder order = createOrder(user.getUsername(), goodid);
        return wxpay(order,spbill_create_ip);
    }

    private Map wxpay(FyOrder order,String spbill_create_ip) throws Exception {
        String code = order.getCode();
        String money = order.getMoney().multiply(new BigDecimal("100")).intValue()+"";
        Long goodsId = order.getGoodsId();

        WXConfig config = new WXConfig();

        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "全民聊聊-钻石充值");
        data.put("out_trade_no", code);
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("total_fee", money);
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", wx_return_url);
        data.put("trade_type", "APP");  // 此处指定为扫码支付
        data.put("product_id", goodsId + "");


        Map<String, String> resp = wxpay.unifiedOrder(data);

        resp.put("partnerid",config.getMchID());

        String prepay_id=resp.get("prepay_id");
        String nonce_str=resp.get("nonce_str");
        String timeStamp=System.currentTimeMillis()/1000+"";
        String signNum = signNum(config.getAppID(), config.getMchID(), prepay_id, "Sign=WXPay", nonce_str, timeStamp, config.getKey());
        resp.put("sign",signNum);
        resp.put("timeStamp",timeStamp);

        System.out.println("wx==="+resp);
        return resp;
    }


    private String signNum(String appid,String partnerId,String prepayId,String packageValue,String nonceStr,String timeStamp,String key){
        String stringA=
                "appid="+appid
                        +"&noncestr="+nonceStr
                        +"&package="+packageValue
                        +"&partnerid="+partnerId
                        +"&prepayid="+prepayId
                        +"&timestamp="+timeStamp;


        String stringSignTemp = stringA+"&key="+key;
        String sign = MD5Tools.MD5(stringSignTemp).toUpperCase();
        return  sign;
    }
}
