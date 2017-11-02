package cn.com.fanyu.service;

import cn.com.fanyu.dao.*;
import cn.com.fanyu.domain.*;
import cn.com.fanyu.utils.BusinessException;
import cn.com.fanyu.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.transaction.Transactional;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IapService {
    @Autowired
    private FyDiamondAccountsRepository fyDiamondAccountsRepository;
    @Autowired
    private FyUserRepository fyUserRepository;
    @Autowired
    private FyGoodsRepository fyGoodsRepository;

    /**
     * 发送请求
     *
     * @param url
     * @param code
     * @return
     */
    @Transactional
    public String sendHttpsCoon(String url, String code, String userId) throws Exception {
        if (url.isEmpty()) {
            return null;
        }
        //设置SSLContext
        SSLContext ssl = SSLContext.getInstance("SSL");
        ssl.init(null, new TrustManager[]{myX509TrustManager}, null);

        //打开连接
        HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
        //设置套接工厂
        conn.setSSLSocketFactory(ssl.getSocketFactory());
        //加入数据
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-type", "application/json");

        JSONObject obj = new JSONObject();
        obj.put("receipt-data", code);


        BufferedOutputStream buffOutStr = new BufferedOutputStream(conn.getOutputStream());
        buffOutStr.write(obj.toString().getBytes());
        buffOutStr.flush();
        buffOutStr.close();

        //获取输入流
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
//        System.out.println(sb.toString());
        //
        //跟苹果验证有返回结果------------------
        JSONObject job = JSON.parseObject(sb.toString());
        String states = job.getString("status");
        if (!states.equals("0")) {
            throw new BusinessException("苹果返回状态" + states);
        }
        String r_receipt = job.getString("receipt");
        JSONObject returnJson = JSON.parseObject(r_receipt);
        String in_app = returnJson.getString("in_app");
        JSONArray objects = JSON.parseArray(in_app);
        if (objects.size() > 0) {
            for (int i = 0; i < objects.size(); i++) {
                JSONObject p = objects.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                //产品ID
                String product_id = p.getString("product_id");
                //数量
                String quantity = p.getString("quantity");
                //交易日期
                String purchase_date = p.getString("purchase_date");
                //交易id
                String transaction_id = p.getString("transaction_id");
                //保存到数据库
                FyDiamondAccounts accounts = fyDiamondAccountsRepository.findByTransactionId(transaction_id);
                if (accounts != null) {
                    throw new BusinessException(transaction_id + "已验证！");
                }
                FyUser user = fyUserRepository.getIMUserByUserName(userId);
                FyGoods g = fyGoodsRepository.findByAppleid(product_id);
                BigDecimal addDec = new BigDecimal(quantity).multiply(g.getDiamondNum().add(g.getGivingDiamondNum()));
                FyDiamondAccounts newAccount = new FyDiamondAccounts();
                newAccount.setUserBeforeNum(user.getDiamondNum());
                newAccount.setDiamondNum(addDec);
                newAccount.setRemark("ios充钻石");
                newAccount.setUsername(userId);
                newAccount.setTransactionId(transaction_id);
                user.setDiamondNum(user.getDiamondNum().add(addDec));
                newAccount.setUserAfterNum(user.getDiamondNum());
                fyUserRepository.saveAndFlush(user);
                fyDiamondAccountsRepository.saveAndFlush(newAccount);
            }
        }
        return sb.toString();
    }

    /**
     * 重写X509TrustManager
     */
    private static TrustManager myX509TrustManager = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }
    };
}
