package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import com.yetthin.web.commit.RedisUtil;
import com.yetthin.web.commit.ValueFormatUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

public class JHdboa implements ValueFormatUtil{
	
	private static JedisPool poolM=RedisUtil.getInstanceMsater();
	
	private static Jedis jedis_M=null;
	
	static{
		 
		jedis_M=poolM.getResource();
	}
	
	
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
    
    public void disconnected()
    {
    	System.out.println("disconnected");
    }
    public void UpdateHistoricalBarData(long tickId,BarData bar,int sendFlag)
    {
    	 
    	System.out.println(tickId+" "+sendFlag+" "+bar);
    	 String value=jedis_M.get(bar.contract.symbol.toUpperCase());
    	 
    	 String [] subStr=value.split(SPLIT_STR);
    	 if(tickId>=8000){//日线
 		subStr[OPEN_INDEX]=doubleformat.format(bar.open);
 		subStr[PRE_CLOSE_INDEX]=doubleformat.format(bar.ystClose);
 		subStr[LIMIT_UP_INDEX]=doubleformat.format(bar.open*1.1);
 		subStr[LIMIT_DOWN_INDEX]=doubleformat.format(bar.open*0.9);
// 		 
 		value =joinStringSplit(subStr, SPLIT_STR);
 		jedis_M.set(bar.contract.symbol.toUpperCase(), value);
    	 }else{
    		 if(tickId>1000&&tickId<8000){// 分钟线
    			 subStr[DATE_INDEX]=format.format(new Date());
    			 
    		 }
    	 }
    	 
    }
    public void UpdateHistoricalTick(long tickId,TickData tick,int sendFlag)
    {
    	System.out.println("tickData:"+tickId+" "+sendFlag+" "+tick.contract.exchange+" "+tick.tickPrice+" "+getDateStr(tick.dateTime*1000)+" "+tick.volume);
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
		Contract contract=new Contract();
		contract.symbol="002362";
		contract.currency="CNY";
		contract.exchange="SZ";
		contract.secType="STK";
		//System.out.println(getDateStr(System.currentTimeMillis()-1000*60*60*24)+getDateStr(System.currentTimeMillis()));
		int status=jhd.HdboaReqHistoricalData(8000, contract,(System.currentTimeMillis()-1000*60*60*25)/1000, System.currentTimeMillis()/1000,CYCTYPE.CYC_DAY.ordinal(), 1, UseRTH.USE_RTH.ordinal());
		System.out.println(status);
	//	status=jhd.HdboaReqHistoricalTickData(3, contract, (System.currentTimeMillis()-1000*60*60*24)/1000, System.currentTimeMillis()/1000,UseRTH.USE_RTH.ordinal());
	}
	private static String getDateStr(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		Formatter ft=new Formatter(Locale.CHINA);
		return ft.format("%1$tY年%1$tm月%1$td日%1$tA，%1$tT %1$tp", cal).toString();
		}

}
