package com.yetthin.web.serviceImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yetthin.web.domain.PhoneVersion;
import com.yetthin.web.domain.UserInfo;
import com.yetthin.web.persistence.UserInfoMapper;
import com.yetthin.web.service.UserInfoService;

@Service("UserInfoService")
public class UserInfoServiceImp extends BaseService implements UserInfoService{

	@Autowired
	private UserInfoMapper userInfoMapper;
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_UNCOMMITTED)
	@Override
	public UserInfo get(String id) {
		// TODO Auto-generated method stub
		int userId=Integer.parseInt(id);
		return userInfoMapper.selectByPrimaryKey(userId);
	}
	 
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED)
	@Override
	public int save(UserInfo entity) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(entity+"===========================");
		return userInfoMapper.insert(entity);
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW,isolation=Isolation.READ_COMMITTED)
	@Override
	public int delete(String id) throws Exception {
		// TODO Auto-generated method stub
		return userInfoMapper.deleteByPrimaryKey(Integer.parseInt(id));
	}
	
 
	@Override
	public String getRegisterVerify(String phoneNum) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String forgetPwd(String phoneNum, String verifyCode, String password) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getforgetPwdVerify(String phoneNum) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String updateJpushId(String userId, String JpushID) {
		// TODO Auto-generated method stub
		String msg=null;
		String statusCode="200";
		UserInfo ui=userInfoMapper.selectByPrimaryKey(Integer.parseInt(userId));
		if(ui==null){
			msg="用户不存在";
			statusCode="504";
			
		}else{
			ui.setJpushId(JpushID);
			int i=userInfoMapper.updateByPrimaryKeySelective(ui);
			if(i==0){
				msg="更新失败";
				statusCode="506";
			}
		}
		return statusCode+"="+msg;
	}
	@Override
	public String updateJpushStatus(String userId, String status) {
		// TODO Auto-generated method stub
		String statusCode="200";
		String msg=null;
		UserInfo ui=userInfoMapper.selectByPrimaryKey(Integer.parseInt(userId));
		if(ui==null){
			msg="用户不存在";
			statusCode="504";
		}else{
		
			ui.setStatus(Integer.parseInt(status));
			int i=userInfoMapper.updateByPrimaryKeySelective(ui);
			if(i==0){
				statusCode="506";
				msg="更新失败";
			}
		}
		return statusCode+"="+msg;
		 
	}
	@Override
	public String bindingEmail(String userID, String email) {
		// TODO Auto-generated method stub
		String statusCode="200";
		String msg=null;
		UserInfo ui=userInfoMapper.selectByPrimaryKey(Integer.parseInt(userID));
		if(ui==null){
			msg="用户不存在";
			statusCode="504";
		}else{
		
			ui.setEmail(email);
			int i=userInfoMapper.updateByPrimaryKeySelective(ui);
			if(i==0){
				msg="更新失败";
				statusCode="506";
			}
		}
		return statusCode+"="+msg;
	}
	@Override
	public String changePwd(UserInfo u) {
		// TODO Auto-generated method stub
		int i=userInfoMapper.updateByPrimaryKey(u);
		return i==0?"更新失败":"200";
	}
	@Override
	public PhoneVersion checkNewVersion() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String feedBack(String userId, String ideaText) {
		// TODO Auto-generated method stub
		String statusCode="200";
		String msg=null;
		UserInfo ui=userInfoMapper.selectByPrimaryKey(Integer.parseInt(userId));
		if(ui==null){
			msg="用户不存在";
			statusCode="504";
		}else{
		
			ui.setIdeaText(ideaText);
			int i=userInfoMapper.updateByPrimaryKeySelective(ui);
			if(i==0){
				msg="更新失败";
				statusCode="506";
			}
		}
		return statusCode+"="+msg;
		 
	}
	@Override
	public List<UserInfo> getListAll() {
		// TODO Auto-generated method stub
		return userInfoMapper.getAllUser();
	}
	@Override
	public int countByExample() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public UserInfo selectByPhoneNum(String phoneNum) {
		// TODO Auto-generated method stub
		return userInfoMapper.selectByPhoneNum(phoneNum);
	}
	@Override
	public UserInfo selectByEmail(String email) {
		// TODO Auto-generated method stub
		return userInfoMapper.selectByEmail(email);
	}
	@Override
	public List<UserInfo> lookIdeaText() {
		// TODO Auto-generated method stub
		return userInfoMapper.lookIdeaText();
	}
	@Override
	public UserInfo getByPhoneAndPassword(String phone, String password) {
		// TODO Auto-generated method stub
		Map<String, String> map=new HashMap<>();
		map.put("phone", phone);
		map.put("password", password);
		UserInfo ui=userInfoMapper.selectByPhoneNumAndPassWord(map);
		return null;
	}
	

}
