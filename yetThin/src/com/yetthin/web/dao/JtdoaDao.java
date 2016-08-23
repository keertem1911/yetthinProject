package com.yetthin.web.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import com.yetthin.web.commit.JtdoaValueMarket;
import com.yetthin.web.commit.QQMarketLevelUtilByMaster;
import com.yetthin.web.commit.RedisUtil;
import com.yetthin.web.commit.SinaMarketIndex;
import com.yetthin.web.commit.ValueFormatUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;
import util.BarData;
import util.Contract;
import util.JHdboa;
import util.TickData;
import util.TickSort;
import util.UseRTH;

@Service("JtdoaDao")
public class JtdoaDao implements ValueFormatUtil,JtdoaValueMarket,QQMarketLevelUtilByMaster{
	
	private static JedisPool poolM=RedisUtil.getInstanceMsater();
	
	private static JedisPool poolS=RedisUtil.getInstanceSlave();
	
	private static Jedis jedis_M=null;
	private static Jedis jedis_S=null;
	static{
		 
		jedis_S=poolS.getResource();
		jedis_M=poolM.getResource();
	}
	private  String joinStringSplit(String [] array,String sp){
		StringBuffer buffer=new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			buffer.append(array[i]);
			if(i<array.length-1)
				buffer.append(sp);
		}
		 
