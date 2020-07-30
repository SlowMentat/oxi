package oxi.listeners;


import oxi.models.UserVerificationToken;
import oxi.models.User;
import oxi.events.OnUserInviteEvent;
import oxi.events.OnRegistrationCompleteEvent;
import oxi.services.UserAccountService;
import oxi.services.EmailService;

import org.springframework.context.ApplicationEvent;
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
*On event OnUserInviteEvent, UserInviteListener sends a confirmation link via email to the recipient
*/
@Component
class UserInviteListener implements ApplicationListener<OnUserInviteEvent> {
	private static final Logger logger = LogManager.getLogger(UserInviteListener.class);
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

	private String getEmailUrl(OnUserInviteEvent event, String token){
		return event.getAppUrl() + "/form/user/updatePassword?token=" + token;
	}

	@Override
	public void onApplicationEvent(OnUserInviteEvent event){
		this.sendEmailInvite(event);
	}

	/*
	*
	*/
	private void sendEmailInvite(OnUserInviteEvent event){
		try{
			// Generate a token and save to database
			User user = event.getUser();
			String token = UUID.randomUUID().toString();
			userAccountService.createUserVerificationToken(user, token);

			// Send email with link to /account/edit containing token in query string
			String recipientAddress = user.getEmail();
			String subject = "Alpha Invite";
		
			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(recipientAddress);
			email.setSubject(subject);
			email.setFrom(env.getProperty("support.email"));
			
			Map<String,Object> templateVars = new HashMap<String,Object>();
			templateVars.put("continueLink", getEmailUrl(event, token));
			templateVars.put("tempPassword", event.getDecodedPassword());
			logger.debug("sending confirmation email to " + recipientAddress);
		
			try{
				emailService.sendSimpleMessage(email, templateVars, "user-invite.html");
			}catch(Exception e){
				logger.error(e);
			}


			// Submit new password is submitted (including token) to /account/edit/password
			// If OK, log user in, and redirect to /shop/browse

		}
		catch(Exception e){
			logger.error(e);
		}
	}
}