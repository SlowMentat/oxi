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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@Relation(value = "content", collectionRelation = "contents")
public class ContentDto implements Serializable, Identifiable<String>
{
	private static final Logger logger = LogManager.getLogger(ContentDto.class);
	private String id;
	private String coverpicuri;
	//private Outfit outfit;
	private Picture picture;
	private List<ItemDto> items;

	public ContentDto(String id, String coverpicuri, List<ItemDto> items){
		super();
		this.id = id;
		this.coverpicuri = coverpicuri;
		this.items = items;
	}

	//Setters==========================================================================	
	public void setId(String id){this.id = id;}
	public void setCoverpicuri(String uri){this.coverpicuri = uri;}	
	//public void setOutfit(Outfit outfit){this.outfit = outfit;}	
	public void setPicture(Picture picture){this.picture = picture;}	
	public void setItems(List<ItemDto> items){logger.debug("items = " + items); this.items = items;}
	
	//Getters==========================================================================	
	@Override
	public String getId(){return this.id;}
	public String getCoverpicuri(){return this.coverpicuri;}
	//public Outfit getOutfit(){return this.outfit;}
	public Picture getPicture(){return this.picture;}
	public List<ItemDto> getItems(){return this.items;}
}
