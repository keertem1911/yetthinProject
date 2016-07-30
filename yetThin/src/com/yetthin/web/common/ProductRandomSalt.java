package com.yetthin.web.common;

import java.util.Random;

public class ProductRandomSalt {
	private static final String []saltArratys=new String[]{
		"1","3","5","7","11",
		"13","17","37","23","29",
		"31","a","b","c","d",
		"e","f","g","z","y",
		"i","r","t","s","p"
	};
	private static final int MAX_LENGTH=17;
	private static int getLength(){
		Random random=new Random(System.currentTimeMillis());
		int len=10;
		int templen=0;
		while(len>templen) templen=random.nextInt(MAX_LENGTH+1);
		len=templen;
	 	return len;
	}
	public static String getSalt(){
		StringBuffer bf=new StringBuffer();
		Random random=new Random(System.currentTimeMillis());
		int length=getLength();
		for(int i=0;i<length;++i){
		int index=random.nextInt(25);
		 	bf.append(saltArratys[index]);
		}
		return bf.toString();
	}
	 
}
