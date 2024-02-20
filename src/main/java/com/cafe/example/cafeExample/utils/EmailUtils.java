package com.cafe.example.cafeExample.utils;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;

	public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noobjr1999@gmail.com");
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(text);
		if (!list.isEmpty())
			mailMessage.setCc(getCcArray(list));
		mailSender.send(mailMessage);
	}

	private String[] getCcArray(List<String> ccList) {
		String[] ccArr = new String[ccList.size()];
		for (int i = 0; i < ccList.size(); i++) {
			ccArr[i] = ccList.get(i);
		}
		return ccArr;
	}
	public void forgetMail(String to,String subject,String password) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper mHelper = new MimeMessageHelper(message,true);
		mHelper.setFrom("noobjr1999@gmail.com");
		mHelper.setTo(to);
		mHelper.setSubject(subject);
		String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
		message.setContent(htmlMsg,"text/html");
		mailSender.send(message);
		
	}
}
