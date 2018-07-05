package oxi.models.projection;

import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.data.rest.core.config.Projection;


@Projection(name="contentProjection", types = {Content.class})
public interface ContentProjection{
	public Long getId();
	public String getCoverpicuri();
	//public Outfit getOutfit();
	public Picture getPicture();
	public List<Item> getItems();
}