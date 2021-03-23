package oxi.listeners;

import oxi.models.User;
import oxi.models.UserVerificationToken;
import oxi.events.OnSendUserPasswordVerificationEvent;
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
*On event OnSendUserPasswordVerificationEvent, SendUserPasswordVerificationListener sends a confirmation link via email to the recipient
*/
@Component
class SendUserPasswordVerificationListener implements ApplicationListener<OnSendUserPasswordVerificationEvent> {
	private static final Logger logger = LogManager.getLogger(SendUserPasswordVerificationListener.class);
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
	public void onApplicationEvent(OnSendUserPasswordVerificationEvent event){
		this.resetUserPasswordVerificationToken(event);
	}

	private void resetUserPasswordVerificationToken(OnSendUserPasswordVerificationEvent event){

		// create token
		User user = event.getUser();
		String refreshedToken = UUID.randomUUID().toString();
		userAccountService.refreshUserPasswordVerificationToken(user, refreshedToken);

		String recipientAddress = user.getEmail();
		String subject = "New password verification token acquired.  Sending email.";

		// Note:  If successfully verified, this link needs to send a redirect update password page
		String verifyEmailURL = event.getBaseUrl() + "/form/user/updatePassword?token=" + refreshedToken; // .../account/user/register/confirm 
		String privacyURL = event.getBaseUrl() + "/legal/privacy-policy.html";
		String termsURL = event.getBaseUrl() + "/legal/terms-and-conditions.html";
		String aboutURL = event.getBaseUrl() + "/legal/about.html";

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setFrom(env.getProperty("support.email"));
		logger.debug("sending confirmation email to " + recipientAddress);
		//mailSender.send(email);

		Map<String,Object> templateVars = new HashMap<String,Object>();
		templateVars.put("verifyLink", verifyEmailURL);
		templateVars.put("aboutLink", aboutURL);
		templateVars.put("termsLink", termsURL);
		templateVars.put("privacyLink", privacyURL);
		templateVars.put("username", (" " + user.getUsername()));

		try{
			emailService.sendSimpleMessage(email, templateVars, "account-verification-password.html");
		}
		catch(Exception e){
			logger.error(e);
		}

		/*try{
			String existingToken = event.getUserPasswordVerificationToken() != null ? event.getUserPasswordVerificationToken().getToken() : null;
			String replacementToken = userAccountService.updateUserPasswordVerificationToken(existingToken).getToken();	

			if(Objects.equals(existingToken, replacementToken)){
				logger.debug("existing UPVT = " + existingToken);
				logger.debug("new UPVT = " + replacementToken);
				throw new Exception("Existing user verification token was not reset.");
			}

			String recipientAddress = event.getUserPasswordVerificationToken().getUser().getEmail();
			String subject = "New password verification token acquired.  Sending email.";
	
			// Note:  If successfully verified, this link needs to send a redirect update password page
			String verifyEmailURL = event.getAppUrl() + "/account/user/password?token=" + replacementToken; // .../account/user/register/confirm 
			String privacyURL = event.getAppUrl() + "/legal/privacy-policy.html";
			String termsURL = event.getAppUrl() + "/legal/terms-and-conditions.html";
			String aboutURL = event.getAppUrl() + "/legal/about.html";

			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(recipientAddress);
			email.setSubject(subject);
			email.setFrom(env.getProperty("support.email"));
			logger.debug("sending confirmation email to " + recipientAddress);
			//mailSender.send(email);
	
			Map<String,Object> templateVars = new HashMap<String,Object>();
			templateVars.put("verifyLink", verifyEmailURL);
			templateVars.put("aboutLink", aboutURL);
			templateVars.put("termsLink", termsURL);
			templateVars.put("privacyLink", privacyURL);
	
			emailService.sendSimpleMessage(email, templateVars, "account-verification-password.html");
		}
		catch(Exception e){
			logger.error(e);
		}*/
	}
}