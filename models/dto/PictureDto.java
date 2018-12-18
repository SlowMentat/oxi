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


public class PictureDto implements Serializable, Identifiable<String>
{
	private String id;	
	private String thumbnailuri;
	private String smalluri;
	private String largeuri;
	//private String contentId;
	
	public PictureDto(String id, String thumbnailuri, String smalluri, String largeuri){
		this.id = id;
		this.thumbnailuri = thumbnailuri;
		this.smalluri = smalluri;
		this.largeuri = largeuri;
	}
	
	//Getters
	@Override
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}
	public String getSmalluri(){return this.smalluri;}
	public String getLargeuri(){return this.largeuri;}
	public String getThumbnailuri(){return this.thumbnailuri;}
	//public String getContentId(){return this.contentId;}
	//public Content getContent(){return this.content;}
	
	//Setters
	public void setId(String id){this.id = id;}
	public void setSmalluri(String smalluri){this.smalluri = smalluri;}
	public void setLargeuri(String largeuri){this.largeuri = largeuri;}
	public void setThumbnailrui(String thumbnailuri){this.thumbnailuri = thumbnailuri;}
	//public void setContentId(String contentId){this.contentId = contentId;}
	//public void setContent(Content content){this.content = content;}

	@Override
	public String toString(){
        StringBuilder sb = new StringBuilder("\nid: ").append(this.id == null ? "null" : this.id)
			.append("\nsmalluri: ").append(this.smalluri)
			.append("\nlargeuri:").append(this.largeuri)
			.append("\nthumbnailuri: ").append(this.thumbnailuri);
			//.append("\ncontentId: ").append(this.contentId);
        return sb.toString();
	}
}