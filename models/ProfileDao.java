package oxi.models;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.springframework.hateoas.*;
import java.util.UUID;
import java.io.Serializable;


interface ProfileDao extends Serializable, Identifiable<UUID>
{
	public void setId(UUID id);
	public void setUsername(String username);
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
	public void setFrontLength(float FrontLength);
	public void setBackLength(float BackLength);
	public void setPantOutseam(float pantOutseam);
	public void setPantInSeam(float pantInseam);
	public void setThigh(float thigh);
	public void setCalf(float calf);
	//Try to consolidate these methods
	public <T extends OutfitDao> void setOutfits(List<T> outfits);
	public <T extends UserDao> void setUser(T user);
	public <T extends ItemDao> void setItems(List<T> item);
	
	//Getters
	//public UUID getId();
	public String getIdText();
	public String getUsername();
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
	public float getPantInSeam();
	public float getThigh();
	public float getCalf();
	public <T extends OutfitDao> List<T> getOutfits();
	public <T extends UserDao> T getUser();
	public <T extends ItemDao> List<T> getItems();
}