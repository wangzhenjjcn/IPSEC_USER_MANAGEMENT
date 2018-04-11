package org.myazure.vpn.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class TrafficData {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(TrafficData.class);

	private String username = "";
	private String connetIp = "0.0.0.0";
	private String dataOut = "0";
	private String dataIn = "0";
	private String connetTime = "";
	private String linkId = "0";

	public TrafficData() {
	}

	@SuppressWarnings("deprecation")
	public TrafficData(String trafficDataLine) {
		if (trafficDataLine.contains("ESP traffic information")
				&& trafficDataLine.contains("xauth-psk")) {
			try {
				this.username = trafficDataLine.split("XAUTHuser=")[1].trim();
				this.dataOut = trafficDataLine.split("out=")[1].split(" ")[0]
						.trim();
				this.dataIn = trafficDataLine.split("in=")[1].split(" ")[0]
						.trim();
				this.connetIp = "";
				if (trafficDataLine.split("xauth-psk")[1].split("#")[0]
						.contains(".")) {
					this.connetIp = trafficDataLine.split("xauth-psk")[1]
							.split("]")[1].split(" #")[0].trim();
				}
				Date date = new SimpleDateFormat("MMM d HH:mm:ss",
						Locale.ENGLISH).parse(trafficDataLine
						.substring(trafficDataLine.indexOf(":") + 1,
								trafficDataLine.indexOf("MyazureServer") - 1)
						.replace("  ", " ").toString());
				date.setYear(new Date(System.currentTimeMillis()).getYear());
				this.connetTime = (date.getTime() + "").substring(0, 10);
				this.linkId = trafficDataLine.split("#")[1].split(":")[0]
						.trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (trafficDataLine.contains("inBytes")
				&& trafficDataLine.contains("username")) {
			try {
				this.username = trafficDataLine.split("username=")[1]
						.split(",")[0].trim();
				this.dataOut = trafficDataLine.split("outBytes=")[1].split(",")[0]
						.trim();
				this.dataIn = trafficDataLine.split("inBytes=")[1].split(",")[0]
						.trim();
				this.connetIp = trafficDataLine.split("]")[1].split(",")[0]
						.trim();
				this.connetTime = trafficDataLine.split("add_time=")[1]
						.split(",")[0];
				this.linkId = trafficDataLine.split("#")[1].split(":")[0]
						.trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (trafficDataLine.contains("connetIp")) {
			TrafficData data = JSON.parseObject(trafficDataLine,
					TrafficData.class);
			this.username = data.username;
			this.connetIp = data.connetIp;
			this.dataOut = data.dataOut;
			this.dataIn = data.dataIn;
			this.connetTime = data.connetTime;
			this.linkId = data.linkId;
		}

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getConnetIp() {
		return connetIp;
	}

	public void setConnetIp(String connetIp) {
		this.connetIp = connetIp;
	}

	public String getDataOut() {
		return dataOut;
	}

	public void setDataOut(String dataOut) {
		this.dataOut = dataOut;
	}

	public String getDataIn() {
		return dataIn;
	}

	public void setDataIn(String dataIn) {
		this.dataIn = dataIn;
	}

	public String getConnetTime() {
		return connetTime;
	}

	public void setConnetTime(String connetTime) {
		this.connetTime = connetTime;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String toString() {
		StringBuffer strb = new StringBuffer();
		strb.append("ID:").append(linkId).append(" ").append("XAUTHuser=")
				.append(username).append(" ").append("IP:").append(connetIp)
				.append(" ").append("IN:").append(dataIn).append(" ")
				.append("OUT:").append(dataOut).append(" ").append("CONTIME:")
				.append(connetTime);
		return strb.toString();
	}
}
