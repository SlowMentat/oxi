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


@Relation(value = "picture", collectionRelation = "pictures")
public class PictureDto extends ResourceSupport
{
	private UUID Id;	
	private String smalluri;
	private String largeuri;
	private Content content;
	
	public PictureDto(){
		super();
	}
	
	//Getters
	//public UUID getId(){return this.Id;}
	public String getSmalluri(){return this.smalluri;}
	public String getLargeuri(){return this.largeuri;}
	public Content getContent(){return this.content;}
	
	//Setters
	//public void setId(UUID id){this.Id = id;}
	public void setSmalluri(String smalluri){this.smalluri = smalluri;}
	public void setLargeuri(String largeuri){this.largeuri = largeuri;}
	public void setContent(Content content){this.content = content;}
}