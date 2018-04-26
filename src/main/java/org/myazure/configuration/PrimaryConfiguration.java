package org.myazure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration("PrimaryConfiguration")
@EnableCaching
public class PrimaryConfiguration {
	
	@Value("${server.port}")
	private String serverPort;
	@Value("${mail.smtp.auth}")
	private String mailSmtpAuth;
	@Value("${mail.host}")
	private String mailHost;
	@Value("${mail.transport.protocol}")
	private String mailTransportProtocol;
	@Value("${mail.smtp.auth.username}")
	private String mailSmtpAuthUsername;
	@Value("${mail.smtp.auth.password}")
	private String mailSmtpAuthPassword;
	@Value("${mail.alert.recievers}")
	private String mailAlertRecivers;
	@Value("${vpn.ipsec.passwdFile}")
	private String passwdFilePath;
	@Value("${vpn.traffic.tmpFile}")
	private String trafficeFilePath;
	@Value("${vpn.trafficing.tmpFile}")
	private String trafficingFilePath;
	@Value("${vpn.ipsec.passwdEncodePre}")
	private String passwdEncodePre;
	@Value("${vpn.tmpPath}")
	private String tmpPath;
	@Value("${vpn.servername}")
	private String serverName;
	@Value("${weixin.compAppId}")
	private String compAppId;
	@Value("${weixin.compAppSecret}")
	private String compAppSecret;
	@Value("${encode.token}")
	private String encodeToken;
	@Value("${encode.key}")
	private String encodeKey;
	
	public PrimaryConfiguration() {

	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getMailSmtpAuth() {
		return mailSmtpAuth;
	}

	public void setMailSmtpAuth(String mailSmtpAuth) {
		this.mailSmtpAuth = mailSmtpAuth;
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getMailTransportProtocol() {
		return mailTransportProtocol;
	}

	public void setMailTransportProtocol(String mailTransportProtocol) {
		this.mailTransportProtocol = mailTransportProtocol;
	}

	public String getMailSmtpAuthUsername() {
		return mailSmtpAuthUsername;
	}

	public void setMailSmtpAuthUsername(String mailSmtpAuthUsername) {
		this.mailSmtpAuthUsername = mailSmtpAuthUsername;
	}

	public String getMailSmtpAuthPassword() {
		return mailSmtpAuthPassword;
	}

	public void setMailSmtpAuthPassword(String mailSmtpAuthPassword) {
		this.mailSmtpAuthPassword = mailSmtpAuthPassword;
	}

	public String getMailAlertRecivers() {
		return mailAlertRecivers;
	}

	public void setMailAlertRecivers(String mailAlertRecivers) {
		this.mailAlertRecivers = mailAlertRecivers;
	}

	public String getPasswdFilePath() {
		return passwdFilePath;
	}

	public void setPasswdFilePath(String passwdFilePath) {
		this.passwdFilePath = passwdFilePath;
	}

	public String getTrafficeFilePath() {
		return trafficeFilePath;
	}

	public void setTrafficeFilePath(String trafficeFilePath) {
		this.trafficeFilePath = trafficeFilePath;
	}

	public String getTrafficingFilePath() {
		return trafficingFilePath;
	}

	public void setTrafficingFilePath(String trafficingFilePath) {
		this.trafficingFilePath = trafficingFilePath;
	}

	public String getPasswdEncodePre() {
		return passwdEncodePre;
	}

	public void setPasswdEncodePre(String passwdEncodePre) {
		this.passwdEncodePre = passwdEncodePre;
	}

	public String getTmpPath() {
		return tmpPath;
	}

	public void setTmpPath(String tmpPath) {
		this.tmpPath = tmpPath;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getCompAppId() {
		return compAppId;
	}

	public void setCompAppId(String compAppId) {
		this.compAppId = compAppId;
	}

	public String getCompAppSecret() {
		return compAppSecret;
	}

	public void setCompAppSecret(String compAppSecret) {
		this.compAppSecret = compAppSecret;
	}

	public String getEncodeToken() {
		return encodeToken;
	}

	public void setEncodeToken(String encodeToken) {
		this.encodeToken = encodeToken;
	}

	public String getEncodeKey() {
		return encodeKey;
	}

	public void setEncodeKey(String encodeKey) {
		this.encodeKey = encodeKey;
	}
}
