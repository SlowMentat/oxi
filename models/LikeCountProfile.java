package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.hateoas.*;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "LikeCountProfile")
@Table(name="like_count_profile")
public class LikeCountProfile extends RelatedEntity{
	@Transient
	private static final Logger logger = LogManager.getLogger(LikeCountProfile.class);
	
	@EmbeddedId
	private LikeCountProfileId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("likeCountId")
	private LikeCount likeCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("profileId")
	private Profile profile;	

	@Column(name = "created_on")
	private Date createdOn = new Date();

	//Constructors 
	private LikeCountProfile(){}

	public LikeCountProfile(Profile profile, LikeCount likeCount){
		this.likeCount = likeCount;
		this.profile = profile;
		this.id  = new LikeCountProfileId(likeCount.getId(), profile.getId());
		this.createdOn = new Date();
	}

	//Getters
	public LikeCount getLikeCount(){return this.likeCount;}
	public Profile getProfile(){return this.profile;}
	public Date getCreatedOn(){return this.createdOn;}

	//Setters
	public void setLikeCount(LikeCount likeCount){this.likeCount = likeCount;}
	public void setProfile(Profile profile){this.profile = profile;}
	public void setCreatedOn(Date createdOn){this.createdOn = createdOn;}


	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Profile string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append("{")
        	.append(((this.id == null) ? "null" : this.id.toString(indents + 1)))
        	.append(indent).append("}")
        	.append(indent).append("likeCount: ").append(likeCount.getId().toString())
			.append(indent).append("profile: ").append(profile.getId().toString());
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}

	@Override
	public boolean equals(Object object){
		//test object reference equivalence
		if(this == object) return true;
		//test for class association equivalence
		if(object == null || getClass() != object.getClass()) return false;
		//test further for equivalence by calling equals method of likeCount and contents properties
		LikeCountProfile that = (LikeCountProfile) object;
		return Objects.equals(likeCount, that.likeCount) && Objects.equals(profile, that.profile);
	}

	@Override
	public int hashCode(){
		return Objects.hash(likeCount, profile);
	}
}