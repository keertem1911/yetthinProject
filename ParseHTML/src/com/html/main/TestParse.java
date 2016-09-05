package com.html.main;

import java.util.List;

import org.junit.Test;

public class TestParse {
		private static final String url_Root="http://data.ce.cn";
	 	@org.junit.Test
	    public void getDatasByClass()  
	    {  
	        Rule rule = new Rule(  
	                "http://data.ce.cn/main/stock/csrs/index.shtml",  
	        new String[] { "Cookie"}, new String[] { "ALLYESID4=0C885B89B3E015C3"},  
	                "tsblue", Rule.CLASS, Rule.GET);  
	        List<LinkTypeData> extracts = ExtractService.extract(rule);  
	        printf(extracts);  
	    }  
	  
	   @Test
	    public void getDatasByCssQuery()  
	    {  
	        Rule rule = new Rule("http://www.11315.com/search",  
	                new String[] { "name" }, new String[] { "兴网" },  
	                "div.g-mn div.con-model", Rule.SELECTION, Rule.GET);  
	        List<LinkTypeData> extracts = ExtractService.extract(rule);  
	        printf(extracts);  
	    }  
	    public void printf(List<LinkTypeData> datas)  
	    {  
	        for (LinkTypeData data : datas)  
	        {  
	           
	            Rule rule = new Rule(url_Root+data.getLinkHref(),  
	            		 new String[] { "Cookie"}, new String[] { "ALLYESID4=0C885B89B3E015C3"},  
		                "aleft", Rule.CLASS, Rule.GET);  
		        List<LinkTypeData> extracts = ExtractService.extract(rule);  
		        System.out.print(data.getLinkText()+"=[");
		        for (int i=0;i<extracts.size();i+=2) {
					System.out.print(extracts.get(i).getLinkText()+",");
				}
		        System.out.println("]");
	            System.out.println("***********************************");  
	        }  
	  
	    }
	    
}
