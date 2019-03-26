package oxi.models.dto.retailer;

import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.dto.PictureDto;
import oxi.models.dto.retailer.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ProductDto.class)
public class ProductDto implements Serializable, Identifiable<String>
{
	@JsonProperty("id")
	private String id;
	private String productId;
	private String type;
	private String link;
	private String sizeGroupId;
	private PictureDto pictureDto;
	private boolean isRetailPicture;
	private boolean isActive;
	private SizeChartDto sizeChartDto;

	public ProductDto(){}

	public ProductDto(String id, String productId, String types, String link, PictureDto pictureDto, boolean isRetailPicture, boolean isActive, SizeChartDto sizeChartDto){
		//super();
		this.id = id;
		this.productId = productId;
		this.type = type;
		this.link = link;
		this.pictureDto = pictureDto;
		this.isRetailPicture = isRetailPicture;
		this.isActive = isActive;
		this.sizeChartDto = sizeChartDto;
	}

	public ProductDto(Item item){
		this.id = item.getIdText() != null ? item.getIdText() : null;
		this.productId = item.getProductId();
		this.sizeGroupId = item.getSizeGroupIdText();
		this.link = item.getLink();
		this.sizeChartDto = item.getSizeChart() != null ? new SizeChartDto(item.getSizeChart()) : null;
		this.type = item.getType();
		this.pictureDto = item.getPicture() != null ? new PictureDto(item.getPicture()) : null;
		this.isRetailPicture = item.getIsRetailPicture();
		this.isActive = item.getIsActive();
	}

	//Setters
	public void setId(String id){this.id = id;}
	public void setProductId(String productId){this.productId = productId;}	
	public void setType(String type){this.type = type;}	
	public void setLink(String link){this.link = link;}
	public void setSizeGroupId(String sizeGroupId){this.sizeGroupId = sizeGroupId;}
	public void setPictureDto(PictureDto pictureDto){this.pictureDto = pictureDto;}
	public void setIsRetailPicture(boolean isRetailPicture){this.isRetailPicture = isRetailPicture;}
	public void setIsActive(boolean isActive){this.isActive = isActive;}
	public void setSizeChartDto(SizeChartDto sizeChartDto){this.sizeChartDto = sizeChartDto;}

	//Getters
	@Override
	public String getId(){return this.id;}
	public String getProductId(){return this.productId;}	
	public String getType(){return this.type;}	
	public String getLink(){return this.link;}
	public String getSizeGroupId(){return this.sizeGroupId;}
	public PictureDto getPictureDto(){return this.pictureDto;}
	public boolean getIsRetailPicture(){return this.isRetailPicture;}
	public boolean getIsActive(){return this.isActive;}
	public SizeChartDto getSizeChartDto(){return this.sizeChartDto;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id))
			.append(indent).append("productId:").append(((this.productId == null) ? "null" : this.productId))
			.append(indent).append("type:").append(((this.type == null) ? "null" : this.type))
			.append(indent).append("link:").append(((this.link == null) ? "null" : this.link))
			.append(indent).append("sizeGroupId:").append(((this.sizeGroupId == null) ? "null" : this.sizeGroupId))
			.append(indent).append("isRetailPicture:").append(this.isRetailPicture)
			.append(indent).append("isActive:").append(this.isActive)
			.append(indent).append("pictureDto:").append(((this.pictureDto == null) ? "null" : this.pictureDto.toString(indents+1)))
			.append(indent).append("sizeChartDto:").append(((this.sizeChartDto == null) ? "null" : this.sizeChartDto.toString(indents+1)));
        return sb.toString();		
	}
}
