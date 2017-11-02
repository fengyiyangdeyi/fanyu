package cn.com.fanyu.huanxin.api.impl;

import cn.com.fanyu.huanxin.api.SendMessageAPI;
import cn.com.fanyu.huanxin.comm.EasemobAPI;
import cn.com.fanyu.huanxin.comm.ResponseHandler;
import cn.com.fanyu.huanxin.comm.TokenUtil;
import cn.com.fanyu.huanxin.comm.OrgInfo;
import io.swagger.client.ApiException;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;

public class EasemobSendMessage implements SendMessageAPI {
    private ResponseHandler responseHandler = new ResponseHandler();
    private MessagesApi api = new MessagesApi();
    @Override
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameMessagesPost(OrgInfo.ORG_NAME,OrgInfo.APP_NAME, TokenUtil.getAccessToken(), (Msg) payload);
            }
        });
    }
}
