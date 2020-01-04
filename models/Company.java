package oxi.models;


import oxi.models.retailer.RetailerAccount;
import oxi.models.dto.CompanyDto;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
import java.util.Collection;
import java.io.Serializable;
import java.lang.*;
import com.fasterxml.jackson.annotation.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

//import org.springframework.security.core.userdetails;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="company")
//@AssociationOverride(
//	name="roles",	//The name of the relationship property whose mapping is being overridden if property-based access is being used, or the name of the relationship field if field-based access is used. 
//	joinTable=@JoinTable(
//		name="companies_roles",
//		joinColumns=@JoinColumn(name = "company_id", referencedColumnName = "id"),
//		inverseJoinColumns = @JoinColumn(name ="role_id", referencedColumnName = "id")
//	)
//)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Company.class)
public class Company extends BaseAccount/*RelatedEntity*/ /*implements Serializable,  Identifiable<UUID>*/{
	@Transient
	private static final Logger logger = LogManager.getLogger(Company.class);
	
	//@Id
	////@JsonProperty("id")
	//@GeneratedValue(generator = "uuid2")
	//@GenericGenerator(name = "uuid2", strategy = "uuid2")
	//@Column(columnDefinition = "BINARY(16)")
	//private UUID id;
	//
	//@Column(name = "id_text", updatable = false, insertable = false)
	//private String idText;	
	////private byte[] picture;
	//private String email;
	//private String password;
	@Column(name = "company_name")
	private String companyName;

	@Column(name = "shop_name")
	private String shopName;
	//private boolean enabled;
    //private boolean tokenExpired;
	
	@OneToOne(cascade=CascadeType.MERGE, mappedBy="company")
	@RestResource(rel="client_0")
	private RetailerAccount retailerAccount;
	
	@ManyToMany
    @JoinTable( 
        name = "companies_roles", 
        joinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) 
    private Collection<CompanyRole> roles;

	//Constructor
	public Company(){
		super();
		//this.enabled = false;
	}

	public Company(CompanyDto companyDto){
		super(companyDto.getEmail(), companyDto.getPassword());

		this.companyName = companyDto.getCompanyName();
		//this.shopName = companyDto.getShopName();
		this.retailerAccount = new RetailerAccount(
			companyDto.getCompanyName(),
			companyDto.getCountry(),
			companyDto.getState(),
			companyDto.getCity(),
			companyDto.getAddress1(),
			companyDto.getAddress2()
		);
	}
	
	//Setters
	//public void setId(UUID id){this.id = id;}
	//public void setEmail(String email){this.email = email;}	
	//public void setPassword(String password){this.password = password;}	
	public void setCompanyName(String companyName){this.companyName = companyName;}
	//public void setEnabled(boolean enabled){this.enabled = enabled;}
	public void setRetailerAccount(RetailerAccount retailerAccount){this.retailerAccount = retailerAccount;}
	public void setRoles(Collection<CompanyRole> roles){this.roles = roles;}
	public void setShopName(String shopName){this.shopName = shopName;}

	
	//Getters
	//public UUID getId(){return this.id;}
	//public String getIdText(){return this.idText;}
	//public String getEmail(){return this.email;}	
	//public String getPassword(){return this.password;}	
	public String getCompanyName(){return this.companyName;}
	//public boolean getEnabled(){return this.enabled;}	
	public RetailerAccount getRetailerAccount(){return this.retailerAccount;}
	public Collection<CompanyRole> getRoles(){return this.roles;}
	public String getShopName(){return this.shopName;}
}