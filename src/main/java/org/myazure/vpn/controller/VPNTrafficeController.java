package org.myazure.vpn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.myazure.utils.F;
import org.myazure.utils.L;
import org.myazure.utils.S;
import org.myazure.vpn.configuration.PrimaryConfiguration;
import org.myazure.vpn.domain.MyazureData;
import org.myazure.vpn.entity.TrafficData;
import org.myazure.vpn.entity.UserData;
import org.myazure.vpn.service.MyazureDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

@Controller
public class VPNTrafficeController {
	@Autowired
	private PrimaryConfiguration primaryConfiguration;
	@Autowired
	private MyazureDataService myazureDataService;

	private static final Logger LOG = LoggerFactory
			.getLogger(VPNTrafficeController.class);
	// public static Map<String, String> userTrafficDataMap = new
	// HashMap<String, String>();
	public static List<TrafficData> trafficingDatas = new ArrayList<TrafficData>();

	public VPNTrafficeController() {
	}

	public void readTrafficHistory() {
		String saveHistoryCommand = "zgrep \"ESP traffic information\"  /var/log/auth.*   > "
				+ primaryConfiguration.getTrafficeFilePath();
		try {
			L.runCommand(saveHistoryCommand);
			String[] historyRecords = F.readFileStrings(
					primaryConfiguration.getTrafficeFilePath(), "\n");
			for (String string : historyRecords) {
				if (!string.contains("ESP traffic information")
						|| !string.contains("xauth-psk")) {
					continue;
				}
				TrafficData data = new TrafficData(string);
				myazureDataService.save(new MyazureData(data.getUsername()
						+ "_" + data.getConnetTime(), JSON.toJSONString(data)));
			}
		} catch (Exception e) {
			LOG.debug("Run this [" + saveHistoryCommand + "] Err");
		}
	}

	public void readTrafficNow() {
		trafficingDatas = new ArrayList<TrafficData>();
		String saveTrafficCommand = " ipsec whack --trafficstatus  > "
				+ primaryConfiguration.getTrafficingFilePath();
		try {
			L.runCommand(saveTrafficCommand);
			String[] historyRecords = F.readFileStrings(
					primaryConfiguration.getTrafficingFilePath(), "\n");
			for (String string : historyRecords) {
				if (!string.contains("username")
						|| !string.contains("xauth-psk")) {
					continue;
				}
				TrafficData data = new TrafficData(string);
				trafficingDatas.add(data);
			}
			readTrafficHistory();
		} catch (Exception e) {
			LOG.debug("Run this [" + saveTrafficCommand + "] Err");
		}
	}

	public Map<String, UserData> readAllUserUsage() {
		Map<String, UserData> usageData = new HashMap<String, UserData>();
		for (String string : VPNUserController.userList) {
			usageData.put(string, getUserTrafficData(string));
		}
		VPNUserController.users = usageData;
		return usageData;
	}

	private UserData getUserTrafficData(String username) {
		UserData userdata = new UserData(username, 0, 0, 0);
		List<MyazureData> userdatas = myazureDataService
				.findMyazureDataByMkeyStartingWith(username + "_");
		long allPurchasedData = getUserPurchaseData(username);

		MyazureData userActiveData = myazureDataService
				.getMyazureData("USER_ACTIVE_" + username);
		long allIn = 0L;
		long allOut = 0L;
		boolean active = true;
		if (userActiveData != null) {
			if (userActiveData.getMvalue() != null) {
				if (userActiveData.getMvalue() == "1"
						|| userActiveData.getMvalue() == "TRUE"
						|| userActiveData.getMvalue() == "true") {
					active = true;
				} else {
					active = false;
				}
			} else {
				active = false;
			}
		} else {
			active = false;
		}
		if (userdatas != null) {
			if (userdatas.size() < 1) {
				myazureDataService.save("ALL_DATA_IN_" + username, "" + allIn);
				myazureDataService
						.save("ALL_DATA_OUT_" + username, "" + allOut);
				myazureDataService.save("ALL_DATA_" + username,
						S.toStroageString(allOut + allIn));
				userdata = new UserData(username, 0, 0, allPurchasedData);
			} else {
				List<TrafficData> userTrafficDatas = new ArrayList<TrafficData>();
				for (MyazureData myazureData : userdatas) {
					userTrafficDatas.add(new TrafficData(myazureData
							.getMvalue()));
				}
				for (TrafficData trafficData : userTrafficDatas) {
					String bytesIn = trafficData.getDataIn()
							.replace("K", "000").replace("M", "000000")
							.replace("G", "000000000")
							.replace("T", "000000000000").replace("B", "");
					String bytesOut = trafficData.getDataOut()
							.replace("K", "000").replace("M", "000000")
							.replace("G", "000000000").replace("B", "");
					allIn += Long.valueOf(bytesIn);
					allOut += Long.valueOf(bytesOut);
				}
				userdata.getUsageData().addAll(userTrafficDatas);
				for (TrafficData trafficData : trafficingDatas) {
					if (trafficData.getUsername().equals(username)) {
						String bytesIn = trafficData.getDataIn()
								.replace("K", "000").replace("M", "000000")
								.replace("G", "000000000")
								.replace("T", "000000000000").replace("B", "");
						String bytesOut = trafficData.getDataOut()
								.replace("K", "000").replace("M", "000000")
								.replace("G", "000000000").replace("B", "");
						allIn += Long.valueOf(bytesIn);
						allOut += Long.valueOf(bytesOut);
						userdata.getUsageData().add(trafficData);
					}
				}
				userdata = new UserData(username, allOut, allIn,
						allPurchasedData);
				myazureDataService.save("ALL_DATA_IN_" + username, "" + allIn);
				myazureDataService
						.save("ALL_DATA_OUT_" + username, "" + allOut);
				myazureDataService.save("ALL_DATA_" + username,
						S.toStroageString(allOut + allIn));
				LOG.debug(username + " USED:"
						+ S.toStroageString(allOut + allIn));
			}
		}
		userdata.setActive(active);
		return userdata;
	}

	private long getUserPurchaseData(String username) {
		List<MyazureData> dataPurchased = myazureDataService
				.findMyazureDataByMkeyStartingWith(username + "_Purchased_");
		if (dataPurchased.size() < 1) {
			myazureDataService.save(
					username + "_Purchased_" + System.currentTimeMillis(),
					"10737418240");
		}
		dataPurchased = myazureDataService
				.findMyazureDataByMkeyStartingWith(username + "_Purchased_");
		long allPurchasedData = 0L;
		for (MyazureData myazureData : dataPurchased) {
			allPurchasedData += Long.valueOf(myazureData.getMvalue());
		}
		myazureDataService.save(username + "_Purchased", "" + allPurchasedData
				+ "");
		return allPurchasedData;
	}
}
