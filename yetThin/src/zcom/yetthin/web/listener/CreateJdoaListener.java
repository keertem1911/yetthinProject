package zcom.yetthin.web.listener;

import java.io.IOException;
import java.util.Calendar;
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
	  
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}
	 private boolean isTimeOut(Calendar cal1,Calendar [] cals){
		 boolean timeOut=true;
		 	//START_END_TIME={9,20,16,30};
		    //END_START_MIDDLE={11,00,13,00};
		 
		 if(((cal1.after(cals[0])&&cal1.before(cals[1]))||(cal1.before(cals[2])&&cal1.before(cals[3])))){
			 timeOut=false;
			 
		 }
		 return timeOut;
	 }
	private Calendar [] getTimeEndAndBegin(){
		Calendar calTimeout1=Calendar.getInstance();				
		Calendar calTimeout2=Calendar.getInstance();				
		Calendar calTimeout3=Calendar.getInstance();				
		Calendar calTimeout4=Calendar.getInstance();				

		//设置小时
		calTimeout1.set(Calendar.HOUR_OF_DAY,START_END_TIME[0]);
		calTimeout2.set(Calendar.HOUR_OF_DAY,END_START_MIDDLE[0]);
		calTimeout3.set(Calendar.HOUR_OF_DAY,END_START_MIDDLE[2]);
		calTimeout4.set(Calendar.HOUR_OF_DAY,START_END_TIME[2]);
		//设置分钟
		calTimeout1.set(Calendar.MINUTE,START_END_TIME[1]);
		calTimeout2.set(Calendar.MINUTE,END_START_MIDDLE[1]);
		calTimeout3.set(Calendar.MINUTE,END_START_MIDDLE[3]);
		calTimeout4.set(Calendar.MINUTE,START_END_TIME[3]);
		Calendar [] cals=new Calendar[4];
		cals[0]=calTimeout1;
		cals[1]=calTimeout2;
		cals[2]=calTimeout3;
		cals[3]=calTimeout4;
		return  cals;
	}
	private void init() {
//		 jtdoa = JtdoaUtil.getInstanceJTdoa();
//		jhdboa =JtdoaUtil.getInstanceJHdboa();
		System.out.println("come into --------------------");
		
		final Calendar [] cals= getTimeEndAndBegin();
		executor.execute(new Runnable() {
			 
			public void run() {
				ReadTextSymbol test = new ReadTextSymbol();
 				  
 				list=test.readTextByContract(FILE_NAME_PATH);
				List<String> symbols = test.readSymolByString(FILE_NAME_PATH);
				if(initFlag)
   				RedisOfReader.initReadInredisKeyLevel1(list);
				 //股票 更新 详细信息   开盘价  收盘价 摆单情况
				Calendar calStock1=Calendar.getInstance();
				while(true){
				calStock1.setTimeInMillis(System.currentTimeMillis());
				 
				if(!isTimeOut(calStock1,cals)){
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
					 
					 jtdoaAPIDao.saveQQ_M_REQUEST_URL(values,false);
				 } catch (IOException e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
				}// end in  for(int j=0;j<symbols.size()/dev_num;++j){ 
				try {
				StringBuffer sb=new StringBuffer(); 
				for (int i = 0; i < symbols.size()%dev_num; i++) {
					  
					sb.append(symbols.get((symbols.size()/dev_num)*dev_num+i));
					if(i!=symbols.size()-1)
						sb.append(",");
				}
				 
				 List<String> values=	urlRequestDao.readContentFromGet(QQ_M_REQUEST_URL+sb.toString());
				 
				 jtdoaAPIDao.saveQQ_M_REQUEST_URL(values,false);
						Thread.sleep(SECOND*10);
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
				Calendar calStock2=Calendar.getInstance();
				
				
				String [] [] husheng=HU_SHEN_STOCK_INDEX;
				if(initFlag)
					RedisOfReader.initReadInredisKeyLevel1Index(HU_SHEN_STOCK_INDEX);

				while(true){
					calStock2.setTimeInMillis(System.currentTimeMillis());
					if(!isTimeOut(calStock2,cals)){
				
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
					}//if
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
