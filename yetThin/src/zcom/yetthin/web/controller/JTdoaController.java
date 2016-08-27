package zcom.yetthin.web.controller;


import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yetthin.web.commit.JtdoaValueMarket;
import com.yetthin.web.service.JtdoaService;

/**
 * 行情业务
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/jtdoa")
public class JTdoaController extends BaseController implements JtdoaValueMarket {
	@Resource
	private JtdoaService jtdoaService;
	 
	
	private String putReturnValue1(String statusCode,String msg,String item){
		return  "{\"status\":\""+statusCode+"\",\"msg\":\""+msg+"\",\"item\":"+item+"}";
	}
	private String putReturnValue2(String statusCode,String index,String msg,String item){
		return  "{\"status\":\""+statusCode+"\",\"index\":"+index+",\"msg\":\""+msg+"\",\"item\":"+item+"}";
	}
 	/**
 	 * 
 	 * @param begin 分页开始位置
 	 * @param end 分页结束位置
 	 * @param market 市场代码
 	 * @param indexMarket 指数市场是否
 	 * @return
 	 */
	@ResponseBody
	@RequestMapping(value="/shenzhen",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	public String shenzhen(@RequestParam(value="begin",required=false,defaultValue="0")String begin,
			@RequestParam(value="end",required=false,defaultValue="9")String end,
			@RequestParam(value="marketCode",defaultValue="0:0,1,2",required=false)String market,
			@RequestParam(value="indexMarket",defaultValue="true",required=false)boolean indexMarket,
			@RequestParam(value="master",defaultValue="true",required=false)boolean master){
		if("".equals(begin)){
			begin="0";
		}
		if("".equals(end)){
			end="9";
		}
		if("".equals(market)){
			market="0:0,1,2";
		}
		long beginIndex=Long.parseLong(begin);
		long endIndex=Long.parseLong(end);
		String [] subStr=jtdoaService.getL1(HU_SHEN,beginIndex,endIndex,market,indexMarket,master);
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
		                    "value": "增长率",
		                }
		                 
		            ]
		        },
		         
		    ], 
		    "msg": { }
		}*/
		 
		return putReturnValue2(subStr[0], subStr[1],subStr[3], subStr[2]);
	}
	/**
	 *  
	 * @param indexCode
	 * @return {"status":"200",
	 * 			"item":{
	 * 				level1:{"name": "上证指数", 
	 * 				 
		            "price": "价格", 
		            "stockID": "股票代码",
		            "increase":"ture|flase",
		            "growth": "增长量", 
		            "growthRate": "增长率"
		            "openPrice":"1.1",
		            "LowPrice":"1.1",
		            "HeightPrice":"1.1",
		            "exchange":"11.2%",
		            "volume":"12121",
		            "totleSum":"12121"}
		            },
	 * 			"msg":""}
	 */
	@ResponseBody
	@RequestMapping(value="/getIndexDetail",method=RequestMethod.POST)
	public String getIndexDetail(@RequestParam(value="symbol")String symbol){
		String [] subStr=jtdoaService.getIndexDetail(symbol);
		return putReturnValue1(subStr[0], subStr[2], subStr[1]);
	}
	/**
	 *  
	 * @param symbol
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getLevel2",method=RequestMethod.POST
	,produces = {"application/json;charset=UTF-8"})
	public String getLevel2(@RequestParam(value="symbol")String symbol){
		String [] subStr= jtdoaService.getL2(symbol.toUpperCase().trim());

		return putReturnValue1(subStr[0],subStr[2], subStr[1]);
	}
	
	@ResponseBody 
	@RequestMapping(value="/getLevel2Detail",method=RequestMethod.POST,
	produces = {"application/json;charset=UTF-8"})
	public String getLevel2DetailTick(@RequestParam(value="symbol")String symbol){
		String [] subStr=jtdoaService.getLevel2Detail(symbol);
		return putReturnValue1(subStr[0],subStr[2], subStr[1]);
	 }
	@ResponseBody
	@RequestMapping(value="/getStockIndexList",method=RequestMethod.POST,
	produces = {"application/json;charset=utf-8"})
	 public String getStockIndexList(@RequestParam(value="begin",defaultValue="0")int begin,
			 @RequestParam(value="end",defaultValue="6")int end,
			 @RequestParam(value="marketCode",defaultValue="0",required=false)String marketCode,
			 @RequestParam(value="master",defaultValue="false",required=false)boolean master){
		
		 String [] subStr=jtdoaService.getStockIndexList(begin,end,marketCode,master);
		 return putReturnValue1(subStr[0], subStr[2], subStr[1]);
	 }
	
	@ResponseBody
	@RequestMapping(value="/getHistoryBar",method=RequestMethod.POST,
	produces={"application/json;charset=utf-8"})
	public String getHistoryBar(@RequestParam(value="symbol")String symbol,
			@RequestParam(value="barNum")String barNum,
			@RequestParam(value="currenyTime")String currenyTime,
			@RequestParam(value="barType")int type,
			@RequestParam(value="cycNum")int cycNum){
		String [] subStr= jtdoaService.getHistoryBar(symbol,barNum,currenyTime,type,cycNum);
		return putReturnValue1(subStr[0], subStr[2], subStr[1]);
	}
}
