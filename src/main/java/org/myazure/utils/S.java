package org.myazure.utils;


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


//
//
//
//
//
//A = @                               U = |_|
//
//B = |3                                V = \/
//
//C = (                                 W = \/\/
//
//D = |)                                 X = )(
//
//E = 3                                 Y = '/
//
//F = |=                                 Z = 2
//
//G = 6                                 a = 4
//
//H = |-|                                 b = 8
//
//I = |                                     c = ©
//
//J = _|                                  d = |>
//
//K = |(                                  e = 3
//
//L = |_                                  f = #
//
//M = /\/\                                g = 9
//
//N = /\/                                  h = h
//
//O = 0                                   i = |
//
//P = |*                                    j = j
//
//Q = 0,                                  k = |<
//
//R = |2                                   l = 1
//
//S = $                                    m = m
//
//T = 7                                     n = n
//
//o = 0                                    programs = progz
//
//p = |*                                    god = r00t
//
//q = 0.                                    fool = f00
//
//r = ®                                     heart/love = <3
//
//s = 5                                     what's up = sup
//
//t = +                                      that = dat
//
//u = 00                                   look at = peep
//
//v = \/                                      kill = frag
//
//w = \/\/                                   sweet = schweet
//
//x = ><                                    sleep = reboot
//
//y = j                                       greater than = >
//
//z = 2                                      newbie = n00b
//
//at = @                                   no = noes
//
//ck = x0r                                 woo hoo = w00t
//
//the = teh                               why = y
//
//you = j00 or u                        be = b
//
//own = pwn                             are = r
//
//dude = d00d                         fear = ph34r
//
//and = &                                super = uber
//
//blah/me = meh                      yo = j0
//
//rock = r0xx0r                         hacker = h4x0r
//
//cool = k3wl                           software = warez
//
//computer = pu73r                chick = chix0r
//
//good = teh win                      bad = teh lose
//
//loser = l4m3r                        aol = uh, 14m3r
//
//money = monies                   bye = bai
//
//kick = punt                           porn = pr0n
//
//skill = m4d 5killz                   hello = ping
//
//robot = b0t                          naked = n3k3d
//
//what = wut                           whatever = wutev
//
//cool = c00                           to/two = 2
//
//with = wit                             sex = cyb3r
