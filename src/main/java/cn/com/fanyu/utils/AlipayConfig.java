package cn.com.fanyu.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

@Component
public class AlipayConfig {

    // 商户appid
    public static String APPID = "2017102509515645";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCpx0iomosMBQVCttRedIdOfFgI6N9MGkrEPR00Q7dQjQkanR4o5cUhSIc0OaldGcLp7w+uoz2mcjDkBKPS9fDXSXzA4OWbAHJyQKIn+SqOVwPvmBWC2BhLyPukxx5eSBkKbOP+veapnti5Qf9Cj3Iy3gb75ajHYYhytG7gsqgf9/1o2E9+OjII14obhqVkWfb1q28qf0+z3skmN+xE088p25yzGjf+gLPmxNGhaEd7+SOHYnGlzr3YfX3BWs8cBQfEgpd3pT5FNu3zKbea2/iljpnC2g6L3MewKRnvuO6Isw8syYIjn+6cTeUNcfGqo5u5cZqjg43g68R29WlKsQGzAgMBAAECggEATyBh7LKaO2/bk8xCDgOhTCMHpJ9j8sQONIeHJOSqexBAPfccJ1NvHKQA34ZAOPPYs86HMI1EHl76CktfTuLw7GWDjyFB4YvDyxv0d0zZWJ2VaAUpgWHVcPyqy5FBxg80HHfDiuZWqpctyqnHne3c+3gfKSdQEhySy2Lsiiy5zquvc6p+ZwtEnlNd6BiFHywHALMA1MqcsEXHXVwAa3MZAvQBhbw4/wAlKE5oRfDETdusMRe3Y7dOkwlAc0gJH3I6AiKsjusOq6nhc8mo2tTLdh30ZLQlai7Jj6/7OSFbrlPchLgLDFiTya0mXAMubosA1L9VZUe3wHs4QW2WS1sbAQKBgQD+Qapey8YD91eLO/AV31MI4ZDB5kcWEgDzAyc/Ol99lLvPdvjUHhovXwgN6D/5tp2/f9UglQTubU50XvhlnUUQ5nFGmqAmrUCSj7QOghl6nRwLtEO5Y2XjVPRNcpsAvcNgUAkf+6QnhtXFoy2OH3sYt+IeQxomU3Tw1oaoddAOmQKBgQCq8VJDsW8e1Z+s11qj+QPtkc7DMZ4hRMeC97jbpU/wk4o9qhzXYMsPHtcLKboe2M1K+seUbEGtidPRfjTZAUUMSYLG3ARxHf4GYzt9EuEDxmP73SqQceq5dN+v2UX8JOwbAqn9iw3hWVEZTcH0W+AaJAXyWO0d/wX5VlwMY9G+KwKBgQDdTqLQqGXPBfe5RY6QOza3QD8TxzCQ0VLqMxByRIjhchiXWGL1ns4D9Z+5GzK0VHOyP75vAcatEY98Uz26lM2NDEgEwIotoBfYeiSrjCpJQ38p4Noh/5ar7UJINz0bxBDPQFrw78POc9qWkJYSYuxuc3/p/1Y/jdz31IKZFxLl4QKBgB2814vRLTyW+y0tVaZa5pUInYiVdCXjejXNdIo5avu2HwF1O4k4XHrn6Y8WljtRif02PUwTvSU8ae3y36y0rNdjXm3LzrYC+rMPxkSO6Tgn5jLlmccBGk1jCJrl2Gfx4GUehIYID8UZLaOSRGCHLyg9Bg/P6ogiWl6y6FZ7cvx7AoGBAKkktLKKv4grMZ9N8jsfwoj4RA5dlMOCjbTrUOhy8EaggN1hRYtYIhxk5F7WtPb6sB3GbLjq4gmHzxlLCIXx7mAH/88hf5TbZsY1cIOmLXjoUgCW3GbgcihK3dMgmrsaHoIb0AAn6nw7mUoakdzWrbKb1PNjgDPllBVunQNHH+97";

    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url;
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url;
    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj/W/dlNfDwL6xjcb18uMGyJ66VmBWc603vQDmN6R7+O2vKodIT69PJl7+vNRqqhT5LRN6a9VK8J4xQRTZ+JlP7Ec4xDZlZ4ywyWib9XeOJU7TgMPJAkCdQINLl36vUYc3HhQ1ikhtGREJeu8+/82vrZo+QSxzM3qZ+d1354VTfgTu2/iQOHuQZc5INaLLAdvS0p1qHFbMLfmGCdYu6c+ZTd+3U05D3/mXQwBeW7RY9awZ8KIEm7IVdB+o4uj0HHdHoT2NrsDH+mBRPoH8CLbk1O8RwfhVwnNuDVUpCADjHJD8dbupbc7Lf2w6AftNgnPpmyHH6KXN0bkhzJ0BH9nAwIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";

    @Value("${notify_url}")
    public void setNotify_url(String notify_url) {
        AlipayConfig.notify_url = notify_url;
    }

    @Value("${return_url}")
    public void setReturn_url(String return_url) {
        AlipayConfig.return_url = return_url;
    }
}

