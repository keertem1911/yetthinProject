package util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.yetthin.web.commit.RedisUtil;
import com.yetthin.web.commit.ValueFormatUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JHdboa implements ValueFormatUtil{
	
	private static JedisPool poolM=RedisUtil.getInstanceMsater();
	
		
	private List<TickSort> tickSorts=null;
	
	
	public boolean isset=false;
	SimpleDateFormat mm_ddformat =new SimpleDateFormat("HH:mm:ss");
	public boolean connected=false;
	public JHdboa() {
		// TODO Auto-generated constructor stub
	}
	public native void HdboaInit(BarData bar,Contract contract,TickData tick);
	
	public native int HdboaConnect(String addr,int port);
	public native void HdboaDestory();
	public native void HdboaDisconnect();
	public native int HdboaReqHistoricalData(long tickId,Contract contract,long beginDatetime,long endDatetime,int cycType,int ncyctype,int userth);
	public native int HdboaReqHistoricalTickData(long tickId,Contract contract,long beginTime,long endTime,int useRth);
    static {
        System.loadLibrary("JHdboa");//
    }
    
    public void error(long tickId,long code,String message)
    {
    	System.out.println(tickId+" "+code+" "+message);
    }
    public static String getWeekOfDate(long time) {
    	Date date=new Date(time);
    	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return simpleDateFormat.format(date)+"-"+weekDays[w];
    }
    private  String joinStringSplit(String [] array,String sp){
  		StringBuffer buffer=new StringBuffer();
  		for (int i = 0; i < array.length; i++) {
  			
  			buffer.append(array[i]);
  			if(i!=array.length-1)
  				buffer.append(sp);
  				
  		}
  		 
  		return buffer.toString();
  	}
    public void connected()
    {
    	System.out.println("connected");
    	connected=true;
    }
    public boolean end =false;
    public void disconnected()
    {
    	System.out.println(Thread.currentThread().getName());
    	System.out.println("disconnected");
    	 Thread.currentThread().interrupt();
    	 end=true;
    	 System.out.println("aaa");
    }
    public void UpdateHistoricalBarData(long tickId,BarData bar,int sendFlag)
    {
    	  
    	System.out.println(tickId+" "+sendFlag+" "+bar);
    	 	 
    	 
    	 if(tickId>=8000){//日线
    		 	System.out.println(bar);
    		 	isset=true;
 		 }else{
    		 
 			 if(tickId>1000&&tickId<8000){// 分钟线
    			 
    		 }
    	 }
    	 
    }
    public List<TickSort> getTickSorts() {
    	 List<TickSort> list1= new LinkedList<>();
		 
    	return list1;
	}
    public void setTickSorts(List<TickSort> sort){
    	this.tickSorts=sort;
    }
     
    public void UpdateHistoricalTick(long tickId,TickData tick,int sendFlag)
    {
    	if(tickSorts!=null){
    	System.out.println(Thread.currentThread().getName());
    	isset=true;
    	String date=mm_ddformat.format(tick.dateTime*1000);
    	TickSort tickSort = new TickSort(tick, date);
    	tickSorts.add(tickSort);
     	System.out.println(tickSort);
    	}
    	System.out.println("tickData:"+tickId+" "+sendFlag+" "+tick+" "+tick.dateTime*1000);
    }
    public boolean isIsset() {
		return isset;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JHdboa jhd=new JHdboa();
		jhd.HdboaInit(new BarData(),new Contract(),new TickData());
		jhd.HdboaConnect("222.173.29.210", 7008);
		while(!jhd.connected)
		{
			System.out.println("wait");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		  SimpleDateFormat yy_MM_ddformat =new SimpleDateFormat("yyyy:MM:dd");
		 SimpleDateFormat ALL_format =new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		 List<TickSort> list=null;
		 Contract contract=new Contract();
		 contract.symbol="002362";
		 contract.currency="CNY";
		 contract.exchange="SZ";
		 contract.secType="STK";
		 int cntTime=0;
		 
		 try {
			 long current_Time=System.currentTimeMillis();
		 String dateStr=yy_MM_ddformat.format(current_Time);
		 Date endTime =ALL_format.parse(dateStr+" 15:01:00");
		 Date beginTime =ALL_format.parse(dateStr+" 09:30:00");
		 long fromTime=0l;
		 long toTime=0l;
		 // 起始时间与终止时间之差 10分钟
		 long index_time=1000*60*30;
		 // 第二天请求昨天的
		 long yestday=1000*60*60*24;
		 do{
			 jhd.setTickSorts(null);
				 if(endTime.getTime()<current_Time){// 超过 收盘时间
					 fromTime=endTime.getTime()-index_time-cntTime;
					 toTime=endTime.getTime();
				 }else{
					 if(beginTime.getTime()>current_Time){// 先于  开盘时间
						 fromTime=endTime.getTime()-index_time-cntTime-yestday;
						 toTime=endTime.getTime()-yestday;
					 }else{
						 toTime=current_Time;
						 fromTime=current_Time-index_time-cntTime;
					 }
				 }
				 
				 list=jhd.getTickSorts();
				 System.out.println(Thread.currentThread().getName());
				 System.out.println("from "+new Date(System.currentTimeMillis()-1000*60*60*20)+"  to "+new Date(toTime));
				 jhd.HdboaReqHistoricalTickData(3, contract, fromTime/1000, toTime/1000,UseRTH.USE_RTH.ordinal());
//				 jhd.HdboaReqHistoricalData(8002, contract,(System.currentTimeMillis()-1000*60*60*20)/1000, (System.currentTimeMillis())/1000,CYCTYPE.CYC_DAY.ordinal(), 1, UseRTH.USE_RTH.ordinal());
				 int cnt=0;
				 while(!jhd.isset){
					Thread.sleep(100);
					cnt++;
					if(cnt==50)
						break;
//				 	System.out.println(jhd.isset);
				}
					 Thread.sleep(1000);
					 System.out.println(list.size());
					 cntTime+=1000*60;
		 }while(false);
		 if(jhd.isset==false){
			 System.out.println("time errir");
		 }
		 } catch (Exception e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 } 
		 
		System.out.println("ceoom i   =---------------------------");
		 
			jhd.setTickSorts(null);
			jhd.HdboaDisconnect();
			jhd.HdboaDestory();
			System.out.println(Thread.currentThread().getName());
			while(!jhd.end);
		System.out.println("sa");
		jhd=null;
	}
	private static String getDateStr(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		Formatter ft=new Formatter(Locale.CHINA);
		return ft.format("%1$tY年%1$tm月%1$td日%1$tA，%1$tT %1$tp", cal).toString();
		}

}
