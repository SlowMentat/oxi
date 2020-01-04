package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.annotation.*;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=LikeCountDto.class)
public class LikeCountDto implements Serializable, Identifiable<String>
{
	@JsonProperty("id")
	private String id;
	private int count;

	public LikeCountDto(){

	}

	public LikeCountDto(LikeCount likeCount){
		if(likeCount != null){
			this.id = likeCount.getId().toString();
			this.count = likeCount.getCount();
		}
	}

	public LikeCountDto(String id, int count){
		this.id = id;
		this.count = count;
	}

	//Setters
	public void setId(String id){this.id = id;}
	public void setCount(int count){this.count = count;}
	
	//Getters
	@Override
	public String getId(){return (this.id == null) ? null : this.id.toLowerCase();}
	public int getCount(){return this.count;}
}
