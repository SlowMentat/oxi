package oxi.listeners;


import oxi.models.Company;
import oxi.events.OnCompanyRegistrationCompleteEvent;
import oxi.services.CompanyAccountService;
import oxi.services.EmailService;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

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
*On event OnCompanyRegistrationCompleteEvent, CompanyRegistrationListener sends a confirmation link via email to the recipient
*/
@Component
class CompanyRegistrationListener implements ApplicationListener<OnCompanyRegistrationCompleteEvent> {
	private static final Logger logger = LogManager.getLogger(CompanyRegistrationListener.class);
	@Autowired
	private CompanyAccountService companyAccountService;

	@Autowired
	private MessageSource messages;

	//@Autowired
	//private JavaMailSender mailSender;

	@Autowired
	private Environment env;

	@Autowired
	private EmailService emailService;

	@Override
	public void onApplicationEvent(OnCompanyRegistrationCompleteEvent event){
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnCompanyRegistrationCompleteEvent event){
		Company company = event.getCompany();
		String token = UUID.randomUUID().toString();
		companyAccountService.createCompanyVerificationToken(company, token);

		String recipientAddress = company.getEmail();
		String subject = "Registration Confirmation";

		// Note:  If successfully verified, this link needs to send a redirect to the profile creation page
		String verifyEmailURL = event.getAppUrl() + "/account/company/confirm?token=" + token; // .../account/company/register/confirm 

		//String message = messages.getMessage("message.regSucc", null, event.getLocale());  //currently not used 
		//String message = "You registered successfully. We will send you a confirmation message to your email account.";

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setFrom(env.getProperty("support.email"));
		logger.debug("sending confirmation email to " + recipientAddress);
		//mailSender.send(email);

		Map<String,Object> templateVars = new HashMap<String,Object>();
		templateVars.put("verifyLink", verifyEmailURL);

		try{
			emailService.sendSimpleMessage(email, templateVars, "account-verification.html");
		}catch(Exception e){
			logger.error(e);
		}
	}
}