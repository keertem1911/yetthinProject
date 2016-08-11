package com.yetthin.web.controller;

 
 

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alibaba.fastjson.JSON;
import com.yetthin.web.common.JtdoaUtil;
import com.yetthin.web.common.RedisOfReader;
import com.yetthin.web.test.ReadTextSymbol;

import util.Contract;
import util.JTdoa;
import util.Level1Value;
import util.Level2Value;
/**
 * 行情业务
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/jtdoa")
public class JTdoaController extends BaseController{
	
	 
	private static  JTdoa jtdoa =null;
	
	private Executor executor=Executors.newFixedThreadPool(200);
	 
	@ResponseBody
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public   void init(){
		jtdoa= JtdoaUtil.getInstance();
		System.out.println("come into --------------------");
		executor.execute(  new Runnable() {
			public void run() {
				ReadTextSymbol test=new ReadTextSymbol();
		String path=request.getSession().getServletContext().getRealPath("/WEB-INF/classes"); 
		System.out.println(path);
		List<Contract> list=test.readTextByContract(path+"/symbol.txt");
		
		for (int i = 0; i < list.size(); i++) {
			jtdoa.TDOASubscibeLevel1Data(i, list.get(i));
			jtdoa.TDOASubscribeMarketDepth(i, list.get(i), 10);
		}	
		 RedisOfReader.initReadInredisKeyLevel1(list);
			}
		});
		
	}
	@ResponseBody
	@RequestMapping(value="/getLevel1",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	public String getLevel1(@RequestParam(value="exchange")String exchange){
		List<Level1Value> list=RedisOfReader.getLevel1(exchange);
		String msg="";
		String statusCode="200";
		String item="";
		if(list==null||list.size()==0){
			msg="市场不存在";
			statusCode="507";
			item="\"\"";
		}else{
			item=JSON.toJSONString(list);
		}
		return "{status:\""+statusCode+"\",msg:\""+msg+"\",item:"+item+"}";
	}
	@ResponseBody
	@RequestMapping(value="/getLevel2",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	public String getLevel2(@RequestParam(value="symbol")String symbol){
		List<Level2Value> list=RedisOfReader.getL2Value(symbol);
		String msg="";
		String statusCode="200";
		String item="";
		if(list==null||list.size()==0){
			msg="股票不存在";
			statusCode="507";
			item="\"\"";
		}else{
			item=JSON.toJSONString(list);
		}
		return "{status:\""+statusCode+"\",msg:\""+msg+"\",item:"+item+"}";

	}
	 
}
