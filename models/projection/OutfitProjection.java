package oxi.models.projection;

import oxi.models.*;
import oxi.models.dto.ContentDto;
import oxi.models.projection.ContentProjection;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.hateoas.Identifiable;

@Projection(name="outfitProjection", types = {Outfit.class})
public interface OutfitProjection /*extends Identifiable<Long>*/{
	//Getters
	public Long getId();
	public int getLikes();
	public String getComments();
	//Profile getProfile();
	public List<ContentDto> getContents();
	public String getCoverpicuri();

	//Setters
	/*
	public void setId(Long id);
	public void setLikes(int likes);
	public void setComments(String comments);
	public void setContents(List<Content> contents);
	public void setCoverpicuri(String coverpicuri);
	*/
	//Getters
	//public Outfit getOutfit();
}