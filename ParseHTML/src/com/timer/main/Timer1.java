package com.timer.main;

import java.util.Date;
/**
 * 定时模块
 * @author keerte
 *
 */
public class Timer1 implements Runnable{
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 long before=0l;
		 long currency=0l;
		final  long timerCnt=60;
		
		while(true){
			if(currency==before){
				before=System.currentTimeMillis();
			}
			currency=System.currentTimeMillis();
			if((currency-before)/1000==timerCnt){
				System.out.println(new Date(currency));
				before=currency;
			}
		}
	}
	public static void main(String[] args) {
		Timer1 timer = new Timer1();
		Thread t=new Thread(timer);
		t.start();
	}	
}
