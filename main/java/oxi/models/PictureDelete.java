package oxi.models;

import oxi.models.dto.PictureDto;

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
@Table(name="picture_delete")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=PictureDelete.class)
public class PictureDelete extends RelatedEntity implements Serializable, Identifiable<UUID>{
	@Transient
	private static final Logger logger = LogManager.getLogger(PictureDelete.class);
	
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
	
	public PictureDelete(){
	}

	public PictureDelete(PictureDto pictureDto){
		this.id = UUID.fromString(pictureDto.getId());
		this.thumbnailuri = pictureDto.getThumbnailuri();
		this.smalluri = pictureDto.getSmalluri();
		this.largeuri = pictureDto.getLargeuri();
	}

	public PictureDelete(Picture picture){
		this.id = picture.getId();
		this.thumbnailuri = picture.getThumbnailuri();
		this.smalluri = picture.getSmalluri();
		this.largeuri = picture.getLargeuri();
	}

	public PictureDelete(UUID id, String thumbnailuri, String smalluri, String largeuri){
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
	
	//Setters
	//@Override
	public void setId(UUID id){this.id = id;}
	//public void setIdText(String idText){this.idText = idText;}
	public void setSmalluri(String smalluri){this.smalluri = smalluri;}
	public void setLargeuri(String largeuri){this.largeuri = largeuri;}
	public void setThumbnailuri(String thumbnailuri){this.thumbnailuri = thumbnailuri;}
	
	@Override
	public String toString(){
		logger.debug("building PictureDelete string");
        StringBuilder sb = new StringBuilder("\nid: ").append(this.id.toString())
			.append("\nsmalluri: ").append(this.smalluri)
			.append("\nlargeuri:").append(this.largeuri)
			.append("\nthumbnail:").append(this.thumbnailuri);
        return sb.toString();
	}
	
}