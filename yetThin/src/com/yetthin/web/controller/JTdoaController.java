package com.yetthin.web.controller;

 
 

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.yetthin.web.common.JtdoaUtil;

import util.Contract;
import util.JTdoa;
/**
 * 行情业务
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/jtdoa")
public class JTdoaController extends BaseController{
	
	 
	private static  JTdoa jtdoa =null;
	 
	@ResponseBody
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(){
		jtdoa= JtdoaUtil.getInstance();
		//connect server 
//		cacheThreadPool.execute(new Runnable() {
//			public void run() {
				jtdoa.TDOACreate();
				jtdoa.TDOAConnect("222.173.29.210", 7005);
				while(!jtdoa.isConnected()){
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("wait connect----------");
				}
				if(jtdoa.isConnected()){
					System.out.println("login has ready ------------");
					int ls=jtdoa.TDOALoginServer("td5", "td5",  "90-b1-1c-80-a4-78", 0, "1");
					System.out.println("ls ls ="+ls);
				}
			 
			while(!jtdoa.isLogined()){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("wait login ----------------");
			}
			if(jtdoa.isLogined()){
				System.out.println("login has ready ===================");
			//	jtdoa.TDOADestory();
			}
			return "a";
	}
	
	@ResponseBody
	@RequestMapping(value="/getL1",method=RequestMethod.POST)
	public String commit(){
		System.out.println("come into --------------------");
		Contract contract=new Contract();
		contract.symbol="000002";
		contract.currency="CNY"; 
		contract.exchange="SZ"; 
		contract.secType="STK";
		System.out.println("hello");
		jtdoa.TDOASubscribeMarketDepth(4, contract,10);
		int i=jtdoa.TDOASubscibeLevel1Data(5, contract);
		return i+"";
	}
	
}
