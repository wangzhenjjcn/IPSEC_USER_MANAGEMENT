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
import org.myazure.utils.S;
import org.myazure.vpn.entity.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("VPNUserController")
public class VPNUserController {
	@Autowired
	PrimaryConfiguration primaryConfiguration;
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(VPNUserController.class);
	@Autowired
	MyazureDataService myazureDataService;
	public static Map<String, UserData> users = new HashMap<String, UserData>();
	public static List<String> ipsecUserList = new ArrayList<String>();
	public static List<String> pptpUserList = new ArrayList<String>();
	public static List<String> l2tpUserList = new ArrayList<String>();
	public static Map<String, String> userEncodePasswd = new HashMap<String, String>();
	public static Map<String, String> userPasswd = new HashMap<String, String>();
	public static long FREE_USER_DATA = 10737418240L;

	public VPNUserController() {
	}

	public void readAllActiveUsers() {
		readAllActiveIpsecUsers();
		readAllActivePptpUsers();
		Map<String, UserData> users = new HashMap<String, UserData>();
		for (String string : ipsecUserList) {
			users.put(string, new UserData(string));
		}
		for (String string : pptpUserList) {
			users.put(string, new UserData(string));
		}
		for (String string : ipsecUserList) {
			users.get(string).setIpsec(true);
			users.get(string).setPasswdEncode(userEncodePasswd.get(string));
		}
		for (String string : pptpUserList) {
			users.get(string).setPptp(true);
			users.get(string).setPasswd(userPasswd.get(string));
		}
		VPNUserController.users = users;
	}

	private void writeAllActiveUsers() throws IOException, InterruptedException {
		writeUsersToIPSecPassWdFile();
		writeUsersToPptpPassWdFile();
	}

	public void readAllActiveIpsecUsers() {
		String[] userPasswdLines = F.readFileStrings(
				primaryConfiguration.getIpsecPasswdFilePath(), "\n");
		List<String> userList2 = new ArrayList<String>();
		Map<String, String> userEncodePasswd2 = new HashMap<String, String>();
		myazureDataService
				.deleteAllMyazureDatasByKeyStartWith("ACTIVE_IPSEC_USER_");
		for (String string : userPasswdLines) {
			if (string.split(":").length > 2 && string.contains(":xauth-psk")) {
				String usernameString = string.split(":")[0];
				String passwdString = string.split(":")[1];
				userList2.add(usernameString);
				userEncodePasswd2.put(usernameString, passwdString);
				try {
					myazureDataService.save("USER_IPSEC_PASSWD_"
							+ usernameString + "", "" + passwdString + "");
					myazureDataService.save("ACTIVE_IPSEC_USER_"
							+ usernameString + "", "1");
				} catch (Exception e) {
					continue;
				}
			}

		}
		ipsecUserList = userList2;
		userEncodePasswd = userEncodePasswd2;
	}

	public void readAllActivePptpUsers() {
		String[] userPasswdLines = F.readFileStrings(
				primaryConfiguration.getPptpPasswdFilePath(), "\n");
		List<String> userList = new ArrayList<String>();
		Map<String, String> userPasswd = new HashMap<String, String>();
		myazureDataService
				.deleteAllMyazureDatasByKeyStartWith("ACTIVE_PPTP_USER_");
		for (String string : userPasswdLines) {
			String infoString = string.trim();
			String[] infStrings = infoString.split("\"");
			if (infStrings.length == 4 && infoString.contains("pptpd")) {
				String usernameString = infStrings[1];
				String passwdString = infStrings[3];
				userList.add(usernameString);
				userPasswd.put(usernameString, passwdString);
				try {
					myazureDataService.save("USER_PPTP_PASSWD_"
							+ usernameString + "", "" + passwdString + "");
					myazureDataService.save("ACTIVE_PPTP_USER_"
							+ usernameString + "", "1");
				} catch (Exception e) {
					continue;
				}
			}
		}
		VPNUserController.pptpUserList = userList;
		VPNUserController.userPasswd = userPasswd;
	}

