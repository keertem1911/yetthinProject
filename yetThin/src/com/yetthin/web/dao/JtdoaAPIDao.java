package com.yetthin.web.dao;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.stereotype.Repository;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;
import com.yetthin.web.commit.JtdoaValueMarket;
import com.yetthin.web.commit.QQMarketLevelUtilByMaster;
import com.yetthin.web.commit.QQMarketLevelUtilBySimple;
import com.yetthin.web.commit.RedisUtil;
import com.yetthin.web.commit.SinaMarketIndex;
import com.yetthin.web.commit.ValueFormatUtil;

import freemarker.template.SimpleNumber;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
	public void saveQQ_M_REQUEST_URL(List<String> values,boolean index) {
		
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
			 if(redisValue!=null){
			String [] subStr= redisValue.split(SPLIT_STR);
			if(symbol.equals("603515")){
				System.out.println(subStr);
			}
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
			subStr[UP_DOWN_PRICE]= subValue[QQ_M_UP_DOWN]; // 涨跌价
			subStr[UP_DOWN_PRICE_RATE] = subValue[QQ_M_UP_DOWN_RATE];//涨跌率
			subStr[NAME]=subValue[QQ_M_NAME];
			if(subValue[QQ_M_LAST_DONE]!=null){
				subStr[LAST_DONE] = subValue[QQ_M_LAST_DONE].replace(":", "-");
			}else
				subStr[LAST_DONE]="0";
			subStr[PRICE_EARING_RATIO]= subValue[QQ_M_PRICE_EARING_RATIO];
			if(subValue[QQ_M_STOCK_AMPLITUPE]==null||"".equals(subValue[QQ_M_STOCK_AMPLITUPE].trim()))
				
				subStr[STOCK_AMPLITUPE]="0";
			else 
				subStr[STOCK_AMPLITUPE]=subValue[QQ_M_STOCK_AMPLITUPE];
			if(subValue[QQ_M_FAMC]==null&&"".equals(subValue[QQ_M_FAMC].trim()))
				subStr[FAMC]= "0";
			else
				subStr[FAMC]= subValue[QQ_M_FAMC];
			if(subValue[TOTLE_MAREKT_VALUE]==null||"".equals(subValue[TOTLE_MAREKT_VALUE].trim()))
				subStr[TOTLE_MAREKT_VALUE]="0";
			else
			subStr[TOTLE_MAREKT_VALUE]=subValue[TOTLE_MAREKT_VALUE];
			if(subValue[QQ_M_TOTLE_NET_WORTH]==null||"".equals(subValue[QQ_M_TOTLE_NET_WORTH].trim()))
				subStr[TOTLE_NET_WORTH]= "0";
			else
				subStr[TOTLE_NET_WORTH]= subValue[QQ_M_TOTLE_NET_WORTH];
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
			if(!index)
			saveSortBalance(symbol.substring(2),symbol.substring(0, 2).toUpperCase(), subValue);
			 }
		 }catch (Exception e) {
		 			 // TODO: handle exception
			 e.printStackTrace();
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
	 
			jedis.zadd(MARKET[marketName][0]+":"+MARKET[marketName][1],desc_reate,symbol+"."+exchange.toUpperCase());
		 
			 
		//换手率
		jedis.zadd(MARKET[marketName][0]+":"+MARKET[marketName][3], Double.parseDouble(subStr[QQ_M_EXCHANGE]),symbol+"."+exchange.toUpperCase());
//		Set<Tuple> keys = jedis.zrevrangeWithScores("0:0", 0, 10);
//		for (Tuple tuple : keys) {
//			System.out.println(tuple.getElement()+ " -------"+tuple.getScore());
//		}
//		System.out.println("-----------------------");
//		System.out.println("-----------------------");
//		System.out.println("-----------------------");
//		System.out.println("-----------------------");
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
	/**
	 * 获取行业数据
	 * @author keerte
	 * return Map  K ->行业代码  V -> 行业排序集合 及 Zset
	 */
 
	private Map<String, Set<String>> getStock(){
		Jedis jedis=jedispool.getResource();
		jedis.select(2);
		/**
		 * 获取行业的排序集合
		 */
		Set<String> names = jedis.keys("*z");
		Map<String, Set<String>> map=new HashMap<String,Set<String>>();
		for (String string : names) {
			
			Set<String> str = jedis.zrange(string, 0, -1);
			map.put(string.substring(0,string.length()-1), str);
		}
		Set<Entry<String, Set<String>>> entry = map.entrySet();
//		Iterator<Entry<String, Set<String>>> it =entry.iterator();
//		while(it.hasNext()){
//			Entry<String, Set<String>> entry1 = it.next();
//			System.out.println(jedis.get(entry1.getKey()+"n")+" "+Arrays.asList(entry1.getValue()));
//		}
		RedisUtil.RealseJedis_M(jedis);
		return map;
	}
	private Map<String, String> getAllSymbolValueList(){
		Jedis jedis=jedispool.getResource();
		jedis.select(0);
		Map<String, String> map =new HashMap<String,String>();
		Set<String> symbols = jedis.keys("*");
		for (String string : symbols) {
			String []arraysSymbols=jedis.get(string).split(SPLIT_STR);
			map.put(string, arraysSymbols[LAST_PRICE_INDEX]+SPLIT_STR+arraysSymbols[VOLUME_INDEX]+SPLIT_STR+arraysSymbols[UP_DOWN_PRICE]+SPLIT_STR+arraysSymbols[UP_DOWN_PRICE_RATE]);
			
		}
		RedisUtil.RealseJedis_M(jedis);
		return map;
	}
	private static final DecimalFormat df=new DecimalFormat("#.00");
	private static final SimpleDateFormat dateFormat=new SimpleDateFormat("HH-mm");
	public void updateMinuteKandIndex() {
		// TODO Auto-generated method stub
		Map<String, String> mapValue=getAllSymbolValueList();
		Jedis jedis=jedispool.getResource();
		jedis.select(4);
		Map<String, Set<String>> map = getStock();
		Set<Entry<String, Set<String>>> set = map.entrySet();
		Iterator<Entry<String, Set<String>>> it= set.iterator();
		Map<String, Double> Indexvalue= new HashMap<String,Double>();
		Set<String> sset = mapValue.keySet();
//		for (String string : sset) {
//			System.out.println(string+" =>"+mapValue.get(string));
//		}
		while(it.hasNext()){
			Entry<String, Set<String>> entrys = it.next();
			String name =entrys.getKey();
			double sum = 0;
			Set<String> value = entrys.getValue();
			for (String string : value) {
				String va=mapValue.get(string);
//				System.out.println(string);
				if(va!=null&&!"".equals(va.trim())){
				jedis.select(2);
				jedis.zadd(name, Double.parseDouble(va.split(SPLIT_STR)[3]), string);
				sum+=(Double.parseDouble(mapValue.get(string).split(SPLIT_STR)[0])*Double.parseDouble(mapValue.get(string).split(SPLIT_STR)[1]));
				jedis.select(4);
				}
				}
			Long length =jedis.llen(name+".val");
			double lastIndex=1000;
			System.out.println("sum ="+sum);
			if(length!=0){
				double preIndex=Double.parseDouble(jedis.lrange(name+".val", length-1, length-1).get(0).split(SPLIT_STR)[2]);
				double preVal = Double.parseDouble(jedis.lrange(name+".val", length-1, length-1).get(0).split(SPLIT_STR)[1]);
				 if(sum!=0)
					   lastIndex=(sum/preVal)*preIndex;
			}else{
				lastIndex=1000;
			}
			if(sum!=0&&lastIndex!=0){
				jedis.lpush(name+".val",dateFormat.format(System.currentTimeMillis())+SPLIT_STR+df.format(sum)+SPLIT_STR+df.format(lastIndex));
//				System.out.println(name+".val"+ "  --------  "+sum+SPLIT_STR+lastIndex);
			}
		}
		
		
		RedisUtil.RealseJedis_M(jedis);
	}
}
