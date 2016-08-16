package zcom.yetthin.web.controller;

 
 

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.yetthin.web.commit.JtdoaValueMarket;
import com.yetthin.web.commit.RedisOfReader;
import com.yetthin.web.serviceImp.JtdoaServiceImp;

import util.Level1Value;
import util.Level2Value;
/**
 * 行情业务
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/jtdoa")
public class JTdoaController extends BaseController implements JtdoaValueMarket {
	@Resource
	private JtdoaServiceImp jtdoaServiceImp;
	 
	
	private String putReturnValue1(String statusCode,String msg,String item){
		return  "{\"status\":\""+statusCode+"\",\"msg\":\""+msg+"\",\"item\":"+item+"}";
	}
	private String putReturnValue2(String statusCode,String index,String msg,String item){
		return  "{\"status\":\""+statusCode+"\",\"index\":\""+index+"\",\"msg\":\""+msg+"\",\"item\":"+item+"}";
	}
	@Autowired
	private HttpServletRequest request;
	 
	public String shenzhen(){
		String msg="";
		String statusCode="200";
		String item="";
		String index="";
		String [] subStr=jtdoaServiceImp.getL1(HU_SHEN);
		/*{
		    "status": "状态码", 
		    "index": [
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
		    "items": [
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
		        }
		         
		    ], 
		    "msg": { }
		}*/
		 
		return putReturnValue2(statusCode, index,msg, item);
	}
	@ResponseBody
	@RequestMapping(value="/getLevel1",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	public String getLevel1(@RequestParam(value="exchange")String exchange){
		List<Level1Value> list=RedisOfReader.getLevel1(exchange);
		String msg="";
		String statusCode="200";
		String item="";
		if(list==null||list.size()==0){
			msg="市场不存在";
			statusCode="507";
			item="\"\"";
		}else{
			item=JSON.toJSONString(list);
		}
		return putReturnValue1(statusCode, msg, item);
	}
	@ResponseBody
	@RequestMapping(value="/getLevel2",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	public String getLevel2(@RequestParam(value="symbol")String symbol){
		String value=RedisOfReader.getL2Value(symbol);
		String msg="";
		String statusCode="200";
		String item="";
		if(value!=null&&"".equals(value.trim())){
			msg="股票不存在";
			statusCode="507";
			item="\"\"";
		}else{
			item="\""+value+"\"";
		}

		return putReturnValue1(statusCode, msg, item);
	}
	 
	 
}
