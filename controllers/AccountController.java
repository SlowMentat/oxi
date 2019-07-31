package oxi.controllers;


import oxi.security.SecurityConfiguration;

import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.persistence.*;
import java.security.Principal;
import com.fasterxml.jackson.databind.*; 

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

import oxi.models.Privilege;
import oxi.models.Profile;
import oxi.models.Company;
import oxi.models.dto.CompanyDto;
import oxi.models.UserVerificationToken;
import oxi.models.CompanyVerificationToken;
import oxi.models.User;
import oxi.models.dto.UserDto;
import oxi.models.dto.util.GenericResponse;
import oxi.services.UserAccountService;
import oxi.services.CompanyAccountService;
import oxi.events.OnRegistrationCompleteEvent;
import oxi.events.OnCompanyRegistrationCompleteEvent;
import oxi.errors.UserAlreadyExistException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.access.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.http.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.annotation.*;

import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;


@Controller
@RequestMapping(value ="/account")
public class AccountController{

	private static final Logger logger = LogManager.getLogger(AccountController.class);

	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private CompanyAccountService companyAccountServices;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	@Autowired
	private MessageSource messages;	

	/*
	* Registers user account with provided details, given details are unique.  On successuful Registration, publishes OnRegistrationCompleteEvent.
	*/
	//@Override
	@Secured({"ROLE_ANONYMOUS"})
	@RequestMapping(value = "/user/register", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerUserAccount(@RequestBody UserDto userDto, final HttpServletRequest request){
		
		logger.debug("Registering user account with information: {} ", userDto);
		User registeredUser = userAccountService.registerAccount(userDto);

		if(registeredUser == null){
			throw new UserAlreadyExistException();
			//result.rejectValue("email", "message.regError");
		}

		//try{
		String appUrl = 
			"https://" + 
			request.getServerName() + ":" + 
			//request.getServerPort() +  
			request.getContextPath();

		logger.debug("registering OnRegistrationCompleteEvent");
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, request.getLocale(), appUrl));
		return new ResponseEntity<>(new GenericResponse("success"), HttpStatus.OK);
		//}catch (Exception e){
		//	return new ModelAndView("emailError", "user", userDto);
		//}
	}

	/*
	* Registers retailer account with provided details, given details are unique.  On successuful Registration, publishes OnRegistrationCompleteEvent.
	*/
	//@Override
	@Secured({"ROLE_ANONYMOUS"})
	@RequestMapping(value = "/retailer/register", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerCompanyAccount(@RequestBody CompanyDto companyDto, final HttpServletRequest request){
		
		logger.debug("Registering company account with information: {} ", companyDto);
		Company registeredCompany = companyAccountServices.registerAccount(companyDto);

		if(registeredCompany == null){
			throw new UserAlreadyExistException();
			//result.rejectValue("email", "message.regError");
		}

		//try{
		String appUrl = 
			"https://" + 
			request.getServerName() + ":" + 
			//request.getServerPort() +  
			request.getContextPath();

		logger.debug("registering OnCompanyRegistrationCompleteEvent");
		eventPublisher.publishEvent(new OnCompanyRegistrationCompleteEvent(registeredCompany, request.getLocale(), appUrl));
		return new ResponseEntity<>(new GenericResponse("success"), HttpStatus.OK);
		//}catch (Exception e){
		//	return new ModelAndView("emailError", "user", userDto);
		//}
	}

	/*
	* Controller handling requests from email registration links, which are sent during account provisioning
	*/
	//@Override
	@Secured({"ROLE_ANONYMOUS"})
	@RequestMapping(value = "/user/confirm", method = RequestMethod.GET)
	public ResponseEntity<?> confirmRegistration(@RequestParam(value="token", required=true) String token, HttpServletRequest request){
	//public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token){
		try{
			Locale locale = request.getLocale();
			final HttpHeaders headers = new HttpHeaders();
			UserVerificationToken userVerificationToken = userAccountService.getUserVerificationToken(token);
	
			//Make sure user agenst hitting this controller are sending tokens
			if(userVerificationToken == null){
				String message = messages.getMessage("auth.message.invalidToken", null, locale);
				headers.setLocation(new URI(request.getServletPath() + "/user/failedRegistration"));
				return new ResponseEntity<>(new GenericResponse(message), headers, HttpStatus.PERMANENT_REDIRECT);
				//model.addAttribute("message", message);
				//return "redirect:/badUser.html?lang=" + locale.getLanguage();
			}
	
			//get the user object associated with the received token
			User user = userVerificationToken.getUser();
			Calendar calendar = Calendar.getInstance();
	
			//Is received token expired
			if((userVerificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0){
				String messageValue = messages.getMessage("auth.message.expired", null, locale);
				headers.setLocation(new URI(request.getServletPath() + "/user/failedRegistration"));
				return new ResponseEntity<>(new GenericResponse(messageValue), headers, HttpStatus.PERMANENT_REDIRECT);
			}
			//model.addAttribute("message", messageValue);
			//return "redirect:/badUser.html?lang=" + locale.getLanguage();
	
			//set the enable property and update the user in the db
			user.setEnabled(true);
			userAccountService.activateProvisionedUser(user);

			headers.setLocation(new URI(request.getServletPath() + "/user/login"));
			headers.set("WWW-Authenticate", SecurityConfiguration.TOKEN_PREFIX);
			return new ResponseEntity<>(null, headers, HttpStatus.PERMANENT_REDIRECT);
			//return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
		}catch(Exception e){
			logger.error(e);
			return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

		/*
	* Controller handling requests from email registration links, which are sent during account provisioning
	*/
	//@Override
	@Secured({"ROLE_ANONYMOUS"})
	@RequestMapping(value = "/company/confirm", method = RequestMethod.GET)
	public ResponseEntity<?> confirmCompanyRegistration(@RequestParam(value="token", required=true) String token, HttpServletRequest request){
	//public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token){
		try{
			Locale locale = request.getLocale();
			final HttpHeaders headers = new HttpHeaders();
			CompanyVerificationToken companyVerificationToken = companyAccountServices.getCompanyVerificationToken(token);
	
			//Make sure company agenst hitting this controller are sending tokens
			if(companyVerificationToken == null){
				String message = messages.getMessage("auth.message.invalidToken", null, locale);
				headers.setLocation(new URI(request.getServletPath() + "/company/failedRegistration"));
				return new ResponseEntity<>(new GenericResponse(message), headers, HttpStatus.PERMANENT_REDIRECT);
				//model.addAttribute("message", message);
				//return "redirect:/badUser.html?lang=" + locale.getLanguage();
			}
	
			//get the company object associated with the received token
			Company company = companyVerificationToken.getCompany();
			Calendar calendar = Calendar.getInstance();
	
			//Is received token expired
			if((companyVerificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0){
				String messageValue = messages.getMessage("auth.message.expired", null, locale);
				headers.setLocation(new URI(request.getServletPath() + "/company/failedRegistration"));
				return new ResponseEntity<>(new GenericResponse(messageValue), headers, HttpStatus.PERMANENT_REDIRECT);
			}
			//model.addAttribute("message", messageValue);
			//return "redirect:/badUser.html?lang=" + locale.getLanguage();
	
			//set the enable property and update the company in the db
			company.setEnabled(true);
			companyAccountServices.activateProvisionedCompany(company);

			headers.setLocation(new URI(request.getServletPath() + "/company/login"));
			headers.set("WWW-Authenticate", SecurityConfiguration.TOKEN_PREFIX);
			return new ResponseEntity<>(null, headers, HttpStatus.PERMANENT_REDIRECT);
			//return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
		}catch(Exception e){
			logger.error(e);
			return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}