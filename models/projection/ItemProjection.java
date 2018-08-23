package oxi.models.projection;

import oxi.models.projection.ContentProjection;
import oxi.models.projection.ProfileProjection;
import java.lang.*;
import java.util.List;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;



public interface ItemDto extends Serializable, Identifiable<String>
{
	public String setId(String id);
	public void getPositionx(Long posx);
	public void getPositiony(Long posy);
	public void setLink(String link);	
	public void setType(String type);	
	public void setSize(String size);	
	public void setProfile(ProfileProjection profile);
	public void setContents(List<? extends ContentProjection> contents);
	
	//Getters
	public Long getPositionx();
	public Long getPositiony();
	public String getLink();	
	public String getType();	
	public String getSize();
	public List<? extends ContentProjection> getContents();
	public ProfileProjection getProfile();
}
