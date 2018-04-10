package org.myazure.vpn.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.myazure.utils.F;
import org.myazure.utils.L;
import org.myazure.vpn.configuration.PrimaryConfiguration;
import org.myazure.vpn.domain.MyazureData;
import org.myazure.vpn.entity.UserData;
import org.myazure.vpn.service.MyazureDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class VPNUserController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(VPNUserController.class);
	@Autowired
	PrimaryConfiguration primaryConfiguration;
	@Autowired
	private MyazureDataService myazureDataService;
	public static Map<String, UserData> users = new HashMap<String, UserData>();
	public static List<String> userList = new ArrayList<String>();
	public static Map<String, String> userPasswd = new HashMap<String, String>();

	public VPNUserController() {

	}

	public void readAllUserDataActive() {
		String[] userPasswdLines = F.readFileStrings(
				primaryConfiguration.getPasswdFilePath(), "\n");
		userList = new ArrayList<String>();
		userPasswd = new HashMap<String, String>();
		myazureDataService.deleteAllMyazureDatasByKeyStartWith("USER_ACTIVE_");
		for (String string : userPasswdLines) {
			if (string.split(":").length > 2 && string.contains(":xauth-psk")) {
				String usernameString = string.split(":")[0];
				String passwdString = string.split(":")[1];
				userList.add(usernameString);
				userPasswd.put(usernameString, passwdString);
				try {
					myazureDataService.save("USER_PASSWD_" + usernameString
							+ "", "" + passwdString + "");
					myazureDataService.save("USER_ACTIVE_" + usernameString
							+ "", "1");
				} catch (Exception e) {
					continue;
				}
			}

		}

	}

	public void addUser(String username, String passwd) throws IOException,
			InterruptedException {
		readAllUserDataActive();
		if (VPNUserController.users.containsKey(username)) {
			deleteUser(username);
		}
		deleteUserHistoryData(username);
		if (userList.contains(username)) {
			try {
				userPasswd.put(username, creatPasswd(passwd));
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			try {
				userList.add(username);
				userPasswd.put(username, creatPasswd(passwd));
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			myazureDataService.save("USER_ACTIVE_" + username + "",""+1+"");
			myazureDataService.save("ALL_DATA_IN_" + username,""+0+"");
			myazureDataService.save("ALL_DATA_OUT_" + username,""+0+"");
			myazureDataService.save("ALL_DATA_" + username,""+0+"");
			myazureDataService.save(username + "_Purchased",""+0+"");
		}
		writeUsersToFile();
	}

	private void deleteUserHistoryData(String username) {
		myazureDataService.delete("USER_ACTIVE_" + username + "");
		myazureDataService.delete("ALL_DATA_IN_" + username);
		myazureDataService.delete("ALL_DATA_OUT_" + username);
		myazureDataService.delete("ALL_DATA_" + username);
		myazureDataService.delete(username + "_Purchased");
		myazureDataService.deleteAllMyazureDatasByKeyStartWith(username
				+ "_Purchased_");
	}

	public void deleteUser(String username) throws IOException,
			InterruptedException {
		readAllUserDataActive();
		if (userList.contains(username)) {
			readAllUserDataActive();
			userList.remove(username);
			userPasswd.remove(username);
			deleteUserHistoryData(username);
			writeUsersToFile();
		} else {
			return;
		}
	}

	public void modifyUser(String username, String passwd) throws IOException,
			InterruptedException {
		readAllUserDataActive();
		deleteUser(username);
		addUser(username, passwd);
	}

	private void writeUsersToFile() throws IOException, InterruptedException {
		StringBuffer fileStringBuffer = new StringBuffer();
		for (String string : userList) {
			fileStringBuffer.append(string).append(":")
					.append(userPasswd.get(string)).append(":xauth-psk")
					.append("\n");
		}
		F.writeStringToFile(fileStringBuffer.toString(),
				primaryConfiguration.getPasswdFilePath());
	}

	private String creatPasswd(String passwd) throws IOException,
			InterruptedException {
		String creatPasswdCommand = "sudo openssl passwd -1 \""
				+ passwd.replace("\"", "").replace("\\", "") + "\"";
		return L.runCommand(creatPasswdCommand);
	}

	public void addUserData(String username, long data) {
		myazureDataService.save(
				username + "_Purchased_" + System.currentTimeMillis(), ""
						+ data + "");
		List<MyazureData> dataPurchased = myazureDataService
				.findMyazureDataByMkeyStartingWith(username + "_Purchased_");
		long allPurchasedData = 0L;
		for (MyazureData myazureData : dataPurchased) {
			allPurchasedData += Long.valueOf(myazureData.getMvalue());
		}
		myazureDataService.save(username + "_Purchased", "" + allPurchasedData
				+ "");
	}

	public void addUserData(String username, long data, long time) {
		myazureDataService
				.save(username + "_Purchased_" + time, "" + data + "");
	}

	public void delUserData(String username, long data) {
		long purchasedDataBytes = 10737418240L;
		MyazureData purchasedDataBytesData = myazureDataService
				.getMyazureData(username + "_Purchased");
		if (purchasedDataBytesData != null) {
			if (purchasedDataBytesData.getMvalue() != null) {
				purchasedDataBytes = Long.valueOf(purchasedDataBytesData
						.getMvalue()) - data;
			} else {
				purchasedDataBytes -= data;
			}
		} else {
			purchasedDataBytes -= data;
		}
		myazureDataService.save(username + "_Purchased", ""
				+ purchasedDataBytes + "");
	}

	public void delAllOverUseUsers() throws IOException, InterruptedException {
		for (String username : VPNUserController.userList) {
			UserData userData = VPNUserController.users.get(username);
			if (!userData.isActive()) {
				deleteUser(username);
			}
			if (userData.getAllDataBytes() >= userData.getPurchasedDataBytes()) {
				deleteUser(username);
			}
		}
	}

}
