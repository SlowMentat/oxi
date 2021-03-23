package oxi.models;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
import java.util.Collection;
import java.io.Serializable;
import java.lang.*;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;
//import com.fasterxml.jackson.annotation.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.Logger;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.LogManager;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="user_password_verification_token")
public class UserPasswordVerificationToken extends BaseVerificationToken{

	// 30 minute token expiration
	private static final int EXPIRATION_PERIOD = 30;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	//Constructor/s
	public UserPasswordVerificationToken(){
		super();
	}

	public UserPasswordVerificationToken(final String token){
		super(token, EXPIRATION_PERIOD);
	}

	public UserPasswordVerificationToken(final String token, final User user){
		super(token, EXPIRATION_PERIOD);
		this.user=user;
	}

	//Getters
	public User getUser(){return this.user;}
	public static int getExpirationPeriod(){return EXPIRATION_PERIOD;}

	//Setters
	public void setUser(User user){this.user = user;}

}