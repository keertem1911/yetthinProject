package com.redis.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import redis.clients.jedis.Jedis;

public class SaveIndexValueInRedis {
	private static Jedis jedis=null;
	static{
		jedis=new Jedis("localhost", 6379);
	}
	/**
	 * 读取文件中的行业分类个股
	 * 将其保存入库并且初始化
	 * @author keerte
	 * @param filename 文件名称
	 */
	public void getStockIndustry(String filename){
		Map<String,List<String>> string= parseFile(filename);
		save(string);
	}
	/**
	 * 保存数据至Redis 2库中
	 * K-V 为  K->行业代码+n --V->行业名称
	 * ZSET Name->行业代码+z score->0  value ->子股票代码
	 * @param string
	 */
	private void save(Map<String,List<String>> string){
		jedis.select(2);
		Set<Entry<String, List<String>>> entry=string.entrySet();
		Iterator<Entry<String, List<String>>> it=entry.iterator();
		int count =0;
		while(it.hasNext()){
			Entry<String, List<String>> list = it.next();
			jedis.set(count+"n", list.getKey());
			for (int i = 0; i < list.getValue().size(); i++) {
				String preffix="";
				if(Integer.parseInt((list.getValue().get(i).substring(0, 1)))>3)
					preffix=".SH";
				else 
					preffix=".SZ";
				jedis.zadd(count+"z", 0,list.getValue().get(i)+preffix);
			}
			count ++;
		}
		jedis.disconnect();
		
	}
	/**
	 * 解析TXT 行业分类
	 * @author keerte
	 * @param filename
	 * @return  K -> 行业名 V -> List 行业 子股票
	 */
	private Map<String,List<String>> parseFile(String filename){
		File file=new File(filename);
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		BufferedReader reader=null;
			try {
				reader=new BufferedReader(new InputStreamReader
						(new FileInputStream(file), "utf-8"));
				String str=null;
				while((str=reader.readLine())!=null){
					if(str.indexOf("*")==-1){
						List<String> list =new ArrayList<>();
						String []values =str.split("[=]");
						String value=values[1].substring(1, values[1].length()-1);
						if(!"".equals(value)){
							String [] symbol =value.split("[,]");
							if(symbol!=null&&symbol.length!=0)
							for (int i = 0; i < symbol.length; i++) {
//								if(!"".equals(symbol[i].trim()))
								if(Integer.parseInt(symbol[i].substring(0, 1))==0||
										Integer.parseInt(symbol[i].substring(0, 1))==3||
										Integer.parseInt(symbol[i].substring(0, 1))==6)
								list.add(symbol[i]);
							}
						}
						map.put(values[0], list);
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 return map;
	}
	/**
	 * 获取行业数据
	 * @author keerte
	 * return Map  K ->行业代码  V -> 行业排序集合 及 Zset
	 */
 
	public Map<String, Set<String>> getStock(){
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
		return map;
	}
	public static void main(String[] args) {
		 SaveIndexValueInRedis obj = new SaveIndexValueInRedis();
		obj.getStockIndustry("src/hangyesql.txt");
//		 obj.getStock();
	}
}