	public void addUser(String username, String passwd) throws IOException,
			InterruptedException {
		readAllActiveUsers();
		if (VPNUserController.users.containsKey(username)) {
			deleteUser(username);
		}

		try {
			VPNUserController.ipsecUserList.add(username);
			VPNUserController.pptpUserList.add(username);
			VPNUserController.userEncodePasswd.put(username,
					creatPasswd(passwd));
			VPNUserController.userPasswd.put(username, passwd);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		myazureDataService.save("ACTIVE_IPSEC_USER_" + username + "", "" + 1
				+ "");
		myazureDataService.save("ACTIVE_PPTP_USER_" + username + "", "" + 1
				+ "");
		myazureDataService.save("USER_PPTP_PASSWD_" + username + "", ""
				+ passwd + "");
		myazureDataService.save("USER_IPSEC_PASSWD_" + username + "", ""
				+ VPNUserController.userEncodePasswd.get(username) + "");
		myazureDataService.save("ALL_DATA_IN_" + username, "" + 0 + "");
		myazureDataService.save("ALL_DATA_OUT_" + username, "" + 0 + "");
		myazureDataService.save("ALL_DATA_" + username, "" + 0 + "");
		myazureDataService.save(username + "_Purchased", "" + 0 + "");
		writeAllActiveUsers();
	}

	public void addUserLimitTime(String username, String timeLimit) {
		if (VPNUserController.users.containsKey(username)) {
			String time = S.toLongTimeFromNow(timeLimit);
			myazureDataService.save("TIME_LIMITED_" + username + "", time);
		}
	}

	private void deleteIpsecUserHistoryData(String username) {
		myazureDataService.delete("ACTIVE_IPSEC_USER_" + username + "");
		myazureDataService.delete("ALL_DATA_IN_" + username);
		myazureDataService.delete("ALL_DATA_OUT_" + username);
		myazureDataService.delete("ALL_DATA_" + username);
		myazureDataService.delete(username + "_Purchased");
		myazureDataService.deleteAllMyazureDatasByKeyStartWith(username
				+ "_Purchased_");
	}

	private void deletePptpUserHistoryData(String username) {
		myazureDataService.delete("ACTIVE_PPTP_USER_" + username + "");
		myazureDataService.delete("ALL_DATA_IN_" + username);
		myazureDataService.delete("ALL_DATA_OUT_" + username);
		myazureDataService.delete("ALL_DATA_" + username);
		myazureDataService.delete(username + "_Purchased");
		myazureDataService.deleteAllMyazureDatasByKeyStartWith(username
				+ "_Purchased_");
	}

	public void deleteUser(String username) throws IOException,
			InterruptedException {
		readAllActiveUsers();
		if (VPNUserController.ipsecUserList.contains(username)) {
			VPNUserController.ipsecUserList.remove(username);
			VPNUserController.userEncodePasswd.remove(username);
			deleteIpsecUserHistoryData(username);
			writeUsersToIPSecPassWdFile();
		}
		if (VPNUserController.pptpUserList.contains(username)) {
			VPNUserController.pptpUserList.remove(username);
			VPNUserController.userPasswd.remove(username);
			deletePptpUserHistoryData(username);
			writeUsersToPptpPassWdFile();
		}
		VPNUserController.users.remove(username);
		return;
	}

	public void disableUser(String username) throws IOException,
			InterruptedException {
		
		if (VPNUserController.ipsecUserList.contains(username)) {
			readAllActiveIpsecUsers();
			VPNUserController.ipsecUserList.remove(username);
			VPNUserController.userEncodePasswd.remove(username);
			myazureDataService.save("ACTIVE_IPSEC_USER_" + username + "", "0");
			writeUsersToIPSecPassWdFile();
		}  
		if (VPNUserController.pptpUserList.contains(username)) {
			readAllActivePptpUsers();
			VPNUserController.pptpUserList.remove(username);
			VPNUserController.userPasswd.remove(username);
			myazureDataService.save("ACTIVE_PPTP_USER_" + username + "", "0");
			writeUsersToPptpPassWdFile();
		}  
			return;
		 
	}

	public void modifyUser(String username, String passwd) throws IOException,
			InterruptedException {
		readAllActiveUsers();
		disableUser(username);
		addUser(username, passwd);
	}

	private void writeUsersToIPSecPassWdFile() throws IOException,
			InterruptedException {
		StringBuffer fileStringBuffer = new StringBuffer();
		for (String string : VPNUserController.ipsecUserList) {
			fileStringBuffer.append(string).append(":")
					.append(VPNUserController.userEncodePasswd.get(string))
					.append(":xauth-psk").append("\n");
		}
		F.writeStringToFile(fileStringBuffer.toString(),
				primaryConfiguration.getIpsecPasswdFilePath());
	}

	private void writeUsersToPptpPassWdFile() throws IOException,
			InterruptedException {
		StringBuffer fileStringBuffer = new StringBuffer();
		for (String string : VPNUserController.pptpUserList) {
			fileStringBuffer.append("\"").append(string).append("\"  pptpd \"")
					.append(VPNUserController.userPasswd.get(string))
					.append("\"  *").append("\n");
		}
		F.writeStringToFile(fileStringBuffer.toString(),
				primaryConfiguration.getPptpPasswdFilePath());
	}

	private String creatPasswd(String passwd) throws IOException,
			InterruptedException {
		String creatPasswdCommand = "sudo openssl passwd -1 \""
				+ passwd.replace("\"", "").replace("\\", "") + "\"";
		return L.runCommand(creatPasswdCommand);
	}

	public void addFreeUserData(String username) {
		addUserData(username, FREE_USER_DATA, "0");
	}

	public void addUserData(String username, long data) {
		addUserData(username, data, System.currentTimeMillis());
	}

	public void addUserData(String username, long data, String orderId) {
		myazureDataService.save(username + "_Purchased_" + orderId, "" + data
				+ "");
	}

	public void addUserData(String username, long data, long time) {
		myazureDataService
				.save(username + "_Purchased_" + time, "" + data + "");
	}

	public void disableAllOverUseUsers() throws IOException,
			InterruptedException {
		for (String username : VPNUserController.ipsecUserList) {
			UserData userData = VPNUserController.users.get(username);
			if (!userData.isActive()) {
				return;
			}
			if (userData.getAllDataBytes() >= FREE_USER_DATA
					+ userData.getPurchasedDataBytes()) {
				disableUser(username);
			}
		}
	}

	public boolean isActive(String username) {
		MyazureData userIpsecActiveData = myazureDataService
				.getMyazureData("ACTIVE_IPSEC_USER_" + username);
		MyazureData userPptpActiveData = myazureDataService
				.getMyazureData("ACTIVE_PPTP_USER_" + username);
		if (userIpsecActiveData == null && userPptpActiveData==null) {
			return false;
		}
		if (userIpsecActiveData != null ) {
			if (userIpsecActiveData.getMvalue().equals("1")
					|| userIpsecActiveData.getMvalue().equals("True")
					|| userIpsecActiveData.getMvalue().equals("true")) {
				return true;
			}
		}
		if (userPptpActiveData != null ) {
			if (userPptpActiveData.getMvalue().equals("1")
					|| userPptpActiveData.getMvalue().equals("True")
					|| userPptpActiveData.getMvalue().equals("true")) {
				return true;
			}
		}
		return false;
	}

}
