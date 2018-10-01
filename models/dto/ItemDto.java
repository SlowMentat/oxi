package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;


public class ItemDto implements Serializable, Identifiable<String>
{
	private String id;
	private Float positionx;
	private Float positiony;
	private String type;
	private String size;
	private String retailer;
	private String brand;

	public ItemDto(String id, Float positionx, Float positiony, String type, String size, String retailer, String brand){
		super();
		this.id = id;
		this.positionx = positionx;
		this.positiony = positiony;
		this.type = type;
		this.size = size;
		this.retailer = retailer;
		this.brand = brand;
	}

	//Setters
	public void setId(String id){this.id = id;}
	public void getPositionx(Float posx){this.positionx = posx;}
	public void getPositiony(Float posy){this.positiony = posy;}
	public void setType(String type){this.type = type;}	
	public void setSize(String size){this.size = size;}	
	public void setRetailer(String retailer){this.retailer = retailer;}
	public void setBrand(String brand){this.brand = brand;}
	//public void setProfile(Profile profile){this.profile = profile;}	
	//public void setContents(List<Content> contents){this.contents = contents;}
	
	//Getters
	@Override
	public String getId(){return this.id;}
	public Float getPositionx(){return this.positionx;}
	public Float getPositiony(){return this.positiony;}	
	public String getType(){return this.type;}	
	public String getSize(){return this.size;}
	public String getRetailer(){return this.retailer;}
	public String getBrand(){return this.brand;}
	//public List<Content> getContents(){return this.contents;}
	//public Profile getProfile(){return this.profile;}
}
