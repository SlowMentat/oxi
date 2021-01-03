package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.Date;
import java.util.Objects;
import java.util.Iterator;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators.*;
import org.springframework.hateoas.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.hibernate.annotations.GenericGenerator;

////import oxi.models.projection.LikeProjection;

@Entity
@Table(name="like_count")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="Like_id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=LikeCount.class)
public class LikeCount extends RelatedEntity implements Serializable/*Identifiable<UUID>*/{
	@Transient
	private static final Logger logger = LogManager.getLogger(LikeCount.class);

	@Id
	@JsonProperty("id")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;
	private String username;
	private int count;
	@Column(name = "created_on")
	private Date updatedOn = new Date();

	@OneToOne(cascade=CascadeType.ALL)
	@JsonIdentityReference(alwaysAsId=true)	
	private Outfit outfit;

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, mappedBy = "likeCount", fetch = FetchType.LAZY)
	@RestResource(rel="vendor_0")
	@JsonProperty("profiles")
	@JsonIdentityReference(alwaysAsId=true)
	private List<LikeCountProfile> profiles = new ArrayList<>(); //TODO:  see if this clears all item assoications on a given 

	@OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, mappedBy = "likeCount", fetch = FetchType.LAZY)
	@RestResource(rel="vendor_0")
	@JsonProperty("items")
	@JsonIdentityReference(alwaysAsId=true)
	private Set<LikeCountItem> items = new HashSet<LikeCountItem>(); 
	
	
	//Constructor
	public LikeCount(){
	}

	public LikeCount(UUID id, int count){
		super();
		this.id = id;
		this.count = count;
		this.updatedOn = new Date();
	}

	private void incrementCount(){this.count++;}
	private void decrementCount(){this.count--;} 
	
	//Setters==========================================================================	
	public void setId(UUID id){this.id = id;}
	public void setUpdatedOn(Date updatedOn){this.updatedOn = updatedOn;}	
	public void setOutfit(Outfit outfit){
		if(outfit != null){
			this.outfit = outfit;
			if(this.outfit.getLikeCount() != this) this.outfit.setLikeCount(this);
		}else{
			logger.warn("outfit parameter is null");
		}
	}
	
	
	//Getters==========================================================================	
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public int getCount(){return this.count;}
	public Date getUpdatedOn(){return this.updatedOn;}
	public Outfit getOutfit(){return this.outfit;}

	public void addProfile(Profile profile){
		LikeCountProfile likeCountProfile = new LikeCountProfile(profile, this);
		this.profiles.add(likeCountProfile);
		profile.getLikeCounts().add(likeCountProfile);		
		incrementCount();
	}

	public void addProfile(LikeCountProfile likeCountProfile){
		this.profiles.add(likeCountProfile);
		likeCountProfile.getProfile().getLikeCounts().add(likeCountProfile);
		incrementCount();
	}

	public void removeProfile(Profile profile){
		for(Iterator<LikeCountProfile> iterator = profiles.iterator(); iterator.hasNext();){
			LikeCountProfile likeCountProfile = iterator.next();

			if(likeCountProfile.getLikeCount().equals(this) && likeCountProfile.getProfile().equals(profile)){
				iterator.remove();
				likeCountProfile.getProfile().getLikeCounts().remove(likeCountProfile);  //Needed for bi-directional mapping
				likeCountProfile.setLikeCount(null);
				likeCountProfile.setProfile(null);
				decrementCount();
			}
		}
	}

	public void addItem(Item item){
		LikeCountItem likeCountItem = new LikeCountItem(item, this);
		this.items.add(likeCountItem);
		item.getLikeCounts().add(likeCountItem);
	}

	public void addItem(LikeCountItem likeCountItem){
		this.items.add(likeCountItem);
		likeCountItem.getItem().getLikeCounts().add(likeCountItem);
	}

	public void removeItem(Item item){
		for(Iterator<LikeCountItem> iterator = items.iterator(); iterator.hasNext();){
			LikeCountItem likeCountItem = iterator.next();

			if(likeCountItem.getLikeCount().equals(this) && likeCountItem.getItem().equals(item)){
				iterator.remove();
				likeCountItem.getItem().getLikeCounts().remove(likeCountItem);  //Needed for bi-directional mapping
				likeCountItem.setLikeCount(null);
				likeCountItem.setItem(null);
			}
		}
	}

	//@Override 
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building LikeCount string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append((this.id == null ? "null" : this.id))
			.append(indent).append("count: ").append(this.count);
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}


	@Override
	public boolean equals(Object object){
		if(this == object) return true;
		if(this == null || getClass() != object.getClass()) return false;
		LikeCount like = (LikeCount) object;
		return Objects.equals(id, like.getId());
	}

	@Override
	public int hashCode(){
		return Objects.hash(id);
	}
}