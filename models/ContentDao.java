package oxi.models;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;

public interface ContentDao extends Serializable, Identifiable<UUID>
{
	
	//Setters==========================================================================	
	public void setId(UUID id);
	public void setCoverpicuri(String uri);
	public <T extends OutfitDao> void setOutfit(T outfit);
	public <T extends PictureDao> void setPicture(T picture);
	public <T extends ItemDao> void setItems(List<T> items);
	public <T extends ItemDao> void addItem(T item);
	
	//Getters==========================================================================	
	@Override
	public UUID getId();
	public String getIdText();
	public String getCoverpicuri();
	public <T extends OutfitDao> T getOutfit();
	public <T extends PictureDao> T getPicture();	
	public <T extends ItemDao> List<T> getItems();
}