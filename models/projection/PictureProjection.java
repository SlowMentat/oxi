package oxi.models.projection;

import oxi.models.projection.ContentProjection;
import java.lang.*;
import java.io.Serializable;
import org.springframework.data.rest.core.config.Projection;

//@Projection(name="outfitProjection", types = {Outfit.class})
public interface OutfitProjection extends Serializable, Identifiable<String>
{
	//Getters
	public String getSmalluri();
	public String getLargeuri();
	public <T extends ContentProjection> T getContent();
	
	//Setters
	public void setId(String id);
	public void setSmalluri(String smalluri);
	public void setLargeuri(String largeuri);
	public <T extends ContentProjection> void setContent(T content);
}