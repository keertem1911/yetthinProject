package com.yetthin.web.domain;

public class HeroIncome {
	private String userName;
	private int vipFlag;
	private String near3MonthIncome;
	 
	private String belongDepart;
	
	public int getVipFlag() {
		return vipFlag;
	}
	public void setVipFlag(int vipFlag) {
		this.vipFlag = vipFlag;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	 
	 
	public String getNear3MonthIncome() {
		return near3MonthIncome;
	}
	public void setNear3MonthIncome(String near3MonthIncome) {
		this.near3MonthIncome = near3MonthIncome;
	}
	 
	 
	public String getBelongDepart() {
		return belongDepart;
	}
	public void setBelongDepart(String belongDepart) {
		this.belongDepart = belongDepart;
	}
 
}
