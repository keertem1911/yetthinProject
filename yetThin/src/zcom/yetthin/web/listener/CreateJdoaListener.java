package zcom.yetthin.web.listener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
	
	private static final int [] START_END_TIME={9,20,16,30};
	private static final int [] END_START_MIDDLE={11,00,13,00};
	
	private static final int dev_num=20;
	private  String FILE_NAME_PATH;
	private UrlRequestDao urlRequestDao=new UrlRequestDao();
	private final static long SECOND=1000;
	private boolean initFlag;
 
	public CreateJdoaListener() {
		// TODO Auto-generated constructor stub
	}
	private Executor executor = Executors.newFixedThreadPool(200);
	private  List<Contract> list;
	private static final String END_TIME_1 ="11:00";
	private static final String START_TIME_1="08:45";  
	private static final String END_TIME_2 ="15:00";
	private static final String START_TIME_2="13:05";  
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
	private static final SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm");
	 private boolean isTimeOut(){
		 boolean timeOut=true;
		 	//START_END_TIME={9,20,16,30};
		    //END_START_MIDDLE={11,00,13,00};
		 Calendar cal = Calendar.getInstance();
		 String currentTime = dateFormat.format(System.currentTimeMillis());
		if(AfterTimeCompared(currentTime, START_TIME_1)&&!AfterTimeCompared(currentTime, END_TIME_1)
				||AfterTimeCompared(currentTime, START_TIME_2)&&!AfterTimeCompared(currentTime, END_TIME_2)){
			 if(cal.get(Calendar.DAY_OF_WEEK)>1&&cal.get(Calendar.DAY_OF_WEEK)<7){
				 timeOut=false;
			 }
		}
		 return timeOut;
	 }
 
	private void init() {
//		 jtdoa = JtdoaUtil.getInstanceJTdoa();
//		jhdboa =JtdoaUtil.getInstanceJHdboa();
		System.out.println("come into --------------------");
		
		 
		executor.execute(new Runnable() {
			 
			public void run() {
				ReadTextSymbol test = new ReadTextSymbol();
				 long before=0l;
				 long currency=0l;
				final  long timerCnt=60; 
				boolean beforeB=true;
				List<String> symbols = test.readSymolByString(FILE_NAME_PATH);
				List<Contract> list = test.readTextByContract(FILE_NAME_PATH);
				if(initFlag)
   				RedisOfReader.initReadInredisKeyLevel1(list);
				 //股票 更新 详细信息   开盘价  收盘价 摆单情况
				Calendar calStock1=Calendar.getInstance();
				 
				 
				while(true){
//					System.out.println(" begin ===========");
					if(beforeB)
						currency =before=System.currentTimeMillis();
					beforeB=false;
					list=test.readTextByContract(FILE_NAME_PATH);
				  
				if(!isTimeOut()){
				for(int j=0;j<symbols.size()/dev_num;++j){
					StringBuffer sb=new StringBuffer();
					int cnt=dev_num;
					if(j==((symbols.size()/dev_num))&&symbols.size()%1000!=0)
						cnt=symbols.size()%dev_num;
				 for (int i = 0; i < cnt; i++) {
//					 if(cnt!=dev_num){
//						 System.out.println(j*dev_num+i+":"+symbols.get(j*dev_num+i));
//					 }
					sb.append(symbols.get(j*dev_num+i));
					 if(i!=symbols.size()-1)
						sb.append(",");
				}
				 
				 try {
					 List<String> values=urlRequestDao.readContentFromGet(QQ_M_REQUEST_URL+sb.toString());
					 
					 jtdoaAPIDao.saveQQ_M_REQUEST_URL(values,false);
				 } catch (IOException e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
				}// end in  for(int j=0;j<symbols.size()/dev_num;++j){ 股票更新完成 一轮
				//余数更新
				try {
				StringBuffer sb=new StringBuffer(); 
				for (int i = 0; i < symbols.size()%dev_num; i++) {
					  
					sb.append(symbols.get((symbols.size()/dev_num)*dev_num+i));
					if(i!=symbols.size()-1)
						sb.append(",");
				}//for
				 
				 List<String> values=	urlRequestDao.readContentFromGet(QQ_M_REQUEST_URL+sb.toString());
				 
				 jtdoaAPIDao.saveQQ_M_REQUEST_URL(values,false);
				 
						Thread.sleep(SECOND*12);
				/**
				 * 行业板块更新	
				 */
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				currency=System.currentTimeMillis();
//				System.out.println("curreny:"+dateFormat.format(currency)+" before:"+new Date(before)+" =>"+ (currency-before)/1000+"    cnt ========");
				if((currency-before)/1000>(timerCnt-5)){
					before=currency;
//					System.out.println(new Date(currency)+" ==========================");
					jtdoaAPIDao.updateMinuteKandIndex();
				}
 				 }else{//if timeOut
 					
 					}
				}//while
			}
		});
		/*
		 *  指数发送
		 */
		executor.execute(new Runnable() {
			public void run() {
				String [] [] husheng=HU_SHEN_STOCK_INDEX;
				if(initFlag)
					RedisOfReader.initReadInredisKeyLevel1Index(HU_SHEN_STOCK_INDEX);

				while(true){

					if(!isTimeOut()){
		    	StringBuffer sb=new StringBuffer();
		    	for (int i = 0; i < husheng.length; i++) {
					sb.append(""+husheng[i][0].substring(7).toLowerCase()+husheng[i][0].substring(0, 6));
					if(i<husheng.length-1)
						sb.append(",");
		    	}
		    	try {
					List<String> values=urlRequestDao.readContentFromGet(QQ_M_REQUEST_URL+sb.toString());
				    jtdoaAPIDao.saveQQ_M_REQUEST_URL(values,true);
		    	} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
		    	try {
					Thread.sleep(SECOND*10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					} //if
						 
					 
			}//while 
				 
				}
		});
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		String path=arg0.getServletContext().getInitParameter("path");
		String initFlag=(arg0.getServletContext().getInitParameter("initFlag"));
		this.initFlag=Boolean.parseBoolean(initFlag);
		FILE_NAME_PATH=path;
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
