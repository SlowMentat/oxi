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
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.annotation.*;
//import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import lombok.Data;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=OutfitDto.class)
public class OutfitDto implements Serializable, Identifiable<String>
{
	private static final Logger logger = LogManager.getLogger(OutfitDto.class);
	/*private final Outfit outfit;

	public OutfitDto(Outfit outfit){
		//super();
		this.outfit = outfit;
	}

	//Getters
	public Outfit getOutfit(){return this.outfit;}*/


	//private static final Logger logger = LogManager.getLogger(OutfitDto.class);*/
	@JsonProperty("id")
	private String id;
	private int likes;
	private String comments;

	//@JsonIdentityReference(alwaysAsId=true)
	private List<ContentDto> contents;
	private String coverpicuri;

	public OutfitDto(){}

	public OutfitDto(String id, int likes, String comments, List<ContentDto> contents, String coverpicuri){
		//super();
		this.id = id;
		this.likes = likes;
		this.comments = comments;
		this.contents = contents;
		this.coverpicuri = coverpicuri;
	}
		
	/*@JsonCreator
	public OutfitDto(
		@JsonProperty("id")
		String id, 
		@JsonProperty("likes")
		int likes, 
		@JsonProperty("comments")
		String comments, 
		@JsonProperty("contents")
		List<ContentDto> contents, 
		@JsonProperty("coverpicuri")
		String coverpicuri){
		super();
		this.id = id;
		this.likes = likes;
		this.comments = comments;
		this.contents = contents;
		this.coverpicuri = coverpicuri;
	}*/


	//Getters
	@Override
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}
	public int getLikes(){return this.likes;}
	public String getComments(){return this.comments;}
	//Profile getProfile();
	public List<ContentDto> getContents(){return this.contents;}
	public String getCoverpicuri(){return this.coverpicuri;}

	//Setters
	public void setId(String id){this.id = id;}
	public void setLikes(int likes){this.likes = likes;}
	public void setComments(String comments){this.comments = comments;}
	public void setContents(List<ContentDto> contents){logger.debug("contents[] = " + contents); this.contents = contents;}
	public void setCoverpicuri(String coverpicuri){this.coverpicuri = coverpicuri;}


	public String toString(int indents) {
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : id))
			.append(indent).append("likes: ").append(this.likes)
			.append(indent).append("comments:").append(this.comments)
			.append(indent).append("coverpic:").append(this.coverpicuri)
			.append(indent).append("contents:")
			.append(indent).append("[");
			for (ContentDto content: this.contents) {
				sb.append(indent).append("{")
				.append(((content == null) ? "null" : content.toString(indents + 1)))
				.append(indent).append("}, ");
			}
			sb.append(indent).append("]");
		
		return sb.toString();
	}	

    @Override
    public String toString(){
    	return toString(0);
    }
}
