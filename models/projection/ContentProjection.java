package oxi.models.projection;

import oxi.models.projection.ItemProjection;
import oxi.models.projection.PictureProjection;
import oxi.models.projection.OutfitProjection;
import java.lang.*;
import java.util.List;
import java.io.Serializable;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.hateoas.Identifiable;


//@Projection(name="contentProjection", types = {Content.class})
public interface ContentProjection extends Serializable, Identifiable<String>
{
	public <T extends OutfitProjection> T getOutfit();
	public <T extends PictureProjection> T getPicture();
	public <T extends ItemProjection> List<T> getItems();

	public void setCoverpicuri(String coverpicuri);
	public <T extends OutfitProjection> void setOutfit(T outfit);
	public <T extends PictureProjection> void setPicture(T picture);
	public <T extends ItemProjection> void setItems(List<T> items);
}