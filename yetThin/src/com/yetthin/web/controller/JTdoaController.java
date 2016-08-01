package com.yetthin.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import util.JTdoa;
/**
 * 行情业务
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/jtdoa")
public class JTdoaController extends BaseController{
	
	 
	private static  JTdoa jtdoa ;
	 
	private static ExecutorService cacheThreadPool= Executors.newCachedThreadPool();

//	static{
//			jtdoa= new JTdoa();
//			//connect server 
//			cacheThreadPool.execute(new Runnable() {
//				public void run() {
//					jtdoa.TDOACreate();
//					jtdoa.TDOAConnect("222.173.29.210", 7005);
//					while(!jtdoa.isConnected()){
//						try {
//							Thread.sleep(200);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						System.out.println("wait connect----------");
//					}
//					if(jtdoa.isConnected()){
//						System.out.println("login has ready ------------");
//						int ls=jtdoa.TDOALoginServer("td5", "td5",  "90-b1-1c-80-a4-78", 0, "1");
//						System.out.println("ls ls ="+ls);
//					}
//				 
//				while(!jtdoa.isLogined()){
//					try {
//						Thread.sleep(200);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					System.out.println("wait login ----------------");
//				}
//				if(jtdoa.isLogined()){
//					System.out.println("login has ready ===================");
//					jtdoa.TDOADestory();
//				}
//				}
//			});
//
//	}
	
	
}
