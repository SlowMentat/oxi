package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;


@Relation(value = "item", collectionRelation = "items")
public class ItemDto extends ResourceSupport
{
	//private Long id;
	private Long positionx;
	private Long positiony;
	private String link;
	private String type;
	private String size;

	public ItemDto(){
		super();
	}

	//Setters
	//public void setId(Long id){this.Id = id;}
	public void getPositionx(Long posx){this.positionx = posx;}
	public void getPositiony(Long posy){this.positiony = posy;}
	public void setLink(String link){this.link = link;}	
	public void setType(String type){this.type = type;}	
	public void setSize(String size){this.size = size;}	
	//public void setProfile(Profile profile){this.profile = profile;}	
	//public void setContents(List<Content> contents){this.contents = contents;}
	
	//Getters
	//public Long getId(){return this.Id;}
	public Long getPositionx(){return this.positionx;}
	public Long getPositiony(){return this.positiony;}
	public String getLink(){return this.link;}	
	public String getType(){return this.type;}	
	public String getSize(){return this.size;}
	//public List<Content> getContents(){return this.contents;}
	//public Profile getProfile(){return this.profile;}
}
