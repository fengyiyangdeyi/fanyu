package cn.com.fanyu.utils;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

public class WXConfig implements WXPayConfig {

    private byte[] certData;

    public WXConfig() throws Exception {
////        URL url = getClass().getClassLoader().getResource("apiclient_cert.p12");
////        System.out.println("url"+url.getFile());
//        URL url1 = getClass().getClassLoader().getResource("");
//        File file = new File(url1.getFile()+"apiclient_cert.p12");
////        System.out.println("url2111"+url1.getFile()+"apiclient_cert.p12");
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
    }

    public String getAppID() {
        return "wxda2aa0cedd084bd5";
    }

    public String getMchID() {
        return "1491283222";
    }

    public String getKey() {
        return "D06B215179DA5AA872AA1E387A8BE95B";
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
