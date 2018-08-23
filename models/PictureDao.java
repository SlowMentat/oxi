package oxi.models;

import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;

public interface PictureDo extends Serializable, Identifiable<UUID>
{	
	//Getters
	//@Override
	public String getIdText();
	public String getSmalluri();
	public String getLargeuri();
	public <T extends ContentDao> T getContent();
	
	//Setters
	//@Override
	public void setId(UUID id);
	//public void setIdText(String idText);
	public void setSmalluri(String smalluri);
	public void setLargeuri(String largeuri);
	public <T extends ContentDao> void setContent(T content);
	
}