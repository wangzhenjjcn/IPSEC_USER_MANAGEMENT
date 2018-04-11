package org.myazure.service;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MailServiceImpl implements MailService {

	@Override
	public void sendMailBySMTP(String subject, String context, String recivers,
			String sender) throws MessagingException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMail(String subject, String context, String recivers)
			throws MessagingException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMail(String host, String username, String password,
			String subject, String context, String recivers, String sender)
			throws MessagingException {
		// TODO Auto-generated method stub

	}
}
