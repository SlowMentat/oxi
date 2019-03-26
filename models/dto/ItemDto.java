package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.dto.retailer.SizeGroupDto;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ItemDto.class)
public class ItemDto implements Serializable, Identifiable<String>
{
	@JsonProperty("id")
	private String id;
	private Float positionx;
	private Float positiony;
	private String type;
	private String link;
	//private String size;
	private SizeGroupDto sizeGroupDto;
	private String retailer;
	private String brand;
	private String coverpicuri;

	public ItemDto(){}

	public ItemDto(String id, Float positionx, Float positiony, String type, SizeGroupDto sizeGroupDto, String retailer, String brand){
		//super();
		this.id = id;
		this.positionx = positionx;
		this.positiony = positiony;
		this.type = type;
		this.sizeGroupDto = sizeGroupDto;
		this.retailer = retailer;
		this.brand = brand;
	}

	public ItemDto(String id, Float positionx, Float positiony, String type, SizeGroupDto sizeGroupDto, String retailer, String brand, String coverpicuri){
		//super();
		this.id = id;
		this.positionx = positionx;
		this.positiony = positiony;
		this.type = type;
		this.sizeGroupDto = sizeGroupDto;
		this.retailer = retailer;
		this.brand = brand;
		this.coverpicuri = coverpicuri;
	}

	public ItemDto(Item item){
		this.id = item.getIdText();
		this.type = item.getType();
		this.link = item.getLink();
		this.sizeGroupDto = new SizeGroupDto(item.getSizeGroupIdText());
		//TODO:  rediesign retailer and brand entities to accomodate for Retailer entities
		this.retailer = item.getRetailerText();
		this.brand = item.getBrandText();
		//this.coverpicuri = item.getCoverpicuri();
	}
	//Had to add jackson annotations for calling controller with ArrayList<ItemDto> typed parameter
	/*@JsonCreator
	public ItemDto(
		@JsonProperty("id")
		String id, 
		@JsonProperty("positionx")
		Float positionx, 
		@JsonProperty("positiony")
		Float positiony, 
		@JsonProperty("type")
		String type, 
		@JsonProperty("size")
		String size, 
		@JsonProperty("retailer")
		String retailer, 
		@JsonProperty("brand")
		String brand){
		//super();
		this.id = id;
		this.positionx = positionx;
		this.positiony = positiony;
		this.type = type;
		this.size = size;
		this.retailer = retailer;
		this.brand = brand;
	}*/

	//Setters
	public void setId(String id){this.id = id;}
	public void setPositionx(Float posx){this.positionx = posx;}
	public void setPositiony(Float posy){this.positiony = posy;}
	public void setType(String type){this.type = type;}	
	public void setLink(String link){this.link = link;}
	public void setSizeGroupDto(SizeGroupDto sizeGroupDto){this.sizeGroupDto = sizeGroupDto;}
	public void setRetailer(String retailer){this.retailer = retailer;}
	public void setBrand(String brand){this.brand = brand;}
	public void setcoverpicuri(String coverpicuri){this.coverpicuri = coverpicuri;}
	//public void setProfile(Profile profile){this.profile = profile;}	
	//public void setContents(List<Content> contents){this.contents = contents;}
	
	//Getters
	@Override
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}
	public Float getPositionx(){return this.positionx;}
	public Float getPositiony(){return this.positiony;}	
	public String getType(){return this.type;}	
	public String getLink(){return this.link;}
	public SizeGroupDto getSizeGroupDto(){return this.sizeGroupDto;}
	public String getRetailer(){return (this.retailer == null) ? this.retailer : this.retailer.toLowerCase();}
	public String getBrand(){return (this.brand == null) ? this.brand : this.brand.toLowerCase();}
	public String getcoverpicuri(){return this.coverpicuri;}
	//public List<Content> getContents(){return this.contents;}
	//public Profile getProfile(){return this.profile;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id))
			.append(indent).append("positionx: ").append(((this.positionx == null) ? "null" : this.positionx))
			.append(indent).append("positiony:").append(((this.positiony == null) ? "null" : this.positiony))
			.append(indent).append("type:").append(((this.type == null) ? "null" : this.type))
			.append(indent).append("sizeGroup:").append(((this.sizeGroupDto == null) ? "null" : this.sizeGroupDto.toString(indents + 1)))
			.append(indent).append("retailer:").append(((this.retailer == null) ? "null" : this.retailer))
			.append(indent).append("brand:").append(((this.brand == null) ? "null" : this.brand))
			.append(indent).append("coverpicuri:").append(((this.coverpicuri == null) ? "null" : this.coverpicuri));
        return sb.toString();		
	}
}
