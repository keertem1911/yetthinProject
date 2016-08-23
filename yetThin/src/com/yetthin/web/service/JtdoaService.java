package com.yetthin.web.service;

public interface JtdoaService {

	String[] getL1(int huShen,long begin,long end, String market,boolean indexMarket,boolean master);

	String[] getL2(String upperCase);

	String[] getLevel2Detail(String symbol);

	String[] getIndexDetail(String indexCode);

	String[] getStockIndexList(int begin, int end, String marketCode, boolean master);

}
