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

@MappedSuperclass
public abstract class BasePicture extends RelatedEntity implements Serializable, Identifiable<UUID>{
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;	
	private String smalluri;
	private String largeuri;
	private String mediumuri;
	private String thumbnailuri;
	private String originaluri;
	@Column(name = "crop", columnDefinition="JSON")
	private String crop;

	
	public BasePicture(){
	}

	public BasePicture(UUID id, String thumbnailuri, String smalluri, String mediumuri, String largeuri ){
		this.id = id;
		this.smalluri = smalluri;
		this.largeuri = largeuri;
		this.mediumuri = mediumuri;
		this.thumbnailuri = thumbnailuri;
	}

	public BasePicture(UUID id, String thumbnailuri, String smalluri, String mediumuri, String largeuri, String originaluri, String crop){
		this.id = id;
		this.smalluri = smalluri;
		this.largeuri = largeuri;
		this.mediumuri = mediumuri;
		this.thumbnailuri = thumbnailuri;
		this.originaluri = originaluri;
		this.crop = crop;
	}


	//@Override 
	//public <T extends Relational> void internalAddChild(T targetChild)
	//@Override
	//public <T extends Relational> void internalRemoveChild(T targetChild)
	
	//Getters
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public String getSmalluri(){return this.smalluri;}
	public String getMediumuri(){return this.mediumuri;}
	public String getLargeuri(){return this.largeuri;}
	public String getThumbnailuri(){return this.thumbnailuri;}
	public String getOriginaluri(){return this.originaluri;}
	public String getCrop(){return this.crop;}
	
	//Setters
	public void setId(UUID id){this.id = id;}
	public void setSmalluri(String smalluri){this.smalluri = smalluri;}
	public void setMediumuri(String mediumuri){this.mediumuri = mediumuri;}
	public void setLargeuri(String largeuri){this.largeuri = largeuri;}
	public void setThumbnailuri(String thumbnailuri){this.thumbnailuri = thumbnailuri;}
	public void setOriginaluri(String originaluri){this.originaluri = originaluri;}
	public void setCrop(String crop){this.crop = crop;}

	public String toString(int indents) {
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : id))
			.append(indent).append("smalluri: ").append(this.smalluri)
			.append(indent).append("mediumuri:").append(this.mediumuri)
			.append(indent).append("largeuri:").append(this.largeuri)
			.append(indent).append("thumbnail:").append(this.thumbnailuri)
			.append(indent).append("originaluri:").append(this.originaluri)
			.append(indent).append("crop:").append(this.crop);
			//.append(indent).append(indent).append("item: ").append((this.item == null ? "null" : this.item.getId() == null ? "null" : item.getId().toString()));
		
		return sb.toString();
	}	

	@Override
	public String toString(){
		return toString(0);
	}
	
}