package org.myazure.vpn.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.myazure.utils.F;
import org.myazure.utils.L;
import org.myazure.vpn.configuration.PrimaryConfiguration;
import org.myazure.vpn.service.MyazureDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class VPNUserController {
	private static final Logger LOG = LoggerFactory
			.getLogger(VPNUserController.class);
	@Autowired
	PrimaryConfiguration primaryConfiguration;
	@Autowired
	private MyazureDataService myazureDataService;
	public static List<String> userList = new ArrayList<String>();
	public static Map<String, String> users = new HashMap<String, String>();

	public VPNUserController() {

	}

	public void readAllUser() {
		String[] userPasswdLines = F.readFileStrings(
				primaryConfiguration.getPasswdFilePath(), "\n");
		userList = new ArrayList<String>();
		users = new HashMap<String, String>();
		for (String string : userPasswdLines) {
			if (string.contains(":") && string.contains(":xauth-psk")) {
				String usernameString = string.split(":")[0];
				String passwdString = string.split(":")[1];
				userList.add(usernameString);
				users.put(usernameString, passwdString);
			}
		}
	}

	public void addUser(String username, String passwd) {
		if (userList.contains(username)) {
			try {
				users.put(username, creatPasswd(passwd));
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			userList.add(username);
			try {
				users.put(username, creatPasswd(passwd));
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		writeUsersToFile();
	}

	public void deleteUser(String username) {
		if (userList.contains(username)) {
			userList.remove(username);
			users.remove(username);
		} else {
			users.remove(username);
		}
		writeUsersToFile();
	}

	public void writeUsersToFile() {
		StringBuffer fileStringBuffer = new StringBuffer();
		for (String string : userList) {
			fileStringBuffer.append(string).append(":")
					.append(users.get(string)).append(":xauth-psk")
					.append("\n");
		}
		try {
			F.writeStringToFile(fileStringBuffer.toString(),
					primaryConfiguration.getPasswdFilePath());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String creatPasswd(String passwd) throws IOException,
			InterruptedException {
		String creatPasswdCommand = "sudo openssl passwd -1 \""
				+ passwd.replace("\"", "").replace("\\", "") + "\"";
		return L.runCommand(creatPasswdCommand);
	}
}
