package org.myazure.vpn;

public class PasswdTest {
	public static void main(String[] args) {
		
		String dataString="\"kuo_win\" pptpd \"080232\" *";
		
		String infoString=dataString.trim();
		String[] infStrings=infoString.split("\"");
		
		for (String string : infStrings) {
			System.out.println(string);
		}
		
		
		
		
	}
}
