package com.yetthin.web.service;

public interface JtdoaService {

	String[] getL1(int huShen,long begin,long end, String market);

	String[] getL2(String upperCase);

	String[] getLevel2Detail(String symbol);

}
