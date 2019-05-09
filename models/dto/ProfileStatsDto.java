package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.dto.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;
import java.util.Date;



public class ProfileStatsDto implements Serializable, Identifiable<String>
{
	//private Long Id;
	@JsonProperty("id")
	private String id;
	private long following;
	private long likes;
	private int points;
	private Date lastUpdate;

	public ProfileStatsDto(){
	}

	public ProfileStatsDto(ProfileStats profileStats){
		this.id = profileStats.getIdText();
		this.following = profileStats.getFollowing();
		this.likes = profileStats.getLikes();
		this.lastUpdate = profileStats.getLastUpdate();
		this.points = profileStats.getPoints();
	}

	public ProfileStatsDto(String id, long following, long likes, Date lastUpdate){
		this.id = id;
		this.following = following;
		this.likes = likes;
		this.lastUpdate = lastUpdate;
	}
	
	
	//Setters
	public void setId(String id){this.id = id;}
	public void setFollowing(long following){this.following = following;}
	public void setLikes(Long likes){this.likes = likes;}
	public void setPoints(int points){this.points = points;}
	public void setLastUpdated(Date lastUpdate){this.lastUpdate = lastUpdate;}

	//Getters
	@Override
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}
	public long getFollowing(){return this.following;}
	public long getLikes(){return this.likes;}
	public int getPoints(){return this.points;}
	public Date getLastUpdated(){return this.lastUpdate;}
}