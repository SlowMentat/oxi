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

import oxi.repositories.RoleRepository;
import oxi.repositories.UserRepository;
import oxi.repositories.UserVerificationTokenRepository;

//import org.baeldung.persistence.dao.PasswordResetTokenRepository;
//import org.baeldung.persistence.model.PasswordResetToken;
//import org.baeldung.web.error.UserAlreadyExistException;

import oxi.models.User;
import oxi.models.Profile;
import oxi.models.UserVerificationToken;
import oxi.models.dto.UserDto;
import oxi.models.User;
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
public class UserAccountService /*implements IAccountService<IVerificationToken>*/{

	private static final Logger logger = LogManager.getLogger(UserAccountService.class);

	@Autowired UserRepository userRep;
	@Autowired UserVerificationTokenRepository userVerificationTokenRep;
	@Autowired RoleRepository roleRep;
	//@Autowired PasswordResetTokenRepository passwordResetTokenRep;
	//@Autowired PasswordEncoder passwordEncoder;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	//@Autowired SessionRegistry sessionRegistry;
	@Autowired EntityManager entityManager;


	//public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
	//public static String APP_NAME = "SpringRegistration";

	private boolean emailExist(String email){
		User user = userRep.findByEmail(email);
		if(user != null){
			return true;
		}
		return false;
	}

	private boolean usernameExist(String username){
		User user = userRep.findByUsername(username);
		if(user != null){
			return true;
		}
		return false;
	}

	//@Override
	public /*ResponseEntity<?>*/ User registerAccount(final UserDto userDto) throws UserAlreadyExistException{
		//try{
			String conflicts = "";
			//check if data exists
			if(emailExist(userDto.getEmail())){
				throw new UserAlreadyExistException("Theres is an account with that email address: " + userDto.getEmail());
			}
			if(usernameExist(userDto.getUsername())){
				throw new UserAlreadyExistException("Username is already in use: " + userDto.getUsername());
			}
			//conflicts += (userRep.findByEmail(userDto.getEmail()) != null) ? "email " : "";
			//conflicts += (userRep.findByUsername(userDto.getUsername()) != null) ? "username " : "";
			//if(conflicts.compareTo("") != 0){
			//	return new ResponseEntity("Fields: " + conflicts, HttpStatus.CONFLICT);
			//}

			User user = new User();
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
			user.setEmail(userDto.getEmail());
			user.setUsername(userDto.getUsername());
			user.setRoles(Arrays.asList(roleRep.findByName("ROLE_USER")));
			user.setEnabled(false);
			return userRep.save(user);

			//logger.debug("User object persisted.  user.id: " + user.getId().toString());
			
			////Create an empty Profile linked to this user
			//Profile profile = new Profile();
			//profile.setUser(user);
			//profile.setUsername(user.getUsername());
			//entityManager.persist(profile);
//
			//logger.debug("Profile object persisted.  profile.user.id: " + profile.getUser().getId().toString());
			////userRep.saveAndFlush(user);	
//
			////Set User roles
			//
			//return new ResponseEntity(user.getUsername(), HttpStatus.OK);
		//}catch(Exception e){
		//	return new ResponseEntity("Our servers seem to have freyed a bit.\nPlease wait a moment and try your request agian.", HttpStatus.INTERNAL_SERVER_ERROR);
		//}
	}

	//@Override
	@Transactional
	public void activateProvisionedUser(User user){

		entityManager.merge(user);

		//Profivision profile entities if User is being activated for the first time
		if(user.getProfile() == null){
			Profile profile = new Profile();
			UserMetrics userMetrics = new UserMetrics();
			Tolerance tolerance = new Tolerance();
			ProfileStats profileStats = new ProfileStats();	
	
			profile.setUsername(user.getUsername());
			profile.setUser(user);
			profile.setUserMetrics(userMetrics);
			profile.setTolerance(tolerance);
			profile.setProfileStats(profileStats);

			entityManager.persist(profile);
		}
	}

	//@Override
	public User getUser(String verificationToken){
		User user = userVerificationTokenRep.findByToken(verificationToken).getUser();
		return user;
	}

	//@Override
	public UserVerificationToken getUserVerificationToken(String verificationToken){
		return userVerificationTokenRep.findByToken(verificationToken);
	}

	//@Override
	public void createUserVerificationToken(User user, String token){
		UserVerificationToken myToken = new UserVerificationToken(token, user);
		entityManager.persist(myToken);
	}
}