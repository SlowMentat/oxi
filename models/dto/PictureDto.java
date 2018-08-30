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
	private String smalluri;
	private String largeuri;
	private Content content;
	
	public PictureDto(){
	}
	
	//Getters
	@Override
	public String getId(){return this.id;}
	public String getSmalluri(){return this.smalluri;}
	public String getLargeuri(){return this.largeuri;}
	public Content getContent(){return this.content;}
	
	//Setters
	public void setId(String id){this.id = id;}
	public void setSmalluri(String smalluri){this.smalluri = smalluri;}
	public void setLargeuri(String largeuri){this.largeuri = largeuri;}
	public void setContent(Content content){this.content = content;}
}