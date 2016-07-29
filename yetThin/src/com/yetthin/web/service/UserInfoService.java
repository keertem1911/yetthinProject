package com.yetthin.web.service;

import com.yetthin.web.domain.UserInfo;

public interface UserInfoService extends BaseService<UserInfo>{
	public abstract UserInfo get(String phoneNum,String password);
}
