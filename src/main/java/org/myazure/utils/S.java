package org.myazure.utils;

public class S {
	public S() {

	}

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

}
