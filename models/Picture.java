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
	private String thumbnailuri;
	
	@OneToOne(cascade=CascadeType.ALL)
	//@JoinColumn(name="content_id")
	//@JoinColumn(name="picture_id", referencedColumnName="picture_id")
	///@RestResource(rel="client_0")
	@JsonIdentityReference(alwaysAsId=true)	
	//@JsonManagedReference
	private Content content;
	
	public Picture(){
	}

	public Picture(UUID id, String thumbnailuri, String smalluri, String largeuri){
		this.id = id;
		this.thumbnailuri = thumbnailuri;
		this.smalluri = smalluri;
		this.largeuri = largeuri;
	}
	
	//Getters
	//@Override
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public String getSmalluri(){return this.smalluri;}
	public String getLargeuri(){return this.largeuri;}
	public String getThumbnailuri(){return this.thumbnailuri;}
	public Content getContent(){return this.content;}
	
	//Setters
	//@Override
	public void setId(UUID id){this.id = id;}
	//public void setIdText(String idText){this.idText = idText;}
	public void setSmalluri(String smalluri){this.smalluri = smalluri;}
	public void setLargeuri(String largeuri){this.largeuri = largeuri;}
	public void setThumbnailuri(String thumbnailuri){this.thumbnailuri = thumbnailuri;}
	public void setContent(Content content){
		logger.warn("setting Content property of Picture POJO");
		//this.content = content;
		if(content != null){
			this.content = content;
			if(this.content.getPicture() != this) this.content.setPicture(this);
		}else{
			logger.warn("Content parameter is null");
		}
	}

	public String toString(int indents) {
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : id))
			.append(indent).append("smalluri: ").append(this.smalluri)
			.append(indent).append("largeuri:").append(this.largeuri)
			.append(indent).append("thumbnail:").append(this.thumbnailuri)
			.append(indent).append(indent).append("content: ").append((this.content == null ? "null" : this.content.getId() == null ? "null" : content.getId().toString()));
		
		return sb.toString();
	}	

	@Override
	public String toString(){
		return toString(0);
	}
	
}