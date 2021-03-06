package org.myazure.vpn.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.myazure.utils.S;

public class UserData {
	private String username = "default";
	private String passwd = "123456";
	private String passwdEncode = "$1$.ov.fnld$UjN/O5aFVcDSIjAU4XI8W1";
	private boolean ipsec = false;
	private boolean pptp = false;
	private boolean l2tp = false;
	private Date lastLinkDate;
	private long dataOutBytes = 0L;
	private long dataInBytes = 0L;
	private long allDataBytes = 0L;
	private long purchasedDataBytes = 10737418240L;
	private boolean active = true;
	private List<TrafficData> usageData = new ArrayList<TrafficData>();
	private List<PurchasedData> purchasedData = new ArrayList<PurchasedData>();

	public UserData() {
	}

	public UserData(String username) {
		this.username = username;
	}

	public UserData(String username, long dataOut, long dataIn) {
		this.username = username;
		this.dataInBytes = dataIn;
		this.dataOutBytes = dataOut;
		this.allDataBytes = dataIn + dataOut;
	}

	public UserData(String username, long dataOut, long dataIn,
			long purchasedData) {
		this.username = username;
		this.dataInBytes = dataIn;
		this.dataOutBytes = dataOut;
		this.allDataBytes = dataIn + dataOut;
		this.purchasedDataBytes = purchasedData;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getDataOutBytes() {
		return dataOutBytes;
	}

	public void setDataOutBytes(long dataOutBytes) {
		this.dataOutBytes = dataOutBytes;
		this.allDataBytes = this.dataInBytes + this.dataOutBytes;
	}

	public long getDataInBytes() {
		return dataInBytes;
	}

	public void setDataInBytes(long dataInBytes) {
		this.dataInBytes = dataInBytes;
		this.allDataBytes = this.dataInBytes + this.dataOutBytes;
	}

	public long getAllDataBytes() {
		return allDataBytes;
	}

	public void setAllDataBytes(long allDataBytes) {
		this.allDataBytes = this.dataInBytes + this.dataOutBytes;
	}

	public long getPurchasedDataBytes() {
		return purchasedDataBytes;
	}

	public void setPurchasedDataBytes(long purchasedDataBytes) {
		this.purchasedDataBytes = purchasedDataBytes;
	}

	public boolean isIpsec() {
		return ipsec;
	}

	public void setIpsec(boolean ipsec) {
		this.ipsec = ipsec;
	}

	public boolean isPptp() {
		return pptp;
	}

	public void setPptp(boolean pptp) {
		this.pptp = pptp;
	}

	public boolean isL2tp() {
		return l2tp;
	}

	public void setL2tp(boolean l2tp) {
		this.l2tp = l2tp;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getPasswdEncode() {
		return passwdEncode;
	}

	public void setPasswdEncode(String passwdEncode) {
		this.passwdEncode = passwdEncode;
	}

	public Date getLastLinkDate() {
		return lastLinkDate;
	}

	public void setLastLinkDate(Date lastLinkDate) {
		this.lastLinkDate = lastLinkDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<TrafficData> getUsageData() {
		return usageData;
	}

	public void setUsageData(List<TrafficData> usageData) {
		this.usageData = usageData;
	}

	public List<PurchasedData> getPurchasedData() {
		return purchasedData;
	}

	public void setPurchasedData(List<PurchasedData> purchasedData) {
		this.purchasedData = purchasedData;
	}

	public void addPurchasedData(PurchasedData purchasedData) {
		this.purchasedData.add(purchasedData);
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("User:").append(username).append(" ").append("Paid:")
				.append(S.toStroageString(purchasedDataBytes)).append(" ")
				.append("AllUsed:").append(S.toStroageString(allDataBytes))
				.append(" ").append("EnableIpsec:")
				.append(ipsec ? "Yes" : "No").append("EnablePPTP:")
				.append(pptp ? "Yes" : "No").append("EnableL2TP:")
				.append(l2tp ? "Yes" : "No");
		return str.toString();
	}

}
