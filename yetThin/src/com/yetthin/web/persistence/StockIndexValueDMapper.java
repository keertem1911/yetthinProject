package com.yetthin.web.persistence;

import java.util.List;
import java.util.Map;

import com.yetthin.web.domain.BarDataNS;

 

 	

public interface StockIndexValueDMapper {

	List<BarDataNS> selectByPrimaryKeyBetween(Map<String, Integer> map);

	List<BarDataNS> insert(List<String> ids);
		
	int insertSelective(List<BarDataNS> barMlist);

}
