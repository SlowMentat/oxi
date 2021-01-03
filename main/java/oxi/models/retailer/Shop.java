package oxi.models.retailer;


import oxi.models.RelatedEntity;
import oxi.models.Relational;

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
@Table(name="shop")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Shop.class)
public class Shop extends RelatedEntity implements Serializable/*Identifiable<UUID>*/{
	@Transient
	private static final Logger logger = LogManager.getLogger(Shop.class);
	
	
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;*/

    @Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	//@JsonProperty("id")
	private UUID id;

	@Column(name = "id_text", columnDefinition="VARCHAR(36)", updatable = false, insertable = false)
	private String idText;

	//TODO:  salt and hash
	@Column(name = "access_token", nullable=false)
	private String accessToken;

	@Column(name = "platform_id", nullable=false)
	private Integer platformId;

	@Column(name = "shop_name", nullable=false)
	private String shopName;

	@Column(name="created_on")
	private Date createdOn = new Date();
	
	//Constructor
	public Shop(){

	}

	public Shop (String accessToken, Integer platform, String shopName){
		this.accessToken = accessToken;
		this.platformId = platformId;
		this.shopName = shopName;
	}
	
	//Setters
	public void setId(UUID id){this.id = id;}
	public void setAccessToken(String accessToken){this.accessToken = accessToken;}	
	public void setPlatformId(Integer platformId){this.platformId = platformId;}
	public void setShopName(String shopName){this.shopName = shopName;}

	//Getters
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public String getAccessToken(){return this.accessToken;}	
	public Integer getPlatformId(){return this.platformId;}
	public String getShopName(){return this.shopName;}
	public Date getCreatedOn(){return this.createdOn;}

	
	//@Override
	//public String toString(){
	//	logger.debug("building Shop string");
    //    StringBuilder sb = new StringBuilder("\nid: ").append(this.id)
	//		.append("\nlink:").append(this.homePageUrl)
	//		.append("\nname:").append(this.name)
	//		.append("\nhomePageUrl:").append(this.homePageUrl)
	//		.append("\nlogoUrl:").append(this.logoUrl)
	//		.append("\ncreatedOn: ").append(this.createdOn != null ? this.createdOn.toString() : "null");
    //    return sb.toString();		
	//}
}