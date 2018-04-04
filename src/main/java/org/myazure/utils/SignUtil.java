package org.myazure.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author wangzhenjjcn@gmail.com
 */
public class SignUtil {

	public static String SHA1(String inStr) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");// 选择SHA-1，也可以选择MD5
			byte[] digest = md.digest(inStr.getBytes());// 返回的是byet[]，要转化为String存储比较方便
			outStr = bytetoString(digest);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return outStr;
	}

	public static boolean checkSignature(String token, String signature,
			String timestamp, String nonce) {
		boolean flag = false;
		if (signature != null && !signature.equals("") && timestamp != null
				&& !timestamp.equals("") && nonce != null && !nonce.equals("")) {
			String sha1 = "";
			String[] ss = new String[] { token, timestamp, nonce };
			Arrays.sort(ss);
			for (String s : ss) {
				sha1 += s;
			}

			sha1 = SHA1(sha1);
			if (sha1.equals(signature)) {
				flag = true;
			}
		}
		return flag;
	}

	public static String bytetoString(byte[] digest) {
		String str = "";
		String tempStr = "";

		for (int i = 0; i < digest.length; i++) {
			tempStr = (Integer.toHexString(digest[i] & 0xff));
			if (tempStr.length() == 1) {
				str = str + "0" + tempStr;
			} else {
				str = str + tempStr;
			}
		}
		return str.toLowerCase();
	}
}
