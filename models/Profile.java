package oxi.models;

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
public class Profile extends RelatedEntity implements Serializable, Identifiable<UUID>
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
	@Column(name = "date_of_birth")
	private String dateOfBirth;
	/*private String bodyShape;
	@Column(nullable=false, columnDefinition="BOOLEAN default false")
	private boolean mens;
	@Column(nullable=false, columnDefinition="BOOLEAN default false")
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
	private float calf;*/

	@OneToOne(cascade=CascadeType.ALL, mappedBy="profile")
	@RestResource(rel="tolerance")
	@JsonIdentityReference(alwaysAsId=true)
	private Tolerance tolerance;

	@OneToOne(cascade=CascadeType.ALL, mappedBy="profile")
	@RestResource(rel="profileStats")
	@JsonIdentityReference(alwaysAsId=true)
	private ProfileStats profileStats;

	@OneToOne(cascade=CascadeType.ALL, mappedBy="profile")
	@RestResource(rel="userMetrics")
	@JsonIdentityReference(alwaysAsId=true)
	private UserMetrics userMetrics;

	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="profile")
	@RestResource(rel="outfits")
	@JsonIdentityReference(alwaysAsId=true)
	//@JsonBackReference
	//@JsonDeserialize(contentUsing=CustomOutfitDeserializer.class) 
	private List<Outfit> outfits;
	
	/*@OneToMany(cascade=CascadeType.ALL, mappedBy="profile")
	@RestResource(rel="vendor_1")
	@JsonIdentityReference(alwaysAsId=true)
	//private List<Item> items;
	private List<Item> items;*/
	
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
	
	/*public void setBodyShape(String bodyShape){this.bodyShape = bodyShape;}

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

	public void setPantInseam(float pantInseam){this.pantInseam = pantInseam;}

	public void setThigh(float thigh){this.thigh = thigh;}

	public void setCalf(float calf){this.calf = calf;}

	public void setFollowers(long followers){this.followers = followers;}

	public void setPoints(long points){this.points = points;}*/

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

	public void setTolerance(Tolerance tolerance){
		if(tolerance != null){
			this.tolerance = tolerance;
			if(this.tolerance.getProfile() != this) this.tolerance.setProfile(this);
		}else{
			logger.warn("tolerance parameter is null");
		}
	}

	public void setUserMetrics(UserMetrics userMetrics){
		if(userMetrics != null){
			this.userMetrics = userMetrics;
			if(this.userMetrics.getProfile() != this) this.userMetrics.setProfile(this);
		}else{
			logger.warn("userMetrics parameter is null");
		}
	}

	public void setProfileStats(ProfileStats profileStats){
		if(profileStats != null){
			this.profileStats = profileStats;
			if(this.profileStats.getProfile() != this) this.profileStats.setProfile(this);
		}else{
			logger.warn("profileStats parameter is null");
		}
	}
	
	
	//Getters
	@Override
	public UUID getId(){return this.id;}

	public String getIdText(){return this.idText;}

	public String getUsername(){return this.username;}
	
	public String getCountry(){return this.country;}
	
	public String getDateOfBirth(){return this.dateOfBirth;}
	
	/*public String getBodyShape(){return this.bodyShape;}

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

	public float getPantInseam(){return this.pantInseam;}

	public float getThigh(){return this.thigh;}

	public float getCalf(){return this.calf;}

	public long getFollowers(){return this.followers;}

	public long getPoints(){return this.points;}*/

	public Tolerance getTolerance(){return this.tolerance;}

	public UserMetrics getUserMetrics(){return this.userMetrics;}

	public ProfileStats getProfileStats(){return this.profileStats;}

	public List<Outfit> getOutfits(){
		//return Collections.unmodifiableList(this.outfits);
		logger.debug("returning outfits: " + outfits.toString());
		return this.outfits;
	}
	public User getUser(){return this.user;}
	//public List<Item> getItems(){return this.items;}
	
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
	/*public void setItems(List<Item> items){
		this.items = items;
	}*/
	
	public void setUser(User user){
		logger.debug("adding user to Profile entity");
		if(user != null){
			this.user = user;
			addUser(user);
		}else{
			logger.error("user must not be empty.  Setting user to null.");
			this.user = null;
		}
	}

	public void addUser(User user){
		if(user != null){
			logger.warn("linking user to profile");
			user.setProfile(this);
		}else{
			logger.warn("user is null");
		}
	}
	
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Profile string");
        StringBuilder sb = new StringBuilder("\nid: ").append(((this.id == null) ? "null" : this.id.toString()))
			.append("\nusername:").append(this.username)
			.append("\ncountry:").append(this.country)
			.append("\ndateOfBirth:").append(this.dateOfBirth)
			.append("\ntolerance: {").append(this.tolerance != null ? this.tolerance.toString(indents + 1) : "null").append("\n}")
			.append("\nprofileStats: {").append(this.profileStats != null ? this.profileStats.toString(indents + 1) : "null").append("\n}")
			.append("\nuserMetrics: {").append(this.userMetrics != null ? this.userMetrics.toString(indents + 1) : "null").append("\n}");
		//sb.append("\nprofile: ").append(((profile.getId() == null) ? "null" : profile.getId().toString()));
        return sb.toString();		
	}

	@Override
	public String toString(){
		return toString(0);
	}
	
	/*@Override
    public String toString() {
		logger.debug("building profile string");
        StringBuilder sb = new StringBuilder("");
		if(this.id != null) sb.append("\nid: ").append(this.id.toString());
    	sb.append("\nusername: ").append(this.username)
			.append("\ncountry:").append(this.country)
			.append("\ndatOfBirth:").append(this.dateOfBirth)
			.append("\nbodyShape:").append(this.bodyShape)
			.append("\nmens:").append(this.mens)
			.append("\nwomens:").append(this.womens)
			.append("\nheight:").append(this.height)
			.append("\nneck:").append(this.neck)
			.append("\nfullShoulder:").append(this.fullShoulder)
			.append("\nhalfShoulder:").append(this.halfShoulder)
			.append("\nchest:").append(this.chest)
			.append("\nwaist:").append(this.waist)
			.append("\nhip:").append(this.hip)
			.append("\nsleeve").append(this.sleeve)
			.append("\nfrontLength:").append(this.frontLength)
			.append("\nbackLength:").append(this.backLength)
			.append("\npantOutseam:").append(this.pantOutseam)
			.append("\npantInseam:").append(this.pantInseam)
			.append("\nthigh:").append(this.thigh)
			.append("\ncalf:").append(this.calf)
			.append("\nUser:").append(this.user.toString())
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
		sb.append("\nitems: [");
		if(this.items != null){
			for (Item item: this.items) {
				logger.debug("building item string");
				sb.append("\n	").append(item.getId());
			}
			sb.append("]");
		}else{
			logger.debug("profile.outfits is null");
		}
		return sb.toString();
    }*/
}
