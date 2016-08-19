package zcom.yetthin.web.listener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.yetthin.web.commit.JtdoaValueMarket;
import com.yetthin.web.commit.QQMarketLevelUtilByMaster;
import com.yetthin.web.commit.QQMarketLevelUtilBySimple;
import com.yetthin.web.commit.RedisOfReader;
import com.yetthin.web.commit.SinaMarketIndex;
import com.yetthin.web.dao.JtdoaAPIDao;
import com.yetthin.web.dao.UrlRequestDao;
import com.yetthin.web.test.ReadTextSymbol;

import util.Contract;


public class CreateJdoaListener implements ServletContextListener,
QQMarketLevelUtilBySimple,QQMarketLevelUtilByMaster,
SinaMarketIndex,JtdoaValueMarket{

	private JtdoaAPIDao jtdoaAPIDao=new JtdoaAPIDao();
	
	private static final SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm");
	private static final String END_TIME ="23:30";
	private static final String START_TIME="08:45";
	private static final int dev_num=20;
	private static final String FILE_NAME_PATH="d:/symbol.txt";
	private UrlRequestDao urlRequestDao=new UrlRequestDao();
	private final static long SECOND=1000;
	private final static long MINUTE=SECOND*60;
//	private JTdoa jtdoa;
//	private JHdboa jhdboa;
	private Executor executor = Executors.newFixedThreadPool(200);
	private  List<Contract> list;
	  
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}
	private boolean  AfterTimeCompared(String current,String target){
		boolean aftercompared=false;
		// 09:31  09:30
		if(current.split(":")[0].compareTo(target.split(":")[0])>0){
			aftercompared=true;
		}else{
			if(current.split(":")[0].compareTo(target.split(":")[0])==0){
				if(current.split(":")[1].compareTo(target.split(":")[1])>0){
					aftercompared=true;
				}
			}
		}
		return aftercompared;
	}
	private void init() {
//		 jtdoa = JtdoaUtil.getInstanceJTdoa();
//		jhdboa =JtdoaUtil.getInstanceJHdboa();
		System.out.println("come into --------------------");
		executor.execute(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				ReadTextSymbol test = new ReadTextSymbol();
 				  
 				list=test.readTextByContract(FILE_NAME_PATH);
				List<String> symbols = test.readSymolByString(FILE_NAME_PATH);
//				RedisOfReader.initReadInredisKeyLevel1(list);
				 //股票 更新 详细信息   开盘价  收盘价 摆单情况
				
				while(true){
				
				String currentTime = dateFormat.format(System.currentTimeMillis());
				if(AfterTimeCompared(currentTime, START_TIME)&&!AfterTimeCompared(currentTime, END_TIME)){
				for(int j=0;j<symbols.size()/dev_num;++j){
					StringBuffer sb=new StringBuffer();
					int cnt=dev_num;
					if(j==((symbols.size()/dev_num))&&symbols.size()%1000!=0)
						cnt=symbols.size()%dev_num;
				 for (int i = 0; i < cnt; i++) {
					 if(cnt!=dev_num){
						 System.out.println(j*dev_num+i+":"+symbols.get(j*dev_num+i));
					 }
					sb.append(symbols.get(j*dev_num+i));
					 if(i!=symbols.size()-1)
						sb.append(",");
				}
				 
				 try {
					 List<String> values=	urlRequestDao.readContentFromGet(QQ_M_REQUEST_URL+sb.toString());
					 
					 jtdoaAPIDao.saveQQ_M_REQUEST_URL(values);
				 } catch (IOException e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
				}// end in  for(int j=0;j<symbols.size()/dev_num;++j){ 
				try {
				StringBuffer sb=new StringBuffer(); 
				for (int i = 0; i < symbols.size()%dev_num; i++) {
					  
					sb.append(symbols.get((symbols.size()/dev_num)*dev_num+i));
//				 	System.out.println((symbols.size()/dev_num)*dev_num+i);
					if(i!=symbols.size()-1)
						sb.append(",");
				}
				 
				 List<String> values=	urlRequestDao.readContentFromGet(QQ_M_REQUEST_URL+sb.toString());
				 
				 jtdoaAPIDao.saveQQ_M_REQUEST_URL(values);
						Thread.sleep(MINUTE<<2);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 				 }// if
				}//while
//				for (int i = 0; i < list.size(); i++) {
//					if(tickId<0)
//						tickId=0;
//			 		jtdoa.TDOASubscibeLevel1Data(tickId++, list.get(i));
//			 		if(tickId<0)
//			 			tickId=0;
//			 		jtdoa.TDOASubscribeMarketDepth(tickId++, list.get(i), 4);
//			 		if(tickId<0)
//			 			tickId=0;
//			 		jtdoa.TDOASubscribeMarketData(tickId++, list.get(i));
//			 		if(tickId<0)
//			 			tickId=0;
//				}
// 				  RedisOfReader.initReadInredisKeyLevel1(list);
//  				  RedisOfReader.initNameToSymbol(names);
			}
		});
		/*
		 *  指数发送
		 */
		executor.execute(new Runnable() {
			public void run() {
				System.currentTimeMillis();
				
				String [] [] husheng=HU_SHEN_STOCK_INDEX;
				while(true){
					String currentTime = dateFormat.format(System.currentTimeMillis());
					if(AfterTimeCompared(currentTime, START_TIME)&&!AfterTimeCompared(currentTime, END_TIME)){
				
		    	StringBuffer sb=new StringBuffer();
		    	for (int i = 0; i < husheng.length; i++) {
					sb.append("s_"+husheng[i][0].substring(7).toLowerCase()+husheng[i][0].substring(0, 6));
					if(i<husheng.length-1)
						sb.append(",");
		    	}
		    	try {
					List<String> values=urlRequestDao.readContentFromGet(SINA_I_REQUEST_URL+sb.toString());
					jtdoaAPIDao.saveSina_REQUEST_URL_INDEX(values);
		    	} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
		    	try {
					Thread.sleep(SECOND*20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					}//if
			}//while 
				 
				}
		});
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		Runnable run=new Runnable() {
			public void run() {
			 
			 	init();
			}
		};
		Thread thread=new Thread(run);
		
		thread.setDaemon(true);
		thread.start();
	}

}
