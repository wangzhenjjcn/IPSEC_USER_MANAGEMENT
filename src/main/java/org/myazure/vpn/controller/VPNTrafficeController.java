package org.myazure.vpn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.myazure.utils.F;
import org.myazure.utils.L;
import org.myazure.configuration.PrimaryConfiguration;
import org.myazure.domain.MyazureData;
import org.myazure.service.MyazureDataService;
import org.myazure.vpn.entity.TrafficData;
import org.myazure.vpn.entity.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

@Controller("VPNTrafficeController")
public class VPNTrafficeController {
	@Autowired
	PrimaryConfiguration primaryConfiguration;
	@Autowired
	MyazureDataService myazureDataService;

	private static final Logger LOG = LoggerFactory.getLogger(VPNTrafficeController.class);
	public static List<TrafficData> trafficingDatas = new ArrayList<TrafficData>();

	public VPNTrafficeController() {

	}

	public void readTrafficHistory() {
		String saveHistoryCommand = "zgrep \"ESP traffic information\"  /var/log/auth.*   > " + primaryConfiguration.getTrafficeFilePath();
		try {
			L.runCommand(saveHistoryCommand);
			String[] historyRecords = F.readFileStrings(primaryConfiguration.getTrafficeFilePath(), "\n");
			for (String string : historyRecords) {
				if (!string.contains("ESP traffic information") || !string.contains("xauth-psk")) {
					continue;
				}
				TrafficData data = new TrafficData(string);
				if(data.getConnetTime()!=null){
					myazureDataService.save(new MyazureData("ESP_TrafficInformation_" + data.getUsername() + "_" + data.getConnetTime(), JSON.toJSONString(data)));
				}else {
					myazureDataService.save(new MyazureData("ESP_TrafficInformation_" + data.getUsername() + "_" + JSON.toJSONString(data).hashCode(), JSON.toJSONString(data)));
				}
			}
		} catch (Exception e) {
			LOG.debug("Run this [" + saveHistoryCommand + "] Err");
		}
	}

	public void readTrafficNow() {
		trafficingDatas = new ArrayList<TrafficData>();
		String saveTrafficCommand = " ipsec whack --trafficstatus  > " + primaryConfiguration.getTrafficingFilePath();
		try {
			L.runCommand(saveTrafficCommand);
			String[] historyRecords = F.readFileStrings(primaryConfiguration.getTrafficingFilePath(), "\n");
			for (String string : historyRecords) {
				if (!string.contains("username") || !string.contains("xauth-psk")) {
					continue;
				}
				TrafficData data = new TrafficData(string);
				trafficingDatas.add(data);
			}
		} catch (Exception e) {
			LOG.debug("Run this [" + saveTrafficCommand + "] Err");
		}
		readTrafficHistory();
	}

	public static void blockUser(String username) {
		for (TrafficData trafficData : trafficingDatas) {
			if (trafficData.getLinkId() == null || trafficData.getLinkId() == "0" || trafficData.getLinkId().equals("0")) {
				continue;
			}
			if (trafficData.getUsername().equals(username)) {
				String blockUserCommand = "  ipsec whack --deletestate  " + trafficData.getLinkId();
				try {
					L.runCommand(blockUserCommand);
				} catch (Exception e) {
					LOG.debug("Run this [" + blockUserCommand + "] Err");
				}
			}
		}
	}

	public  Map<String, UserData> calculateAllUserData() {
		readTrafficNow();
		Map<String, UserData> userData = new HashMap<String, UserData>();
		for (String string : VPNUserController.ipsecUserList) {
			userData.put(string, calculateUserTrafficData(string));
		}
		VPNUserController.users = userData;
		return userData;
	}

	private  UserData calculateUserTrafficData(String username) {
		UserData userdata = new UserData(username, 0, 0, 0);
		long allPurchasedData = getUserPurchaseData(username);
		long allIn = 0L;
		long allOut = 0L;
		long dataRemain = allPurchasedData - allIn - allOut;
		List<MyazureData> userdatas = myazureDataService.findMyazureDataByMkeyStartingWith("ESP_TrafficInformation_" + username + "_");
		if (userdatas != null) {
			if (userdatas.size() < 1) {
				myazureDataService.save("ALL_DATA_IN_" + username, "" + allIn);
				myazureDataService.save("ALL_DATA_OUT_" + username, "" + allOut);
				myazureDataService.save("ALL_DATA_" + username, "" + (allOut + allIn) + "");
			} else {
				List<TrafficData> userTrafficDatas = new ArrayList<TrafficData>();
				for (MyazureData myazureData : userdatas) {
					userTrafficDatas.add(new TrafficData(myazureData.getMvalue()));
				}
				for (TrafficData trafficData : userTrafficDatas) {
					String bytesIn = trafficData.getDataIn().replace("K", "000").replace("M", "000000").replace("G", "000000000").replace("T", "000000000000").replace("B", "");
					String bytesOut = trafficData.getDataOut().replace("K", "000").replace("M", "000000").replace("G", "000000000").replace("B", "");
					allIn += Long.valueOf(bytesIn);
					allOut += Long.valueOf(bytesOut);
				}
				userdata.getUsageData().addAll(userTrafficDatas);
				for (TrafficData trafficData : trafficingDatas) {
					if (trafficData.getUsername().equals(username)) {
						String bytesIn = trafficData.getDataIn().replace("K", "000").replace("M", "000000").replace("G", "000000000").replace("T", "000000000000").replace("B", "");
						String bytesOut = trafficData.getDataOut().replace("K", "000").replace("M", "000000").replace("G", "000000000").replace("B", "");
						allIn += Long.valueOf(bytesIn);
						allOut += Long.valueOf(bytesOut);
						userdata.getUsageData().add(trafficData);
					}
				}
				dataRemain = VPNUserController.FREE_USER_DATA + allPurchasedData - allIn - allOut;
				userdata = new UserData(username, allOut, allIn, allPurchasedData);
				myazureDataService.save("ALL_DATA_IN_" + username, "" + allIn);
				myazureDataService.save("ALL_DATA_OUT_" + username, "" + allOut);
				myazureDataService.save("ALL_DATA_" + username, "" + (allOut + allIn) + "");
				myazureDataService.save("DATA_REMAIN_" + username, "" + dataRemain + "");
			}
		}
		userdata = new UserData(username, allOut, allIn, allPurchasedData);
		userdata.setActive(VPNUserController.users.containsKey(username));
		return userdata;
	}

	private  long getUserPurchaseData(String username) {
		List<MyazureData> dataPurchased = myazureDataService.findMyazureDataByMkeyStartingWith(username + "_Purchased_");
		long allPurchasedData = 0L;
		for (MyazureData myazureData : dataPurchased) {
			if (myazureData.getMkey().equals(username + "_Purchased_0")) {
				continue;
			}
			allPurchasedData += Long.valueOf(myazureData.getMvalue());
		}
		myazureDataService.save(username + "_Purchased", "" + allPurchasedData + "");
		return allPurchasedData;
	}

}

































