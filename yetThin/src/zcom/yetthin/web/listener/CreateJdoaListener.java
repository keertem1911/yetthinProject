package zcom.yetthin.web.listener;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.yetthin.web.commit.QQMarketLevelUtilByMaster;
import com.yetthin.web.commit.QQMarketLevelUtilBySimple;
import com.yetthin.web.commit.RedisOfReader;
import com.yetthin.web.commit.SinaMarketIndex;
import com.yetthin.web.dao.JtdoaAPIDao;
import com.yetthin.web.dao.UrlRequestDao;
import com.yetthin.web.test.ReadTextSymbol;

import util.Contract;


public class CreateJdoaListener implements ServletContextListener,QQMarketLevelUtilBySimple,QQMarketLevelUtilByMaster,SinaMarketIndex {

	private JtdoaAPIDao jtdoaAPIDao=new JtdoaAPIDao();
	
	private static final int dev_num=20;
	private UrlRequestDao urlRequestDao=new UrlRequestDao();
//	private JTdoa jtdoa;
//	private JHdboa jhdboa;
	private Executor executor = Executors.newFixedThreadPool(200);
	private  List<Contract> list;
	  
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	private void init() {
//		 jtdoa = JtdoaUtil.getInstanceJTdoa();
//		jhdboa =JtdoaUtil.getInstanceJHdboa();
		System.out.println("come into --------------------");
		executor.execute(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				ReadTextSymbol test = new ReadTextSymbol();
 				Map<String, Object> map= test.readTextByContract("d:/symbol.txt");
 				list=(List<Contract>) map.get("contracts");
				List<String> symbols = test.readSymolByString("d:/symbol.txt");
				RedisOfReader.initReadInredisKeyLevel1(list);
				 //股票 更新 详细信息   开盘价  收盘价 摆单情况
				
				while(true){
				for(int j=0;j<symbols.size()/dev_num;++j){
					StringBuffer sb=new StringBuffer();
					int cnt=dev_num;
					if(j==symbols.size()-1&&symbols.size()%1000!=0)
						cnt=symbols.size()%dev_num;
				 for (int i = 0; i < cnt; i++) {
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
				}
					 try {
						Thread.sleep(1000*60*5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 				 }
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
