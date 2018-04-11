package org.myazure.vpn.entity;


public class PurchasedData {
	private String username="";
	private String orderId="";
	private long purchasedPricse = 0;
	private long purchasedDataBytes = 0;
	private long purchasedTime = System.currentTimeMillis();

	public PurchasedData() {
	}

	public PurchasedData(String username) {
		this.username = username;
		this.purchasedTime  =  0;
		this.purchasedPricse = 0;
		this.purchasedDataBytes = purchasedPricse*537418240L;
	}

	public PurchasedData(String username, long purchasedPricse) {
		this.username = username;
		this.purchasedTime  =  0;
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

	public PurchasedData(String username, long purchasedPricse,
			long purchasedDate, long purchasedDataBytes,String orderId) {
		this.purchasedDataBytes = purchasedDataBytes;
		this.username = username;
		this.purchasedTime = purchasedDate;
		this.purchasedPricse = purchasedPricse;
		this.orderId=orderId;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
