package com.yetthin.web.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yetthin.web.commit.JtdoaUtil;

import util.Contract;
import util.JHdboa;
import util.JTdoa;

public class ReadTextSymbol {
	
//  	private static JTdoa jtdoa=JtdoaUtil.getInstanceJTdoa();
//  	private static JHdboa jhdboa=JtdoaUtil.getInstanceJHdboa();
//	public void jtdoaFun(Contract contract){
//		jtdoa.TDOASubscribeMarketDepth(5, contract, 3);
//	}
	public List<String> readSymolByString(String path){
		BufferedReader reader=null;
		List<String> lists=new ArrayList<>();
		try {
			 reader=new BufferedReader(
					new InputStreamReader(new FileInputStream(path)));
			String line=null;
			while((line=reader.readLine())!=null){
				line =line.split("\\s")[0];
				String [] sub=line.split("[.]");
				String symbol=sub[0];
				String exchange=sub[1];
				 
				lists.add(exchange.toLowerCase()+symbol);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		 
		return lists;
	}
	public Map<String,Object> readTextByContract(String path){
		BufferedReader reader=null;
		Map<String, Object> map=new HashMap<>();
		List<Contract> lists=new ArrayList<>();
		Map<String, String> names=new HashMap<>();
		try {
			 reader=new BufferedReader(
					new InputStreamReader(new FileInputStream(path)));
			String line=null;
			while((line=reader.readLine())!=null){
				String [] subStr= line.split("\\s");
				System.out.println(line);
				String [] sub=subStr[0].split("[.]");
				String symbol=sub[0];
				String exchange=sub[1];
				Contract contract=new Contract();
				contract.symbol=symbol; 
				contract.currency="CNY"; 
				contract.exchange=exchange;
				contract.secType="STK";
				lists.add(contract);
				names.put(subStr[0].toUpperCase(), subStr[1]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		map.put("contracts", lists);
		map.put("names", names);
		return map;
	}
	
	public static void main(String[] args) {
		ReadTextSymbol readerObj = new ReadTextSymbol();
		 readerObj.readSymolByString("src/symbol.txt");
	//	System.out.println(Arrays.asList(lists));
	}
}
