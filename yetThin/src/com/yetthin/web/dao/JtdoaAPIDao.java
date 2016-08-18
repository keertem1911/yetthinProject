package com.yetthin.web.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.sun.xml.internal.fastinfoset.util.ValueArray;
import com.yetthin.web.commit.JtdoaValueMarket;
import com.yetthin.web.commit.QQMarketLevelUtilByMaster;
import com.yetthin.web.commit.QQMarketLevelUtilBySimple;
import com.yetthin.web.commit.RedisUtil;
import com.yetthin.web.commit.SinaMarketIndex;
import com.yetthin.web.commit.ValueFormatUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import sun.awt.SunHints.Value;

@Repository("JtdoaAPIDao")
public class JtdoaAPIDao implements 
	QQMarketLevelUtilByMaster,QQMarketLevelUtilBySimple,SinaMarketIndex,ValueFormatUtil,JtdoaValueMarket{
	
	private static JedisPool jedispool=RedisUtil.getInstanceMsater();
	 
	
	private  String joinStringSplit(String [] array,String sp){
		StringBuffer buffer=new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			buffer.append(array[i]);
			if(i<array.length-1)
				buffer.append(sp);
		}
		 
		return buffer.toString();
	}
	public void saveQQ_M_REQUEST_URL(List<String> values) {
		
		Jedis jedis=jedispool.getResource();
		// TODO Auto-generated method stub
		jedis.select(0);
		for (int i = 0; i < values.size(); i++) {
			//请求的信息 
			if(values.get(i)!=null&&!"".equals(values.get(i))){
			String []  subValue = values.get(i).split("&")[0].split(QQ_M_SPLIT_STR);
			
			String symbol=values.get(i).split("&")[1];
			//股票代码
			String redisValue=jedis.get(symbol.substring(2)+"."+symbol.substring(0, 2).toUpperCase());
		 try{
			// 保存的值
			String [] subStr= redisValue.split(SPLIT_STR);
			//更新操作
			subStr[DATE_INDEX]=subValue[QQ_M_UP_DOWN_TIME];
			subStr[PRE_CLOSE_INDEX]=subValue[QQ_M_YST_CLOSE];// 前收盘价
			subStr[OPEN_INDEX]=subValue[QQ_M_OPEN_PRICE];// 开盘价
			subStr[HEIGHT_INDEX]=subValue[QQ_M_HEIGHT_PRICE];// 最高价
			subStr[LOW_INDEX]=subValue[QQ_M_LOW_PRICE];//最低价
			subStr[LAST_PRICE_INDEX]=subValue[QQ_M_LAST_PRICE];// 最新价
			subStr[VOLUME_INDEX]=subValue[QQ_M_VOLUME];// 成交量
			subStr[TOTLE_SUM_INDEX]=subValue[QQ_M_TOTLE_SUM];// 成交额
			subStr[LIMIT_UP_INDEX]=subValue[QQ_M_LIMIT_UP];// 涨停价
			subStr[LIMIT_DOWN_INDEX]=subValue[QQ_M_LIMIT_DOWN];//跌停价
			subStr[EXCHANGE_RATE]=subValue[QQ_M_EXCHANGE]; //换手率
			subStr[UP_DOWN_PRICE]= subValue[QQ_M_UP_DOWN];
			subStr[UP_DOWN_PRICE_RATE] = subValue[QQ_M_UP_DOWN_RATE];
			for (int j = 0,k=0; j < 10;k++,j+=2) {
				// 15 14 13 12 11
				// 25 24 23 22 21
				subStr[LEVEL2_INDEX_SIDE1+4-k]=subValue[QQ_M_DEPTH_SIDE1+j];// 卖方 卖5
				subStr[LEVEL2_INDEX_SIDE1+10+4-k]=subValue[QQ_M_DEPTH_SIDE1 +j+1];// 卖方 卖5
				// 16 17 18 19 20
				// 26 27 28 29 30 
				subStr[LEVEL2_INDEX_SIDE0+k]=subValue[QQ_M_DEPTH_SIDE0+j];// 买方  买1
				subStr[LEVEL2_INDEX_SIDE0+10+k]=subValue[QQ_M_DEPTH_SIDE0+j+1];// 买方  买1
				
			}
			redisValue=joinStringSplit(subStr, SPLIT_STR);
			jedis.set(symbol.substring(2)+"."+symbol.substring(0, 2).toUpperCase(), redisValue);
			saveSortBalance(symbol.substring(2),symbol.substring(0, 2).toUpperCase(), subValue);
		 }catch (Exception e) {
			 System.out.println("-----------errorrrr          redisValue = "+ redisValue);
			 System.out.println("-----------errorrrr          redisValue = "+ symbol.substring(2)+"."+symbol.substring(0, 2).toUpperCase());
			 System.out.println("-----------errorrrr          redisValue = "+ redisValue);
			 System.out.println("-----------errorrrr          redisValue = "+ redisValue);
			 System.out.println("-----------errorrrr          redisValue = "+ redisValue);
			 System.out.println("-----------errorrrr          redisValue = "+ redisValue);
			 // TODO: handle exception
		 }
		}
		}
		RedisUtil.RealseJedis_M(jedis);
	}
	/**
	 * 涨幅 及 跌幅排序
	 * @param symbol
	 * @param exchange
	 * @param L1Value
	 */
	private void saveSortBalance(String symbol,String exchange,String [] subStr){
		Jedis jedis=jedispool.getResource();
		jedis.select(1);
		
		 
		double desc_reate=Double.parseDouble(subStr[QQ_M_UP_DOWN_RATE]);
		
		int marketName=0;
		if("SZ".equals(exchange.toUpperCase().trim())||"SH".equals(exchange.toUpperCase().trim())){
			marketName=HU_SHEN;
		}
		//涨跌幅
		if(desc_reate>0)
			jedis.zadd(MARKET[marketName][0]+":"+MARKET[marketName][1],desc_reate,symbol+"."+exchange.toUpperCase()+":"+subStr[QQ_M_NAME]+":"+subStr[QQ_M_LAST_PRICE]+":"+subStr[QQ_M_UP_DOWN]+":"+subStr[QQ_M_UP_DOWN_TIME]);
		else
			jedis.zadd(MARKET[marketName][0]+":"+MARKET[marketName][2],desc_reate,symbol+"."+exchange.toUpperCase()+":"+subStr[QQ_M_NAME]+":"+subStr[QQ_M_LAST_PRICE]+":"+subStr[QQ_M_UP_DOWN]+":"+subStr[QQ_M_UP_DOWN_TIME]);
		//换手率
		jedis.zadd(MARKET[marketName][0]+":"+MARKET[marketName][3], Double.parseDouble(subStr[QQ_M_EXCHANGE]),symbol+"."+exchange.toUpperCase()+":"+subStr[QQ_M_NAME]+":"+subStr[QQ_M_LAST_PRICE]+":"+subStr[QQ_M_UP_DOWN_TIME]);
		jedis.select(0);
		RedisUtil.RealseJedis_M(jedis);
	}
	public void saveSina_REQUEST_URL_INDEX(List<String> values) {
		// TODO Auto-generated method stub
		Jedis jedis=jedispool.getResource();
		jedis.select(1);
		for (int i = 0; i <values.size(); i++) {
			String sybmol = values.get(i).split("&")[1];
			sybmol=sybmol.substring(2)+"."+sybmol.substring(0, 2).toUpperCase();
			String  subValue =values.get(i).split("&")[0];
			jedis.set(sybmol, subValue); 
		}
		
		RedisUtil.RealseJedis_M(jedis);
		
	}
}
