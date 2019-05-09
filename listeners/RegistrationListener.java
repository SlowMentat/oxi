package oxi.listeners;


import oxi.models.User;
import oxi.events.OnRegistrationCompleteEvent;
import oxi.services.UserAccountService;
import oxi.services.EmailService;

import java.util.UUID;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/*
*On event OnRegistrationCompleteEvent, RegistrationListener sends a confirmation link via email to the recipient
*/
@Component
class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	private static final Logger logger = LogManager.getLogger(RegistrationListener.class);
	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private MessageSource messages;

	//@Autowired
	//private JavaMailSender mailSender;

	@Autowired
	private Environment env;

	@Autowired
	private EmailService emailService;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event){
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event){
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userAccountService.createUserVerificationToken(user, token);

		String recipientAddress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationURI = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
		String message = messages.getMessage("message.regSucc", null, event.getLocale());  //TODO: fix message bundle configuration so this works
		//String message = "You registered successfully. We will send you a confirmation message to your email account.";

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + " \r\n" +  "https://oxisalechannel.com:8080" + confirmationURI);
		email.setFrom(env.getProperty("support.email"));
		logger.debug("sending confirmation email to " + recipientAddress);
		//mailSender.send(email);
		try{
			emailService.sendSimpleMessage(email);
		}catch(Exception e){
			logger.error(e);
		}
	}
}