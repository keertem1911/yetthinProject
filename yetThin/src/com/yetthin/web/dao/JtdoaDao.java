package com.yetthin.web.dao;

import java.util.Date;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.yetthin.web.commit.JtdoaValueMarket;
import com.yetthin.web.commit.RedisUtil;
import com.yetthin.web.commit.ValueFormatUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service("JtdoaDao")
public class JtdoaDao implements ValueFormatUtil,JtdoaValueMarket{
	
	private static JedisPool poolM=RedisUtil.getInstanceMsater();
	
	private static Jedis jedis_M=null;
	
	static{
		 
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
		jedis_M.select(1);
		
		double l1Val=Double.parseDouble(L1Value);
		double openVal=Double.parseDouble(openValue);
		double desc =(l1Val-openVal)/openVal;
		int marketName=0;
		if("SZ".equals(exchange.toUpperCase().trim())||"SH".equals(exchange.toUpperCase().trim())){
			marketName=HU_SHEN;
		}
		if(desc>0)
			jedis_M.zadd(MARKET[marketName][0]+":"+MARKET[marketName][1],desc,symbol+"."+exchange.toUpperCase()+":"+l1Val+":"+date);
		else 
			jedis_M.zadd(MARKET[marketName][0]+":"+MARKET[marketName][2],desc,symbol+"."+exchange.toUpperCase()+":"+l1Val+":"+l1Val+":"+date);
		
		
		
		jedis_M.select(0);
		
		
	}
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
		case 4: // LAST 最新成交价
			
			subStr[LAST_PRICE_INDEX] =L1Value;
			saveSortBalance(symbol, exchange, L1Value, subStr[OPEN_INDEX], date);
			break;
		case 5: // 最后一次成交笔数
			subStr[PRE_VOLUME_INDEX] =L1Value;
			System.out.println(L1Value);
			break;
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
	public String[] getStockIndex(int huShen) {
		// TODO Auto-generated method stub
		String [] StockIndexs=new String [5];
		String [][] STOCK_NAME=null;
		switch(huShen){
		case HU_SHEN:STOCK_NAME=HU_SHEN_STOCK_INDEX;
		break;
		case MEI_GU:
			break;
		case GANG_GU:
			break;
		}
		for (int i = 0; i < StockIndexs.length; i++) {
		int mod=	(int) (System.currentTimeMillis()/1000%2);
		if(mod==1)
		StockIndexs[i]=STOCK_NAME[i][0]+":"+STOCK_NAME[i][1]+":"+"12.1"+":"+"1.1"+"-1.1%";
		else
		StockIndexs[i]=STOCK_NAME[i][0]+":"+STOCK_NAME[i][1]+":"+"12.1"+":"+"3.3"+"2.1%";
		}
	 	return StockIndexs;
	}
	public String getNameBySymbol(String string) {
		// TODO Auto-generated method stub
		return jedis_M.get("name"+string.toUpperCase());
	}
}
