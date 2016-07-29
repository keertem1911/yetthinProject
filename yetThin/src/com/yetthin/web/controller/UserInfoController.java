package com.yetthin.web.controller;

import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yetthin.web.common.Md5UnitTool;
import com.yetthin.web.domain.UserInfo;
 
import com.yetthin.web.service.TempCodeService;
import com.yetthin.web.service.UserInfoService;

@Controller
@RequestMapping("/user")
public class UserInfoController extends BaseController{

	@Resource(name="UserInfoService")
	private UserInfoService userInfoService;
	@Resource(name="TempCodeSerivce")
	private TempCodeService tempCodeService;
	
	/**
	 * 注册
	 * @param userInfo
	 * @param pw
	 */
	@ResponseBody
	@RequestMapping(value="/register")
	public Map<String, Object> Rigister(UserInfo u){
		String password=getEncrty(u.getPhoneNum()+","+u.getPassword());
		System.out.println("MD5 done password   "+password+"-------------------");
		u.setPassword(password);
		int flag=0;
		try {
			System.out.println(u+"----------------------------");
			flag=userInfoService.save(u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag=0;
		}
		String msg="200";
		if(flag!=1)
			msg="添加失败";
			status.put("status", msg);
			return status;
	}
	/**
	 * 注册验证码
	 * @param phone
	 * @param pw
	 */
	@ResponseBody
	@RequestMapping(value="/getRegisterVerify",method=RequestMethod.GET)
	public void getRegisterVerify(@RequestParam(value="phoneNum",required=true)String phone,PrintWriter pw){
		
	}
	@ResponseBody
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Map<String, Object> login(@RequestParam("phoneNum")String phone,
			@RequestParam("password")String password){
		password=getEncrty(phone+","+password);
		UserInfo u = userInfoService.get(phone,password);
		String msg="200";
		
		if(u==null)
			msg="添加失败";
		
			status.put("status", msg);
			return status;
		 
			
		
	}
}
