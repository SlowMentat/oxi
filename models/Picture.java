package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.List;
import java.io.Serializable;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Entity
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="picture_id")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="picture_id", scope=Picture.class)
public class Picture extends RelatedEntity implements Serializable{
	@Transient
	private static final Logger logger = LogManager.getLogger(Picture.class);
	
	@Id
	@JsonProperty("picture_id")
	//@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String smalluri;
	private String largeuri;
	
	@OneToOne(cascade=CascadeType.ALL)
	//@JoinColumn(name="content_id")
	//@JoinColumn(name="picture_id", referencedColumnName="picture_id")
	///@RestResource(rel="client_0")
	@JsonIdentityReference(alwaysAsId=true)	
	//@JsonManagedReference
	private Content content;
	
	public Picture(){
	}
	/*
	public Picture(String smalluri, String largeuri){
		this.smalluri = smalluri;
		this.largeuri = largeuri;
	}
	*/
	//Getters
	public long getId(){return this.id;}
	public String getSmalluri(){return this.smalluri;}
	public String getLargeuri(){return this.largeuri;}
	public Content getContent(){return this.content;}
	
	//Setters
	public void setId(long id){this.id = id;}
	public void setSmalluri(String smalluri){this.smalluri = smalluri;}
	public void setLargeuri(String largeuri){this.largeuri = largeuri;}
	public void setContent(Content content){
		logger.warn("setting Content property of Picture POJO");
		this.content = content;
		/*if(content != null){
			this.content = content;
			if(this.content.getPicture() != this) this.content.setPicture(this);
		}else{
			logger.warn("Content parameter is null");
		}*/
	}
	
	@Override
	public String toString(){
		logger.debug("building Picture string");
        StringBuilder sb = new StringBuilder("\nID: ").append(this.id)
			.append("\nsmalluri: ").append(this.smalluri)
			.append("\nlargeuri:").append(this.largeuri)
			.append("\ncontent: ");
		if(this.content != null){		
			logger.debug("building outfit string");
			sb.append(this.content.getId());
		}else{
			logger.debug("profile.content is null");
		}
        return sb.toString();
	}
	
}