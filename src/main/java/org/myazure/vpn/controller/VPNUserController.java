package org.myazure.vpn.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.myazure.configuration.PrimaryConfiguration;
import org.myazure.domain.MyazureData;
import org.myazure.service.MyazureDataService;
import org.myazure.utils.F;
import org.myazure.utils.L;
import org.myazure.vpn.entity.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("VPNUserController")
public class VPNUserController {
	@Autowired
	PrimaryConfiguration primaryConfiguration;
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(VPNUserController.class);
	@Autowired
	MyazureDataService myazureDataService;
	public static Map<String, UserData> users = new HashMap<String, UserData>();
	public static List<String> userList = new ArrayList<String>();
	public static Map<String, String> userPasswd = new HashMap<String, String>();
	public static long FREE_USER_DATA = 10737418240L;

	public VPNUserController() {
	}

	public void readAllUserDataActive() {
		String[] userPasswdLines = F.readFileStrings(primaryConfiguration.getPasswdFilePath(), "\n");
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
					myazureDataService.save("USER_PASSWD_" + usernameString + "", "" + passwdString + "");
					myazureDataService.save("USER_ACTIVE_" + usernameString + "", "1");
				} catch (Exception e) {
					continue;
				}
			}

		}

	}

	public void addUser(String username, String passwd) throws IOException, InterruptedException {
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
			myazureDataService.save("USER_ACTIVE_" + username + "", "" + 1 + "");
			myazureDataService.save("ALL_DATA_IN_" + username, "" + 0 + "");
			myazureDataService.save("ALL_DATA_OUT_" + username, "" + 0 + "");
			myazureDataService.save("ALL_DATA_" + username, "" + 0 + "");
			myazureDataService.save(username + "_Purchased", "" + 0 + "");
		}
		writeUsersToFile();
	}

	private void deleteUserHistoryData(String username) {
		myazureDataService.delete("USER_ACTIVE_" + username + "");
		myazureDataService.delete("ALL_DATA_IN_" + username);
		myazureDataService.delete("ALL_DATA_OUT_" + username);
		myazureDataService.delete("ALL_DATA_" + username);
		myazureDataService.delete(username + "_Purchased");
		myazureDataService.deleteAllMyazureDatasByKeyStartWith(username + "_Purchased_");
	}

	public void deleteUser(String username) throws IOException, InterruptedException {
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

	public   void disableUser(String username) throws IOException, InterruptedException {
		readAllUserDataActive();
		if (userList.contains(username)) {
			userList.remove(username);
			userPasswd.remove(username);
			myazureDataService.save("USER_ACTIVE_" + username + "", "0");
			writeUsersToFile();
			VPNTrafficeController.blockUser(username);
		} else {
			return;
		}
	}

	public void modifyUser(String username, String passwd) throws IOException, InterruptedException {
		readAllUserDataActive();
		disableUser(username);
		addUser(username, passwd);
	}

	private void writeUsersToFile() throws IOException, InterruptedException {
		StringBuffer fileStringBuffer = new StringBuffer();
		for (String string : userList) {
			fileStringBuffer.append(string).append(":").append(userPasswd.get(string)).append(":xauth-psk").append("\n");
		}
		F.writeStringToFile(fileStringBuffer.toString(), primaryConfiguration.getPasswdFilePath());
	}

	private String creatPasswd(String passwd) throws IOException, InterruptedException {
		String creatPasswdCommand = "sudo openssl passwd -1 \"" + passwd.replace("\"", "").replace("\\", "") + "\"";
		return L.runCommand(creatPasswdCommand);
	}

	public void addFreeUserData(String username) {
		addUserData(username, FREE_USER_DATA, "0");
	}

	public void addUserData(String username, long data) {
		addUserData(username, data, System.currentTimeMillis());
	}

	public void addUserData(String username, long data, String orderId) {
		myazureDataService.save(username + "_Purchased_" + orderId, "" + data + "");
	}

	public void addUserData(String username, long data, long time) {
		myazureDataService.save(username + "_Purchased_" + time, "" + data + "");
	}

	public void disableAllOverUseUsers() throws IOException, InterruptedException {
		for (String username : VPNUserController.userList) {
			UserData userData = VPNUserController.users.get(username);
			if (!userData.isActive()) {
				return;
			}
			if (userData.getAllDataBytes() >= FREE_USER_DATA + userData.getPurchasedDataBytes()) {
				disableUser(username);
			}
		}
	}

	public boolean isActive(String username) {
		MyazureData userActiveData = myazureDataService.getMyazureData("USER_ACTIVE_" + username);
		if (userActiveData == null) {
			return false;
		}
		if (userActiveData.getMvalue().equals("0")) {
			return false;
		}
		if (userActiveData.getMvalue().equals("1") || userActiveData.getMvalue().equals("True") || userActiveData.getMvalue().equals("true")) {
			return true;
		}
		return false;
	}

}
