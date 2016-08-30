package com.yetthin.web.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StockIndustryTest {
	private static final String [] symbols={"sz000592","sh600265","sz002679","sz002200"};
	public static final String QQ_M_REQUEST_URL="http://qt.gtimg.cn/q=";

	public static void readContentFromGet(String url) throws IOException {   

	        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码   

	      

	        URL getUrl = new URL(url);   

	        // 根据拼凑的URL，打开连接，URL.openConnection()函数会根据 URL的类型，返回不同的URLConnection子类的对象，在这里我们的URL是一个http，因此它实际上返回的是HttpURLConnection   

	        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();   

	        // 建立与服务器的连接，并未发送数据   

	        connection.connect();   

	        // 发送数据到服务器并使用Reader读取返回的数据   

	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"gb2312"));   

	        System.out.println(" ============================= ");   

	        System.out.println(" Contents of get request ");   

	        System.out.println(" ============================= ");   

	        String lines;   

	        while ((lines = reader.readLine()) != null) {   
	     	   		 
	        	 
//	        	String [] subStr = lines.split("=");
//	        	String value=subStr[1];
//	        	String []sub=value.split("~"); 
//	        	for (int i = 0; i < sub.length; i++) {
//					System.out.println(i+" ="+sub[i]);
//				} 
	 	   		 System.out.println(lines);
	        }   

	        reader.close();   

	        // 断开连接   

	        connection.disconnect();   

	        System.out.println(" ============================= ");   

	        System.out.println(" Contents of get request ends ");   

	        System.out.println(" ============================= ");   

	 }   


	public static void main(String[] args) {
	StringBuffer buffer= new StringBuffer();
	for (int i = 0; i < symbols.length; i++) {
		buffer.append(symbols[i]);
		if(i<symbols.length-1)
			buffer.append(",");
	}
	try {
		new StockIndustryTest().readContentFromGet(QQ_M_REQUEST_URL+buffer.toString());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
