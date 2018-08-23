package oxi.models.dto;

import oxi.models.projection.*;
//import oxi.models.*;
import oxi.models.projection.ContentProjection;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;


@Relation(value = "picture", collectionRelation = "pictures")
public class PictureDto implements PictureProjection
{
	private String id;	
	private String smalluri;
	private String largeuri;
	private ContentDto content;
	
	public PictureDto(){
		super();
	}
	
	//Getters
 	@Override
	public String getId(){return this.id;}

	public String getSmalluri(){return this.smalluri;}

	public String getLargeuri(){return this.largeuri;}

	public <T extends ContentProjection> T getContent(){return this.content;}

	
	//Setters
	public void setId(String id){this.id = id;}

	public void setSmalluri(String smalluri){this.smalluri = smalluri;}

	public void setLargeuri(String largeuri){this.largeuri = largeuri;}

	public <T extends ContentProjection> void setContent(T content){this.content = content;}
}