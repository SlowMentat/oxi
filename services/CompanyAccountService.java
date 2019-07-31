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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.DatatypeConverter;


@Service
@Transactional
public class CompanyAccountService /*implements IAccountService<IVerificationToken>*/{

	private static final Logger logger = LogManager.getLogger(CompanyAccountService.class);

	@Autowired CompanyRepository companyRep;
	@Autowired CompanyVerificationTokenRepository companyVerificationTokenRep;
	@Autowired CompanyRoleRepository companyRoleRep;
	//@Autowired PasswordResetTokenRepository passwordResetTokenRep;
	//@Autowired PasswordEncoder companyPasswordEncoder;
	@Autowired
	private BCryptPasswordEncoder companyPasswordEncoder;
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
	public /*ResponseEntity<?>*/ Company registerAccount(final CompanyDto companyDto) throws UserAlreadyExistException{
		//try{
			String conflicts = "";
			//check if data exists
			if(emailExist(companyDto.getEmail())){
				throw new UserAlreadyExistException("Theres is an account with that email address: " + companyDto.getEmail());
			}
			if(companyNameExist(companyDto.getCompanyName())){
				throw new UserAlreadyExistException("Company name is already in use: " + companyDto.getCompanyName());
			}
			//conflicts += (companyRep.findByEmail(companyDto.getEmail()) != null) ? "email " : "";
			//conflicts += (companyRep.findByCompanyName(companyDto.getCompanyName()) != null) ? "companyName " : "";
			//if(conflicts.compareTo("") != 0){
			//	return new ResponseEntity("Fields: " + conflicts, HttpStatus.CONFLICT);
			//}

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

			//logger.debug("Company object persisted.  company.id: " + company.getId().toString());
			
			////Create an empty RetailerAccount linked to this company
			//RetailerAccount retailerAccount = new RetailerAccount();
			//retailerAccount.setUser(company);
			//retailerAccount.setCompanyName(company.getCompanyName());
			//entityManager.persist(retailerAccount);
//
			//logger.debug("RetailerAccount object persisted.  retailerAccount.company.id: " + retailerAccount.getCompany().getId().toString());
			////companyRep.saveAndFlush(company);	
//
			////Set Company roles
			//
			//return new ResponseEntity(company.getCompanyName(), HttpStatus.OK);
		//}catch(Exception e){
		//	return new ResponseEntity("Our servers seem to have freyed a bit.\nPlease wait a moment and try your request agian.", HttpStatus.INTERNAL_SERVER_ERROR);
		//}
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
}