package zcom.yetthin.web.listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.yetthin.web.commit.JtdoaUtil;
import com.yetthin.web.commit.RedisOfReader;
import com.yetthin.web.test.ReadTextSymbol;

import util.CYCTYPE;
import util.Contract;
import util.JHdboa;
import util.JTdoa;
import util.UseRTH;

public class CreateJdoaListener implements ServletContextListener {

	private JTdoa jtdoa;
	private JHdboa jhdboa;
	private Executor executor = Executors.newFixedThreadPool(200);
	private static final String UPDATE_TIME="14:33:30";
	private  List<Contract> list;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	private void init() {
		 jtdoa = JtdoaUtil.getInstanceJTdoa();
		jhdboa =JtdoaUtil.getInstanceJHdboa();
		System.out.println("come into --------------------");
		executor.execute(new Runnable() {
			public void run() {
				ReadTextSymbol test = new ReadTextSymbol();

				// String
				// path=request.getSession().getServletContext().getRealPath("/WEB-INF/classes");
				// System.out.println(path);

				list= test.readTextByContract("e:/symbol.txt");
				System.out.println("tdia 0-00000000000000000");
				for (int i = 0; i < list.size(); i++) {

			 		jtdoa.TDOASubscibeLevel1Data(1000+i, list.get(i));
			 		jtdoa.TDOASubscribeMarketDepth(1000+i, list.get(i), 4);
				}
 				  RedisOfReader.initReadInredisKeyLevel1(list);
			}
		});
//		executor.execute(new Runnable() { //秒线请求
//			
//			public void run() {
//				
//			}
//		});
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		Runnable run=new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			 	init();
				int count=0;
				while(true){
					 
				long timedate=System.currentTimeMillis();
				Date date=new Date(timedate);
				SimpleDateFormat simple=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String format_time = simple.format(date);
				String now_date=format_time.substring(format_time.lastIndexOf(" ")+1);
				String hourse=format_time.substring(0,format_time.lastIndexOf(" ")+1);
				hourse+=UPDATE_TIME;
				 
				Date  date1=null;
				
				try {
					 date1=simple.parse(hourse);
					 while(now_date.trim().equals(UPDATE_TIME)&&count==0){
						 for (int i = 0; i < list.size(); i++) {
							
							 jhdboa.HdboaReqHistoricalData(8001+i, list.get(i),(System.currentTimeMillis()-1000*60*60*12)/1000, (System.currentTimeMillis())/1000,CYCTYPE.CYC_DAY.ordinal(), 1, UseRTH.USE_RTH.ordinal());
						}
//						 System.out.println("come into call ");
//						 Contract contract=new Contract();
//							contract.symbol="002362";
//							contract.currency="CNY";
//							contract.exchange="SZ";
//							contract.secType="STK";
//						 jhdboa.HdboaReqHistoricalData(9,contract  ,(System.currentTimeMillis()-1000*60*60*24*2)/1000, System.currentTimeMillis()/1000,CYCTYPE.CYC_DAY.ordinal(), 1, UseRTH.USE_RTH.ordinal());
						 count++;
					 }
					 if(!now_date.trim().equals(UPDATE_TIME)&&count ==1){
						 count=0;
					 }
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				}
			 
			}
		};
		Thread thread=new Thread(run);
		
		thread.setDaemon(true);
		thread.start();
	}

}
