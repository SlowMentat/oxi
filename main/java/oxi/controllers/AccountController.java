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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.security.access.annotation.*;

import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.net.URISyntaxException;


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
	private String appURI = "/retailer";

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
	* Registers company account with provided details, given details are unique.  On successuful Registration, publishes OnRegistrationCompleteEvent.
	*/
	//@Override
	@Secured({"ROLE_ANONYMOUS"})
	@RequestMapping(value = "/company/register", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerCompanyAccount(@RequestBody CompanyDto companyDto, final HttpServletRequest request){

		try{	
			logger.debug("Registering company account with information: {} ", companyDto);
			Company registeredCompany = companyAccountServices.registerAccount(companyDto);
	
			if(registeredCompany == null){
				throw new UserAlreadyExistException();
			}
	
			String appUrl = 
				"https://" + 
				request.getServerName() + ":" + 
				//request.getServerPort() +  
				request.getContextPath();
	
			logger.debug("registering OnCompanyRegistrationCompleteEvent");
			eventPublisher.publishEvent(new OnCompanyRegistrationCompleteEvent(registeredCompany, request.getLocale(), appUrl));
			return new ResponseEntity<>(new GenericResponse("success"), HttpStatus.OK);
		}

		catch(Exception e){
			logger.error("", e);
			return new ResponseEntity<>(new GenericResponse(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_RETAILER_ADMIN"})
	@RequestMapping(value = "/company/update", method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCompanyAccount(@RequestBody CompanyDto companyDto, final Principal principal){

		try{
			companyAccountServices.updateAccount(companyDto, companyDto.getCompanyName()/*principal.getName()*/);
			return new ResponseEntity<>(new GenericResponse("success"), HttpStatus.OK);
		}

		catch(Exception e){
			logger.error("", e);
			return new ResponseEntity<>(new GenericResponse(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// TODO:  move to seperate class
	//**********************************************
	//******** Shopify related controllers *********
	//**********************************************

	@Secured({"ROLE_ANONYMOUS"})
	@RequestMapping(value = "/company/finishRegistration", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> finishRegisteringCompanyAccount(@RequestParam(value="token", required=true) String token, @RequestBody CompanyDto companyDto, final HttpServletRequest request){

		try{
			Locale locale = request.getLocale();
			final HttpHeaders headers = new HttpHeaders();		
			CompanyVerificationToken companyVerificationToken = companyAccountServices.getCompanyVerificationToken(token);		
			ResponseEntity<?> response = checkVerificationToken(request, locale, headers, companyVerificationToken);
	
			if(response == null){
				// Token authenticated
				companyAccountServices.finishRegistration(companyDto, companyVerificationToken.getCompany().getId());			
				response = new ResponseEntity<>(HttpStatus.OK);
			}
	
			return response;
		}

		catch(Exception e){
			logger.error("", e);
			return new ResponseEntity<>(new GenericResponse(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured({"ROLE_ANONYMOUS"})
	//@RequestMapping(value = "/company/activate", method=RequestMethod.GET)
	//public ResponseEntity<?> acceptBilling(@RequestParam(value="token", required=true) String chargId, final HttpServletRequest request){

	//	try{
	//		Locale locale = request.getLocale();
	//		final HttpHeaders headers = new HttpHeaders();		
	//		CompanyVerificationToken companyVerificationToken = companyAccountServices.getCompanyVerificationToken(token);		
	//		ResponseEntity<?> response = checkVerificationToken(request, locale, headers, companyVerificationToken);
	//
	//		if(response == null){
	//			// Token authenticated

	//			// check send request to activate bill on sopify


	//			//send a redirect to finishRegistration URL
	//			companyAccountServices.finishRegistration(companyDto, companyVerificationToken.getCompany().getId());			
	//			response = new ResponseEntity<>(HttpStatus.OK);
	//		}
	//
	//		return response;
	//	}

	//	catch(Exception e){
	//		logger.error("", e);
	//		return new ResponseEntity<>(new GenericResponse(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
	//	}
	//}

	//**********************************************
	//**********************************************
	//**********************************************


	/*
	* Controller handling requests from email registration links, which are sent during account provisioning
	*/
	//@Override
	@Secured({"ROLE_ANONYMOUS"})
	@RequestMapping(value = "/user/confirm", method = RequestMethod.GET)
	public ResponseEntity<?> confirmRegistration(@RequestParam(value="token", required=true) String token, HttpServletRequest request){
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
		return handleConfirmation(token, request);
	}

	/*
	* Controller that receives auth redirects from shopify and invokes handle confirmation, which will redirect user to ../finishRegistration page.
	*/
	//@Override
	@Secured({"ROLE_ANONYMOUS"})
	@RequestMapping(value = "/shopify/confirm", method = RequestMethod.GET)
	public ResponseEntity<?> confirmShopifyRegistration(@RequestParam(value="token", required=true) String token, @RequestParam(value="charge_id", required=true) String chargeId, HttpServletRequest request){
		
		ResponseEntity<?> response = handleConfirmation(token, request);

		// Insert chargeId into company database associated with token.
		if(response.getStatusCode() == HttpStatus.PERMANENT_REDIRECT){
			// User successfully verified against token.
			logger.debug("chargeId = ", chargeId);

			// insert chargeId into shop database.
		}

		return response;
	}

	private ResponseEntity<?> handleConfirmation(String token, HttpServletRequest request){

		try{
			Locale locale = request.getLocale();
			final HttpHeaders headers = new HttpHeaders();
			CompanyVerificationToken companyVerificationToken = companyAccountServices.getCompanyVerificationToken(token);		
			ResponseEntity<?> tokenFailedResponse = checkVerificationToken(request, locale, headers, companyVerificationToken);
			
			if(tokenFailedResponse != null){
				// VerificationToken either is invalid or does not exist
				return tokenFailedResponse;
			}

			// Get the company object associated with the received token
			Company company = companyVerificationToken.getCompany();
		
			// If password is not present, account was made via 3rd party platform, so redirect to complete registration page
			if(company.getPassword() != null){
				headers.setLocation(new URI(appURI + request.getServletPath() + "/company/finishRegistration"));
				headers.set("X-Token", token);
				return new ResponseEntity<>(null, headers, HttpStatus.PERMANENT_REDIRECT);
			}

			// Otherwise, account was made from a private web platform and registration is complete.
			// set the enable property and update the company in the db
			company.setEnabled(true);
			companyAccountServices.activateProvisionedCompany(company);
			headers.setLocation(new URI(appURI + request.getServletPath() + "/company/login"));
			headers.set("WWW-Authenticate", SecurityConfiguration.TOKEN_PREFIX);
			return new ResponseEntity<>(null, headers, HttpStatus.PERMANENT_REDIRECT);
		}

		catch(Exception e){
			logger.error(e);
			return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	*  For companies that register via 3rd party platforms this will be the endpoint at which they complete registration by specifying a password
	*/
	/*@Secured({"ROLE_ANONYMOUS"})
	@RequestMapping(value = "/company/completeRegistration")
	public ResponseEntity<?> confirmCompanyAndRegister(@RequestParam(value="token", required=true) String token, HttpServletRequest request){

	}*/

	private ResponseEntity<?> checkVerificationToken(HttpServletRequest request, Locale locale, HttpHeaders headers, CompanyVerificationToken companyVerificationToken) throws URISyntaxException{

		//Make sure company agents hitting this controller are sending correct verification tokens
		if(companyVerificationToken == null){
			String message = messages.getMessage("auth.message.invalidToken", null, locale);
			headers.setLocation(new URI(appURI + request.getServletPath() + "/company/failedRegistration"));
			return new ResponseEntity<>(new GenericResponse(message), headers, HttpStatus.PERMANENT_REDIRECT);
		}
	
		Calendar calendar = Calendar.getInstance();
	
		//Is received token expired
		if((companyVerificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0){
			String message = messages.getMessage("auth.message.expired", null, locale);
			headers.setLocation(new URI(appURI + request.getServletPath() + "/company/failedRegistration"));
			return new ResponseEntity<>(new GenericResponse(message), headers, HttpStatus.PERMANENT_REDIRECT);
		}

		return null;
	}
}