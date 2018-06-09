package org.myazure.utils;

import java.sql.Date;

public class S {
	public S() {

	}

	@SuppressWarnings("unused")
	public static String toStroageString(long data) {
		int unit = 0;
		long userdata = data;
		long userdata2 = data - (data / 1000L) * 1000L;

		while (userdata > 1000L) {
			userdata = userdata / 1000L;
			userdata2 = userdata%1000L;
			unit++;
		}
		StringBuffer dataBuffer=new StringBuffer();
		dataBuffer.append(userdata).append((unit + "").replace("1", "KB").replace("2", "MB")
						.replace("3", "GB").replace("4", "TB").replace("0", "B")
						.replace("5", "PB"));
//		dataBuffer.append(userdata2).append((unit + "").replace("1", "B").replace("2", "KB")
//				.replace("3", "MB").replace("4", "GB")
//				.replace("5", "TB"));
		return dataBuffer.toString();
	}

	public static String toLongTimeFromNow(String timeLimit) {
		String timeString=timeLimit;
		timeString=timeString.toLowerCase().replace(" ", "").replace("-","").replace("(", "").replace(")", "").replace("$", "").replace("*","");
		if (timeLimit.contains("d")|| timeLimit.contains("day")) {
			String time=timeLimit.replace("day","").replace("d", "");
			Long day= Long.valueOf(time)* 24 * 60 * 60 * 1000L+System.currentTimeMillis();
			return day+"";
		} 
		if (timeLimit.contains("m")|| timeLimit.contains("month")) {
			String time=timeLimit.replace("month","").replace("m", "");
			Long day= Long.valueOf(time)*30 * 24 * 60 * 60 * 1000L+System.currentTimeMillis();
			return day+"";
		} 
		if (timeLimit.contains("y")|| timeLimit.contains("year")) {
			String time=timeLimit.replace("year","").replace("y", "");
			Long day= Long.valueOf(time)*365 * 24 * 60 * 60 * 1000L+System.currentTimeMillis();
			return day+"";
		} 
		return ""+System.currentTimeMillis();
	}

}
