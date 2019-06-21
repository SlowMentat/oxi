package oxi.services;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Service
public class EmailService{
	private static final Logger logger = LogManager.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	public void sendSimpleMessage(SimpleMailMessage mail, Map<String,Object> templateVariables) throws MessagingException, IOException{
		MimeMessage mimMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		
		//mimeMessageHelper.addAttachment("logo.png", new ClassPathResource("logo.png"));

		Context context = new Context();
		context.setVariables(templateVariables);

		//Create the HTML body usring Thymeleaf
		String html = templateEngine.process("account-verification.html", context);

		mimeMessageHelper.setTo(mail.getTo());
		mimeMessageHelper.setText(html, true);
		mimeMessageHelper.setSubject(mail.getSubject());
		mimeMessageHelper.setFrom(mail.getFrom());

		// Send mail
		mailSender.send(mimMessage);
	}
}

