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
import com.fasterxml.jackson.annotation.*;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ContentDto.class)
public class ContentDto implements Serializable, Identifiable<String>
{
	private static final Logger logger = LogManager.getLogger(ContentDto.class);
	@JsonProperty("id")
	private String id;
	private String coverpicuri;
	private String outfitId;
	private PictureDto picture;
	private List<ItemDto> items;

	public ContentDto(){}

	public ContentDto(String id, String coverpicuri, PictureDto picture, List<ItemDto> items){
		//super();
		this.id = id;
		this.coverpicuri = coverpicuri;
		this.picture = picture;
		this.items = items;
	}

	public ContentDto(String id, String coverpicuri, PictureDto picture, List<ItemDto> items, String outfitId){
		//super();
		this.id = id;
		this.coverpicuri = coverpicuri;
		this.picture = picture;
		this.items = items;
		this.outfitId = outfitId;
	}

	public ContentDto(Content content){
		this.id = content.getIdText();
		this.coverpicuri = content.getCoverpicuri();
		this.outfitId = content.getOutfit().getId().toString();
	}

	//@JsonCreator
	/*public ContentDto(
		@JsonProperty("id")
		String id, 
		@JsonProperty("coverpicuri")
		String coverpicuri, 
		@JsonProperty("picture")
		PictureDto picture, 
		@JsonProperty("items")
		List<ItemDto> items){
		super();
		this.id = id;
		this.coverpicuri = coverpicuri;
		this.picture = picture;
		this.items = items;
	}*/

	//Setters==========================================================================	
	public void setId(String id){this.id = id;}
	public void setCoverpicuri(String uri){this.coverpicuri = uri;}	
	//public void setOutfit(Outfit outfit){this.outfit = outfit;}	
	public void setPicture(PictureDto picture){this.picture = picture;}	
	public void setItems(List<ItemDto> items){logger.debug("items = " + items); this.items = items;}
	public void setOutfitId(String outfitId){this.outfitId = outfitId;}
	
	//Getters==========================================================================	
	@Override
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}
	public String getCoverpicuri(){return this.coverpicuri;}
	//public Outfit getOutfit(){return this.outfit;}
	public PictureDto getPicture(){return this.picture;}
	public List<ItemDto> getItems(){return this.items;}
	public String getOutfitId(){return (this.outfitId == null) ? this.outfitId : this.outfitId.toLowerCase();}

	//@Override 
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Content string");
        StringBuilder sb = new StringBuilder("id: ").append(((this.id == null) ? "null" : this.id))
        	.append(indent).append("outfitId: ").append(((this.outfitId == null) ? "null" : this.outfitId))
			.append(indent).append("coverpicuri: ").append(this.coverpicuri)
			.append(indent).append("items: [");
			if(this.items != null){
				for (ItemDto item: this.items) {
					sb.append(indent).append("{")
						.append(((item == null) ? "null" : item.toString((indents + 1)))).append(", ")
						.append(indent).append("},");
				}
			}
			sb.append(indent).append("]")
				.append(indent).append("picture: {")
				.append(picture.toString(indents + 1)).append("}");
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}
}
