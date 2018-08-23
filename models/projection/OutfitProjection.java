package oxi.models.projection;

import oxi.models.*;
import oxi.models.projection.ContentProjection;
import java.lang.*;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.hateoas.Identifiable;

//@Projection(name="outfitProjection", types = {Outfit.class})
public interface OutfitProjection extends Serializable, Identifiable<String>
{
	//Getters
	public int getLikes();
	public String getComments();
	//Profile getProfile();
	public <T extends ContentProjection> List<T> getContents();
	public String getCoverpicuri();

	//Setters
	public void setId(String id);
	public void setLikes(int likes);
	public void setComments(String comments);
	public <T extends ContentProjection> void setContents(List<T> contents);
	public void setCoverpicuri(String coverpicuri);
	//Getters
	//public Outfit getOutfit();
}