package oxi.models;

import oxi.models.dto.ProfileStatsDto;
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

import java.util.Date;

@Entity
@Table(name="profile_stats")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ProfileStats.class)
public class ProfileStats extends RelatedEntity implements Serializable/*Identifiable<UUID>*/
{
	@Transient
	private static final Logger logger = LogManager.getLogger(ProfileStats.class);
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;	
	
	private long following = 0;
	private long likes = 0;
	private int points = 0;
	@Column(name = "last_update")
	private Date lastUpdate = new Date();
	
	@OneToOne(cascade=CascadeType.ALL)
	@JsonIdentityReference(alwaysAsId=true)
	private Profile profile;

	//Constructor
	public ProfileStats(){
	}
	
	public ProfileStats(ProfileStatsDto profileStatsDto){
		if(profileStatsDto != null){
			this.id = profileStatsDto.getId() != null ? UUID.fromString(profileStatsDto.getId()) : null;
			this.following = profileStatsDto.getFollowing();
			this.points = profileStatsDto.getPoints();
			this.likes = profileStatsDto.getLikes();
		}
	}

	//Setters
	public void setId(UUID id){this.id = id;}
	public void setFollowing(long following){this.following = following;}
	public void setPoints(int points){this.points = points;}
	public void setLikes(long likes){this.likes = likes;}
	public void setProfile(Profile profile){
		logger.warn("SETTING PROFILE");
		this.profile = profile;
		if (this.profile != null){		
			logger.warn("Profile POJO Not NULL");
			if(this.profile.getProfileStats() != this){
				logger.warn("Linking Profile to ProfileStats");
				profile.setProfileStats(this);
			}
		}
		else{
			logger.warn("!!Profile IS NULL!!");
		}
	}
	
	//Getters
	
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public long getFollowing(){return this.following;}
	public int getPoints(){return this.points;}
	public long getLikes(){return this.likes;}
	public Date getLastUpdate(){return this.lastUpdate;}
	public Profile getProfile(){return this.profile;}


	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building ProfileStats string");
        StringBuilder sb = new StringBuilder("\nid: ").append(((this.id == null) ? "null" : this.id.toString()))
			.append(indent).append("\nfollowing:").append(this.following)
			.append(indent).append("\nlikes:").append(this.likes)
			.append(indent).append("\npoints:").append(this.points)
			.append(indent).append("\nlastUpdate:").append(this.lastUpdate)
			.append(indent).append("\nprofile: {\n")
				.append(indent).append("    ").append("id: ")
				.append( ((this.profile != null && this.profile.getId() != null) ?  profile.getId().toString() : "null") )
				.append("\n").append(indent).append("}");
        return sb.toString();		
	}

	@Override
	public String toString(){
		return toString(0);
	}
}
