package com.yetthin.web.serviceImp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yetthin.web.domain.TempCode;
import com.yetthin.web.persistence.TempCodeMapper;
import com.yetthin.web.service.TempCodeService;

@Service("TempCodeSerivce")
public class TempCodeServiceImp implements TempCodeService{
	
	@Resource 
	private TempCodeMapper tempCodeMapper;
	@Override
	public TempCode get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int save(TempCode entity) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String id) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<TempCode> getListAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countByExample() {
		// TODO Auto-generated method stub
		return 0;
	}

 

}
