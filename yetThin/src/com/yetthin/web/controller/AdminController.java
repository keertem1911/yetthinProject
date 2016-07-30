package com.yetthin.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.yetthin.web.common.ProductRandomSalt;
import com.yetthin.web.domain.Admin;
import com.yetthin.web.service.AdminService;
import com.yetthin.web.service.PhoneVersionService;
import com.yetthin.web.service.TempCodeService;
import com.yetthin.web.service.UserInfoService;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController{
	@Resource(name="UserInfoService")
	private UserInfoService userInfoService;
	@Resource(name="TempCodeSerivce")
	private TempCodeService tempCodeService;
	@Resource(name="PhoneVersionService")
	private PhoneVersionService phoneVersionService;
	@Resource(name="AdminService")
	private AdminService adminService;
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(Admin admin){
		
		return null;
	}
	public AdminController() {
		// TODO Auto-generated constructor stub
		super(ProductRandomSalt.getSalt());
	}
	
	 
}
