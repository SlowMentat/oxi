package oxi.models.retailer;

import oxi.models.RelatedEntity;
import oxi.models.Relational;
import oxi.models.projection.*;
import oxi.models.*;
//import oxi.models.retailer.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.GenericGenerator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Entity
@Table(name="retailer_account")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=RetailerAccount.class)
public class RetailerAccount extends RelatedEntity implements Serializable, Identifiable<UUID>
{
	@Transient
	private static final Logger logger = LogManager.getLogger(RetailerAccount.class);
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	//@JsonProperty("id")
	private UUID id;
	@Column(name = "id_text", columnDefinition="VARCHAR(36)", updatable = false, insertable = false)
	private String idText;
	@Column(name = "company_name", columnDefinition="VARCHAR(36)")
	private String companyName;

	@Column(name = "street_address1", columnDefinition="VARCHAR(36)")
	private String streetAddress1;
	@Column(name = "street_address2", columnDefinition="VARCHAR(36)")
	private String streetAddress2;
	private String city;
	private String state;
	private String country;

	@Column(name = "work_email", columnDefinition="VARCHAR(36)")
	private String workEmail;
	@Column(name = "work_phone_number", columnDefinition="VARCHAR(36)")
	private String workPhoneNumber;


	@OneToMany(cascade = CascadeType.ALL, orphanRemoval= true, mappedBy = "retailerAccount")
	@RestResource(rel="client_0")	
	@JsonIdentityReference(alwaysAsId=true)
	private List<Item> items;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval= true, mappedBy = "retailerAccount")
	@RestResource(rel="client_0")	
	@JsonIdentityReference(alwaysAsId=true)
	private List<SizeChart> sizeCharts;

	@OneToOne(cascade=CascadeType.ALL)
	@RestResource(rel="vendor_3")
	private User user;
	
	//Constructor
	public RetailerAccount(){
	}
	
	//Setters

	public void setId(UUID id){this.id = id;}
	public void setCompanyName(String companyName){logger.warn("adding companyName"); this.companyName = companyName;}
	public void setStreetAddress1(String streetAddress1){this.streetAddress1 = streetAddress1;}
	public void setStreetAddress2(String streetAddress2){this.streetAddress2 = streetAddress2;}
	public void setCity(String city){this.city = city;}
	public void setState(String state){this.state = state;}
	public void setCountry(String country){this.country = country;}
	public void setWorkEmail(String workEmail){this.workEmail = workEmail;}
	public void setWorkPhoneNumber(String workPhoneNumber){this.workPhoneNumber = workPhoneNumber;}

	public void setItems(List<Item> items){this.items = items;}
	public void addItem(Item item){
		this.items.add(item);
		if(item.getRetailerAccount() != this){item.setRetailerAccount(this);}
	}

	public void setSizeCharts(List<SizeChart> sizeCharts){this.sizeCharts = sizeCharts;}
	public void addSizeChart(SizeChart sizeChart){
		this.sizeCharts.add(sizeChart);
		if(sizeChart.getRetailerAccount() != this){sizeChart.setRetailerAccount(this);}
	}
	
	//Getters
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public String getCompanyName(){return this.companyName;}
	public String getStreetAddress1(){return this.streetAddress1;}
	public String getStreetAddress2(){return this.streetAddress2;}
	public String getCity (){return this.city;}
	public String getState(){return this.state;}
	public String getCountry(){return this.country;}
	public String getWorkPhoneNumber(){return this.workPhoneNumber;}
	public String getWorkEmail(){return this.workEmail;}	
	public User getUser(){return this.user;}
	public List<Item> getItems(){return this.items;}
	public List<SizeChart> getSizeCharts(){return this.sizeCharts;}
	
	
	@Override 
	public <T extends Relational> void internalAddChild(T targetChild){
		if(targetChild instanceof Item){
			if(this.items == null){
				logger.debug("instantiating new List<Items>");
				this.items = new ArrayList<Item>();
			}
			this.items.add((Item)targetChild);
		}
		else if(targetChild instanceof SizeChart){
			if(this.sizeCharts == null){
				logger.debug("instantiating new List<SizeChart>");
				this.sizeCharts = new ArrayList<SizeChart>();
			}
			this.sizeCharts.add((SizeChart)targetChild);
		}
	}
	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		if(targetChild instanceof Item){
			this.items.remove((Item)targetChild);
		}
		else if(targetChild instanceof SizeChart){
			this.sizeCharts.remove((SizeChart)targetChild);
		}
	}
}
