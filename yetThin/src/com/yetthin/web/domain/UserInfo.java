package com.yetthin.web.domain;

public class UserInfo {
    private Integer userId;

    private String phoneNum;

    private Double myMoney;

    private String email;

    private String password;

    private String jpushId;

    private int status;

    private String ideaText;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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

    

    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getIdeaText() {
        return ideaText;
    }

    public void setIdeaText(String ideaText) {
        this.ideaText = ideaText;
    }

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", phoneNum=" + phoneNum + ", myMoney=" + myMoney + ", email=" + email
				+ ", password=" + password + ", jpushId=" + jpushId + ", status=" + status + ", ideaText=" + ideaText
				+ "]";
	}
    
}