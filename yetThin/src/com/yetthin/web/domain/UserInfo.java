package com.yetthin.web.domain;

public class UserInfo {
    private String userId;

    private String phoneNum;

    private Double myMoney;

    private String email;

    private String password;
    //极光id
    private String jpushId;
    //极光开关
    private int jpushStatus;
    //建议
    private String ideaText;
    // 推送类型
    private String jpushType;
 
     
    
    
	public UserInfo( ) {
		super();
		this.userId = "";
		this.phoneNum = "";
		this.myMoney = 0.0;
		this.email = "";
		this.password = "";
		this.jpushId = "";
		this.jpushStatus = 1;
		this.ideaText = "";
		this.jpushType = "";
	}




	public String getUserId() {
		return userId;
	}




	public void setUserId(String userId) {
		this.userId = userId;
	}




	public String getPhoneNum() {
		return phoneNum;
	}




	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}




	public Double getMyMoney() {
		return myMoney;
	}




	public void setMyMoney(Double myMoney) {
		this.myMoney = myMoney;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public String getJpushId() {
		return jpushId;
	}




	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}




	public int getJpushStatus() {
		return jpushStatus;
	}




	public void setJpushStatus(int jpushStatus) {
		this.jpushStatus = jpushStatus;
	}




	public String getIdeaText() {
		return ideaText;
	}




	public void setIdeaText(String ideaText) {
		this.ideaText = ideaText;
	}




	public String getJpushType() {
		return jpushType;
	}




	public void setJpushType(String jpushType) {
		this.jpushType = jpushType;
	}




	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", phoneNum=" + phoneNum + ", myMoney=" + myMoney + ", email=" + email
				+ ", password=" + password + ", jpushId=" + jpushId + ", jpushStatus=" + jpushStatus + ", ideaText="
				+ ideaText + ", jpushType=" + jpushType + "]";
	}
	 



	 

	 
	 

	 
    
}