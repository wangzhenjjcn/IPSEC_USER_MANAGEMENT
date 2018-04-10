package org.myazure.vpn.entity;


public class PurchasedData {
	private String username;
	private long purchasedPricse = 100L;
	private long purchasedTime =  System.currentTimeMillis() ;
	private long purchasedDataBytes = 10737418240L;

	public PurchasedData() {

	}

	public PurchasedData(String username) {
		this.username = username;
		this.purchasedTime  =  System.currentTimeMillis();
		this.purchasedPricse = 0;
		this.purchasedDataBytes = purchasedPricse*537418240L;
	}

	public PurchasedData(String username, long purchasedPricse) {
		this.username = username;
		this.purchasedTime  =  System.currentTimeMillis();
		this.purchasedPricse = purchasedPricse;
		this.purchasedDataBytes = purchasedPricse*537418240L;
	}

	public PurchasedData(String username, long purchasedPricse,
			long purchasedDate) {
		this.purchasedDataBytes = purchasedPricse*537418240L;
		this.username = username;
		this.purchasedTime = purchasedDate;
		this.purchasedPricse = purchasedPricse;

	}

	public PurchasedData(String username, long purchasedPricse,
			long purchasedDate, long purchasedDataBytes) {
		this.purchasedDataBytes = purchasedDataBytes;
		this.username = username;
		this.purchasedTime = purchasedDate;
		this.purchasedPricse = purchasedPricse;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getPurchasedPricse() {
		return purchasedPricse;
	}

	public void setPurchasedPricse(long purchasedPricse) {
		this.purchasedPricse = purchasedPricse;
	}



	public long getPurchasedDataBytes() {
		return purchasedDataBytes;
	}

	public void setPurchasedDataBytes(long purchasedDataBytes) {
		this.purchasedDataBytes = purchasedDataBytes;
	}

	public long getPurchasedTime() {
		return purchasedTime;
	}

	public void setPurchasedTime(long purchasedTime) {
		this.purchasedTime = purchasedTime;
	}
}
