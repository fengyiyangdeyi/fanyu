package cn.com.fanyu.huanxin.api.impl;

import cn.com.fanyu.huanxin.api.AuthTokenAPI;
import cn.com.fanyu.huanxin.comm.TokenUtil;


public class EasemobAuthToken implements AuthTokenAPI {

	@Override
	public Object getAuthToken(){
		return TokenUtil.getAccessToken();
	}
}
