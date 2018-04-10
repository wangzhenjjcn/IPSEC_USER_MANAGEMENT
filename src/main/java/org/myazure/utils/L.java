package org.myazure.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class L {
	private static final Logger LOG = LoggerFactory.getLogger(L.class);
	private static boolean isWindows = true;

	public L() {

	}

	public static String runCommand(String command) throws IOException,
			InterruptedException {
		if (isWindows) {
			if (command.contains("ESP traffic")) {
				return F.readFileString("D:\\vpnServer\\tmp\\grep.txt");
			}
			if (command.contains("trafficstatus")) {
				return "006 #64: \"xauth-psk\"[26] 121.236.219.148, username=vpnuser, type=ESP, add_time=1523355469, inBytes=9494, outBytes=26677, lease=192.168.43.17/32";
			}
			return "HUEQTR7865897mn89789h";
		}

		String[] commands = { "/bin/sh", "-c", command };
		String encoding = "UTF-8";
		String s = null;
		Runtime run = Runtime.getRuntime();
		Process p = run.exec(commands);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		LOG.debug(command + "->Resault:");
		StringBuffer out = new StringBuffer();
		while ((s = stdInput.readLine()) != null) {
			out.append(new String(s.getBytes(), encoding));
		}
		LOG.debug(command + "->ERR:");
		while ((s = stdError.readLine()) != null) {
			out.append(new String(s.getBytes(), encoding));
		}
		if (p.waitFor() != 0) {
			if (p.exitValue() == 1)
				LOG.debug("命令执行失败!");
			return "";
		}
		stdError.close();
		stdInput.close();
		return out.toString();
	}
}
