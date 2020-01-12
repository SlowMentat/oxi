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


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=FollowingDto.class)
public class FollowingDto implements Serializable{
	@Transient
	private static final Logger logger = LogManager.getLogger(FollowingDto.class);

	//private String id;
	private String followerUsername;
	private String followeeUsername;
	
	
	//Constructor
	public FollowingDto(){
	}


	public FollowingDto(/*String id, */String followerUsername, String followeeUsername){
		//super();
		//this.id = id;
		this.followerUsername = followerUsername;
		this.followeeUsername = followeeUsername;
		//this.createdOn = new Date();
	}
	

	//Setters==========================================================================	
	//public void setId(UUID id){this.id = id;}
	public void setFollowerUsername(String followerUsername){this.followerUsername = followerUsername;}	
	public void setFolloweeUsername(String followeeUsername){this.followeeUsername = followeeUsername;}
	
	
	//Getters==========================================================================	
	//public String getIdText(){return this.id;}
	public String getFollowerUsername(){return this.followerUsername;}
	public String getFolloweeUsername(){return this.followeeUsername;}


	//@Override 
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Following string");
        StringBuilder sb = new StringBuilder(indent)
			.append(indent).append("followerUsername: ").append(this.followerUsername)
			.append(indent).append("followeeUsername: ").append(this.followerUsername);
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}

	/*@Override
	public boolean equals(Object object){
		if(this == object) return true;
		if(this == null || getClass() != object.getClass()) return false;
		Following following = (Following) object;
		return Objects.equals(id, following.getId());
	}

	@Override
	public int hashCode(){
		return Objects.hash(id);
	}*/
}