package oxi.models.projection;

import oxi.models.*;
import oxi.models.projection.ContentProjection;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.data.rest.core.config.Projection;


@Projection(name="outfitProjection", types = {Outfit.class})
public interface OutfitProjection{
	//Getters
	public Long getId();
	public int getLikes();
	public String getComments();
	//Profile getProfile();
	public List<ContentProjection> getContents();
	public String getCoverpicuri();
}