package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import org.springframework.hateoas.*;
import com.fasterxml.jackson.databind.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="picture")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Picture.class)
public class Picture extends RelatedEntity implements Serializable, Identifiable<UUID>{
	@Transient
	private static final Logger logger = LogManager.getLogger(Picture.class);
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;	
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
	
	//Getters
	//@Override
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public String getSmalluri(){return this.smalluri;}
	public String getLargeuri(){return this.largeuri;}
	public Content getContent(){return this.content;}
	
	//Setters
	//@Override
	public void setId(UUID id){this.id = id;}
	//public void setIdText(String idText){this.idText = idText;}
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
        StringBuilder sb = new StringBuilder("\nid: ").append(this.id)
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