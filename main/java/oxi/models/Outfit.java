package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
//import org.hibernate.annotations.*;
import com.fasterxml.jackson.databind.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
//import oxi.jackson.*;
import org.springframework.hateoas.*;
import java.lang.*;

import oxi.models.projection.OutfitProjection;

@Entity
@Table(name="outfit")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Outfit.class)
//@JsonDeserialize(contentUsing=CustomOutfitDeserializer.class) 
public class Outfit extends RelatedEntity implements Serializable/*, Identifiable<UUID>*/
{
	@Transient
	private static final Logger logger = LogManager.getLogger(Outfit.class);
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	@JsonProperty("id")
	private UUID id;

	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;	
	private int likes;
	private String comments;
	/*@OneToOne
	@JoinColumn(name="coverpicture_id")
	@RestResource(rel="vendor_1")*/
	@Column(columnDefinition = "BINARY(16)", name = "cover_picture_id")
	private UUID coverPictureId;
	private String coverpicuri;
	
	//@Cascade(CascadeType.SAVED_UPDATE)
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="outfit")
	@RestResource(rel="contents")
	@JsonIdentityReference(alwaysAsId=true)
	private List<Content> contents;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="profile_id")
	@RestResource(rel="profile")
	@JsonIdentityReference(alwaysAsId=true)
	//@JsonManagedReference
	private Profile profile;

	@OneToOne(cascade=CascadeType.MERGE, mappedBy="outfit")
	@RestResource(rel="vendor_1")
	@JsonProperty("likeCount")
	@JsonIdentityReference(alwaysAsId=true)	
	private LikeCount likeCount;

	private String username;
	@Column(columnDefinition = "BINARY(16)", name = "picture_profile_id", updatable = true, insertable = true)
	private UUID pictureProfileId;

	@Column(name = "created_on", nullable=false, updatable = false)
	private Date createdOn = new Date();
	private String createdOnText = this.createdOn.toString();

	@Column(name = "updated_on", nullable=true)
	private Date updatedOn = null;

	/*@Column(name = "profile_id_text", updatable = false)
	private String profileText;*/
	
	//Constructor
	public Outfit(){
	}

	public Outfit(UUID id, int likes, String comments, List<Content> contents, UUID coverPictureId){
		//super();
		this.id = id;
		this.likes = likes;
		this.comments = comments;
		this.contents = contents;
		this.coverPictureId = coverPictureId;
		this.likeCount = new LikeCount(id, 0);
	}

	public Outfit(UUID id, int likes, String comments, List<Content> contents, UUID coverPictureId, String username){
		//super();
		this.id = id;
		this.likes = likes;
		this.comments = comments;
		this.contents = contents;
		this.coverPictureId = coverPictureId;
		this.username = username;
		this.likeCount = new LikeCount(id, 0);
	}

	public Outfit(UUID id, int likes, String comments, List<Content> contents, UUID coverPictureId, String username, LikeCount likeCount){
		//super();
		this.id = id;
		this.likes = likes;
		this.comments = comments;
		this.contents = contents;
		this.coverPictureId = coverPictureId;
		this.username = username;
		this.likeCount = likeCount;
	}

	public Outfit(UUID id, int likes, String comments, List<Content> contents, UUID coverPictureId, String username, LikeCount likeCount, String coverpicuri){
		//super();
		this.id = id;
		this.likes = likes;
		this.comments = comments;
		this.contents = contents;
		this.coverPictureId = coverPictureId;
		this.username = username;
		this.likeCount = likeCount;
		this.coverpicuri = coverpicuri;
	}
	
	//Setters
	//@Override
	public void setId(UUID id){this.id = id;}
	//public void setIdText(String idText){this.idText = idText;}
	public void setLikes(int likes){this.likes = likes;}		
	public void setComments(String comments){this.comments = comments;}	
	public void setCoverPictureId(UUID coverPictureId){this.coverPictureId = coverPictureId;}
	public void setCoverpicuri(String coverpicuri){this.coverpicuri = coverpicuri;}	
	public void setUsername(String username){this.username = username;}
	//set the binary and text profile ids and assures that changes are propogated to all child entity objects
	public void setProfile(Profile profile){
		
		/*//Check if there is already a Profile Object associated with this Order Object.
		//If so remove this outfit object from the refereced Profile object's List<Outfit>
		//and reference this Outfit object to the passed Profile Object.  
		//If the passed Profile Object is not null, add this Outfit object to the referenced Profile Object's List<Outfit>.
		
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
		//this.profileText = this.<Profile, Outfit>setManyToOneParent(profile, this.profileText, this);
		if(this.profile == null) logger.debug("Outfit.profile is null");
		//if(this.profile == null) logger.debug("Outfit.profileText is null");
		//else logger.debug("Outfit's profile variable:  " + this.toString());
	}	

	public void setLikeCount(LikeCount likeCount){
		logger.warn("SETTING PICURE");
		this.likeCount = likeCount;
		if (this.likeCount != null){		
			logger.warn("likeCount POJO Not NULL");
			if(this.likeCount.getOutfit() != this){
				logger.warn("LINKING LikeCount TO Outfit");
				likeCount.setOutfit(this);
			}
		}
		else{
			logger.warn("!!PICTURE IS NULL!!");
		}
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

	public void setPictureProfileId(UUID pictureProfileId){this.pictureProfileId = pictureProfileId;}

	
	//Getters
	//@Override
	public UUID getId(){return this.id;} 
	public String getIdText(){return this.idText;}
	public int getLikes(){return this.likes;}	
	public String getComments(){return this.comments;}	
	public Profile getProfile(){return this.profile;}
	public List<Content> getContents(){return this.contents;}
	public UUID getCoverPictureId(){return this.coverPictureId;}
	public String getCoverpicuri(){return this.coverpicuri;}
	public String getUsername(){return this.username;}
	public LikeCount getLikeCount(){return this.likeCount;}
	public UUID getPictureProfileId(){return this.pictureProfileId;}
	public Date getUpdatedOn(){return this.updatedOn;}
	public Date getCreatedOn(){return this.createdOn;}
	public String getCreatedOnText(){return this.createdOn.toString();}
	
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
		if (!this.contents.contains((Content)targetChild)) this.contents.add((Content)targetChild);
	}
	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		this.contents.remove((Content)targetChild);
	}
	
	public String toString(int indents) {
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : id))
			.append(indent).append("likes: ").append(this.likes)
			.append(indent).append("comments:").append(this.comments)
			.append(indent).append("coverPictureId:").append(this.coverPictureId)
			.append(indent).append("coverpicuri").append(this.coverpicuri)
			.append(indent).append("contents: [");
		for (Content content: this.contents){
			sb.append(indent).append("{")
			.append(((content == null) ? "null" : content.toString(indents + 1)))
			.append(indent).append("}, ");
		}
		sb.append(indent).append("]");
		
		return sb.toString();
	}	

	@Override
	public String toString(){
		return toString(0);
	}
}