package com.yetthin.web.persistence;

import java.util.List;

public interface BaseMapper <IdEntity>{
	
	public abstract int countById(Integer id);

	 
	public abstract int deleteByPrimaryKey(Integer id);

	public abstract int insert(IdEntity record);

	public abstract int insertSelective(IdEntity record);

	public abstract IdEntity selectByPrimaryKey(Integer id);

	public abstract int updateByPrimaryKeySelective(IdEntity record);

	public abstract int updateByPrimaryKey(IdEntity record);
	
}
