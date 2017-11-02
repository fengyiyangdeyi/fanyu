package cn.com.fanyu.controller;

import cn.com.fanyu.service.IapService;
import cn.com.fanyu.utils.ResultCode;
import cn.com.fanyu.utils.ResultJson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("iap")
public class IapController {

    //购买凭证验证地址
    private static final String certificateUrl = "https://buy.itunes.apple.com/verifyReceipt";

    //测试的购买凭证验证地址
    private static final String certificateUrlTest = "https://sandbox.itunes.apple.com/verifyReceipt";
    @Autowired
    private IapService iapService;


    /**
     * 接收iOS端发过来的购买凭证
     *
     * @param userId
     * @param receipt
     * @param chooseEnv
     */
    @RequestMapping(value = "/setIapCertificate", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String setIapCertificate(@RequestParam() String userId, @RequestParam() String receipt, boolean chooseEnv) {
        String url = chooseEnv == true ? certificateUrl : certificateUrlTest;
        final String certificateCode = receipt;
        try {
            String s = iapService.sendHttpsCoon(url, certificateCode, userId);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", s).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }


}