package com.updateData.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Set;

import org.junit.Test;

import com.redis.main.SaveIndexValueInRedis;

import redis.clients.jedis.Jedis;
import util.BarData;
import util.CYCTYPE;
import util.Contract;
import util.JHdboa;
import util.TickData;
import util.TickSort;
import util.UseRTH;


public class StockIndustryTest {
	/**
	 * 默认值 K线
	 */
	private static final String default_string="Date,Open,High,Low,Close,Volume,Adj Close#2016-07-01,22.40,24.54,22.05,23.87,19033600,23.87#2016-07-04,23.74,24.46,23.32,24.33,15844900,24.33#2016-07-05,24.01,24.02,23.39,23.60,12779800,23.60#2016-07-06,23.59,24.09,23.21,23.46,8366500,23.46#2016-07-07,23.22,23.60,22.75,23.16,9541400,23.16#2016-07-08,23.10,23.35,22.88,23.14,6706400,23.14#2016-07-11,23.27,23.34,22.31,22.44,9920800,22.44#2016-07-12,22.55,23.60,21.91,23.58,13609600,23.58#2016-07-13,23.46,23.90,23.02,23.19,8939600,23.19#2016-07-14,23.14,23.35,22.77,23.14,5288700,23.14#2016-07-15,23.30,23.49,22.37,22.48,5287700,22.48#2016-07-18,22.33,22.80,22.12,22.52,3356000,22.52#2016-07-19,22.52,23.94,22.25,23.88,8968400,23.88#2016-07-20,23.65,25.80,23.56,24.77,18701800,24.77#2016-07-21,24.51,25.25,24.31,24.53,11165300,24.53#2016-07-22,24.40,26.17,24.23,25.95,15377900,25.95#2016-07-25,26.22,26.88,25.39,25.79,13604400,25.79#2016-07-26,25.46,25.79,24.81,25.41,8559300,25.41#2016-07-27,25.47,25.60,22.87,22.87,12048900,22.87#2016-07-28,22.88,23.12,21.25,22.51,9689600,22.51#2016-07-29,22.87,22.97,21.70,21.80,6832300,21.80#2016-08-01,21.89,22.00,21.01,21.95,5432900,21.95#2016-08-02,21.97,22.17,21.72,22.10,4733900,22.10#2016-08-03,22.01,22.10,21.61,22.01,4484900,22.01#2016-08-04,21.95,22.95,21.65,22.76,7442000,22.76#2016-08-05,22.78,22.78,22.10,22.13,4740700,22.13#2016-08-08,22.02,22.25,21.50,22.20,3321800,22.20#2016-08-09,22.24,22.64,22.12,22.57,3920300,22.57#2016-08-10,22.60,22.79,22.26,22.29,3946700,22.29#2016-08-11,22.28,22.45,21.68,21.71,3504400,21.71#2016-08-12,21.53,22.10,21.53,22.01,2977500,22.01#2016-08-15,22.02,23.36,21.73,23.00,7415800,23.00#2016-08-16,23.29,23.29,22.80,22.89,5067000,22.89#2016-08-17,22.80,23.09,22.58,22.93,3700700,22.93#2016-08-18,22.93,24.65,22.81,23.92,9847600,23.92#2016-08-19,23.80,25.25,23.61,25.20,14044500,25.20#2016-08-22,24.88,24.88,23.26,23.47,12928400,23.47#2016-08-23,23.33,23.68,22.68,22.94,7277600,22.94#2016-08-24,22.99,23.82,22.96,23.39,5635600,23.39#2016-08-25,23.00,23.19,22.67,23.13,4492200,23.13#2016-08-26,23.14,24.15,23.14,24.01,8392400,24.01#2016-08-29,23.90,24.20,23.63,24.06,5286100,24.06#2016-08-30,24.25,24.80,23.31,23.44,7478700,23.44#2016-08-31,23.46,23.87,23.12,23.70,4126300,23.70";

