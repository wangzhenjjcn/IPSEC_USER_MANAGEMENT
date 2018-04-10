package org.myazure.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class F {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(F.class);

	public F() {

	}

	public static String[] readFileStrings(String filePath) {
		return readFileString(filePath, "UTF-8").split(System.lineSeparator());
	}

	public static String[] readFileStrings(String filePath, String separator) {
		return readFileString(filePath, "UTF-8").split(separator);
	}

	public static String[] readFileStrings(String filePath, String separator,
			String encode) {
		return readFileString(filePath, encode).split(separator);
	}

	public static String readFileString(String filePath) {
		return readFileString(filePath, "UTF-8");
	}

	@SuppressWarnings("finally")
	public static String readFileString(String filePath, String encoding) {
		String fileString = "";
		File file = new File(filePath);
		Long filelength = file.length();
		if (!file.exists()) {
			return fileString;
		}
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			fileString = new String(filecontent, encoding);
			return fileString;
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
		} finally {
			return fileString;
		}
	}

	public static void writeStringToFile(String data, String filePath)
			throws IOException, InterruptedException {
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		if (!file.canWrite()) {
			L.runCommand("sudo chmod 777 " + filePath);
		}
		FileOutputStream fos = new FileOutputStream(filePath);
		fos.write(data.getBytes());
		fos.close();
	}

	public static void appendStringToFile(String data, String filePath)
			throws IOException, InterruptedException {
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		if (!file.canWrite()) {
			L.runCommand("sudo chmod 777 " + filePath);
		}
		PrintStream ps = new PrintStream(new FileOutputStream(file));
		ps.append(data);
		ps.close();
	}

}
