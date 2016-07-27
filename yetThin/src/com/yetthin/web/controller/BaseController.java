package com.yetthin.web.controller;

 
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
 
 

@Component
@Scope("prototype")
public class BaseController {
	//设置 COOKIE 标志位
	private static final String AUTH_COOKIE_NAME="yetthin";
	//设置MD5 加盐参数
	private static final String ENCRYPT_SALT="34d4yf73s!23fd";
	//设置Cookie 的持续时间
	//长 1个月
	private static final int longLogin= 60*60*24*30;
	//短 1天
	private static final int shortLogin= 60*60*24*1;
	//用户 id
	private String userId=null;
//	@Resource(name="userService")
//	private UserService userService;
	
}
