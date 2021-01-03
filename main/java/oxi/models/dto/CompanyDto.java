package oxi.models.dto;

import oxi.models.retailer.RetailerAccount;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.server.core.*;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.Identifiable;


public class CompanyDto implements Serializable
{
	//@JsonProperty("id")
	//private String id;
	private String email;
	private String password;
	private String companyName;
	//private RetailerAccount retailerAccount;
  
	private String country;
	private String state;
	private String city;
	private String address1;
	private String address2;

	public CompanyDto(){
	}

	public CompanyDto(String email, String password, String companyName){
		this.email = email;
		this.password = password;
		this.companyName = companyName;
	}

	public CompanyDto(String email, String password, String companyName, String country, String state, String city, String address1, String address2){
		this.email = email;
		this.password = password;
		this.companyName = companyName;

		this.country = country;
		this.state = state;
		this.city = city;
		this.address1 = address1;
		this.address2 = address2;		
	}

	//Getters
	//@Override
	//public String getId(){return null;}
	public String getEmail(){return this.email;}
	public String getPassword(){return this.password;}
	public String getCompanyName(){return this.companyName;}
	//public RetailerAccount getRetailerAccount(){return this.retailerAccount;}

	public String getCountry(){return this.country;}
	public String getState(){return this.state;}
	public String getCity(){return this.city;}
	public String getAddress1(){return this.address1;}
	public String getAddress2(){return this.address2;}
	

	//Setters
	//public void setId(UUID id){this.Id = id;}
	public void setEmail(String email){this.email = email;}
	public void setPassword(String password){this.password = password;}
	public void setUsername(String companyName){this.companyName = companyName;}

	public void setCountry(String country){this.country = country;}
	public void setState(String state){this.state = state;}
	public void setCity(String city){this.city = city;}
	public void setAddress1(String address1){this.address1 = address1;}
	public void setAddress2(String address2){this.address2 = address2;}
	//public void setProfile(RetailerAccount retailerAccount){this.retailerAccount = retailerAccount;}
}