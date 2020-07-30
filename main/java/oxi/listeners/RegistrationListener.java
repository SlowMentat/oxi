package oxi.listeners;


import oxi.models.User;
import oxi.events.OnRegistrationCompleteEvent;
import oxi.events.OnUserInviteEvent;
import oxi.services.UserAccountService;
import oxi.services.EmailService;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

	private String getEmailUrl(OnRegistrationCompleteEvent event, String token){
		// Note:  If successfully verified, this link needs to send a redirect to the profile creation page
		return event.getAppUrl() + "/account/user/confirm?token=" + token;
	}

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event){
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event){

		// create token
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userAccountService.createUserVerificationToken(user, token);

		// Prepare email recipient
		String recipientAddress = user.getEmail();
		String subject = "Registration Confirmation";

		// Note:  If successfully verified, this link needs to send a redirect to the profile creation page
		//String verifyEmailURL = event.getAppUrl() + "/account/user/confirm?token=" + token; // .../account/user/register/confirm 
		String verifyEmailURL = getEmailUrl(event, token);

		//String message = messages.getMessage("message.regSucc", null, event.getLocale());  //currently not used 
		//String message = "You registered successfully. We will send you a confirmation message to your email account.";

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setFrom(env.getProperty("support.email"));
		logger.debug("sending confirmation email to " + recipientAddress);
		
		Map<String,Object> templateVars = new HashMap<String,Object>();
		templateVars.put("verifyLink", verifyEmailURL);

		try{
			emailService.sendSimpleMessage(email, templateVars, "account-verification.html");
		}catch(Exception e){
			logger.error(e);
		}
	}
}