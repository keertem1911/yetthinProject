package com.yetthin.web.serviceImp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yetthin.web.domain.UserInfo;
import com.yetthin.web.persistence.UserInfoMapper;
import com.yetthin.web.service.UserInfoService;

@Service("UserInfoService")
public class UserInfoServiceImp implements UserInfoService{

	@Autowired
	private UserInfoMapper userInfoMapper;
	@Override
	public UserInfo get(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public UserInfo get(String phoneNum,String password){
		UserInfo u=new UserInfo();
		u.setPassword(password);
		u.setPhoneNum(phoneNum);
		return userInfoMapper.selectByPhoneNumAndPassWord(u);
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
		return 0;
	}
	
	@Override
	public List<UserInfo> getListByEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countByExample(UserInfo entity) {
		// TODO Auto-generated method stub
		return 0;
	}

}
