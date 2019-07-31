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
@Table(name="company_verification_token")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=CompanyVerificationToken.class)
public class CompanyVerificationToken implements Serializable, Identifiable<Long>{
	private static final int EXPIRATION = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String token;


	@OneToOne(targetEntity = Company.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "company_id")
	private Company company;
	@Column(name = "expiry_date")
	private Date expiryDate;

	private Date calculateExpiryDate(final int expiryTimeInMinutes){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Timestamp(calendar.getTime().getTime()));
		calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(calendar.getTime().getTime());
	}

	//Constructor/s
	public CompanyVerificationToken(){
		super();
	}

	public CompanyVerificationToken(final String token){
		super();
		this.token = token;
		this.expiryDate = this.calculateExpiryDate(CompanyVerificationToken.EXPIRATION);
	}

	public CompanyVerificationToken(final String token, final Company company){
		super();
		this.token=token;
		this.company=company;
		this.expiryDate=calculateExpiryDate(CompanyVerificationToken.EXPIRATION);
	}

	//Getters
	public Long getId(){return this.id;}
	public Company getCompany(){return this.company;}
	public Date getExpiryDate(){return this.expiryDate;}

	//Setters
	public void setId(Long id){this.id = id;}
	public void setCompany(Company company){this.company = company;}
}