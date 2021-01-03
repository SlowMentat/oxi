package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.Objects;
import java.util.Date;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators.*;
import org.springframework.hateoas.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.hibernate.annotations.GenericGenerator;

////import oxi.models.projection.BookmarkProjection;

@Entity
@Table(name="following")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="Bookmark_id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Following.class)
public class Following extends RelatedEntity implements Serializable/*, Identifiable<FollowingId>*/{
	@Transient
	private static final Logger logger = LogManager.getLogger(Following.class);

	/*@Id
	@JsonProperty("id")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;*/

	@EmbeddedId
	private FollowingId id;

	//@Column(name = "id_text", updatable = false, insertable = false)
	//private String idText;
	//@Column(name = "follower_profile_id")
	//private UUID followerProfileId;
	//@Column(name = "followee_profile_id")
	//private UUID followeeProfileId;


	@Column(name = "created_on")
	private Date createdOn = new Date();
	
	
	//Constructor
	public Following(){
	}

	/*public Following(UUID id, String followerProfileId, String followeeProfileId){
		super();
		this.id = id;
		this.followerProfileId = followerProfileId;
		this.followeeProfileId = followeeProfileId;
		this.createdOn = new Date();
	}*/

	public Following(FollowingId id){
		this.id = id;
	}
	
	//Setters==========================================================================	
	public void setId(FollowingId id){this.id = id;}
	//public void setId(UUID id){this.id = id;}
	//public void setFollowerProfileId(UUID followerProfileId){this.followerProfileId = followerProfileId;}	
	//public void setFolloweeProfileId(UUID followeeProfileId){this.followeeProfileId = followeeProfileId;}
	public void setCreatedOn(Date createdOn){this.createdOn = createdOn;}
	
	
	//Getters==========================================================================	
	public FollowingId getId(){return this.id;}
	//public UUID getId(){return this.id;}
	//public String getIdText(){return this.idText;}
	//public UUID getFollowerProfileId(){return this.followerProfileId;}
	//public UUID getFolloweeProfileId(){return this.followeeProfileId;}
	public Date getCreatedOn(){return this.createdOn;}


	//@Override 
	/*public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Following string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append((this.id == null ? "null" : this.id))
			.append(indent).append("followerProfileId: ").append((this.followerProfileId == null ? "null" : this.followerProfileId))
			.append(indent).append("followeeProfileId: ").append((this.followeeProfileId == null ? "null" : this.followeeProfileId));
        return sb.toString();
	}*/

	//@Override 
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Following string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append((this.id == null ? "null" : this.id))
			//.append(indent).append("followerProfileId: ").append((this.followerProfileId == null ? "null" : this.followerProfileId))
			//.append(indent).append("followeeProfileId: ").append((this.followeeProfileId == null ? "null" : this.followeeProfileId))
			.append(indent).append("createdOn: ").append((this.createdOn == null ? "null" :  this.createdOn));
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
		Following following = (Following) object;
		return Objects.equals(id, following.getId());
	}

	@Override
	public int hashCode(){
		return Objects.hash(id);
	}
}