package oxi.services;



import java.lang.*;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Arrays;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;

import oxi.repositories.CompanyRoleRepository;
import oxi.repositories.CompanyRepository;
import oxi.repositories.CompanyVerificationTokenRepository;
import oxi.repositories.retailer.RetailerAccountRepository;

//import org.baeldung.persistence.dao.PasswordResetTokenRepository;
//import org.baeldung.persistence.model.PasswordResetToken;
//import org.baeldung.web.error.UserAlreadyExistException;

import oxi.models.Company;
import oxi.models.retailer.RetailerAccount;
import oxi.models.CompanyVerificationToken;
import oxi.models.dto.CompanyDto;
import oxi.models.UserMetrics;
import oxi.models.Tolerance;
import oxi.models.ProfileStats;
import oxi.errors.UserAlreadyExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.DatatypeConverter;
import org.springframework.http.HttpStatus;


@Service
@Transactional
public class BillingService /*implements IAccountService<IVerificationToken>*/{

	private static final Logger logger = LogManager.getLogger(BillingService.class);

	@Autowired CompanyRepository companyRep;
	@Autowired CompanyVerificationTokenRepository companyVerificationTokenRep;
	@Autowired CompanyRoleRepository companyRoleRep;
	@Autowired RetailerAccountRepository retailerAccountRep;
	//@Autowired PasswordResetTokenRepository passwordResetTokenRep;
	@Autowired
	private PasswordEncoder companyPasswordEncoder;
	@Autowired EntityManager entityManager;


	@Transactional
	public void activateRecurringBilling(String token) throws UserAlreadyExistException, Exception{
	}
}