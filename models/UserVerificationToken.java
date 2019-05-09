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
@Table(name="user_verification_token")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=UserVerificationToken.class)
public class UserVerificationToken implements Serializable, Identifiable<Long>{
	private static final int EXPIRATION = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String token;


	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;
	@Column(name = "expiry_date")
	private Date expiryDate;

	private Date calculateExpiryDate(final int expiryTimeInMinutes){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Timestamp(calendar.getTime().getTime()));
		calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(calendar.getTime().getTime());
	}

	//Constructor/s
	public UserVerificationToken(){
		super();
	}

	public UserVerificationToken(final String token){
		super();
		this.token = token;
		this.expiryDate = this.calculateExpiryDate(UserVerificationToken.EXPIRATION);
	}

	public UserVerificationToken(final String token, final User user){
		super();
		this.token=token;
		this.user=user;
		this.expiryDate=calculateExpiryDate(UserVerificationToken.EXPIRATION);
	}

	//Getters
	public Long getId(){return this.id;}
	public User getUser(){return this.user;}
	public Date getExpiryDate(){return this.expiryDate;}

	//Setters
	public void setId(Long id){this.id = id;}
	public void setUser(User user){this.user = user;}
}