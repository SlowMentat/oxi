package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@Relation(value = "content", collectionRelation = "contents")
public class ContentDto extends ResourceSupport
{
	private static final Logger logger = LogManager.getLogger(ContentDto.class);
	//private Long id;
	private String coverpicuri;
	private Outfit outfit;
	private Picture picture;
	private List<Item> items;

	public ContentDto(){
		super();
	}

	//Setters==========================================================================	
	//public void setId(Long id){this.Id = id;}
	public void setCoverpicuri(String uri){this.coverpicuri = uri;}	
	public void setOutfit(Outfit outfit){this.outfit = outfit;}	
	public void setPicture(Picture picture){this.picture = picture;}	
	public void setItems(List<Item> items){logger.debug("items = " + items); this.items = items;}
	
	//Getters==========================================================================	
	//public Long getId(){return this.Id;}
	public String getCoverpicuri(){return this.coverpicuri;}
	public Outfit getOutfit(){return this.outfit;}
	public Picture getPicture(){return this.picture;}
	public List<Item> getItems(){return this.items;}
}
