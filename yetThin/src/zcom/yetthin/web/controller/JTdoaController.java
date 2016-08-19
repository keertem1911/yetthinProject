package zcom.yetthin.web.controller;


import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
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
 	
	@ResponseBody
	@RequestMapping(value="/shenzhen",method=RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	public String shenzhen(@RequestParam(value="begin",required=false,defaultValue="0")String begin,
			@RequestParam(value="end",required=false,defaultValue="9")String end,
			@RequestParam(value="marketCode",defaultValue="0:0,1,2")String market){
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
		String [] subStr=jtdoaService.getL1(HU_SHEN,beginIndex,endIndex,market);
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
	 
	 
	@ResponseBody
	@RequestMapping(value="/getLevel2",method=RequestMethod.POST
	,produces = {"application/json;charset=UTF-8"})
	public String getLevel2(@RequestParam(value="symbol")String symbol){
		String [] subStr= jtdoaService.getL2(symbol.toUpperCase());

		return putReturnValue1(subStr[0],subStr[2], subStr[1]);
	}
	
	@ResponseBody 
	@RequestMapping(value="/getLevel2Detail",method=RequestMethod.POST,
	produces = {"application/json;charset=UTF-8"})
	public String getLevel2DetailTick(@RequestParam(value="symbol")String symbol){
		String [] subStr=jtdoaService.getLevel2Detail(symbol);
		return putReturnValue1(subStr[0],subStr[2], subStr[1]);
	 }
	 
}
