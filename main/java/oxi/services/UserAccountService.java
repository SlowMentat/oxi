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
import org.springframework.security.crypto.bcrypt.BCrypt;

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
	@Autowired UserVerificationTokenRepository uvtRep;
	@Autowired RoleRepository roleRep;
	//@Autowired PasswordResetTokenRepository passwordResetTokenRep;
	//@Autowired PasswordEncoder userPasswordEncoder;
	
	//@Autowired
	//private BCryptPasswordEncoder userPasswordEncoder;

	@Autowired 
	private PasswordEncoder userPasswordEncoder;

	//@Autowired SessionRegistry sessionRegistry;
	@Autowired 
	private EntityManager entityManager;

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
	
    /**
    *Creates a new User Account
    *<p>
    *This service method will create a new User entity after verifying the new user's email and username is unique.
    *</p>
    *@param userDto - UserDto object containing the new user's credentials.
    *@throws UserAlreadyExistException
    */
	public User registerAccount(final UserDto userDto) throws UserAlreadyExistException{
		String conflicts = "";
		//check if data exists
		if(emailExist(userDto.getEmail())){
			throw new UserAlreadyExistException("Theres is an account with that email address: " + userDto.getEmail());
		}
		if(usernameExist(userDto.getUsername())){
			throw new UserAlreadyExistException("Username is already in use: " + userDto.getUsername());
		}

		User user = new User();
		user.setPassword(userPasswordEncoder.encode(userDto.getPassword()));
		user.setEmail(userDto.getEmail());
		user.setUsername(userDto.getUsername());
		user.setRoles(Arrays.asList(roleRep.findByName("ROLE_USER")));
		user.setEnabled(false);
		return userRep.save(user);
	}

    /**
    *Changes account password for users that are not logged in.
    *<p>
    *This service method allows users who are coming from invite email links.
    *It allows them to update the randomly generated password created during their account provisioning.
    *This is only intended for securely onboarding users during alpha testing.
    *</p>
    *@param newPassword - the new password to which the user is changing.
    *@param uvt - The user verification token sent in the invite email.
    */
	@Transactional
	public void initializeAccountPassword(String newPassword, UserVerificationToken uvt){
		User user =  userRep.findByUsername(uvt.getUser().getUsername());
		logger.debug("Initializing new password for user " + user.getUsername());
		logger.debug("Password = " + newPassword);
		String newEncodedPassword = userPasswordEncoder.encode(newPassword);
		logger.debug("Encoded Password = " + newEncodedPassword);
		user.setPassword(userPasswordEncoder.encode(newPassword));

		// Changes to user are merged in this call
		activateProvisionedUser(user);
		uvtRep.deleteById(uvt.getId());
	}

    /**
    *Changes account passwords for logged in users.
    *<p>
    *This is the standard service method for updating users' passwords
    *</p>
    *@param newPassword - the new password to which the user is changing.
    *@param oldPassword - the current password associated the user.
    *@param username - the username associated with this user.
    *@throws IllegalStateException
    */
	@Transactional
	public void updateAccountPassword(String newPassword, String oldPassword, String username){
		User user =  userRep.findByUsername(username);
		
		if(userPasswordEncoder.matches(oldPassword, user.getPassword())){
			logger.debug("Old password verification succeeded for " + username);
			user.setPassword(userPasswordEncoder.encode(newPassword));
			entityManager.merge(user);
		}
		else{
			logger.debug("Old password verification failed");
			throw new IllegalStateException("Invalid existing password");
		}
	}


    /**
    *Activates users account
    *<p>
    *Activates users' account once they're emails hav been verified.  Once the user is Activated
    *this method will provision profile, user metrics, and user tolerance entities.
    *</p>
    *@param User - User entity to activate.
    *@throws IllegalStateException
    */
	@Transactional
	public void activateProvisionedUser(User user){

		if(user.getEnabled()){
			entityManager.merge(user);
			throw new IllegalStateException("User account already activated");
		}

		user.setEnabled(true);
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

	public User getUser(String token){
		User user = uvtRep.findByToken(token).getUser();
		return user;
	}

	public UserVerificationToken getUserVerificationToken(String token){
		return uvtRep.findByToken(token);
	}

	public UserVerificationToken getUvtByUsername(String username){
		User user = userRep.findByUsername(username);
		return uvtRep.findByUserId(user.getId());
	}

	public void createUserVerificationToken(User user, String token){
		UserVerificationToken uvt = new UserVerificationToken(token, user);
		entityManager.persist(uvt);
	}

	@Transactional
	public UserVerificationToken updateUserVerificationToken(String token){
		UserVerificationToken existingUVT = getUserVerificationToken(token);
		existingUVT.setToken(UUID.randomUUID().toString());
		existingUVT.resetExpiryDate();
		return entityManager.merge(existingUVT);
	}

	public void deleteVerificationToken(String token){

	}
}