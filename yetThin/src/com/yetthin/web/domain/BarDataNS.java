package com.yetthin.web.domain;

public class BarDataNS {

	private long id;
	private String dateTime;
	private String open;
	private String high;
	private String low;
	private String close;
	private String ystClose;//昨收
	private String volume;
	private int matchItems;//成交笔数
	private int sid;
	 
 
	
	public BarDataNS() {
		this.id = 0;
		this.dateTime = "0";
		this.open = "0";
		this.high = "0";
		this.low = "0";
		this.close = "0";
		this.ystClose = "0";
		this.volume = "0";
		this.matchItems = 0;
	}



	@Override
	public String toString() {
		return "BarDataNS [id=" + id + ", dateTime=" + dateTime + ", open=" + open + ", high=" + high + ", low=" + low
				+ ", close=" + close + ", ystClose=" + ystClose + ", volume=" + volume + ", matchItems=" + matchItems
				+ "]";
	}



	public int getSid() {
		return sid;
	}



	public void setSid(int sid) {
		this.sid = sid;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getYstClose() {
		return ystClose;
	}

	public void setYstClose(String ystClose) {
		this.ystClose = ystClose;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

 

	public int getMatchItems() {
		return matchItems;
	}

	public void setMatchItems(int matchItems) {
		this.matchItems = matchItems;
	}

	 
}
