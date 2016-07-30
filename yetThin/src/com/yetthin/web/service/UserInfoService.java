package com.yetthin.web.service;

import com.yetthin.web.domain.PhoneVersion;
import com.yetthin.web.domain.UserInfo;

public interface UserInfoService extends BaseService<UserInfo>{
	public abstract UserInfo get(String phoneNum,String password);
	public abstract String getRegisterVerify(String phoneNum);
	public abstract String forgetPwd(String phoneNum,String verifyCode,String password);
	public abstract String getforgetPwdVerify(String phoneNum);
	public abstract String updateJpushId(String userId,String JpushID);
	public abstract String updateJpushStatus(String userId,String status);
	public abstract String bindingEmail(String userID,String email);
	public abstract String changePwd(UserInfo u);
	public abstract PhoneVersion checkNewVersion();
	public abstract String feedBack(String userId,String ideaText);
}
