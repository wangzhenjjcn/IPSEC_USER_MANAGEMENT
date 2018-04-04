package org.myazure.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class L {
	private static final Logger LOG = LoggerFactory.getLogger(L.class);

	public L() {

	}

	public static String runCommand(String command) throws IOException, InterruptedException {
		String[] commands = { "/bin/sh", "-c", command};
		String encoding = "UTF-8";
		String s = null;
		Runtime run = Runtime.getRuntime();
		Process p = run.exec(commands);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		LOG.debug(command + "->Resault:");
		StringBuffer out=new StringBuffer();
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
