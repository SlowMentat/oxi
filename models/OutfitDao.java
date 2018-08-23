package oxi.models;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;

public interface OutfitDao extends Serializable, Identifiable<UUID>
{
	//Setters
	public void setId(UUID id);
	//public void setIdText(String idText);
	public void setLikes(int likes);		
	public void setComments(String comments);	
	public void setCoverpicuri(String uri);	
	//set the binary and text profile ids and assures that changes are propogated to all child entity objects
	public <T extends ProfileDao> void setProfile(T profile);
	public <T extends CotnentDao> void setContents(List<T> contents);
	
	//Getters
	public String getIdText();
	public int getLikes();	
	public String getComments();	
	public <T extends ProfileDao> T getProfile();
	public <T extends CotnentDao> List<T> getContents();
	public String getCoverpicuri();
}