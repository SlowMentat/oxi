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
public class CompanyAccountService /*implements IAccountService<IVerificationToken>*/{

	private static final Logger logger = LogManager.getLogger(CompanyAccountService.class);

	@Autowired CompanyRepository companyRep;
	@Autowired CompanyVerificationTokenRepository companyVerificationTokenRep;
	@Autowired CompanyRoleRepository companyRoleRep;
	@Autowired RetailerAccountRepository retailerAccountRep;
	//@Autowired PasswordResetTokenRepository passwordResetTokenRep;
	//@Autowired PasswordEncoder companyPasswordEncoder;
	@Autowired
	private PasswordEncoder companyPasswordEncoder;
	//@Autowired SessionRegistry sessionRegistry;
	@Autowired EntityManager entityManager;


	//public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
	//public static String APP_NAME = "SpringRegistration";

	private boolean emailExist(String email){
		Company company = companyRep.findByEmail(email);
		if(company != null){
			return true;
		}
		return false;
	}

	private boolean companyNameExist(String companyName){
		Company company = companyRep.findByCompanyName(companyName);
		if(company != null){
			return true;
		}
		return false;
	}

	//@Override
	public Company registerAccount(final CompanyDto companyDto) throws UserAlreadyExistException{
		
		String conflicts = "";
		//check if data exists
		if(emailExist(companyDto.getEmail())){
			throw new UserAlreadyExistException("Theres is an account with that email address: " + companyDto.getEmail());
		}
		if(companyNameExist(companyDto.getCompanyName())){
			throw new UserAlreadyExistException("Company name is already in use: " + companyDto.getCompanyName());
		}

		Company company = new Company();
		RetailerAccount ra = new RetailerAccount(
			companyDto.getCompanyName(),
			companyDto.getCountry(),
			companyDto.getState(),
			companyDto.getCity(),
			companyDto.getAddress1(),
			companyDto.getAddress2()
		);

		entityManager.persist(ra);

		company.setPassword(companyPasswordEncoder.encode(companyDto.getPassword()));
		company.setEmail(companyDto.getEmail());
		company.setCompanyName(companyDto.getCompanyName());
		company.setRoles(Arrays.asList(companyRoleRep.findByName("ROLE_RETAILER_ADMIN")));
		company.setEnabled(false);
		company.setRetailerAccount(ra);

		return companyRep.save(company);
	}

	@Transactional
	public void updateAccount(final CompanyDto companyDto, String callerCompanyName) throws Exception{
		String companyName = companyDto.getCompanyName();
		
		if(companyName == null || companyName.isEmpty()) throw new Exception("username not provided in request body");
		if(companyName != callerCompanyName) throw new Exception("Unauthorized");

		Company existingCompany = companyRep.findByCompanyName(companyName);
		
		if(existingCompany == null) throw new Exception("Company does not exist");
		
		RetailerAccount ra = retailerAccountRep.findById(existingCompany.getRetailerAccount().getId()).get();
		Company updatedCompany = new Company(companyDto);

		updatedCompany.setId(existingCompany.getId());
		updatedCompany.getRetailerAccount().setId(ra.getId());
		updatedCompany.getRetailerAccount().setItems(ra.getItems());
		updatedCompany.getRetailerAccount().setSizeCharts(ra.getSizeCharts());

		updatedCompany.setPassword(companyPasswordEncoder.encode(companyDto.getPassword()));

		entityManager.merge(updatedCompany);

		return;
	}

	//@Override
	@Transactional
	public void activateProvisionedCompany(Company company){

		entityManager.merge(company);

		//Profivision retailerAccount entities if Company is being activated for the first time
		if(company.getRetailerAccount() == null){
			RetailerAccount retailerAccount = new RetailerAccount();	
			retailerAccount.setCompanyName(company.getCompanyName());
			entityManager.persist(retailerAccount);
		}
	}

	//@Override
	public Company getCompany(String verificationToken){
		Company company = companyVerificationTokenRep.findByToken(verificationToken).getCompany();
		return company;
	}

	//@Override
	public CompanyVerificationToken getCompanyVerificationToken(String verificationToken){
		return companyVerificationTokenRep.findByToken(verificationToken);
	}

	//@Override
	public void createCompanyVerificationToken(Company company, String token){
		CompanyVerificationToken myToken = new CompanyVerificationToken(token, company);
		entityManager.persist(myToken);
	}

	/*
	* Updates company with new password.
	* IMPORTANT:  Caller is expected to have already been authenticated with a validation Token received during initial account provisioning from 3rd party platform.
	*/
	@Transactional
	public void finishRegistration(CompanyDto companyDto, UUID companyId) throws UserAlreadyExistException, Exception{

		//check again if data exists
		if(emailExist(companyDto.getEmail())){
			throw new UserAlreadyExistException("Theres is an account with that email address: " + companyDto.getEmail());
		}
		if(companyNameExist(companyDto.getCompanyName())){
			throw new UserAlreadyExistException("Company name is already in use: " + companyDto.getCompanyName());
		}
		if(companyDto.getPassword() == null || companyDto.getPassword().isEmpty()){
			throw new Exception("Invalid password");
		}

		Company company = companyRep.findById(companyId).get();

		RetailerAccount ra = new RetailerAccount(
			companyDto.getCompanyName(),
			companyDto.getCountry(),
			companyDto.getState(),
			companyDto.getCity(),
			companyDto.getAddress1(),
			companyDto.getAddress2()
		);

		entityManager.persist(ra);

		company.setPassword(companyPasswordEncoder.encode(companyDto.getPassword()));
		company.setEmail(companyDto.getEmail());
		company.setCompanyName(companyDto.getCompanyName());
		company.setRoles(Arrays.asList(companyRoleRep.findByName("ROLE_RETAILER_ADMIN")));
		company.setEnabled(true);
		company.setRetailerAccount(ra);

		entityManager.merge(company);
	}
}