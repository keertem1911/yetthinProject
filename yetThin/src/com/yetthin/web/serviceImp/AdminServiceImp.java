package com.yetthin.web.serviceImp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yetthin.web.domain.Admin;
import com.yetthin.web.persistence.AdminMapper;
import com.yetthin.web.persistence.PhoneVersionMapper;
import com.yetthin.web.persistence.UserInfoMapper;
import com.yetthin.web.service.AdminService;

@Service("AdminService")
public class AdminServiceImp implements AdminService{
	
	@Resource
	AdminMapper adminMapper;
	 
	
	@Override
	public Admin get(String id) {
		// TODO Auto-generated method stub
		return adminMapper.selectByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public int save(Admin entity) throws Exception {
		// TODO Auto-generated method stub
		return adminMapper.insertSelective(entity);
	}

	@Override
	public int delete(String id) throws Exception {
		// TODO Auto-generated method stub
		return adminMapper.deleteByPrimaryKey(Integer.parseInt(id));
	}

	@Override
	public List<Admin> getListAll() {
		// TODO Auto-generated method stub
		return adminMapper.getAll();
	}

	@Override
	public int countByExample() {
		// TODO Auto-generated method stub
		return adminMapper.countById();
	}

}