		return buffer.toString();
	}
	/**
	 * JNI SAVE LEVE2
	 * @param tickId
	 * @param symbol
	 * @param exchange
	 * @param currency
	 * @param market
	 * @param side
	 * @param price
	 * @param size
	 * @param checksum
	 */
	public void Leve2Depth(long tickId, String symbol,String exchange,String currency, String market, int side, double price, int size, int checksum){
		jedis_M.select(0);
		symbol= symbol+"."+exchange.toUpperCase();
		if(checksum<6){
			 
			int start =LEVEL2_INDEX_SIDE1;
			int index= start+(5-checksum);
			if(side==0){
				start=LEVEL2_INDEX_SIDE0;
				index= start+checksum-1;
			}
			 
			System.out.println(index);
			String value=jedis_M.get(symbol);
			String [] subStr=value.split(SPLIT_STR);
			Date date=new Date();
			String dateStr=format.format(date);
			subStr[DATE_INDEX]=dateStr;
			subStr[index]=Double.toString(price);
			subStr[index+10]=Integer.toString(size);
			value = joinStringSplit(subStr, SPLIT_STR);
			jedis_M.set(symbol, value);
		}
	}
	/**
	 * 涨幅 及 跌幅排序
	 * @param symbol
	 * @param exchange
	 * @param L1Value
	 */
	private void saveSortBalance(String symbol,String exchange,String L1Value,String openValue,String date){
//		jedis_M.select(1);
//		
//		double l1Val=Double.parseDouble(L1Value);
//		double openVal=Double.parseDouble(openValue);
//		double desc =(l1Val-openVal)/openVal;
//		int marketName=0;
//		if("SZ".equals(exchange.toUpperCase().trim())||"SH".equals(exchange.toUpperCase().trim())){
//			marketName=HU_SHEN;
//		}
//		if(desc>0)
//			jedis_M.zadd(MARKET[marketName][0]+":"+MARKET[marketName][1],desc,symbol+"."+exchange.toUpperCase()+":"+l1Val+":"+date);
//		else 
//			jedis_M.zadd(MARKET[marketName][0]+":"+MARKET[marketName][2],desc,symbol+"."+exchange.toUpperCase()+":"+l1Val+":"+l1Val+":"+date);
//		
//		jedis_M.select(0);
		
	}
	/**
	 * FROM JNI 
	 *  level1 的数据处理 根据不同的 tickType 更新对应的值
	 * @param tickId
	 * @param symbol 股票代码
	 * @param secType
	 * @param exchange 市场代码
	 * @param currency 
	 * @param tickType 更新类型
	 * @param L1Value 类型对应的值
	 * @param size 大小
	 */
	public void Level1Data(long tickId, String symbol,String secType,String exchange,String currency,int tickType,String L1Value, int size){
		jedis_M.select(0);
		String value=jedis_M.get(symbol+"."+exchange);
		String [] subStr=value.split(SPLIT_STR);
		String date=format.format(System.currentTimeMillis());
		System.out.println("1");
		subStr[DATE_INDEX]=date;
		double price=Double.parseDouble(L1Value);
		L1Value=doubleformat.format(price);
		switch(tickType){
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
//		case 4: // LAST 最新成交价
//			
//			subStr[LAST_PRICE_INDEX] =L1Value;
//			saveSortBalance(symbol, exchange, L1Value, subStr[OPEN_INDEX], date);
//			break;
//		case 5: // 最后一次成交笔数
//			subStr[PRE_VOLUME_INDEX] =L1Value;
//			System.out.println(L1Value);
//			break;
		case 6: // 开盘价
			subStr[OPEN_INDEX] =L1Value;
			break;
		case 7:// 最高价
			subStr[HEIGHT_INDEX] =L1Value;
			break;
		case 8: // 最低价
			subStr[LOW_INDEX] =L1Value;
			break;
		case 9: // 总成交量
			subStr[VOLUME_INDEX] =L1Value;
			break;
		case 10: // 收盘价
			break;
		case 11: // 涨停价
			subStr[LIMIT_UP_INDEX] =L1Value;
			break;  
		case 12: // 跌停价
			subStr[LIMIT_DOWN_INDEX] =L1Value;
			break;
		case 13: //昨日收盘价
			subStr[PRE_CLOSE_INDEX]=L1Value;
			break;
		case 14: // 今日结算
			break;
		case 15: // 昨日结算
			break;
		case 16: // 委托买入总量
			break; 
		case 17: // 委托卖出总量
			break;
		case 18: // 持仓量
			break;
		case 19: // 昨日持仓量
			break;
		case 20: // 成交总金额
			break;
		}
		value=joinStringSplit(subStr, SPLIT_STR);
		System.out.println("save succes "+value );
		jedis_M.set(symbol+"."+exchange, value);
	 
	}
	/**
	 * 获取指数字符串
	 * @param huShen 市场编号 
	 * @param begin 开始位置
	 * @param end  结束位置(不包含)
	 * @return
	 */
	public String [] getStockIndex(int huShen,int begin,int end,boolean master) {
		// TODO Auto-generated method stub
		String [][] STOCK_NAME=null;
		switch(huShen){
		case HU_SHEN:// 指数为沪深指数
			STOCK_NAME=HU_SHEN_STOCK_INDEX;
		break;
		case MEI_GU:
			break;
		case GANG_GU:
			break;
		}
		int size=end-begin+1; 
		if(size<0) size=STOCK_NAME.length;
		String [] StockIndexs=new String [size];
		/*
		 * 模拟指数部分值
		 */
		jedis_S.select(0);
		for (int i = begin; i < size; i++) {
		String value=jedis_S.get(HU_SHEN_STOCK_INDEX[i][0]);
		String [] subStr=value.split(SPLIT_STR);
		StockIndexs[i]=HU_SHEN_STOCK_INDEX[i][0]+":"+HU_SHEN_STOCK_INDEX[i][1]+":"+subStr[LAST_PRICE_INDEX]+":"+subStr[UP_DOWN_PRICE]+":"+subStr[UP_DOWN_PRICE_RATE];
		if(master)
			StockIndexs[i]+=":"+subStr[STOCK_AMPLITUPE]+":"+subStr[TOTLE_SUM_INDEX]+":"+subStr[VOLUME_INDEX];
			//		int mod=	(int) (System.currentTimeMillis()/1000%2);
//		if(mod==1)
//		StockIndexs[i]=STOCK_NAME[i][0]+":"+STOCK_NAME[i][1]+":"+"12.1"+":"+"1.1:-1.1%";
//		else
//		StockIndexs[i]=STOCK_NAME[i][0]+":"+STOCK_NAME[i][1]+":"+"12.1"+":"+"3.3:2.1%";
		}
	 	return StockIndexs;
	}
 
	/**
	 * 获取行情下的 股票涨跌榜信息 
	 * @param huShen  股票市场
	 * @param params 
	 * @param b 是否全部
	 * @return
	 */
	public Map<String, List<String>> getL1StockMarketData(int huShen,long begin,long end,
			String[] params,boolean master) {
		// TODO Auto-generated method stub
		Map<String, List<String>> map=new HashMap<>();
//		jedis_M.select(1);
//		Set<String> sets=jedis_M.keys(huShen+":*");
//		int size=b==false?4:-1;
//		for (String string : sets) {
//			Set<Tuple> tuple=jedis_M.zrevrangeByScoreWithScores(string, 0, size);
//			map.put(string, tuple);
//		}
		 Map<String, Set<Tuple>> mapTuple=new HashMap<>();
		jedis_S.select(1);
			for (int i=0;i<params.length;++i) {
				int index= Integer.parseInt(params[i])+1;
				String string =MARKET[huShen][0]+":"+MARKET[huShen][index];
				if(jedis_S.zcard(string)<=end){
					end =jedis_S.zcard(string)-1;
				}
				
				Set<Tuple> range=jedis_S.zrevrangeWithScores(string, begin,end);
				
				if(MARKET[huShen][index].equals("1"))
					range=jedis_S.zrangeWithScores(string, begin,end); 
				
				mapTuple.put(string, range);
				
			}
		jedis_S.select(0);
		
		/**
		 *  sb =>>  股票代码,名称,价格,涨跌幅率
		 */
		Set<Entry<String, Set<Tuple>>> set=mapTuple.entrySet();
		for (Entry<String, Set<Tuple>> entry : set) {
			String market = entry.getKey();
			List<String> symbolList=new ArrayList<>();
			Set<Tuple> tupleSet=entry.getValue();
			for (Tuple tuple : tupleSet) {
				StringBuffer sb=new StringBuffer();
				String symbol=tuple.getElement();
				double rate=tuple.getScore();
				String redisValue = jedis_S.get(symbol);
				symbolList.add(redisValue+":"+symbol);
			}
			map.put(market, symbolList);
		}
			
		return map;
	}
	/**
	 *  模拟l1 的数据
	 */
	public Map<String, Set<Tuple>> getL1StockMarketDataM(){
		Map<String, Set<Tuple>> map=new HashMap<String, Set<Tuple>>();
		String []name={"0:涨幅榜","0:跌幅榜"};
		for(int j=0;j<2;++j){
			Set<Tuple> tuples=new TreeSet();
			 
				try {
					Tuple t3=new Tuple("300506.SZ:3.5", 7.16);
					Tuple t=new Tuple("002800.SZ:1.1", 3.32);
					Tuple t1=new Tuple("300520.SZ:3.1", 3.22);
					Tuple t4=new Tuple("300474.SZ:12.1", 2.33);
					Tuple t2=new Tuple("002805.SZ:0.2", 1.11);
					tuples.add(t3);
					tuples.add(t);
					tuples.add(t1);
					tuples.add(t4);
					tuples.add(t2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
				}
				map.put(name[j], tuples);
		}
		return map;
	}
	/**
	 * FROM JNI 
	 * @param tickId
	 * @param symbol
	 * @param secType
	 * @param exchange
	 * @param currency
	 * @param price
	 * @param time
	 * @param volume
	 */
	public void tickPrice(long tickId, String symbol, String secType, String exchange, String currency, double price,
			String time, long volume) {
		// TODO Auto-generated method stub
			jedis_M.select(0);
			String value=jedis_M.get(symbol+"."+exchange.toUpperCase());
			String [] subStr=value.split(SPLIT_STR);
			String date=format.format(System.currentTimeMillis());
			System.out.println("2");
			subStr[DATE_INDEX]=date;
			
			String L1Value=doubleformat.format(price);
			 
				subStr[LAST_PRICE_INDEX] =L1Value;
				saveSortBalance(symbol, exchange, L1Value, subStr[OPEN_INDEX], date);
			//	subStr[PRE_VOLUME_INDEX] =Long.toString(volume);
				
			value=joinStringSplit(subStr, SPLIT_STR);
			jedis_M.set(symbol+"."+exchange.toUpperCase(), value);
	}
	/**
	 * 获取单支 股票的  level2 
	 * @param symbol
	 * @return
	 */
	public String getL2(String symbol) {
		// TODO Auto-generated method stub
		jedis_S.select(0);
		String value = jedis_S.get(symbol);
		
		return value;
	}
	public List<TickSort> getLevel2Detail(String symbol){
	 
	  
			 List<TickSort> list=null;
			JHdboa jhd=new JHdboa();
			jhd.HdboaInit(new BarData(),new Contract(),new TickData());
			jhd.HdboaConnect("222.173.29.210", 7008);
			while(!jhd.connected)
			{
				System.out.println("wait");
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			  SimpleDateFormat yy_MM_ddformat =new SimpleDateFormat("yyyy:MM:dd");
			 SimpleDateFormat ALL_format =new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			 Contract contract=new Contract();
			 contract.symbol="002362";
			 contract.currency="CNY";
			 contract.exchange="SZ";
			 contract.secType="STK";
			 int cntTime=0;
			 
			 try {
				 long current_Time=System.currentTimeMillis();
			 String dateStr=yy_MM_ddformat.format(current_Time);
			 Date endTime =ALL_format.parse(dateStr+" 15:01:00");
			 Date beginTime =ALL_format.parse(dateStr+" 09:30:00");
			 long fromTime=0l;
			 long toTime=0l;
			 // 起始时间与终止时间之差 10分钟
			 long index_time=1000*60*30;
			 // 第二天请求昨天的
			 long yestday=1000*60*60*24;
			 do{
				 jhd.setTickSorts(null);
					 if(endTime.getTime()<current_Time){// 超过 收盘时间
						 fromTime=endTime.getTime()-index_time-cntTime;
						 toTime=endTime.getTime();
					 }else{
						 if(beginTime.getTime()>current_Time){// 先于  开盘时间
							 fromTime=endTime.getTime()-index_time-cntTime-yestday;
							 toTime=endTime.getTime()-yestday;
						 }else{
							 toTime=current_Time;
							 fromTime=current_Time-index_time-cntTime;
						 }
					 }
					 
					 list=jhd.getTickSorts();
					 System.out.println("from "+new Date(fromTime)+"  to "+new Date(toTime));
					int j= jhd.HdboaReqHistoricalTickData(3, contract, fromTime/1000, toTime/1000,UseRTH.USE_RTH.ordinal());
					System.out.println(j);
					while(!jhd.isset){
						Thread.sleep(100);
					//	System.out.println(jhd.isset);
					}
						 Thread.sleep(1000);
						 System.out.println(list.size());
						 cntTime+=1000*60;
			 }while(list.size()<20&&fromTime>beginTime.getTime());
			 } catch (Exception e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 } 
			//System.out.println(getDateStr(System.currentTimeMillis()-1000*60*60*24)+getDateStr(System.currentTimeMillis()));
		//	int status=jhd.HdboaReqHistoricalData(8000, contract,(System.currentTimeMillis()-1000*60*60*25)/1000, System.currentTimeMillis()/1000,CYCTYPE.CYC_DAY.ordinal(), 1, UseRTH.USE_RTH.ordinal());
//			System.out.println(status);
//		 jhd.HdboaReqHistoricalTickData(3, contract, (System.currentTimeMillis()-1000*60*60*24)/1000, (System.currentTimeMillis())/1000,UseRTH.USE_RTH.ordinal());
				
		 	Collections.sort(list);
			System.out.println("ceoom i   =---------------------------");
				System.out.println(Arrays.asList(list));
				System.out.println(list.size());
				 
				jhd.HdboaDisconnect();
//				jhd.HdboaDestory();
				System.out.println(Thread.currentThread().getName());
				while(!jhd.end);
			System.out.println("sa");
			jhd=null;
			System.out.println(Arrays.asList(list));
	 
	 
			return list;
	}
	public String[] getIndexDetail(String indexCode) {
		// TODO Auto-generated method stub
		Jedis jedis=poolS.getResource();
		jedis.select(0);
		String [] subStr=jedis.get(indexCode).split(SPLIT_STR);
		RedisUtil.RealseJedis_S(jedis);
		return subStr;
	}
}
