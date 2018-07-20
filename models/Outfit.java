package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
//import org.hibernate.annotations.*;
import com.fasterxml.jackson.databind.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
//import oxi.jackson.*;
import org.springframework.hateoas.*;
import java.lang.*;

import oxi.models.projection.OutfitProjection;

@Entity
@Table(name="outfit")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Outfit.class)
//@JsonDeserialize(contentUsing=CustomOutfitDeserializer.class) 
public class Outfit extends RelatedEntity implements Serializable, Identifiable<Long>
{
	@Transient
	private static final Logger logger = LogManager.getLogger(Outfit.class);
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	//@JsonProperty("id")
	private Long id;
	private int likes;
	private String comments;
	/*@OneToOne
	@JoinColumn(name="coverpicture_id")
	@RestResource(rel="vendor_1")*/
	private String coverpicuri;
	
	//@Cascade(CascadeType.SAVED_UPDATE)
	@OneToMany(cascade=CascadeType.ALL, mappedBy="outfit")
	@RestResource(rel="contents")
	@JsonIdentityReference(alwaysAsId=true)
	private List<Content> contents;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="profile_id")
	@RestResource(rel="profile")
	@JsonIdentityReference(alwaysAsId=true)
	//@JsonManagedReference
	private Profile profile;
	
	//Constructor
	public Outfit(){
	}

	public Outfit(Long id, int likes, String comments, List<Content> contents, String coverpicuri){
		//super();
		this.id = id;
		this.likes = likes;
		this.comments = comments;
		this.contents = contents;
		this.coverpicuri = coverpicuri;
	}
	
	//Setters
	//@Override
	public void setId(Long id){this.id = id;}
	public void setLikes(int likes){this.likes = likes;}		
	public void setComments(String comments){this.comments = comments;}	
	public void setCoverpicuri(String uri){this.coverpicuri = uri;}	
	public void setProfile(Profile profile){
		
		/*//Check if there is already a Profile Object associated with this Order Object.
		//If so remove Remove this outfit object from the refereced Profile object's List<Outfit>
		//and reference this OUtfit object to the passed Profile Object.  If the passed Profile Object is not null,
		//add this Outfit object to the referenced Profile Object's List<Outfit>.
		if(this.profile != null){
			logger.warn("Removing Outfit from List<Order> property of Profile object");
			this.profile.internalRemoveOutfit(this);
		}
		logger.warn("Referencing Outfit.profile to Profile object");
		this.profile = profile;
		if(profile != null){
			logger.warn("Profile reference (before adding outfit):  " + this.profile.toString());
			logger.warn("Adding Outfit to List<Outfit> property of Profile object");
			profile.internalAddOutfit(this);
			logger.warn("Profile reference (after adding outfit):  " + this.profile.toString());
		}*/
		//this.profile = (Profile)this.setManyToOneParent(profile, this.profile, this);
		this.profile = this.<Profile, Outfit>setManyToOneParent(profile, this.profile, this);
		if(this.profile == null) logger.debug("Outfit.profile is null");
		//else logger.debug("Outfit's profile variable:  " + this.toString());
	}	
	public void setContents(List<Content> contents){
		logger.debug("Setting contents list in Outfit entity");
		this.contents = contents;
	}	
	@JsonAnySetter
	public void addContent(Content content){
		this.contents.add(content);
		if (content.getOutfit() != this) content.setOutfit(this);
	}
	
	//Getters
	//@Override
	public Long getId(){return this.id;} 
	public int getLikes(){return this.likes;}	
	public String getComments(){return this.comments;}	
	public Profile getProfile(){return this.profile;}
	public List<Content> getContents(){return this.contents;}
	public String getCoverpicuri(){return this.coverpicuri;}
	
	//Auxillary methods
	/*@Override
	public void internalAddChild(Relational targetChild){
	Outfit currentParent = (Outfit)this;
	Content child = (Content)targetChild;
		if(currentParent.contents == null){
			logger.debug("instantiating new List<Outfit> outfits");
			currentParent.contents = new ArrayList<Content>();
		}
		currentParent.contents.add(child);
	}
	
	@Override
	public void internalRemoveChild(Relational targetChild){
		((Outfit)this).contents.remove((Content)targetChild);
	}*/
	@Override 
	public <T extends Relational> void internalAddChild(T targetChild){
		if(this.contents == null){
			logger.debug("instantiating new List<T>");
			this.contents = new ArrayList<Content>();
		}
		this.contents.add((Content)targetChild);
	}
	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		this.contents.remove((Content)targetChild);
	}
	
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nid: ").append(this.id)
			.append("\nlikes: ").append(this.likes)
			.append("\ncomments:").append(this.comments)
			.append("\ncoverpic:").append(this.coverpicuri)
			.append("\ncontents:");
		if(this.contents != null){
			sb.append("[\n");
			for (Content content: this.contents) {
				sb.append(content.getId());
			}
			sb.append("\n]");
		}else{
			sb.append("null");
		}
		sb.append("\nprofile:");
		if(this.profile != null){
			sb.append("[\n	")
			.append(this.profile.getId())
			.append("\n]");
		}else{
			sb.append("null");
		}
		
        return sb.toString();
    }
}