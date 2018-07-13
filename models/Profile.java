package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
//import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;
//import oxi.jackson.*;
import org.apache.logging.log4j.Logger;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.LogManager;

@Entity
@Table(name="profile")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Profile.class)
public class Profile extends RelatedEntity implements Serializable, Identifiable<Long>
{
	@Transient
	private static final Logger logger = LogManager.getLogger(Profile.class);
	
	@Id
	//@JsonProperty("id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long Id;
	private String alias;
	private String country;
	private String city;
	private String birthday;
	private int height;
	private int chest;
	private int waist;
	private int hip;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="profile")
	@RestResource(rel="outfits")
	@JsonIdentityReference(alwaysAsId=true)
	//@JsonBackReference
	//@JsonDeserialize(contentUsing=CustomOutfitDeserializer.class) 
	private List<Outfit> outfits;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="profile")
	@RestResource(rel="vendor_1")
	@JsonIdentityReference(alwaysAsId=true)
	//private List<Item> items;
	private List<Item> items;
	
	@OneToOne(cascade=CascadeType.ALL)
	@RestResource(rel="vendor_3")
	private User user;
	
	//Constructor
	public Profile(){
	}
	
	//Setters
	//@Override
	public void setId(Long id){this.Id = id;}
	public void setAlias(String alias){logger.warn("adding alias"); this.alias = alias;}	
	public void setCountry(String country){this.country = country;}	
	public void setCity(String city){this.city = city;}	
	public void setBirthday(String birthday){this.birthday = birthday;}	
	public void setHeight(int height){this.height = height;}	
	public void setChest(int chest){this.chest = chest;}	
	public void setWaist(int waist){this.waist = waist;}	
	public void setHip(int hip){this.hip = hip;}	
	//Try to consolidate these methods
	public void setOutfits(List<Outfit> outfits){
		logger.warn("adding outfits list to Profile entity");
		if(!outfits.isEmpty()){
			for(Outfit outfit : outfits){
				//logger.warn("Adding outfit " + outfit.toString());
				addOutfit(outfit);
			}
		}else{
			logger.warn("outfits is empty setting to null");
			this.outfits = null;
		}
		//if (outfit.getProfile() != this) outfit.setProfile(this);
	}
	
	//Getters
	//@Override
	public Long getId(){return this.Id;}
	public String getAlias(){return this.alias;}	
	public String getCountry(){return this.country;}	
	public String getCity(){return this.city;}	
	public String getBirthday(){return this.birthday;}
	public int getHeight(){return this.height;}	
	public int getChest(){return this.chest;}	
	public int getWaist(){return this.waist;}	
	public int getHip(){return this.hip;}
	public List<Outfit> getOutfits(){
		//return Collections.unmodifiableList(this.outfits);
		logger.debug("returning outfits: " + outfits.toString());
		return this.outfits;
	}
	public User getUser(){return this.user;}
	public List<Item> getItems(){return this.items;}
	
	//Auxillary methods		
	public void addOutfit(Outfit outfit){
		if(outfit != null){
			logger.warn("linking outfit to profile");
			outfit.setProfile(this);
		}else{
			logger.warn("outfit is null");
		}
	}
	public void removeOutfit(Outfit outfit){
		outfit.setProfile(null);
	}
	/*@Override
	public void internalAddChild(Relational outfit){
		if(this.outfits == null){
			logger.debug("instantiating new List<Outfit> outfits");
			this.outfits = new ArrayList<Outfit>();
		}
		logger.debug("adding outfit to Profile.outfits[]");
		if(outfit != null) logger.debug("Relational parameter in Profile.internalAddChild is not null");
		else logger.debug("outfit passed to Profile.internalAddChild() is null");
		this.outfits.add((Outfit)outfit);
	}
	@Override
	public void internalRemoveChild(Relational outfit){
		this.outfits.remove(outfit);
	}*/	
	@Override 
	public <T extends Relational> void internalAddChild(T targetChild){
		if(this.outfits == null){
			logger.debug("instantiating new List<T>");
			this.outfits = new ArrayList<Outfit>();
		}
		this.outfits.add((Outfit)targetChild);
	}
	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		this.outfits.remove((Outfit)targetChild);
	}
	/*
	public void internalAddChild(Outfit outfit){
		if(this.outfits == null){
			logger.debug("instantiating new List<Outfit> outfits");
			this.outfits = new ArrayList<Outfit>();
		}
		this.outfits.add(outfit);
	}
	
	public void internalRemoveChild(Outfit outfit){
		this.outfits.remove(outfit);
	}
	*/
	public void setItems(List<Item> item){
		this.items = items;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	@Override
    public String toString() {
		logger.debug("building profile string");
        StringBuilder sb = new StringBuilder("\nID: ").append(this.Id)
			.append("\nalias: ").append(this.alias)
			.append("\ncountry:").append(this.country)
			.append("\ncity:").append(this.city)
			.append("\nbirthday:").append(this.birthday)
			.append("\nheight:").append(this.height)
			.append("\nchest:").append(this.chest)
			.append("\nwaist:").append(this.waist)
			.append("\nhip:").append(this.hip)
		.append("\noutfits: [");
		if(this.outfits != null){
			for (Outfit outfit: this.outfits) {
				logger.debug("building outfit string");
				sb.append("\n	").append(outfit.getId());
			}
			sb.append("]");
		}else{
			logger.debug("profile.outfits is null");
		}
        return sb.toString();
    }
}
