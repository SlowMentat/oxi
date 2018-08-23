package oxi.models;

import oxi.models.projection.ProfileProjection;

import javax.persistence.*;
import javax.persistence.CascadeType;
//import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;
//import oxi.jackson.*;
import org.apache.logging.log4j.Logger;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.LogManager;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="profile")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Profile.class)
public class Profile extends RelatedEntity implements ProfileDao
{
	@Transient
	private static final Logger logger = LogManager.getLogger(Profile.class);
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	//@JsonProperty("id")
	private UUID id;
	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;
	private String username;
	private String country;
	private String dateOfBirth;
	private String bodyShape;
	private boolean mens;
	private boolean womens;
	private float height;
	private float neck;
	private float fullShoulder;
	private float halfShoulder;
	private float chest;
	private float waist;
	private float hip;
	private float sleeve;
	private float frontLength;
	private float backLength;
	private float pantOutseam;
	private float pantInseam;
	private float thigh;
	private float calf;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="profile")
	@RestResource(rel="outfits")
	@JsonIdentityReference(alwaysAsId=true)
	private List<Outfit> outfits;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="profile")
	@RestResource(rel="vendor_1")
	@JsonIdentityReference(alwaysAsId=true)
	private List<Item> items;
	
	@OneToOne(cascade=CascadeType.ALL)
	@RestResource(rel="vendor_3")
	private User user;
	
	//Constructor
	public Profile(){
	}
	
	//Setters

	public void setId(UUID id){this.id = id;}

	public void setUsername(String username){logger.warn("adding username"); this.username = username;}
	
	public void setCountry(String country){this.country = country;}
	
	public void setDateOfBirth(String dateOfBirth){this.dateOfBirth = dateOfBirth;}
	
	public void setBodyShape(String bodyShape){this.bodyShape = bodyShape;}

	public void setMens(boolean mens){this.mens = mens;}
	
	public void setWomens(boolean womens){this.womens = womens;}

	public void setHeight(float height){this.height = height;}
	
	public void setNeck(float neck){this.neck = neck;}

	public void setFullShoulder(float fullShoulder){this.fullShoulder = fullShoulder;}

	public void setHalfShoulder(float halfShoulder){this.halfShoulder = halfShoulder;}

	public void setChest(float chest){this.chest = chest;}
	
	public void setWaist(float waist){this.waist = waist;}
	
	public void setHip(float hip){this.hip = hip;}
	
	public void setSleeve(float sleeve){this.sleeve = sleeve;}

	public void setFrontLength(float FrontLength){this.frontLength = frontLength;}

	public void setBackLength(float BackLength){this.backLength = backLength;}

	public void setPantOutseam(float pantOutseam){this.pantOutseam = pantOutseam;}

	public void setPantInSeam(float pantInseam){this.pantInseam = pantInseam;}

	public void setThigh(float thigh){this.thigh = thigh;}

	public void setCalf(float calf){this.calf = calf;}

	//Try to consolidate these methods
	public <T extends OutfitDao> void setOutfits(List<T> outfits){
		logger.debug("adding outfits list to Profile entity");
		if(!outfits.isEmpty()){
			for(T outfit : outfits){
				//logger.warn("Adding outfit " + outfit.toString());
				addOutfit(outfit);
			}
		}else{
			logger.debug("outfits is empty. setting to null.");
			this.outfits = null;
		}
		//if (outfit.getProfile() != this) outfit.setProfile(this);
	}

	/*public void setItems(List<Item> items){
		logger.debug("adding items list to Profile entity");
		if(!items.isEmpty()){
			for (Item item : items){
				addItem(item);
			}
		}else{
			logger.debug("items is empty.  Setting items to null.");
			this.items = null;
		}
	}*/

	public <T extends UserDao> void setUser(T user){
		logger.debug("adding user to Profile entity");
		if(!user.isEmpty()){
			this.user = user;
			addUser(user);
		}else{
			logger.error("user must not be empty.  Setting user to null.");
			this.user = null;
		}
	}

	public <T extends ItemDao> void setItems(List<T> item){this.items = items;}
	
	//Getters
	@Override
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}

	public String getUsername(){return this.username;}
	
	public String getCountry(){return this.country;}
	
	public String getDateOfBirth(){return this.dateOfBirth;}
	
	public String getBodyShape(){return this.bodyShape;}

	public boolean getMens(){return this.mens;}
	
	public boolean getWomens(){return this.womens;}

	public float getHeight(){return this.height;}
	
	public float getNeck(){return this.neck;}

	public float getFullShoulder(){return this.fullShoulder;}

	public float getHalfShoulder(){return this.halfShoulder;}

	public float getChest(){return this.chest;}
	
	public float getWaist(){return this.waist;}
	
	public float getHip(){return this.hip;}
	
	public float getSleeve(){return this.sleeve;}

	public float getFrontLength(){return this.frontLength;}

	public float getBackLength(){return this.backLength;}

	public float getPantOutseam(){return this.pantOutseam;}

	public float getPantInSeam(){return this.pantInseam;}

	public float getThigh(){return this.thigh;}

	public float getCalf(){return this.calf;}

	public <T extends OutfitDao> List<T> getOutfits(){
		//return Collections.unmodifiableList(this.outfits);
		logger.debug("returning outfits: " + outfits.toString());
		return this.outfits;
	}

	public <T extends UserDao> T getUser(){return this.user;}

	public <T extends ItemDao> List<T> getItems(){return this.items;}
	

	//Auxillary methods	
	//Outfit	
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
	//Item
	public void addItem(Item item){
		if(item != null){
			logger.warn("linking item to profile");
			item.setProfile(this);
		}else{
			logger.warn("outfit is null");
		}
	}
	public void removeItem(Item item){
		item.setProfile(null);
	}
	//User
	public void addUser(User user){
		if(user != null){
			logger.warn("linking user to profile");
			user.setProfile(this);
		}else{
			logger.warn("user is null");
		}
	}
	public void removeUser(User user){
		user.setProfile(null);
	}

	/*@Override 
	public <T extends Relational> void internalAddChild(T targetChild, Class<T> childClass){
		if(this.outfits == null){
			logger.debug("instantiating new List<T>");
			this.outfits = new ArrayList<Outfit>();
		}
		this.outfits.add((Outfit)targetChild);
	}*/

	@Override 
	public <T extends Relational> void internalAddChildByType(T targetChild, Class<T> childClass, List<Class<T>> childList){
		//child list should be this.outfits or this.items or ...
		if(childList == null){
			logger.debug("instantiating new List<T>");
			childList = new ArrayList<childClass>();
		}
		logger.debug("adding targetChild: <" + targetChild + ">\n to childList: <" + childList + "> of profile");
		childList.add(childClass.cast(targetChild));
	}

	/*@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		this.outfits.remove((Outfit)targetChild);
	}*/

	@Override public <T extends Relational> void internalRemoveChildByType(T targeChild, Class<T> childClass, List<Class<T>> childList){
		childList.remove(childClass.cast(targetChild));
	}
	
	@Override
    public String toString() {
		logger.debug("building profile string");
        StringBuilder sb = new StringBuilder("\nID: ").append(this.id)
			.append("\nusername: ").append(this.username)
			.append("\ncountry:").append(this.country)
			.append("\ndateOfBirth:").append(this.dateOfBirth)
			.append("\nbodyShape:").append(this.bodyShape)
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
