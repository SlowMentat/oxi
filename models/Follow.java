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

//import oxi.models.projection.BookmarkProjection;

@Entity
@Table(name="follow")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="Bookmark_id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Follow.class)
public class Follow extends RelatedEntity implements Serializable, Identifiable<UUID>{
	@Transient
	private static final Logger logger = LogManager.getLogger(Follow.class);

	@Id
	@JsonProperty("id")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;
	@Column(name = "follower_username")
	private String followerUsername;
	@Column(name = "followed_username")
	private String followedUsername;
	@Column(name = "created_on")
	private Date createdOn = new Date();
	
	
	//Constructor
	public Follow(){
	}

	public Follow(UUID id, String followerUsername, String followedUsername){
		super();
		this.id = id;
		this.followerUsername = followerUsername;
		this.followedUsername = followedUsername;
		this.createdOn = new Date();
	}
	
	//Setters==========================================================================	
	public void setId(UUID id){this.id = id;}
	public void setfollowerUsername(String followerUsername){this.followerUsername = followerUsername;}	
	public void setfollowedUsername(String followedUsername){this.followedUsername = followedUsername;}
	public void setCreatedOn(Date createdOn){this.createdOn = createdOn;}
	
	
	//Getters==========================================================================	
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public String getFollowerUsername(){return this.followerUsername;}
	public String getFollowedUsername(){return this.followedUsername;}
	public Date getCreatedOn(){return this.createdOn;}


	//@Override 
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Follow string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append((this.id == null ? "null" : this.id))
			.append(indent).append("followerUsername: ").append(this.followerUsername)
			.append(indent).append("followedUsername: ").append(this.followerUsername);
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
		Follow follow = (Follow) object;
		return Objects.equals(id, follow.getId());
	}

	@Override
	public int hashCode(){
		return Objects.hash(id);
	}
}