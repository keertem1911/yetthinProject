package com.yetthin.web.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.fabric.xmlrpc.base.Array;

public class RegularTest {
	
	public static String joinStringSplit(String [] array,String sp){
		StringBuffer buffer=new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			buffer.append(array[i]+sp);
		}
		System.out.println(buffer.toString());
		return buffer.toString();
	}
	public void test() {
	
		String rest="11.2:21.45:21.63:20.55:20.80:2.8:5000:12.2:10.2"
//					   1	   2     3     4     5   6	  7    8       
			 	+ ":20.89:20.88:20.86:20.85:20.801:20.79:20.78:20.77:20.76:20.75"
//				 	9    10    11    12    13     14    15    16    17    18       
			 	+ ":22:14:8:72:31:42:85:147:63:84";
//				  19 20 2122 23 24 25 26  27 28  
	
		String rest2="0:0:0:0:0:8:0:0:0"
//				   1	   2     3     4     5   6	  7    8       
		 	+ ":0:0:0:0:0:0:0:0:0:0"
//			 	9    10    11    12    13     14    15    16    17    18       
		 	+ ":0:0:0:0:0:0:0";
//			  19 20 2122 23 24 25 26  27 28  
		String str=null;
		String [] substr=rest2.split(":");
		substr[1]="11.2";
		System.out.println(joinStringSplit(substr, ":"));
	}
	public static void main(String[] args) {
		 
		String str=" [ "+
				"		        {"+
				"		            \"group\": \"组名\", "+
				"		            \"index\": ["+
				"		                {"+
				"		                    \"name\": \"名字\", "+
				"		                    \"stockID\": \"股票代码\", "+
				"		                    \"increase\": \"ture|flase\", "+
				"		                    \"price\": \"价格\", "+
				"		                    \"value\": \"增长率\""+
				"		                }"+
				"		                "+
				"		            ]"+
				"		        }, "+
				"		        {"+
				"		            \"group\": \"组名\", "+
				"		            \"index\": ["+
				"		                {"+
				"		                    \"name\": \"名字\", "+
				"		                    \"stockID\": \"股票代码\", "+
				"		                    \"increase\": \"ture|flase\", "+
				"		                    \"price\": \"价格\", "+
				"		                    \"value\": \"增长率\""+
				"		                },"+
				"{"+
					"		                    \"name\": \"名字\", "+
					"		                    \"stockID\": \"股票代码\", "+
					"		                    \"increase\": \"ture|flase\", "+
					"		                    \"price\": \"价格\", "+
					"		                    \"value\": \"增长率\""+
					"		                },"+
				"		                 "+
				"		            ]"+
				"		        },"+
				"		         "+
				"		    ]";
		str=str.substring(0,str.lastIndexOf(","))+"]";
		str=str.substring(0,str.lastIndexOf(","))+" ]}]";
	}
}
