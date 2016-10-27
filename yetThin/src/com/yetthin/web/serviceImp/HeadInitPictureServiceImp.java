package com.yetthin.web.serviceImp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yetthin.web.domain.HeadPicture;
import com.yetthin.web.persistence.HeadPictureMapper;
import com.yetthin.web.service.HeadInitPictureService;
@Service("HeadInitPictureService")
public class HeadInitPictureServiceImp implements HeadInitPictureService{
	
	@Resource
	private HeadPictureMapper headPictureMapper;
	@Override
	public HeadPicture get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int save(HeadPicture entity) throws Exception {
		// TODO Auto-generated method stub
		return headPictureMapper.saveOrUpdate(entity);
	}

	@Override
	public int delete(String id) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<HeadPicture> getListAll() {
		// TODO Auto-generated method stub
		return null;
	}

	private String toJson(List<HeadPicture> list){
		StringBuffer buffer =new StringBuffer();
		buffer.append("\"value\": ");
		buffer.append("[");
		for (int i = 0; i < list.size(); i++) {
			buffer.append("{");
			buffer.append("\"id\":\""+list.get(i).getId()+"\",");
			buffer.append("\"href\":\""+list.get(i).getHrefUrl()+"\"");
			buffer.append("}");
			if(i!=list.size()-1)
				buffer.append(",");
		}
		buffer.append("]");
		
		return buffer.toString();
	}
	@Override
	public String getPictureList() {
		System.out.println();
		List<HeadPicture> list1= headPictureMapper.getPictureList();
		String json =toJson(list1);
		// TODO Auto-generated method stub
		return json;
	}

	@Override
	public int countByExample() {
		// TODO Auto-generated method stub
		return 0;
	}

	 

}
