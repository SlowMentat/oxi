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

@Relation(value = "outfit", collectionRelation = "outfits")
public class OutfitDto extends ResourceSupport
{
	private static final Logger logger = LogManager.getLogger(OutfitDto.class);
	//private Long id;
	private int likes;
	private String comments;
	private List<Content> contents;
	private String coverpicuri;

	public OutfitDto(){
		super();
	}
	//Getters
	//public Long getId(){return this.id;}
	public int getLikes(){return this.likes;}
	public String getComments(){return this.comments;}
	//Profile getProfile();
	public List<Content> getContents(){return this.contents;}
	public String getCoverpicuri(){return this.coverpicuri;}

	//Setters
	//public void setId(Long id){this.id = id;}
	public void setLikes(int likes){this.likes = likes;}
	public void setComments(String comments){this.comments = comments;}
	public void setContents(List<Content> contents){logger.debug("contents[] = " + contents); this.contents = contents;}
	public void setCoverpicuri(String coverpicuri){this.coverpicuri = coverpicuri;}
}
