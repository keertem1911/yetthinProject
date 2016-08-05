package com.yetthin.web.controller;

 
 
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.yetthin.web.domain.UserInfo;
 
import com.yetthin.web.service.UserInfoService;

@Controller
@SessionAttributes(value="auth_id,type",types=String.class)
@RequestMapping("/user")
public class UserInfoController extends BaseController{

	@Resource(name="UserInfoService")
	private UserInfoService userInfoService;
	  
	private HttpServletRequest request;
	private HttpServletResponse response;
	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        this.request = request;  
        this.response = response;  
         
    }  
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	    dateFormat.setLenient(false);  
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));  
	}
	/**
	 * 注册
	 * @param userInfo
	 * @param pw
	 */
	@ResponseBody
	@RequestMapping(value="/register",method=RequestMethod.POST,
			produces = {"application/json;charset=UTF-8"})
	public String Rigister(UserInfo u,
			@RequestParam(value="verifyCode",required=false)String verifyCodes){
		u.setPhoneNum(u.getPhoneNum().trim());
		u.setPassword(u.getPassword().trim());
		String msg=null;
		String statusCode="200";
		if(!"".equals(u.getPassword().trim())&&!"".equals(u.getPhoneNum().trim())){
		List<UserInfo> users=userInfoService.getListAll();
		boolean same=false;
		System.out.println("come into register $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("verifyCode= "+verifyCodes);
		for (UserInfo userInfo : users) {
			if(userInfo.getPhoneNum().equals(u.getPhoneNum())){
				same=true;
				break;
			}
		}
		if(!same){
		String password=getEncrty(u.getPhoneNum()+","+u.getPassword());
		System.out.println("MD5 done password   "+password+"-------------------");
		u.setPassword(password);
		int flag=0;
		try {
			System.out.println(u+"----------------------------");
		 
			String s =  UUID.randomUUID().toString();
			s=s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
			System.out.println(s +"  22222222222222");
			u.setUserId(s );
			flag=userInfoService.save(u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag=0;
		}
		if(flag!=1){
			msg="添加失败";
			statusCode="501";
		}
		}else{
			msg="电话号码已注册";
			statusCode="502";
		}
		}else{
			statusCode="503";
			msg="用户名或密码不能为空";
		}
		status.put("status",statusCode);
		status.put("msg", msg);
			return JSON.toJSONString(status);
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
		phone=phone.trim();
		password=password.trim();
		System.out.println("come into login $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		password=getEncrty(phone+","+password);
		
		UserInfo u = userInfoService.selectByPhoneNum(phone);

		String msg="";
		String statusCode="200";
		if(u==null){
			msg="用户不存在";
			statusCode="504";
		}else if(u.getPassword().trim().equals(password.trim())){
			String auth_id=phone+"="+getEncrty(u.getPhoneNum());
			model.addAttribute("auth_id", auth_id);
			model.addAttribute("userID",u.getUserId());
			u.setPassword("");
			u.setIdeaText("");
			status.put("items", u);
			HttpSession session = request.getSession();
			String sessionId=session.getId();
			Cookie cookie=new Cookie("JSESSION", sessionId);
			response.addCookie(cookie);
		}else{
			msg="密码错误";
			statusCode="505";
		}
			status.put("status", statusCode);
			if(msg!=null&&!"".equals(msg))
			status.put("msg", msg);
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
	public Map<String, Object>  forgetPwd(@RequestParam(value="phoneNum")String phoneNum,
			@RequestParam(value="verifyCode")String verifyCode,
			@RequestParam(value="password")String password){
		System.out.println("come into forgetPwd $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("phoneNum="+phoneNum+",verifyCode="+verifyCode+",password="+password);
		String msg=null;
		String statusCode="200";
		Map<String, Object> map=new HashMap<>();
		map.put("status", statusCode);
		if(msg!=null&&"".equals(msg))
		map.put("msg", msg);
		return map;
	
	}
	/**
	 * 忘记密码的验证码
	 * @param phoneNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getforgetPwdVerify",method=RequestMethod.PUT,
			produces = {"application/json;charset=UTF-8"})
	public Map<String, Object> getForgetPwdVerify(@RequestParam(value="phoneNum",required=true)String phoneNum){
		String statusCode="200";
		String msg=null;
		System.out.println("come into getForgetPwdVerify $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("phoneNum="+phoneNum);
		Map<String, Object> map=new HashMap<>();
		map.put("status", statusCode);
		if(msg!=null&&"".equals(msg))
		map.put("msg", msg);
		return map;
	}
	
	 
	/**
	 * 更新极光id
	 * @param userId
	 * @param JpushID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateJPushID",method=RequestMethod.PUT,
			produces = {"application/json;charset=UTF-8"})
	public Map<String , Object> updateJpushID(@RequestParam("userID")String userId,
			@RequestParam(value="JpushID")String JpushID,@RequestParam(value="phoneType")String type,
			@RequestParam(value="JpushType")String JpushType){
		String msg=null;
		String statusCode="200";
 		     
 		 HttpSession session = request.getSession();
 		 System.out.println(session.getId()+" ididididididididid");
 		 if(session.getAttribute("type")!=null )
 			System.out.println(" $$$$$$$$$$$      "+type+"   ￥￥￥￥￥￥￥￥￥￥￥￥￥");
		if(!"".equals(userId)&&!"".equals(JpushID)&&!"".equals(type)&&!"".equals(JpushType)){
			userId=userId.trim();
			JpushID =JpushID.trim();
	 	session.setAttribute("phoneType", type);
	 	  session = request.getSession();
		String sessionId=session.getId();
		Cookie cookie=new Cookie("JSESSION", sessionId);
		response.addCookie(cookie);
		System.out.println("come into updateJpushID $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println(userId+"="+JpushID);
		msg=userInfoService.updateJpushId(userId, JpushID,JpushType);
		String []subStr=msg.split("=");
		if(subStr[1]!=null){
			msg=subStr[1];
			
		}
		statusCode=subStr[0];
		}else{
			msg="用户名或极光ID不能为空";
			statusCode="503";
		}
		Map<String, Object> map=new HashMap<>();
		map.put("status", statusCode);
		if(msg!=null&&"".equals(msg))
		map.put("msg", msg);
		return map;
	}
	/**
	 * 更新本机推送开关
	 * @param userId
	 * @param status
	 * @return
	 */
	@ResponseBody()
	@RequestMapping(value="/updateJPushStatus",method=RequestMethod.POST,
			produces = {"application/json;charset=UTF-8"})
	public Map<String, Object> updateJpushStatus(@RequestParam(value="userID")String userId,
			@RequestParam(value="JpushStatus")String jpushStatus){
		String msg=null;
		String statusCode="200";
		if(!"".equals(userId)&&!"".equals(jpushStatus)){
			userId =userId.trim();
			jpushStatus=jpushStatus.trim();
			if(jpushStatus.equals("1")||jpushStatus.equals("0")){
			System.out.println("come into updateJpushStatus $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		String flag=userInfoService.updateJpushStatus(userId, jpushStatus);
		String [] subStr=flag.split("=");
		if(subStr[1]!=null){
			msg=subStr[1];
		}
		statusCode=subStr[0];
		System.out.println(userId+"="+status);
			}else{
				msg="开关值只能为1或0";
				statusCode="507";
			}
		}else{
			msg="id不能为空或开关为空";
			statusCode="503";
		}
		Map<String, Object> map=new HashMap<>();
		map.put("status", statusCode);
		System.out.println("msg ="+msg);
		if(msg!=null&&"".equals(msg))
		map.put("msg", msg);
		return map;
	}
	public boolean checkEmail(String email){
		boolean flag=true;
		int i=email.indexOf("@");
		int j=email.lastIndexOf(".");
		if(i==0&&i>=(email.length())){
			flag=false;
		}else{
			if(j>email.length()){
				flag=false;
			}else{
				if(i>j)
					flag=false;
			}
		}
		return flag;
	}
	 
	/**
	 * 绑定邮箱   
	 * @param userId
	 * @param email
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/bindingEmail",method=RequestMethod.PUT,produces = {"application/json;charset=UTF-8"})
	public Map<String, Object> bindingEmail(@RequestParam(value="userID")String userId,@RequestParam(value="email")String email){
		String statusCode="200";
		String msg=null;
		userId=userId.trim();
		email=email.trim();
		if(checkEmail(email)){
		System.out.println("come into updateJpushStatus bindingEmail $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("userID="+userId+",email="+email);
		String flag=userInfoService.bindingEmail(userId, email);
		String [] subStr=flag.split("=");
		if(subStr[1]!=null){
			msg=subStr[1];
		}
		statusCode=subStr[0];
		}else{
			statusCode="507";
			msg="邮箱格式错误";
		}
		Map<String, Object> map=new HashMap<>();
		map.put("status", statusCode);
		if(msg!=null&&"".equals(msg))
		map.put("msg", msg);
		return map;
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
	public Map<String, Object> changePwd(@RequestParam(value="userID")String userId,
			@RequestParam(value="oldPwd")String oldPassword,
			@RequestParam(value="newPwd")String newPassword){
		System.out.println("come into  changePwd $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		String statusCode="200";
		String msg=null;
		if(!"".equals(userId)&&!"".equals(newPassword)&&!"".equals(oldPassword)){
		userId =userId.trim();
		oldPassword=oldPassword.trim();
		newPassword=newPassword.trim();
		UserInfo u=userInfoService.get(userId);
		if(u==null){
			msg="用户不存在";
			statusCode="504";
		}else{
			System.out.println(u);
			String oldP=getEncrty(u.getPhoneNum()+","+oldPassword);
			if(u.getPassword().equals(oldP.trim())){
			String newP=getEncrty(u.getPhoneNum()+","+newPassword);
			u.setPassword(newP);
			String flag=userInfoService.changePwd(u);
			if(!flag.equals("200")){
				msg=flag;
				statusCode="506";
			}
			}else{
				statusCode="505";
				msg="密码错误";
			}
		}
		System.out.println(userId+"="+oldPassword+"="+newPassword);
		}else{
			statusCode="503";
			if("".equals(userId)){
			msg="用户名为空";
			}else if("".equals(oldPassword)){
			msg="旧为空";
			}else{
			msg="新为空";
			}
		}
		Map<String, Object> map=new HashMap<>();
		map.put("status", statusCode);
		if(msg!=null&&"".equals(msg))
		map.put("msg", msg);
		return map;
		
	}
	
	/**
	 * 意见反馈
	 * @param userId
	 * @param ideaText
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/feedback",method=RequestMethod.PUT,produces = {"application/json;charset=UTF-8"})
	public Map<String, Object> feedBack(@RequestParam(value="userID",required=true)String userId,
			@RequestParam(value="ideaText",required=false)String ideaText){
		System.out.println("come into feedback $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		String statusCode="200";
		String msg=null;
		if(!"".equals(userId)&&!"".equals(ideaText)){
		String flag=userInfoService.feedBack(userId, ideaText);
		String [] subStr=flag.split("=");
		if(subStr[1]!=null){
			msg=subStr[1];
		}
		statusCode=subStr[0];
		System.out.println(userId+"="+ideaText);
		}else{
			statusCode="503";
			if("".equals(userId))
					msg="用户ID为空";
			else msg="反馈内容为空";
		}
		Map<String, Object> map=new HashMap<>();
		map.put("status", statusCode);
		if(msg!=null&&!"".equals(msg))
		map.put("msg", msg);
		return map;
	}
	@ResponseBody 
	@RequestMapping(value="/uploadPicture")
	public Map<String, Object> updatePicture(@RequestParam("file")MultipartFile file,@RequestParam("userID")String userId){
		Map<String, Object> map=new HashMap<>();
		String msg=null;
		String statusCode="200";
		if(!"".equals(userId)&&file!=null){
				String fileName=file.getOriginalFilename();
				String format=fileName.substring(fileName.lastIndexOf("."));
				 
				userId=userId.trim();
				UserInfo user = userInfoService.get(userId);
				if(user!=null){
				String path=request.getSession().getServletContext().getRealPath("/");
				System.out.println(path);
				String savepicture=path+"picture";
				File directory=new File(savepicture);
				if(directory.exists()==false){
					directory.mkdirs();
				}
			
//				InputStream is=file.getInputStream();
// 				OutputStream out=FileUtils.openOutputStream(new File(savepicture++"/"+user.));
				}else{
					msg="用户不存在";
					statusCode="504";
				}
				
		}else{
			msg="输入不能为空";
			statusCode="503";
		}
		map.put("status", statusCode);
		if(msg!=null&&!"".equals(msg))
			map.put("msg", msg);
		return map;
	}
}
