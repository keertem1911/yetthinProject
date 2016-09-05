package com.updateData.main;

import java.util.List;

import org.junit.Test;

import redis.clients.jedis.Jedis;

/**
 * 数据来源:
 * 1 JNI 接口调用 
 * 2 Yahoo 历史接口调用
 * 存储类型:	
 * 1 分钟K线(临时存储)
 * 2 日K线
 * 存储数据库 
 * 1. Redis(release)
 * 2. Mysql(debug)
 * 说明:
 * 模拟更新行业数据排名 及一分钟各行业指数计算 
 * 计算公式:
 * 当前指数价格  = ((当前成分股价格*成交量)/(一分钟之前的成分股价格*成交量))*(一分钟之前的指数价格)
 * @author keerte
 *
 */
public class update1 {
	@Test
	public void testReidsLarray(){
		Jedis jedis =new Jedis("localhost",6379);
		jedis.select(4);
		Long len = jedis.llen("31");
		if(len==0){
			System.out.println("empty");
		}else{
			  List<String> value = jedis.lrange("31", len-1, len-1);
			  System.out.println(value.get(0));
		}
		jedis.disconnect();
	}
}
