package com.yetthin.web.commit;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;

import java.util.Map.Entry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import util.Contract;
import util.Level1Value;
import util.Level2Value;

public class RedisOfReader {
	private static JedisPool pool=RedisUtil.getInstanceSlave();
	
	private static Jedis jedis=null;
	
	
	static{
		// TODO Auto-generated constructor stub
		jedis=pool.getResource();
	}
	/**
	 * 
	 */
	public static void initReadInredisKeyLevel1(List<Contract> list){
		
		jedis.select(0);
		for (Contract contract : list) {
			jedis.hset(contract.getExchange(), contract.getSymbol(), "");
		}
	}
	/**
	 * 
	 * @param symbol 股票代码
	 * @param side 买或卖 
	 * @param checkSum 层级
	 */
	// 模糊匹配 查询key
	public static void getL2BysideAndCheckSum(String symbol,int side,int checkSum) {
			
				jedis.select(1);
		
		 		String l2=jedis.hget(symbol,side+":"+checkSum);
				System.out.println(l2);
	}
	/**
	 * 返回指定 代码的 摆单 list 
	 * @param symbol
	 * @param exchange
	 * @return
	 */
	public static List<Level2Value> getL2Value(String symbol){
		List<Level2Value> list2=new LinkedList<>();
		jedis.select(1);
		String exchange=symbol.split(":")[1];
		Map<String, String> map=jedis.hgetAll(symbol);
		Set<Entry<String, String>>  sets=map.entrySet();
		Iterator<Entry<String, String>> it=sets.iterator();
		while(it.hasNext()){
			Entry<String, String> entry=it.next();
			String key=entry.getKey();
			String value=entry.getValue();
			if(Integer.parseInt(key.split(":")[1])<6){
			Level2Value level2Value=new Level2Value();
			level2Value.setChecksum(Integer.parseInt(key.split(":")[1]));
			level2Value.setSide(Integer.parseInt(key.split(":")[0]));
			level2Value.setSymbol(symbol);
			level2Value.setExchange(exchange);
			level2Value.setPrice(Double.parseDouble(value.split(":")[0]));
			level2Value.setSize(Integer.parseInt(value.split(":")[1]));
			level2Value.setExchange(value.split(":")[2]);
			level2Value.setCurrency(value.split(":")[3]);
		 
			list2.add(level2Value);
			}
		}
		 
		return list2;
	}
	public static List<Level2Value> getL2Value(String symbol,String exchange){
		return getL2Value(symbol+":"+exchange);
		
	}
	/**
	 *  根据市场代码解析 
	 * @param exchange  市场代码
	 * @return 返回本市场的所有股票
	 */
	// 解析 l1  
	public static List<Level1Value> getLevel1(String exchange){
			jedis.select(0);
			Map<String , String> map=jedis.hgetAll(exchange);
			List<Level1Value> listsL1= new LinkedList<>();
			Set<Entry<String, String>> entry = map.entrySet();
			Iterator<Entry<String, String>> it = entry.iterator();
			Class clazz=Level1Value.class;
			while(it.hasNext()){
				Level1Value con=new Level1Value();
				Entry<String, String> obj = it.next();
				String sybmol =obj.getKey();
				String fieds= obj.getValue();
				if(!fieds.equals("")){
				String [] param=fieds.split(",");
				for (int i = 0; i < param.length; i++) {
					String [] sub=param[i].split("=");
					String field =(sub[0].substring(0, 1)).toUpperCase()+sub[0].substring(1);
					try {
						Method method = clazz.getDeclaredMethod("set"+field, String.class);
						method.invoke(con, sub[1]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}
				con.setSymbol(sybmol);
				listsL1.add(con);
			}
			 Collections.sort(listsL1);
			return listsL1;
		}
	public static void main(String[] args) {
		List<Level2Value> list=getL2Value("002647", "SZ");
		System.out.println(JSON.toJSON(list));
		for (Level2Value level2Value : list) {
			System.out.println(level2Value);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
