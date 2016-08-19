package com.yetthin.web.serviceImp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yetthin.web.commit.JtdoaValueMarket;
import com.yetthin.web.commit.ValueFormatUtil;
import com.yetthin.web.dao.JtdoaAPIDao;
import com.yetthin.web.dao.JtdoaDao;
import com.yetthin.web.service.JtdoaService;

import redis.clients.jedis.Tuple;
import util.BarData;
import util.Contract;
import util.JHdboa;
import util.TickData;
import util.TickSort;
import util.TickType;
import util.UseRTH;

@Service("JtdoaService")
public class JtdoaServiceImp implements JtdoaValueMarket,ValueFormatUtil,JtdoaService{
	/**
	 * 指数通用模板
	 * @return
	 * "index": [
		        {
		            "name": "上证指数", 
		            "price": "价格", 
		            "stockID": "股票代码",
		            "increase":"ture|flase",
		            "growth": "增长量", 
		            "growthRate": "增长率"
		        }, 
		        {
		            "name": "深证成指", 
		            "price": "价格",
		            "stockID": "股票代码", 
		            "increase":"ture|flase",
		            "growth": "增长量", 
		            "growthRate": "增长率"
		        }
		    ], 
	 
	 * 指数格式为 [{指数代码,指数名称,当前价格,点数,涨跌率},{},{}]
	 * 			  0      1      2    3    4
	 * @param index
	 * @return
	 */
	private static final SimpleDateFormat yy_MM_ddformat =new SimpleDateFormat("yyyy:MM:dd");
	private static final SimpleDateFormat ALL_format =new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
	private String putIndex(String [] index){
		StringBuffer sb=new StringBuffer();
		sb.append("[");
		for (int i = 0; i < index.length; i++) {
			String [] subStr=index[i].split(JTDOA_SPLIT_STR);
			sb.append("{\"name\":\"" +subStr[1]+"\","+ 
		            "\"price\":\""+subStr[2]+"\","+
		            "\"stockID\":\""+subStr[0]+"\",");
			boolean plus=true;
			 
			if(subStr[4].indexOf("-")!=-1){
				plus=false;
				subStr[4]=subStr[4].substring(1);
			}
			
			sb.append("\"increase\":\""+plus+"\","+
		            "\"growth\":\""+subStr[3]+"\","+ 
		            "\"growthRate\": \""+subStr[4]+"\"}");
			if(i<index.length-1)
				sb.append(",");
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	/**
	 * 股票市场 l1 模板
	 *  [
		        {
		            "group": "组名", 
		            "index": [
		                {
		                    "name": "名字", 
		                    "stockID": "股票代码", 
		                    "increase": "ture|flase", 
		                    "price": "价格", 
		                    "value": "增长率"
		                }
		                
		            ]
		        }, 
		        {
		            "group": "组名", 
		            "index": [
		                {
		                    "name": "名字", 
		                    "stockID": "股票代码", 
		                    "increase": "ture|flase", 
		                    "price": "价格", 
		                    "value": "增长率"
		                }
		                 
		            ]
		        },
		         
		    ]
	 * @param params 
	 * @return
	 * @param Map<String,List<String>> market 
	 * 			 	组名    有序set 
	 * 			0		1	 2	  3			
	 * List  601595.SH:N上影:14.67:43.96
	 * 			  股票代码:名称:  价格:涨跌幅率
	 */
	private String putStockMarketData(Map<String,List<String>> market){
		String newStr=null;
		StringBuffer sb=new StringBuffer();
		if(!market.isEmpty()){
		sb.append("[");
		Set<Entry<String, List<String>>> entrySet=market.entrySet(); 
		for (Entry<String, List<String>> entry2 : entrySet) {
			StringBuffer subSb=new StringBuffer();
			subSb.append("{\"group\":\""+NAME_MARKET[Integer.parseInt(entry2.getKey().split(JTDOA_SPLIT_STR)[1])]+"\",");
			subSb.append("\"index\":[");
			List<String> tu1=entry2.getValue();
			for (String tuple : tu1) {
				String []values = tuple.split(SPLIT_STR);//  股票代码:名称:价格:涨跌幅率
				 
				subSb.append(  "{\"name\":\""+values[1]+"\","+ 
		                    "\"stockID\":\" "+values[0].substring(0, 6)+"\",");
				boolean plus =true;
				if(values[3].indexOf("-")!=-1){
					plus=false;
					values[3]=values[3].substring(1);
				}
				subSb.append("\"increase\":\""+plus+"\","+ 
		                    "\"price\": \""+values[2]+"\","+ 
		                    "\"rate\": \""+values[3]+"\"},");
			}
			
			String str=subSb.toString();
			str=str.substring(0,str.lastIndexOf(","))+"]},";
			sb.append(str);
			
		}
		
		
		newStr= sb.toString();
		 
		newStr=newStr.substring(0,newStr.lastIndexOf(","))+"]";
//		str=str.substring(0,str.lastIndexOf(","))+" ";
		
		return newStr;
		}
		return sb.toString();
	}
	/**
	 * [
		        {
		            "group": "组名", 
		            "index": [
		                {
		                    "name": "名字", 
		                    "stockID": "股票代码", 
		                    "increase": "ture|flase", 
		                    "price": "价格", 
		                    "value": "增长率"
		                }
		            ]
		        }, 
		        {
		            "group": "组名", 
		            "index": [
		                {
		                    "name": "名字", 
		                    "stockID": "股票代码", 
		                    "increase": "ture|flase", 
		                    "price": "价格", 
		                    "value": "增长率"
		                },
		            ]
		        },
		    ]
	 */
	@Resource
	private JtdoaDao jtdoaDao;
	
	@Resource
	private JtdoaAPIDao jtdoaAPIDao;
	
	
	/**
	 *  获取l1值 包括 股指 股票涨跌分类等
	 * @param id
	 * @return 字符数组  1 状态码 2 指数的JSON 3 股票的分类排行L1 4 错误信息 
	 */
	@Override
	@Transactional
	public String []  getL1(int id,long begin,long end,String market){
		String [] subMarket=market.split(SPLIT_STR);
		int marketIndex=Integer.parseInt(subMarket[0]);
		// level1 属性 涨跌幅及换手率等
		String [] params= subMarket[1].split(",");
		String [] subStr=new String [4];
		if(begin<end&&begin>=0&&end>0){
		switch(id){
		case HU_SHEN: 
			String [] index= jtdoaDao.getStockIndex(marketIndex,0,6);
			subStr[1]=putIndex(index);// 指数解析
		 	Map<String,List<String>> marketS =jtdoaDao.getL1StockMarketData(HU_SHEN,begin,end,params);
			//模拟数据
		//	Map<String,Set<Tuple>> market =jtdoaDao.getL1StockMarketData(HU_SHEN,false);
			
			subStr[2]=putStockMarketData(marketS);
			if((subStr[1].equals("")||subStr[1]==null)&&(subStr[2].equals("")||subStr[2]==null)){
				subStr[0]="520";//
				subStr[3]="level1 data 查询失败";
			}else{
				subStr[0]="200";
				subStr[3]=" ";
			}
			break;
		}
		}else{
			subStr[0]="502";
			subStr[1]="[]";
			subStr[2]="输入错误";
		}
		return subStr;
	}
	 
	/**
	 * @param 	String symbol 股票代码 000001.SH
	 * @return  字符数组  0 状态码 1 股票对应的level2的JSON 2 错误信息
	 */
	@Override
	@Transactional 
	public String[] getL2(String symbol) {
		String [] subStr=new String [3];
		// TODO Auto-generated method stub
			String level2= jtdoaDao.getL2(symbol);
			if(level2==null||"".equals(level2.trim())){
				subStr[0]="520";
				subStr[2]="level2 代码为空";
			}else{
				String [] subValue=level2.split(SPLIT_STR);
				subStr[1]=putLevel2Depth(subValue);
				subStr[0]="200";
				subStr[2]="";
				
			}
		return subStr;
	}
	/**
	 * level2模板格式 
	 * { 	"level1":{"last":"11.2","open":"12.1",
	 * 			"updown":"12.1","rate":"1.1%","increate":"false",
	 * 			"low":"12.11","height":"13.11","exchange":"1.1%"
	 * 			"volum":"11112","totlesum":"121122(万)"},
	 * 		"level2":{"buy5":["1.1","200"],"buy4":["2.1","500"]}
	 * }
	 * 
	 * 将level2 的字符串转换为 json  的 item格式
	 * @param subValue
	 * @return
	 */
	private String putLevel2Depth(String[] subValue) {
		// TODO Auto-generated method stub
		StringBuffer sb1=new StringBuffer();
		sb1.append("\"level1\":{");
		sb1.append("\"open\":\""+subValue[OPEN_INDEX]+"\",");
		sb1.append("\"last\":\""+subValue[LAST_PRICE_INDEX]+"\",");
		sb1.append("\"updown\":\""+subValue[UP_DOWN_PRICE]+"\",");
		sb1.append("\"exchange\":\""+subValue[EXCHANGE_RATE]+"\",");
		boolean plus=true;
		String plusPrice=subValue[UP_DOWN_PRICE_RATE];
		if(subValue[UP_DOWN_PRICE_RATE].indexOf("-")!=-1){
			plus=false;
			plusPrice=plusPrice.substring(1);
		}
		sb1.append("\"rate\":\""+plusPrice+"\",");
		sb1.append("\"increate\":\""+plus+"\",");
		
		sb1.append("\"low\":\""+subValue[LOW_INDEX]+"\",");
		sb1.append("\"height\":\""+subValue[HEIGHT_INDEX]+"\",");
		sb1.append("\"totlesum\":\""+subValue[TOTLE_SUM_INDEX]+"\",");
		sb1.append("\"volume\":\""+subValue[VOLUME_INDEX]+"\"");
		sb1.append("}");
		/**
		 * "level2":{"buy5":["1.1","200"],"buy4":["2.1","500"]}
		 */
		StringBuffer sb=new StringBuffer();
		
		sb.append("{");
		for (int i = 0; i < 5; i++) {
			sb.append("\"sell"+(5-i)+"\":");
			sb.append("[");
			sb.append("\""+subValue[LEVEL2_INDEX_SIDE1+i]+"\",");
			sb.append("\""+subValue[LEVEL2_INDEX_SIDE1+10+i]+"\"");
			sb.append("],");
		}
//		sb.append(",");
		for (int i = 1; i < 6; i++) {
			sb.append("\"buy"+i+"\":");
			sb.append("[");
			sb.append("\""+subValue[LEVEL2_INDEX_SIDE0+i-1]+"\",");
			sb.append("\""+subValue[LEVEL2_INDEX_SIDE0+10+i-1]+"\"");
			sb.append("],");
		}
		sb.append("}");
		String value=sb.toString();
		value = value.substring(0,value.lastIndexOf(","))+"}";
		value="{"+sb1.toString()+",\"level2\":"+value+"}";
		return value;
	}
	private static JHdboa getInstanceJhdboa(){
		 JHdboa jHdboa=new JHdboa();
	 	 jHdboa.HdboaInit(new BarData(),new Contract(),new TickData());
			
		jHdboa.HdboaConnect("222.173.29.210", 7008);
		while(!jHdboa.connected)
		{
			System.out.println("wait");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jHdboa; 
	}
	@Override
	public String [] getLevel2Detail(String symbol) {
		// TODO Auto-generated method stub
//		JHdboa jhdboa=getInstanceJhdboa();
//		Contract contract=new Contract();
//		contract.symbol="002362";
//		contract.currency="CNY";
//		contract.exchange="SZ";
//		contract.secType="STK";
//		String dateStr=yy_MM_ddformat.format(System.currentTimeMillis());
//		long startTime=0l;
//		try {
//			Date date=ALL_format.parse(dateStr+" 09:00:01");
//			startTime=date.getTime();
//			jhdboa.HdboaReqHistoricalTickData(3, contract, (startTime-1000*60)/1000, System.currentTimeMillis()/1000,UseRTH.USE_RTH.ordinal());
//			List<TickSort> lists=jhdboa.getLevel2Detail();
//			Thread.sleep(1000*10);
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
		List<TickSort> list=jtdoaDao.getLevel2Detail(symbol);
		String [] subStr=new String[3];
		String json =putLevel2Detail(list);
		if(json==null||"".equals(json)){
			subStr[0]="520";
			subStr[2]="level2交易记录失败";
			subStr[1]="{}";		
		}else{
			subStr[0]="200";
			subStr[2]="";
			subStr[1]=json;		
			
		}
		return subStr;
	}
	/**
	 * level2 交易详细 模板
	 * {"14:21:00":["4.3","122221"],"14:20:11":["4.2","322212"]}
	 * @param list
	 * @return
	 */
	private String putLevel2Detail(List<TickSort> list) {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		sb.append("{");
		for (int i = 0; i < list.size(); i++) {
			TickSort tick=list.get(i);
			sb.append("\""+tick.getDateTime()+"\":[");
			sb.append("\""+tick.getTickPrice()+"\",\""+tick.getVolume()+"\"]");
			if(i<list.size()-1)
				sb.append(",");
		}
		
		sb.append("}");
		
		
		
		
		return sb.toString();

	
	
	}
	
}


