package zcom.yetthin.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yetthin.web.service.IndexPageContextService;

@Controller
public class IndexPageContext {
	
	@Resource
	private IndexPageContextService IndexPageContextService;
	@ResponseBody
	@RequestMapping(value="/incomeRecommendList",method=RequestMethod.POST,
	produces={"application/json;charset=utf-8"})
	public String incomeRecommendList(
			@RequestParam(value="IncomeType")int type,
			@RequestParam(value="pageNum")int pageNum,
			@RequestParam(value="pageSize")int pageSize){
		String json =null;
		json =IndexPageContextService.getIncomeRecommendList(type, pageNum, pageSize);
		
		return json;
	}
	@ResponseBody
	@RequestMapping(value="/bestIncomeList",method=RequestMethod.POST,
	produces={"application/json;charset=utf-8"})
	public String bestIncomeList(
			@RequestParam(value="pageNum")int pageNum,
			@RequestParam(value="pageSize")int pageSize){
		String json =null;
		json =IndexPageContextService.getBestIncomeList(pageNum,pageSize);
		return json;
	}
}
