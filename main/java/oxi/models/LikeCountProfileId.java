package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.util.Objects;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.hateoas.*;

import org.hibernate.annotations.GenericGenerator;

@Embeddable
public class LikeCountProfileId implements Serializable{
	//@Transient
	//private static final Logger logger = LogManager.getLogger(Item.class);

	@Column(name = "like_count_id")
	private UUID likeCountId;

	@Column(name = "profile_id")
	private UUID profileId;

	private LikeCountProfileId(){}

	public LikeCountProfileId(UUID likeCountId, UUID profileId){
		this.likeCountId = likeCountId;
		this.profileId = profileId;
	}

	//Getters
	public UUID getLikeCountId(){return this.likeCountId;}
	public UUID getProfileId(){return this.profileId;}

	//Setters
	public void setLikeCountId(UUID likeCountId){this.likeCountId = likeCountId;}
	public void setProfileId(UUID profileId){this.profileId = profileId;}

	
	
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("likeCountId: ").append(((this.likeCountId == null) ? "null" : this.likeCountId.toString()))
        	.append(indent).append("profileId: ").append(((this.profileId == null) ? "null" : this.profileId.toString()));
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}

	@Override
	public boolean equals(Object object){
		if(this == object) return true;
		if(object == null || getClass() != object.getClass()) return false;
		LikeCountProfileId that = (LikeCountProfileId)object;
		return Objects.equals(likeCountId, that.likeCountId) && Objects.equals(profileId, that.profileId);
	}

	@Override 
	public int hashCode(){
		return Objects.hash(likeCountId, profileId);
	}
}