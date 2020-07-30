package oxi.models;

import oxi.models.dto.PictureDto;
import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;
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
@Table(name="picture_profile")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=PictureProfile.class)
public class PictureProfile extends BasePicture implements Serializable, Identifiable<UUID>{
	@Transient
	private static final Logger logger = LogManager.getLogger(PictureProfile.class);
	
	//@Id
	//@GeneratedValue(generator = "uuid2")
	//@GenericGenerator(name = "uuid2", strategy = "uuid2")
	//@Column(columnDefinition = "BINARY(16)")
	//private UUID id;
//
	//@Column(name = "id_text", updatable = false, insertable = false)
	//private String idText;	
	//private String smalluri;
	//private String largeuri;
	//private String mediumuri;
	//private String thumbnailuri;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JsonIdentityReference(alwaysAsId=true)	
	private Profile profile;

	
	public PictureProfile(){
		super();
	}

	public PictureProfile(UUID id, String thumbnailuri, String smalluri, String largeuri){
		super(id, thumbnailuri, smalluri, null, largeuri);
		//this.id = id;
		//this.thumbnailuri = thumbnailuri;
		//this.smalluri = smalluri;
		//this.largeuri = largeuri;
	}

	public PictureProfile(UUID id, String thumbnailuri, String smalluri, String mediumuri, String largeuri){
		super(id, thumbnailuri, smalluri, mediumuri, largeuri);
		//this.id = id;
		//this.thumbnailuri = thumbnailuri;
		//this.smalluri = smalluri;
		//this.mediumuri = mediumuri;
		//this.largeuri = largeuri;
	}

	public PictureProfile(UUID id, String thumbnailuri, String smalluri, String mediumuri, String largeuri, String original, String crop){
		super(id, thumbnailuri, smalluri, mediumuri, largeuri, original, crop);
	}

	public PictureProfile(PictureDto pictureDto){
		super(
			UUID.fromString(pictureDto.getId()),
			pictureDto.getThumbnailuri(),
			pictureDto.getSmalluri(),
			pictureDto.getMediumuri(),
			pictureDto.getLargeuri(),
			pictureDto.getOriginaluri(),
			pictureDto.getCrop()
		);
		//this.id = UUID.fromString(pictureDto.getId());
		//this.thumbnailuri = pictureDto.getThumbnailuri();
		//this.smalluri = pictureDto.getSmalluri();
		//this.mediumuri = pictureDto.getMediumuri();
		//this.largeuri = pictureDto.getLargeuri();
	}
	
	//Getters
	//public UUID getId(){return this.id;}
	//public String getIdText(){return this.idText;}
	//public String getSmalluri(){return this.smalluri;}
	//public String getMediumuri(){return this.mediumuri;}
	//public String getLargeuri(){return this.largeuri;}
	//public String getThumbnailuri(){return this.thumbnailuri;}
	public Profile getProfile(){return this.profile;}
	
	//Setters
	//public void setId(UUID id){this.id = id;}
	//public void setSmalluri(String smalluri){this.smalluri = smalluri;}
	//public void setMediumuri(String mediumuri){this.mediumuri = mediumuri;}
	//public void setLargeuri(String largeuri){this.largeuri = largeuri;}
	//public void setThumbnailuri(String thumbnailuri){this.thumbnailuri = thumbnailuri;}
	public void setProfile(Profile profile){
		if(profile != null){
			this.profile = profile;
			if(this.profile.getPictureProfile() != this) this.profile.setPictureProfile(this);
		}else{
			logger.warn("profile parameter is null");
		}
	}


/*
	public void setItem(Item item){
		if(item != null){
			this.item = item;
			if(this.item.getPicture() != this) this.item.setPicture(this);
		}else{
			logger.warn("Item parameter is null");
		}
	}*/
	@Override
	public String toString(int indents) {
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		//StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : id))
		StringBuilder sb = new StringBuilder(indent).append(super.toString(indents))
			//.append(indent).append("smalluri: ").append(this.smalluri)
			//.append(indent).append("mediumuri:").append(this.mediumuri)
			//.append(indent).append("largeuri:").append(this.largeuri)
			//.append(indent).append("thumbnail:").append(this.thumbnailuri)
			.append(indent).append(indent).append("profile: ").append((this.profile == null ? "null" : this.profile.getId() == null ? "null" : profile.getId().toString()));
		return sb.toString();
	}	

	@Override
	public String toString(){
		return toString(0);
	}
	
}