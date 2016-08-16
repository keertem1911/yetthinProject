package com.yetthin.web.serviceImp;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yetthin.web.commit.JtdoaValueMarket;
import com.yetthin.web.commit.ValueFormatUtil;
import com.yetthin.web.dao.JtdoaDao;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

@Service("JtdoaService")
public class JtdoaServiceImp implements JtdoaValueMarket,ValueFormatUtil{
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
	 
	 * 指数格式为 [{指数代码,指数名称,当前价格,增长量,增长率},{},{}]
	 * @param index
	 * @return
	 */
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
	 * @return
	 */
	private String putStockMarketData(Map<String,Set<Tuple>> market){
		StringBuffer sb=new StringBuffer();
		sb.append("[");
		Set<Entry<String, Set<Tuple>>> entry=market.entrySet();
		
		for (Entry<String, Set<Tuple>> entry2 : entry) {
			sb.append("{\"group\":\""+entry2.getKey().split(JTDOA_SPLIT_STR)[1]+"\",");
			sb.append("\"index\":[");
			Set<Tuple> tu1=entry2.getValue();
			for (Tuple tuple : tu1) {
				String desc= doubleformat.format(tuple.getScore());
				String []values = tuple.getElement().split(JTDOA_SPLIT_STR);//股票代码.市场:价格:日期
				String name=jtdoaDao.getNameBySymbol(values[0]);
				sb.append(  "{\"name\":\""+name+"\","+ 
		                    "\"stockID\":\" "+values[0]+"\",");
				boolean plus =true;
				if(desc.indexOf("-")!=-1){
					plus=false;
					desc=desc.substring(1);
				}
		                   sb.append("\"increase\":\""+plus+"\","+ 
		                    "\"price\": \""+values[1]+"\","+ 
		                    "\"value\": \""+desc+"\"},");
			}
			
			sb.append("] },");
		}
		
		sb.append("]");
		
		String str=sb.toString();
		str=str.substring(0,str.lastIndexOf(","))+"]";
		str=str.substring(0,str.lastIndexOf(","))+" ]}]";
		
		
		return str;
	}
	@Resource
	private JtdoaDao jtdoaDao;
	
	/**
	 *  获取l1值 包括 股指 股票涨跌分类等
	 * @param id
	 * @return 字符数组  1 状态码 2 指数的JSON 3 股票的分类排行L1 4 错误信息 
	 */
	@Transactional
	public String []  getL1(int id){
		String [] subStr=new String [4];
		switch(id){
		case HU_SHEN: 
			String [] index= jtdoaDao.getStockIndex(HU_SHEN);
			subStr[2]=putIndex(index);// 指数解析
			
			break;
		}
		
		
		return subStr;
	}
}
