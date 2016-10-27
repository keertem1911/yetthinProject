package com.yetthin.web.serviceImp;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.tomcat.jni.Buffer;
import org.springframework.stereotype.Service;

import com.yetthin.web.domain.HeroIncome;
import com.yetthin.web.domain.RecommendList;
import com.yetthin.web.persistence.IndexPageContextMapper;
import com.yetthin.web.service.IndexPageContextService;
@Service("IndexPageContextService")
public class IndexPageContextServiceImp implements IndexPageContextService{
	
	@Resource
	private IndexPageContextMapper indexPageContextMapper;
	
	private <E>String tojson(List<E> list){
		
		StringBuffer buffer =new StringBuffer();
		buffer.append("\"value\":[");
		Class clazz =list.get(0).getClass();
		Field[] fiedls = clazz.getDeclaredFields();
		final String [] fiedlsName=new String [fiedls.length];
		for (int i = 0; i < fiedls.length; i++) {
			String name =fiedls[i].getName();
			fiedlsName[i] =name.substring(0, 1).toUpperCase()+name.substring(1);
		}
		for (int i = 0; i < list.size(); i++) {
			buffer.append("{");
			for(int j=0;j<fiedlsName.length;++j){
				try{
			Method method = clazz.getDeclaredMethod("get"+fiedlsName[j]);
			Object value = method.invoke(list.get(i));
			String name =fiedlsName[j].substring(0, 1).toLowerCase()+fiedlsName[j].substring(1);
			
			buffer.append("\""+name+"\":\""+value+"\"");
			
			if(j!=fiedlsName.length-1)
				buffer.append(",");
				}catch(Exception e){
					e.printStackTrace();
					}
				 
			}
			
			buffer.append("}");
			if(i!=list.size()-1)
				buffer.append(",");
		}
		
		buffer.append("]");
		return buffer.toString();
		
	}
	private static final SimpleDateFormat format =new SimpleDateFormat("yyyy/MM/dd");
	private static final DecimalFormat   df=new   java.text.DecimalFormat("#.##");  
	@Override
	public String getIncomeRecommendList(int type, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		int totlePageSize=2;
//	 	int totlePage=indexPageContextMapper.getTotlePageIncome(type);
//		int totlePageNum=totlePage/pageSize;
//		if(pageNum>totlePageNum) pageSize=totlePageNum;
//		if(pageNum<=0) pageNum=1;
//		
//		if(totlePage%pageSize!=0) 
//			totlePageNum++;
//		int begin=0;
//		int end=0;
//		if(pageNum==totlePageNum){
//			begin =(totlePageNum-1)*pageSize+1;
//			end=totlePage;
//		}else{
//		 begin=(pageSize-1)*pageSize+1;
//		 end =(pageSize)*pageSize;
//		}
//		List<RecommendList> list =indexPageContextMapper.getIncomeRecommendList(type, begin, end);
		List<RecommendList> list =new ArrayList<RecommendList>();
		for(int i=0;i<pageSize;++i){
			RecommendList recommend=new RecommendList();
			recommend.setGroupName("新能源汽车");
			recommend.setIncomeRatio(df.format(10.0-0.1*i));
			recommend.setIncomeType(type);
			recommend.setUserName("王"+i);
			recommend.setUserId("ea193c352fda49de8e34f319ec411960");
			recommend.setRecommendReason("人工智能+智能算法");
			recommend.setVipFlag(i%2);
			recommend.setCreateTime(format.format(new Date()));
			 
			list.add(recommend);
		}
		String json =tojson(list);
		json="{\"currentPage\":\""+pageNum+"\",\"totlePageSize\":\""+
		totlePageSize+"\","+json+"}";
		return json;
	}
	/**
	 * 
	 * @param pageNum  每页的数目
	 * @param pageSize 页码数
	 * @return
	 */
	@Override
	public String getBestIncomeList(int pageNum,int pageSize) {
		// TODO Auto-generated method stub
		int totlePage=2;
//		int totlePage=indexPageContextMapper.getTotlePageHero();
//		int totlePageNum=totlePage/pageSize;
//		if(pageNum>totlePageNum) pageSize=totlePageNum;
//		if(pageNum<=0) pageNum=1;
//		
//		if(totlePage%pageSize!=0) 
//			totlePageNum++;
//		int begin=0;
//		int end=0;
//		if(pageNum==totlePageNum){
//			begin =(totlePageNum-1)*pageSize+1;
//			end=totlePage;
//		}else{
//		 begin=(pageSize-1)*pageSize+1;
//		 end =(pageSize)*pageSize;
//		}
//		List<RecommendList> list =indexPageContextMapper.getIncomeRecommendList(type, begin, end);
//		List<HeroIncome> list=indexPageContextMapper.getBestIncomeList();
		List<HeroIncome> list =new ArrayList<HeroIncome>();
		for(int i=0;i<pageSize;++i){
			HeroIncome income=new HeroIncome();
			income.setUserName("李"+i);
			income.setVipFlag(i%2);
			income.setNear3MonthIncome(df.format(10.2-0.1*i));
			income.setBelongDepart("前景私募股份有限公司"+i);
			 list.add(income);
		}
		String json =tojson(list);
		json="{\"currentPage\":\""+pageNum+"\",\"totlePageSize\":\""+
				totlePage+"\","+json+"}";
		return json;
	}

}
