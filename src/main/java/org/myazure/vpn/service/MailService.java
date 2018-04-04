package org.myazure.vpn.service;

import javax.mail.MessagingException;

public interface MailService {
	public void sendMailBySMTP(String subject, String context, String recivers,
			String sender) throws MessagingException;

	public void sendMail(String subject, String context, String recivers)
			throws MessagingException;

	public void sendMail(String host, String username, String password,
			String subject, String context, String recivers, String sender)
			throws MessagingException;
}
