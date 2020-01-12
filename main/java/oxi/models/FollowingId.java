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
public class FollowingId implements Serializable{
	@Transient
	private static final Logger logger = LogManager.getLogger(FollowingId.class);

	@Column(name = "follower_profile_id")
	private UUID followerProfileId;

	@Column(name = "followee_profile_id")
	private UUID followeeProfileId;

	public FollowingId(){

	}

	public FollowingId(UUID followerProfileId, UUID followeeProfileId){
		this.followerProfileId = followerProfileId;
		this.followeeProfileId = followeeProfileId;
	}

	//Getters
	public UUID getFollowerProfileId(){return this.followerProfileId;}
	public UUID getFolloweeProfileId(){return this.followeeProfileId;}

	//Setters
	public void setFollowerProfileId(UUID followerProfileId){this.followerProfileId = followerProfileId;}
	public void setFolloweeProfileId(UUID followeeProfileId){this.followeeProfileId = followeeProfileId;}

	
	
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("followerProfileId: ").append(((this.followerProfileId == null) ? "null" : this.followerProfileId.toString()))
        	.append(indent).append("followeeProfileId: ").append(((this.followeeProfileId == null) ? "null" : this.followeeProfileId.toString()));
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
		FollowingId that = (FollowingId)object;
		return Objects.equals(followerProfileId, that.followerProfileId) && Objects.equals(followeeProfileId, that.followeeProfileId);
	}

	@Override 
	public int hashCode(){
		return Objects.hash(followerProfileId, followeeProfileId);
	}
}