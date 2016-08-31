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
	public void getStockIndustry(String filename){
		Map<String,List<String>> string= parseFile(filename);
		save(string);
	}
	private void save(Map<String,List<String>> string){
		jedis.select(2);
		Set<Entry<String, List<String>>> entry=string.entrySet();
		Iterator<Entry<String, List<String>>> it=entry.iterator();
		while(it.hasNext()){
			Entry<String, List<String>> list = it.next();
			System.out.println(list.getKey()+"="+Arrays.asList(list.getValue()));
		}
		
	}
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
	public static void main(String[] args) {
		new SaveIndexValueInRedis().getStockIndustry("src/hangyesql.txt");
	}
}
