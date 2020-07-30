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
import java.lang.IllegalStateException;

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
import oxi.events.OnUserInviteEvent;
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

import org.apache.commons.lang3.RandomStringUtils;

import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;


@Controller
@RequestMapping(value ="/admin")
public class AdminController{

	private static final Logger logger = LogManager.getLogger(AdminController.class);

	@Autowired
	private ApplicationEventPublisher eventPublisher;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private MessageSource messages;	
	private String appURI = "/retailer";

	/*
	*
	*/
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/sendInvite", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendUserInvite(@RequestBody UserDto userDto, final HttpServletRequest request){
		
		// Set a random tempory password to userDto
		userDto.setPassword(RandomStringUtils.randomAlphanumeric(10));
		User registeredUser = userAccountService.registerAccount(userDto);

		String appUrl = 
			"https://" + 
			request.getServerName();

		logger.debug("registering OnUserInviteEvent");
		eventPublisher.publishEvent(new OnUserInviteEvent(registeredUser, request.getLocale(), appUrl, userDto.getPassword()));
		return new ResponseEntity<>(new GenericResponse("success"), HttpStatus.OK);
	}
}