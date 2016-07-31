package com.yetthin.web.controller;

 
 
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.yetthin.web.common.Md5UnitTool;
import com.yetthin.web.common.ProductRandomSalt;
import com.yetthin.web.domain.PhoneVersion;
import com.yetthin.web.domain.UserInfo;
 
import com.yetthin.web.service.TempCodeService;
import com.yetthin.web.service.UserInfoService;

@Controller
@SessionAttributes(value="auth_id",types=String.class)
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
	@RequestMapping(value="/register",method=RequestMethod.POST,
			produces = {"application/json;charset=UTF-8"})
	public Map<String, Object> Rigister(UserInfo u,
			@RequestParam(value="verifyCode")String verifyCodes){
		System.out.println("come into register $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("verifyCode= "+verifyCodes);
		
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
	@RequestMapping(value="/getRegisterVerify",method=RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
	public Map<String, Object> getRegisterVerify(@RequestParam(value="phoneNum",required=true)String phone){
		System.out.println("come into getRegisterVerify $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		return null;
		
	}
	/**
	 * 登陆
	 * @param phone
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/login",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	public Map<String, Object> login(@RequestParam("phoneNum")String phone,
			@RequestParam("password")String password,Model model){
		
		System.out.println("come into login $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		password=getEncrty(phone+","+password);
		
		UserInfo u = userInfoService.get(phone,password);

		String msg="200";

		if(u==null)
			msg="用户不存在";
		else{
			String auth_id=phone+"="+getEncrty(u.getPhoneNum());
			model.addAttribute("auth_id", auth_id);
			model.addAttribute("userID",u.getUserId());
		}
			
			status.put("status", msg);
			return status;
	}
	
	/**
	 * 忘记密码
	 * @param phoneNum
	 * @param verifyCode 验证码
	 * @param password
	 * @return 状态码
	 */
	@ResponseBody
	@RequestMapping(value="/forgetPwd",method=RequestMethod.POST,
			produces = {"application/json;charset=UTF-8"})
	public String  forgetPwd(@RequestParam(value="phoneNum")String phoneNum,
			@RequestParam(value="verifyCode")String verifyCode,
			@RequestParam(value="password")String password){
		System.out.println("come into forgetPwd $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("phoneNum="+phoneNum+",verifyCode="+verifyCode+",password="+password);
		String flag="200";
		return "{status:"+flag+"}";
	}
	/**
	 * 忘记密码的验证码
	 * @param phoneNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getforgetPwdVerify",method=RequestMethod.PUT,
			produces = {"application/json;charset=UTF-8"})
	public String getForgetPwdVerify(@RequestParam(value="phoneNum",required=true)String phoneNum){
		String flag="200";
		System.out.println("come into getForgetPwdVerify $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("phoneNum="+phoneNum);
		return "{status:"+flag+"}";
	}
	/**
	 * 更新极光id
	 * @param userId
	 * @param JpushID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateJpushID",method=RequestMethod.PUT,
			produces = {"application/json;charset=UTF-8"})
	public String updateJpushID(@RequestParam("userID")String userId,
			@RequestParam(value="JpushID")String JpushID){
		String flag=null;
		System.out.println("come into updateJpushID $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println(userId+"="+JpushID);
		flag=userInfoService.updateJpushId(userId, JpushID);
		return "{status:"+flag+"}";
	}
	/**
	 * 更新本机推送开关
	 * @param userId
	 * @param status
	 * @return
	 */
	@ResponseBody()
	@RequestMapping(value="/updateJpushStatus",method=RequestMethod.POST,
			produces = {"application/json;charset=UTF-8"})
	public String updateJpushStatus(@RequestParam(value="userID")String userId,
			@RequestParam(value="status")String status){
		
		System.out.println("come into updateJpushStatus $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		String flag=null;
		flag=userInfoService.updateJpushStatus(userId, status);
		System.out.println(userId+"="+status);
		return "{status:"+flag+"}";
	}
	/**
	 * 绑定邮箱   
	 * @param userId
	 * @param email
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/bindingEmail",method=RequestMethod.PUT,produces = {"application/json;charset=UTF-8"})
	public String bindingEmail(@RequestParam(value="userID")String userId,@RequestParam(value="email")String email){
		String flag="200";
		
		System.out.println("come into updateJpushStatus bindingEmail $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("userID="+userId+",email="+email);
		flag=userInfoService.bindingEmail(userId, email);
		return "{status:"+flag+"}";
	}
	/**
	 * 更改密码
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/changePwd",method=RequestMethod.PUT,produces = {"application/json;charset=UTF-8"})
	public String changePwd(@RequestParam(value="userID")String userId,
			@RequestParam(value="oldPwd")String oldPassword,
			@RequestParam(value="newPwd")String newPassword){
		System.out.println("come into  changePwd $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		String flag="200";
		UserInfo u=userInfoService.get(userId);
		if(u==null){
			flag="用户不存在";
		}else{
			System.out.println(u);
			String oldP=getEncrty(u.getPhoneNum()+","+oldPassword);
			if(u.getPassword().equals(oldP.trim())){
			String newP=getEncrty(u.getPhoneNum()+","+newPassword);
			u.setPassword(newP);
			flag=userInfoService.changePwd(u);
			}else{
				flag="密码错误";
			}
		}
		System.out.println(userId+"="+oldPassword+"="+newPassword);
		return "{status:"+flag+"}";
		
	}
	
	/**
	 * 意见反馈
	 * @param userId
	 * @param ideaText
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/feedback",method=RequestMethod.PUT,produces = {"application/json;charset=UTF-8"})
	public String feedBack(@RequestParam(value="userID",required=true)String userId,
			@RequestParam(value="ideaText",required=false)String ideaText){
		System.out.println("come into feedback $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		String flag="200";
		flag=userInfoService.feedBack(userId, ideaText);
		System.out.println(userId+"="+ideaText);
		return "{status:"+flag+"}";
	}
}
