package com.yetthin.web.serviceImp;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.yetthin.web.common.Md5UnitTool;
import com.yetthin.web.common.sendEmailVerify;

public class BaseService {
	// 设置MD5 加盐参数
	protected static final String ENCRYPT_SALT="34d4yf73s!23fd";
	
	protected  static final String EMAIL_CALLBACK_ADDRESS="www.cktim.net/yetThin/user/emailCallback";
	 
	 
	private Md5UnitTool md5Tool = Md5UnitTool.getInstance();
	
	protected SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Resource(name="Sender")
	private sendEmailVerify sender;
	
	public sendEmailVerify getSender() {
		return sender;
	}
	/**
	 * 用户名及密码 MD5加密存储
	 * 
	 * @param plainText
	 * @return
	 */
	protected String getEncrty(String plainText) {

		String res = md5Tool.encrypt(plainText, ENCRYPT_SALT);

		return res;
	}
	

}
