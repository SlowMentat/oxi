package oxi.models.projection;

import oxi.models.*;
import oxi.models.dto.*;
import oxi.models.projection.*;
import java.lang.*;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.hateoas.Identifiable;

//@Projection(name="profileProjection", types = {Profile.class})
public interface ProfileProjection extends Serializable, Identifiable<String>
{
	//Getters
	public String getCountry();
	public String getDateOfBirth();
	public String getBodyShape();
	public boolean getMens();
	public boolean getWomens();
	public float getHeight();
	public float getNeck();
	public float getFullShoulder();
	public float getHalfShoulder();
	public float getChest();
	public float getWaist();
	public float getHip();
	public float getSleeve();
	public float getFrontLength();
	public float getBackLength();
	public float getPantOutseam();
	public float getPantInseam();
	public float getThigh();
	public float getCalf();
	public <T extends OutfitProjection> List<T> getOutfits();
	public <T extends ItemProjection> List<T> getItems();
	public <T extends UserProjection> T getUser();

	//Setters
	public void setId(String id);
	public void setCountry(String country);
	public void setDateOfBirth(String dateOfBirth);
	public void setBodyShape(String bodyShape);
	public void setMens(boolean mens);
	public void setWomens(boolean womens);
	public void setHeight(float height);
	public void setNeck(float neck);
	public void setFullShoulder(float fullShoulder);
	public void setHalfShoulder(float halfShoulder);
	public void setChest(float chest);
	public void setWaist(float waist);
	public void setHip(float hip);
	public void setSleeve(float sleeve);
	public void setFrontLength(float frontLength);
	public void setBackLength(float backLength);
	public void setPantOutseam(float pantOutseam);
	public void setPantInseam(float pantInseam);
	public void setThigh(float thigh);
	public void setCalf(float calf);
	public <T extends OutfitProjection> void setOutfits(List<T> outfits);
	public <T extends ItemProjection> void setItems(List<T> items);
	public <T extends UserProjection> void setUser(T user);
}