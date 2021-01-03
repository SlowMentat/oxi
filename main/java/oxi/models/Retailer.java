package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Date;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.hateoas.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="retailer")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Retailer.class)
public class Retailer extends RelatedEntity implements Serializable/*Identifiable<Integer>*/{
	@Transient
	private static final Logger logger = LogManager.getLogger(Retailer.class);
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	private String name;
	@Column(name = "home_page_url")
	private String homePageUrl;
	@Column(name = "logo_url")
	private String logoUrl;
	@Column(name="created_on")
	private Date createdOn = new Date();
	
	//Constructor
	public Retailer(){

	}

	public Retailer (String name, String homePageUrl, String logoUrl){
		this.name = name;
		this.homePageUrl = homePageUrl;
		this.logoUrl = logoUrl;
	}
	
	//Setters
	public void setId(Integer id){this.id = id;}
	public void setHomePageUrl(String homePageUrl){this.homePageUrl = homePageUrl;}	
	public void setName(String name){this.name = name;}
	public void setLogoUrl(String logoUrl){this.logoUrl = logoUrl;}

	//Getters
	public Integer getId(){return this.id;}
	public String getHomePageUrl(){return this.homePageUrl;}	
	public String getName(){return this.name;}
	public String getLogoUrl(){return this.logoUrl;}
	public Date getCreatedOn(){return this.createdOn;}

	
	@Override
	public String toString(){
		logger.debug("building Retailer string");
        StringBuilder sb = new StringBuilder("\nid: ").append(this.id)
			.append("\nlink:").append(this.homePageUrl)
			.append("\nname:").append(this.name)
			.append("\nhomePageUrl:").append(this.homePageUrl)
			.append("\nlogoUrl:").append(this.logoUrl)
			.append("\ncreatedOn: ").append(this.createdOn != null ? this.createdOn.toString() : "null");
        return sb.toString();		
	}
}