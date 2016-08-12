package com.yetthin.web.commit;

import org.springframework.beans.factory.InitializingBean;

import util.JTdoa;

public class JtdoaUtil implements InitializingBean{
	private JtdoaUtil(){};
	private static volatile JTdoa jTdoa=null;
	
	public static JTdoa getInstance(){
		if(jTdoa==null){
			synchronized(JtdoaUtil.class){
				if(jTdoa==null){
					jTdoa =new JTdoa();
					jTdoa.TDOACreate();
					jTdoa.TDOAConnect("222.173.29.210", 7005);
					while(!jTdoa.isConnected()){
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					//	System.out.println("wait connect----------");
					}
					if(jTdoa.isConnected()){
						System.out.println("login has ready ------------");
						int ls=jTdoa.TDOALoginServer("td5", "td5",  "90-b1-1c-80-a4-78", 0, "1");
						System.out.println("ls ls ="+ls);
					}
				 
				while(!jTdoa.isLogined()){
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println("wait login ----------------");
				}
				if(jTdoa.isLogined()){
					System.out.println("login has ready ===================");
				//	jtdoa.TDOADestory();
				}
				}
			}
		}
		return jTdoa;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println(" aftet ------------------");
	}

	 
}
