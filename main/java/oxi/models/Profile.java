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

	@OneToMany(orphanRemoval= true, mappedBy = "profile")
	@RestResource(rel="client_0")	
	@JsonIdentityReference(alwaysAsId=true)
	private List<LikeCountProfile> likeCounts = new ArrayList<>();

	//Constructor
	public Profile(){
	}
	
	//Setters

	public void setId(UUID id){this.id = id;}

	public void setUsername(String username){logger.warn("adding username"); this.username = username;}
	
	public void setCountry(String country){this.country = country;}
	
	public void setDateOfBirth(String dateOfBirth){this.dateOfBirth = dateOfBirth;}

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

	public List<LikeCountProfile> getLikeCounts(){return this.likeCounts;}
	
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
