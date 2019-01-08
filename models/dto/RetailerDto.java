package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=RetailerDto.class)
public class RetailerDto implements Serializable, Identifiable<String>
{
	private static final Logger logger = LogManager.getLogger(RetailerDto.class);
	@JsonProperty("id")
	private String id;	
	private String name;
	private String link;
	private Integer red;
	private Integer blue;
	private Integer green;

	public RetailerDto(){}

	public RetailerDto(String id, String name, String link, Integer red, Integer blue, Integer green){
		super();
		this.id = id;
		this.name = name;
		this.link = link;
		this.red = red;
		this.blue = blue; 
		this.green = green;
	}

	/*	@JsonCreator
	public RetailerDto(
		@JsonProperty("id")
		String id, 
		@JsonProperty("name")
		String name, 
		@JsonProperty("link")
		String link, 
		@JsonProperty("red")
		Integer red, 
		@JsonProperty("blue")
		Integer blue, 
		@JsonProperty("green")
		Integer green){
		super();
		this.id = id;
		this.name = name;
		this.link = link;
		this.red = red;
		this.blue = blue; 
		this.green = green;
	}*/

	//Setters==========================================================================	
	public void setId(String id){this.id = id;}
	public void setName(String name){this.name = name;}	
	public void setLink(String link){this.link = link;}	
	public void setRed(Integer red){this.red = red;}
	public void setBlue(Integer blue){this.blue = blue;}
	public void setGreen(Integer green){this.green = green;}
	
	//Getters==========================================================================	
	@Override
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}
	public String getName(){return this.name;}
	public String getLink(){return this.link;}
	public Integer getRed(){return this.red;}
	public Integer getBlue(){return this.blue;}
	public Integer getGreen(){return this.green;}
}
