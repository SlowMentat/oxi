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
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Date;
import java.io.*;
import java.io.FileNotFoundException;
import java.io.Serializable;

import oxi.repositories.RoleRepository;
import oxi.repositories.UserRepository;
import oxi.repositories.UserVerificationTokenRepository;
import oxi.repositories.UserPasswordVerificationTokenRepository;

//import org.baeldung.persistence.dao.PasswordResetTokenRepository;
//import org.baeldung.persistence.model.PasswordResetToken;
//import org.baeldung.web.error.UserAlreadyExistException;

import oxi.models.User;
import oxi.models.Profile;
import oxi.models.PictureProfile;
import oxi.models.UserVerificationToken;
import oxi.models.UserPasswordVerificationToken;
import oxi.models.dto.UserDto;
import oxi.models.User;
import oxi.models.UserMetrics;
import oxi.models.Tolerance;
import oxi.models.ProfileStats;
import oxi.errors.UserAlreadyExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Primary;
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
	@Autowired UserVerificationTokenRepository uVTRep;
	@Autowired UserPasswordVerificationTokenRepository uPVTRep;
	@Autowired RoleRepository roleRep;
	//@Autowired PictureProfileRepository pictureProfileRep;
	//@Autowired PasswordResetTokenRepository passwordResetTokenRep;
	//@Autowired PasswordEncoder userPasswordEncoder;
	
	//@Autowired
	//private BCryptPasswordEncoder userPasswordEncoder;

	@Autowired 
	private PasswordEncoder userPasswordEncoder;

	//@Autowired SessionRegistry sessionRegistry;
	//@Qualifier("default")
	//@PersistenceContext//(unitName = "default")
	@PersistenceContext
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
		uVTRep.deleteById(uvt.getId());
	}

	private static byte[] uuidToByteArray(UUID uuid){
		if(uuid == null) return new byte[0];

		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		byte[] result = new byte[16];

		for(int i = 0; i < 16; i++){
			result[i] = (byte) (msb >>> 8 * (7-i));
		}

		for(int i = 8; i <16; i++){
			result[i] = (byte) (lsb >>> 8 * (7 - i));
		}

		return result;
	}

    /**
    *Performs bitwise, unoptimized comparison between provided parameters.
    *<p>
    *</p>
    *@param a - Byte array operand.
    *@param b - Byte array operand.
    *@throws IllegalArgumentException
    */
	private static boolean isEqualSafe(byte[] a, byte[] b){
		if(a == null || b == null) throw new IllegalArgumentException();
		if(a.length != b.length) return false;

		int isNotEqual = 0;

		// If a and b are not equal, isNotEqual will be set to 1
		for(int i = 0; i < a.length; i++){
			isNotEqual |= a[i] ^ b[i];
		}

		return (isNotEqual == 0);
	}

    /**
    *Changes account passwords for logged in users.
    *<p>
    *This is the standard service method for updating users' passwords.
    *Users must have a valid bearer token (be logged in) and valid email token to make successful calls to this service method.
    *</p>
    *@param newPassword - the new password to which the user is changing.
    *@param oldPassword - the current password associated the user.
    *@param token - user password verification token sent in the reset emails.
    *@param username - the username associated with this user.
    *@throws IllegalStateException
    */
	@Transactional
	public void updateAccountPassword(String newPassword, /*String oldPassword,*/ String token/*, String username*/){
		// Get user password verification token associated with this user.
		UserPasswordVerificationToken uPVT = uPVTRep.findByToken(token);

		if(uPVT == null) throw new IllegalStateException("Invalid token.");

		// Verify not expired
		Date now = new Date();
		long dateDiff = getDateDiff(uPVT.getExpiryDate(), now, TimeUnit.MINUTES);		
		if(dateDiff > 30) throw new IllegalStateException("token has expired");

		// Get user id's associated with the retreived principle and token.
		//User user =  userRep.findByUsername(username);
		User user =  userRep.findById(uPVT.getUser().getId()).get();
		byte[] principleUserIdBytes = uuidToByteArray(user.getId());
		byte[] tokenUserIdBytes = uuidToByteArray(uPVT.getUser().getId());
		int isUPVTOwner = 0;

		if(!isEqualSafe(principleUserIdBytes, tokenUserIdBytes)) throw new IllegalStateException("Invalid token.");

		user.setPassword(userPasswordEncoder.encode(newPassword));
		entityManager.merge(user);

		uPVTRep.deleteById(uPVT.getId());
	}


    /**
    *Activates users account
    *<p>
    *Activates users' account once their emails have been verified.  Once the user is Activated
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
			PictureProfile pp = new PictureProfile();

			entityManager.persist(pp);
	
			profile.setUsername(user.getUsername());
			profile.setUser(user);
			profile.setUserMetrics(userMetrics);
			profile.setTolerance(tolerance);
			profile.setProfileStats(profileStats);
			profile.setPictureProfile(pp);

			entityManager.persist(profile);
		}
	}

	public User getUser(String token){
		User user = uVTRep.findByToken(token).getUser();
		return user;
	}

	/*
	=============================================================================================
	*User Verification Token 
	=============================================================================================
	*/

	public UserVerificationToken getUserVerificationToken(String token){
		if(token == null) throw new IllegalArgumentException();

		UserVerificationToken uVT = uVTRep.findByToken(token);

		if(uVT == null) throw new IllegalStateException("Token does not exist");

		return uVT;
	}

	public UserVerificationToken getUvtByUsername(String username){
		if(username == null) throw new IllegalArgumentException();

		User user = userRep.findByUsername(username);

		return uVTRep.findByUserId(user.getId());
	}

	public void createUserVerificationToken(User user, String token){
		UserVerificationToken uvt = new UserVerificationToken(token, user);
		entityManager.persist(uvt);
	}

	/**
	* Get a diff between two dates
	* @param date1 the oldest date
	* @param date2 the newest date
	* @param timeUnit the unit in which you want the diff
	* @return the diff value, in the provided unit
	*/
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

    /**
    *Udpates token and resets expiration date of a user verification token
    *<p>
    *</p>
    *@param token - Existing token which will be updated.
    *@throws IllegalStateException
    */
	@Transactional
	public UserVerificationToken updateUserVerificationToken(String token, UUID userId){		
		UserVerificationToken existingUVT = getUserVerificationToken(token);
		byte[] userIdFromUVTBytes = uuidToByteArray(existingUVT.getUser().getId());
		byte[] userIdBytes = uuidToByteArray(userId);

		// Verify token and userId match
		if(!isEqualSafe(userIdFromUVTBytes, userIdBytes)) throw new IllegalStateException("Validation check failed. User id or token is invalid.");

		existingUVT.setToken(UUID.randomUUID().toString());
		existingUVT.resetExpiryDate(existingUVT.getExpirationPeriod());

		return entityManager.merge(existingUVT);
	}

	public void deleteVerificationToken(String token){

	}

	/*
	=============================================================================================
	Password update token 
	=============================================================================================
	*/

	public UserPasswordVerificationToken getUserPasswordVerificationToken(String token){
		UserPasswordVerificationToken uPVT = uPVTRep.findByToken(token);

		if(uPVT == null) throw new IllegalStateException("Token does not exist");

		return uPVT;
	}

	public UserPasswordVerificationToken getUPVTByUsername(String username){
		User user = userRep.findByUsername(username);

		if(user == null) throw new IllegalStateException("User does not exist");

		return uPVTRep.findByUserId(user.getId());
	}

    /**
    *Creates new password verification token.
    *<p>
    *</p>
    *@param user - Owner of the token.
    *@throws IllegalStateException
    */
	public UserPasswordVerificationToken createUserPasswordVerificationToken(User user, String token){
		if(user == null) throw new IllegalArgumentException();

		token = token == null ? UUID.randomUUID().toString() : token;
		UserPasswordVerificationToken uPVT = new UserPasswordVerificationToken(token, user);
		entityManager.persist(uPVT);

		return uPVT;
	}
    
    /**
    *Udpates token and resets its expiration date.
    *<p>
    *</p>
    *@param existingToken - Existing token which will be updated. If null a new user password verification token will be created.
    *@param refreshedToken - New token to replace the existing token. If null a random UUID is used.
    *@throws IllegalStateException
    */
	@Transactional
	public UserPasswordVerificationToken updateUserPasswordVerificationToken(UUID userId, String existingToken, String refreshedToken){
		if(userId == null || existingToken == null) throw new IllegalArgumentException();

		refreshedToken = refreshedToken == null ? UUID.randomUUID().toString() : refreshedToken;
		UserPasswordVerificationToken updatedUPVT;

		// Replace exisitng user password verification token.
		UserPasswordVerificationToken uPVT = getUserPasswordVerificationToken(existingToken);
		byte[] userIdFromUPVTBytes = uuidToByteArray(uPVT.getUser().getId());
		byte[] userIdBytes = uuidToByteArray(userId);

		// Verify token and userId match
		if(!isEqualSafe(userIdFromUPVTBytes, userIdBytes)) throw new IllegalStateException("Validation check failed. User id or token is invalid.");

		uPVT.setToken(refreshedToken);
		uPVT.resetExpiryDate(uPVT.getExpirationPeriod());
		updatedUPVT = entityManager.merge(uPVT);		

		return updatedUPVT;
	}

	/**
    *Either creates a new UserPasswordVerificationToken or update one that is exisitng. 
    *<p>
    *</p>
    *@param user - User owning the existing or created token.
    *@throws IllegalArgumentException
    */
	public UserPasswordVerificationToken refreshUserPasswordVerificationToken(User user, String refreshedToken){
		if(user == null) throw new IllegalArgumentException();

		UserPasswordVerificationToken updatedUPVT;
		UserPasswordVerificationToken existingUPVT = uPVTRep.findByUserId(user.getId());

		// Create new user password verification token.
		if(existingUPVT == null){
			updatedUPVT = createUserPasswordVerificationToken(user, null);
		}
		// Replace exisitng user password verification token.
		else{
			updatedUPVT = updateUserPasswordVerificationToken(user.getId(), existingUPVT.getToken(), refreshedToken);
		}

		return updatedUPVT;
	}

	public void deleteUserPasswordVerificationToken(String token){

	}

	public User getUserByUsername(String username){
		User user = userRep.findByUsername(username);
		
		// Check that user exists
		if(user == null) throw new IllegalStateException("Something went wrong with your request");

		return user;
	}

	public User getUserByEmail(String email){
		logger.debug("email = " + email);
		User user = userRep.findByEmail(email);

		if(user == null) throw new IllegalStateException("Something went wrong with your request");

		return user;
	}
}