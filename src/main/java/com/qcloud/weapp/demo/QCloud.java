package com.qcloud.weapp.demo;

import org.json.JSONException;


public class QCloud {
	
	public static void setupSDK() {
		 
	}

	private static String getConfigFilePath() {
		String defaultConfigFilePath = null;
		String osName = System.getProperty("os.name").toLowerCase();
		boolean isWindows = osName.indexOf("windows") > -1;
		boolean isLinux = osName.indexOf("linux") > -1;
		
		if (isWindows) {
			defaultConfigFilePath = "C:\\qcloud\\sdk.config";
		}
		else if (isLinux) {
			defaultConfigFilePath = "/etc/qcloud/sdk.config";
		}
		return defaultConfigFilePath;
	}
	
}