	/**
	 * 使用雅虎API 历史数据接口调用 模拟产生数据
	 * yahoo API 返回数据的格式如下
	 * Date,Open,High,Low,Close,Volume,Adj Close
	 * @param url 日期及时间
	 * @return 返回 以 # 分割的日期K线值
	 */
	private String  YaHooget(String url){
		  
		        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码   

			StringBuffer sb=new StringBuffer();
		        URL getUrl = null;
				try {
					getUrl = new URL("http://table.finance.yahoo.com/table.csv?"+url);
				} catch (MalformedURLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}   

		        // 根据拼凑的URL，打开连接，URL.openConnection()函数会根据 URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection   

		        HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) getUrl.openConnection();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}   

		        // 建立与服务器的连接，并未发送数据   

		        try {
					connection.connect();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}   

		        // 发送数据到服务器并使用Reader读取返回的数据   

		        BufferedReader reader = null;
				try {
					reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"gb2312"));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					reader=null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					reader=null;
				}   
		        String lines;   
		        if(reader!=null){
		        try {
					while ((lines = reader.readLine()) != null) {   
						sb.append(lines+"#");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
		        try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
		        // 断开连接   
		        }else{
		        	sb.append(default_string);
		        }
		        connection.disconnect();   

		   return sb.toString();

	}
	/**
	 * @param 获取模拟分钟K线值
	 * @return
	 * MAP  k -> 股票代码 V -> 模拟分钟值String []
	 */
	public Map<String, String [] > getKminuteMap(){
		Map<String, String[]> map=new HashMap<>();
		Jedis jedis = new Jedis("localhost",6379);
	  	
		  jedis.select(3);
		  Set<String> keys = jedis.keys("K*");
		  for(String string: keys){
			List<String> list =jedis.lrange(string, 0, -1);  
			String [] s= new String[list.size()];
			int i=0;
			for (String string2 : list) {
				s[i++]=string2;
			}
			map.put(string.substring(2), s);
		  }
		  
		  jedis.disconnect();
			
		return map;
	}
	/**
	 * 模拟计算股票指数 并保存到 Redies 的临时单元
	 * @author keerte 
	 * @param map(K线分钟模拟集合)   mapName(行业集合排序序列)
	 */
	private void countHistoryStockIndex(Map<String , String [] >map,Map<String, Set<String>> mapName,int industrySize){
	   Jedis jedis=new Jedis("localhost",6379);
	   /**
	    * 存储 行业指数值  Key 天为单位 
	    */
 
	   /**
	    * 遍历的天数
	    */
	   int size =map.get("603018.SH").length;
	   
		   /**
		    * 获取行业集合 名称
		    */
		   Set<Entry<String, Set<String>>> setEntry = mapName.entrySet();
		   Iterator<Entry<String, Set<String>>> it= setEntry.iterator();
		  
		   /**
		    * 行业的天数存储
		    */
		  Map<String,  ArrayList<String> []>list =
				  new HashMap<String, ArrayList<String> []>();
			 
		  /**
		   * 遍历每个行业
		   */
		   while(it.hasNext()){
			  
			   Entry<String, Set<String>> entry = it.next();
			   /**
			    *  获取行业名称编号
			    */
			   String stockIndustry = entry.getKey();
			   /**
			    * 天数 具体存储的值
			    */
			   ArrayList<String> []  list2= new ArrayList[industrySize];
			   /**
			    * 获取行业名称对应的排序集合 及  行业的子股
			    */
			   Set<String> symbolName = entry.getValue();
			 
			   //行业子股票计算
			   for (String string : symbolName) {
				  String[] sets = map.get(string);
				  
				  for (int j = 0; j < sets.length; j++) {
					   if(list2[j]==null)
						   list2[j]= new ArrayList<>();
					   list2[j].add(sets[j]);
					
				} 
			   }
			   list.put(stockIndustry, list2);
			   
			   
		   }// while
 
		 
		   Set<Entry<String, ArrayList<String>[]>> entry = list.entrySet();
		   Iterator<Entry<String, ArrayList<String>[]>> it1 =entry.iterator();
		   while(it1.hasNext()){
			   Entry<String, ArrayList<String>[]> subv = it1.next();
			   String code = subv.getKey();
			   double preIndex= 1000;
			   double lastIndex= 1000;
			   System.out.println(code+"--------------------------");
			   ArrayList<String>[] list2sub = subv.getValue();
		   for (int i = 1; i < list2sub.length; i++) {
			   List<String> l1 = list2sub[i];// 当天
			   double tempLastSum=0;
			   double tempPreSum=0;
			   String day=null;
			   double sum=0;
			   List<String> l0 =list2sub[i-1];//昨天
//			   System.out.println(Arrays.asList(l1));
//			   System.out.println(Arrays.asList(l0));
			   if(l1!=null){
			   for (int j = 0; j < l0.size(); j++) {
				   if(l1!=null){
					    
				   if(j==0) day=l1.get(j).split("[,]")[0];
				 tempLastSum+=(Double.parseDouble(l1.get(j).split("[,]")[4])
						 *Double.parseDouble(l1.get(j).split("[,]")[5])/1000);
				 tempPreSum+=(Double.parseDouble(l0.get(j).split("[,]")[4])
						   *Double.parseDouble(l0.get(j).split("[,]")[5])/1000);
				   }
				  
			   }// for 2
			   if(tempPreSum!=0)
			   lastIndex=(tempLastSum/tempPreSum)*preIndex;
			   System.out.println(day+" => "+tempLastSum/tempPreSum+ " : " +lastIndex);
			   preIndex=lastIndex;
			   }
		   }// for 1
		   } //while()
		   
	   jedis.disconnect();
	}
	 
	/**
	 * 调用JNI 历史K线值 
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @param symbol 代码
	 * @return 分钟线的List
	 */
	public List<BarData> connection(long beginTime,long endTime,String symbol){
		System.out.println(symbol);
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
		 
		 Contract contract=new Contract();
		 contract.symbol=symbol.split("[.]")[0];
		 contract.currency="CNY";
		 contract.exchange=symbol.split("[.]")[1];
		 contract.secType="STK";
		 jhd.setBarDatas(null);
		List<BarData>  barDatas=null;
		 barDatas=jhd.getBarDatas();
		 System.out.println(contract+" begin "+new Date(beginTime)+" end "+new Date(endTime));
		 int j =jhd.HdboaReqHistoricalData(System.currentTimeMillis()%211,contract,
				 beginTime/1000,endTime/1000,CYCTYPE.CYC_MINUTE.ordinal(),1,UseRTH.USE_RTH.ordinal());
		int cnt =0;
		while(!jhd.isset){
			cnt++;
			if(cnt==30) break;
			try {
				System.out.println("false");
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(barDatas.size()!=0)
		System.out.println(Arrays.asList(barDatas));
		jhd.HdboaDisconnect();
		return barDatas;
	}
	private static final SimpleDateFormat simple = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
	/** 
	 * 保存分钟线的值
	 * @author keerte
	 * @param string
	 * @param symbol
	 */
	public void saveMinuteKData(String[] string,String symbol){
		Jedis jedis=new Jedis("localhost",6379);
		jedis.select(3);
		for (int i = 1; i < string.length; i++) {
			jedis.lpush("K."+symbol, string[i]);
		}
		System.out.println("save over "+symbol);
		jedis.disconnect();
	}
	/**
	 * 读取行业分类文件 解析 并且 存入到Redis中初始化
	 */
 
	public  void readOrWriteTOrRedis() {
	 
	SaveIndexValueInRedis save= new SaveIndexValueInRedis();
//	Map  K ->行业代码  V -> 行业排序集合 及 Zset
	Map<String, Set<String>> mapName =save.getStock();
//	获取行业 Key 值 
	Set<Entry<String, Set<String>>> entry= mapName.entrySet();
//	获取K 线分钟键值 
	Map<String, String[]> mapKminute = getKminuteMap();
	/**
	 *  计算分钟K线指数值
	 */
	int industrySize=mapKminute.keySet().size();
	 countHistoryStockIndex(mapKminute,mapName,industrySize);
//	Iterator<Entry<String, Set<String>>> it = entry.iterator();
//	Calendar cal=Calendar.getInstance();
//	cal.setTimeInMillis(System.currentTimeMillis());
//	int count =0;
// 
//	while(it.hasNext()){
//		Entry<String, Set<String>> entrys = it.next();
//		Set<String> set=entrys.getValue();
//		 
//		for (String string : set) {
//			String tempSymbol=string;
//			if(string.indexOf("H")!=-1){
//				string = string.split("[.]")[0]+".SS";
//			}
//			System.out.println("send "+string);
//			count ++;
//			String sy=test.YaHooget("s="+string+"&a=06&b=01&c=2016&d=08&e=01&f=2016&g=d&ignore=.csv");
//			System.out.println(sy);
//						String [] symbolArray=sy.split("#");
//			test.saveMinuteKData(symbolArray,tempSymbol);
//		}
//		
//	}
//	System.out.println(count );
//	while(true){
//		if(currency==before){
//			before=System.currentTimeMillis();
//		}
//		if((currency-before)/1000==timerCnt){
//			System.out.println(new Date(currency));

		/*		while(it.hasNext()){ 
		Entry<String, Set<String>> en=it.next();
		Set<String> set= en.getValue();
		System.out. println("------------------"+en.getKey()+"------------------------------");
		
	for (String string : set) {
			String tempSymbol=test.YaHooget("s="+string+"&a="+cal+"&b=01&c=2016&d=07&e=22&f=2016&g=d&ignore=.csv");
			if(!"".equals(string.trim())&&string !=null){
			List<BarData> bardatas=test.connection(currency-(1000*60*60*2), currency, string);
			map.put(symbols[i], tempSymbol.split("[#]"));
			if(bardatas.size()!=0){
				test.saveMinuteKData(bardatas,string);
				break;
			}
		}
			break;
		}
		System.out.println("---------------------------------------------------");
		System.out.println("---------------------------------------------------");
		break;
		//		System.out.println(s);
		}// while()
		before=currency;
	}// if((currency-before)/1000==timerCnt)
	 */
}

//	test.countHistoryStockIndex(map);
	 
//	}
	public static void main(String[] args) {
		StockIndustryTest test= new StockIndustryTest();
		test.readOrWriteTOrRedis();
	}
}
