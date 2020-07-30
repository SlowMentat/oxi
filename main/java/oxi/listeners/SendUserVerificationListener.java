package oxi.listeners;


import oxi.models.UserVerificationToken;
import oxi.events.OnSendUserVerificationEvent;
import oxi.services.UserAccountService;
import oxi.services.EmailService;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.mail.internet.MimeMessage;

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
*On event OnSendUserVerificationEvent, SendUserVerificationListener sends a confirmation link via email to the recipient
*/
@Component
class SendUserVerificationListener implements ApplicationListener<OnSendUserVerificationEvent> {
	private static final Logger logger = LogManager.getLogger(SendUserVerificationListener.class);
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
	public void onApplicationEvent(OnSendUserVerificationEvent event){
		this.resetUserVerificationToken(event);
	}

	private void resetUserVerificationToken(OnSendUserVerificationEvent event){
		try{
			String existingToken = event.getUserVerificationToken().getToken();
			String replacementToken = userAccountService.updateUserVerificationToken(existingToken).getToken();	

			if(Objects.equals(existingToken, replacementToken)){
				logger.debug("existing UVT = " + existingToken);
				logger.debug("new UVT = " + replacementToken);
				throw new Exception("Existing user verification token was not reset.");
			}

			String recipientAddress = event.getUserVerificationToken().getUser().getEmail();
			String subject = "new verification token acquired.  sending email.";
	
			// Note:  If successfully verified, this link needs to send a redirect to the profile creation page
			String verifyEmailURL = event.getAppUrl() + "/account/user/confirm?token=" + replacementToken; // .../account/user/register/confirm 

			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(recipientAddress);
			email.setSubject(subject);
			email.setFrom(env.getProperty("support.email"));
			logger.debug("sending confirmation email to " + recipientAddress);
			//mailSender.send(email);
	
			Map<String,Object> templateVars = new HashMap<String,Object>();
			templateVars.put("verifyLink", verifyEmailURL);
	
			emailService.sendSimpleMessage(email, templateVars, "account-verification.html");
		}
		catch(Exception e){
			logger.error(e);
		}
	}
}