package oxi.models;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.hateoas.*;

import org.hibernate.annotations.GenericGenerator;

public interface Item extends Serializable, Identifiable<UUID>
{
	
	//Setters
	//@Override
	public void setId(UUID id);
	//public void setIdText(String idText);
	public void setLocationx(Long posx);
	public void setLocationy(Long posy);
	public void setLink(String link);	
	public void setType(String type);	
	public void setSize(String size);	
	public <T extends ProfileDao> void setProfile(T profile);
	public <T extends ContentDao> void setContents(List<T> contents);

	//Getters
	public String getIdText();
	public Long getPositionx();
	public Long getPositiony();
	public String getLink();	
	public String getType();	
	public String getSize();
	public <T extends ContentDao> List<T> getContents();
	public <T extends ProfileDao> T getProfile();
}