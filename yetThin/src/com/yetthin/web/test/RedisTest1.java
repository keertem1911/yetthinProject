package com.yetthin.web.test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import com.yetthin.web.commit.JtdoaValueMarket;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import util.Contract;
import util.Level1Value;

/**
 * find l2 side checksum ...
 * @author Administrator
 *
 */
public class RedisTest1 implements JtdoaValueMarket{
	
	private static Jedis jedis=new Jedis("127.0.0.1",6379);
	
	// 模糊匹配 查询key
	public  void getL2BysideAndCheckSum(String symbol,int side,int checkSum) {
		
			jedis.select(1);
	
	 		String l2=jedis.hget(symbol,side+":"+checkSum);
			System.out.println(l2);
	}
	// 解析 l1
	public void expireL1(String exchange){
		jedis.select(0);
		Map<String , String> map=jedis.hgetAll(exchange);
		List<Level1Value> contracts= new ArrayList<>();
		Set<Entry<String, String>> entry = map.entrySet();
		Iterator<Entry<String, String>> it = entry.iterator();
		Class clazz=Level1Value.class;
		while(it.hasNext()){
			Level1Value con=new Level1Value();
			Entry<String, String> obj = it.next();
			String sybmol =obj.getKey();
			String fieds= obj.getValue();
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
			con.setSymbol(sybmol);
			contracts.add(con);
		}
		System.out.println(Arrays.asList(contracts));
		
		
	}
	public  void main1() {
		RedisTest1 test = new RedisTest1();
		ReadTextSymbol sy=new ReadTextSymbol();
		List<String> lists=sy.readSymolByString("src/symbol.txt");
		for (int i = 0; i < lists.size(); i++) {
			System.out.println(lists.get(i));
			test.getL2BysideAndCheckSum(lists.get(i), 1, 2);
		}
	 //	test.expireL1("SZ");
	}
	public static void main(String[] args) {
		
		jedis.select(1);
			 for (int i = 0; i < MARKET.length; i++) {
				for (int j = 1; j < MARKET[i].length; j++) {
					
					 String string =MARKET[i][0]+":"+MARKET[i][j];
					 Set<Tuple> range=jedis.zrevrangeWithScores(string, 0, 5);

					 if(j==2)
						 range=jedis.zrangeWithScores(string, 0, 5); 
					 
						for (Tuple tuple : range) {
							System.out.println(tuple.getElement()+", "+tuple.getScore());
						}
						
				}
				 
		}
	}
	
	
}
