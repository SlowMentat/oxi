package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.dto.retailer.SizeGroupDto;
import oxi.models.dto.retailer.SizeChartDto;
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
	private Integer apparelType;
	private String sizeGroupId;
	private SizeChartDto sizeChartDto;
	private String coverpicuri;

	@JsonRawValue
	private String product;
	private String platform;

	public ItemDto(){}

	public ItemDto(String id, Float positionx, Float positiony, Integer apparelType, String sizeGroupId, String retailer, String brand, String product){
		//super();
		this.id = id;
		this.positionx = positionx;
		this.positiony = positiony;
		this.apparelType = apparelType;
		this.sizeGroupId = sizeGroupId;
		//this.retailer = retailer;
		//this.brand = brand;
		this.product = product;
	}

	public ItemDto(String id, Float positionx, Float positiony, Integer apparelType, String sizeGroupId, String retailer, String brand, String coverpicuri, String product){
		//super();
		this.id = id;
		this.positionx = positionx;
		this.positiony = positiony;;
		this.sizeGroupId = sizeGroupId;
		this.coverpicuri = coverpicuri;
		this.product = product;
	} 

	public ItemDto(String id, Float positionx, Float positiony, Integer apparelType, String sizeGroupId, SizeChartDto sizeChartDto, String coverpicuri, String product, String platform){
		this.id = id;
		this.positionx = positionx;
		this.positiony = positiony;
		this.apparelType = apparelType;
		this.sizeGroupId = sizeGroupId;
		this.sizeChartDto = sizeChartDto;
		this.coverpicuri = coverpicuri;
		this.product = product;
		this.platform = platform;
	}

	public ItemDto(Item item){
		this.id = item.getIdText();
		this.sizeGroupId = item.getSizeGroupIdText();
		this.sizeChartDto = new SizeChartDto(item.getSizeChart());
		this.apparelType = item.getApparelType();
		this.product = item.getProduct();
		this.platform = item.getPlatform();
	}

	//Setters
	public void setId(String id){this.id = id;}
	public void setPositionx(Float posx){this.positionx = posx;}
	public void setPositiony(Float posy){this.positiony = posy;}
	public void setApparelType(Integer apparelType){this.apparelType = apparelType;}
	public void setSizeGroupId(String sizeGroupId){this.sizeGroupId = sizeGroupId;}
	public void setSizeChartDto(SizeChartDto sizeChartDto){this.sizeChartDto = sizeChartDto;}
	public void setcoverpicuri(String coverpicuri){this.coverpicuri = coverpicuri;}
	public void setProduct(String product){this.product = product;}
	
	//Getters
	@Override
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}
	public Float getPositionx(){return this.positionx;}
	public Float getPositiony(){return this.positiony;}	
	public Integer getApparelType(){return this.apparelType;}
	public String getSizeGroupId(){return this.sizeGroupId;}
	public SizeChartDto getSizeChartDto(){return this.sizeChartDto;}
	public String getcoverpicuri(){return this.coverpicuri;}
	public String getProduct(){return this.product;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id))
			.append(indent).append("positionx: ").append(((this.positionx == null) ? "null" : this.positionx))
			.append(indent).append("positiony:").append(((this.positiony == null) ? "null" : this.positiony))
			.append(indent).append("apparelType:").append(((this.apparelType == null) ? "null" : this.apparelType))
			.append(indent).append("sizeGroupId:").append(((this.sizeGroupId == null) ? "null" : this.sizeGroupId))
			.append(indent).append("sizeChartDto:").append(((this.sizeChartDto == null) ? "null" : this.sizeChartDto.toString(indents + 1)))
			.append(indent).append("coverpicuri:").append(((this.coverpicuri == null) ? "null" : this.coverpicuri))
			.append(indent).append("product : ").append(this.product == null ? "null" : this.product);
        return sb.toString();		
	}
}
