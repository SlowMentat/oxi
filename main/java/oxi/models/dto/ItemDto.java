package oxi.models.dto;

//import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.dto.retailer.SizeGroupDto;
import oxi.models.dto.retailer.SizeChartDto;
import java.lang.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.server.core.*;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ItemDto.class)
public class ItemDto implements Serializable/*Identifiable<String>*/
{
	@JsonProperty("id")
	private String id;
	private Float positionx;
	private Float positiony;
	private Integer apparelType;
	private String sizeGroupId;
	private SizeChartDto sizeChartDto;

	//private String userDefinedRetailer;
	//private String userDefinedSize;

	@JsonRawValue
	private String product;
	private String platform;
	private String outfitId;
	private String coverpicuri;	
	private String pictureId;

	@JsonIgnoreType
	@JsonIgnoreProperties(ignoreUnknown = true)
	static public class Product{
		private String udr = "";
		private String uds = "";
		private String handle = "";
		private String onlineStoreUrl = "";
		//private float rotation = 0;

		public Product(){
		}

		public static Product build(String productString) throws Exception{
			ObjectMapper mapper = null;

			try{
				mapper = new ObjectMapper();
			}
			catch(Exception e){
				//throw new Exception("Error creating profile pciture");
			}

			return mapper.readValue(productString, Product.class);
		}

		public String getUdr(){return this.udr;}
		public String getUds(){return this.uds;}
		public String getHandle(){return this.handle;}
		public String getOnlineStoreUrl(){return this.onlineStoreUrl;}

		public void setUdr(String udr){this.udr = udr;}
		public void setUds(String uds){this.uds = uds;}
		public void setHandle(String handle){this.handle = handle;}
		public void setOnlineStoreUrl(String onlineStoreUrl){this.onlineStoreUrl = onlineStoreUrl;}
	}

	public ItemDto(){}

	public ItemDto(String id, Float positionx, Float positiony, Integer apparelType, String sizeGroupId, String retailer, String brand, String product){
		//super();
		this.id = id;
		this.positionx = positionx;
		this.positiony = positiony;
		this.apparelType = apparelType == null ? new Integer(8) : apparelType;
		this.sizeGroupId = sizeGroupId;
		//this.retailer = retailer;
		//this.brand = brand;
		this.product = product;
	}

	public ItemDto(String id, Float positionx, Float positiony, Integer apparelType, String sizeGroupId, String retailer, String brand, String coverpicuri, String product){
		//super();
		this.id = id;
		this.positionx = positionx;
		this.positiony = positiony;
		this.apparelType = apparelType == null ? new Integer(8) : apparelType;
		this.sizeGroupId = sizeGroupId;
		this.coverpicuri = coverpicuri;
		this.product = product;
	} 

	public ItemDto(String id, Float positionx, Float positiony, Integer apparelType, String sizeGroupId, SizeChartDto sizeChartDto, String coverpicuri, String product, String platform){
		this.id = id;
		this.positionx = positionx;
		this.positiony = positiony;
		this.apparelType = apparelType == null ? new Integer(8) : apparelType;
		this.sizeGroupId = sizeGroupId;
		this.sizeChartDto = sizeChartDto;
		this.coverpicuri = coverpicuri;
		this.product = product;
		this.platform = platform;
	}

	public ItemDto(Item item){
		this.id = item.getId().toString();
		this.sizeGroupId = item.getSizeGroupIdText();
		this.sizeChartDto = item.getSizeChart() != null ? new SizeChartDto(item.getSizeChart()) : null;
		this.apparelType = item.getApparelType() == null ? new Integer(8) : item.getApparelType();
		this.product = item.getProduct();
		this.platform = item.getPlatform();
		this.coverpicuri = item.getPicture() != null ? item.getPicture().getMediumuri() : null;
		this.pictureId = item.getPicture() != null ? item.getPicture().getId().toString() : null;
	}

	public ItemDto(Item item, String coverpicuri){
		this.id = item.getId().toString();
		this.sizeGroupId = item.getSizeGroupIdText();
		this.sizeChartDto = item.getSizeChart() != null ? new SizeChartDto(item.getSizeChart()) : null;
		this.apparelType = item.getApparelType() == null ? new Integer(8) : item.getApparelType();
		this.product = item.getProduct();
		this.platform = item.getPlatform();
		//this.coverpicuri = coverpicuri;
		this.coverpicuri = item.getPicture() != null ? item.getPicture().getMediumuri() : null;
		this.pictureId = item.getPicture() != null ? item.getPicture().getId().toString() : null;
	}

	public ItemDto(ItemContent itemContent){
		if(itemContent == null) return;		
		Item item = itemContent.getItem();
		if(item == null) return;

		this.id = item.getId().toString();
		this.sizeGroupId = item.getSizeGroupIdText();
		this.sizeChartDto = item.getSizeChart() != null ? new SizeChartDto(item.getSizeChart()) : null;
		this.apparelType = item.getApparelType() == null ? new Integer(8) : item.getApparelType();
		this.product = item.getProduct();
		this.platform = item.getPlatform();
		this.positionx = itemContent.getPositionx();
		this.positiony = itemContent.getPositiony();
		this.coverpicuri = item.getPicture() != null ? item.getPicture().getMediumuri() : null;
		this.pictureId = item.getPicture().getId().toString();
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
	public void setPlatform(String platform){this.platform = platform;}
	public void setOutfitId(String outfitId){this.outfitId = outfitId;}
	public void setPictureId(String pictureId){this.pictureId = pictureId;}
	//public void setUserDefinedSize(String uds){this.userDefinedSize = uds;}
	//public void setUserDefinedRetailer(String udr){this.userDefinedRetailer = udr;}
	
	//Getters
	
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}
	public Float getPositionx(){return this.positionx;}
	public Float getPositiony(){return this.positiony;}	
	public Integer getApparelType(){return this.apparelType;}
	public String getSizeGroupId(){return this.sizeGroupId;}
	public SizeChartDto getSizeChartDto(){return this.sizeChartDto;}
	public String getcoverpicuri(){return this.coverpicuri;}
	public String getProduct(){return this.product;}
	public String getPlatform(){return this.platform;}
	public String getOutfitId(){return this.outfitId;}
	public String getPictureId(){return this.pictureId;}

	//public String getUserDefinedSize(){return this.userDefinedSize;}
	//public String getUserDefinedRetailer(){return this.userDefinedRetailer;}

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
			//.append(indent).append("udr : ").append(this.userDefinedRetailer == null ? "null" : this.userDefinedRetailer)
			//.append(indent).append("uds : ").append(this.userDefinedSize == null ? "null" : this.userDefinedSize)
			.append(indent).append("product : ").append(this.product == null ? "null" : this.product)
			.append(indent).append("pictureId : ").append(this.pictureId == null ? "null" : this.pictureId)
			.append(indent).append("outfitId : ").append(this.outfitId == null ? "null" : this.outfitId);
        return sb.toString();		
	}
}
