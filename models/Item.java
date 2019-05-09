package oxi.models;

import oxi.models.retailer.*;
import oxi.models.dto.ItemDto;
import oxi.models.dto.retailer.*; 

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.hateoas.*;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Item")
@Table(name = "item")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Item.class)
public class Item extends RelatedEntity implements Serializable, Identifiable<UUID>{
	@Transient
	private static final Logger logger = LogManager.getLogger(Item.class);
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "id_text", columnDefinition="VARCHAR(36)", updatable = false, insertable = false)
	private String idText;

	@Column(name = "platform", columnDefinition="VARCHAR(24)", nullable = false)
	private String platform;
	@Column(name = "product", columnDefinition="JSON")
	private String product;

	@Column(name = "apparel_type",  columnDefinition="INT", nullable = false)
	private Integer apparelType;

	@Column(name="size_chart_id", columnDefinition="BINARY(16)")
	private UUID sizeChartId;

	@Column(name = "size_chart_id_text",  columnDefinition="VARCHAR(36)",  updatable = false, insertable = false)
	private String sizeChartIdText;

	@Transient
	private SizeChart sizeChart;  // must be set by setSizeChart()

	@Column(name= "size_group_id", columnDefinition="BINARY(16)")
	private UUID sizeGroupId;

	@Column(name = "size_group_id_text",  columnDefinition="VARCHAR(36)",  updatable = false, insertable = false)
	private String sizeGroupIdText;

	@Column(name = "is_active", nullable=false, columnDefinition="BOOLEAN default true")
	private boolean isActive;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval= true, mappedBy = "item")
	@RestResource(rel="client_0")	
	@JsonIdentityReference(alwaysAsId=true)
	private List<ItemContent> contents = new ArrayList<>();

	@ManyToOne(cascade=CascadeType.ALL)
	@RestResource(rel="picture")
	@JoinColumn(name="picture_id")
	@JsonIdentityReference(alwaysAsId=true)
	private Picture picture;

	@ManyToOne(cascade=CascadeType.ALL)
	@RestResource(rel="client_1")
	@JsonIdentityReference(alwaysAsId=true)
	@JoinColumn(name = "retailer_account_id")
	private RetailerAccount retailerAccount;

	//Constructor
	public Item(){

	}

	public Item(UUID id, String platform, String product, Integer apparelType, UUID sizeChartId, UUID sizeGroupId, boolean isActive){
		this.id = id;
		this.platform = platform;
		this.product = product;
		this.apparelType = apparelType;
		this.sizeChartId = sizeChartId;
		this.sizeGroupId = sizeGroupId;
		this.isActive = isActive;
	}

	public Item(UUID id, String apparelType, String size, UUID retailer, UUID brand){
		super();
		this.id = id;
		/*this.positionx = positionx;
		this.positiony = positiony;*/
		//this.apparelType = apparelType;
		//this.size = size;
		//this.retailer = retailer;
		//this.brand = brand;
		//this.isRetailPicture = false;
		this.isActive = false;
	}

	public Item(
		UUID id,
		String productId,
		Integer apparelType,
		UUID sizeGroup,
		String link,
		boolean isRetailPicture,
		boolean isActive,
		Picture picture,
		UUID sizeChartId,
		RetailerAccount retailerAccount)
	{
		super();
		this.id = id;
		//this.productId = productId;
		/*this.positionx = positionx;
		this.positiony = positiony;*/
		this.apparelType = apparelType;
		this.sizeGroupId = sizeGroupId;
		//this.link = link;
		//this.isRetailPicture = isRetailPicture;
		this.picture = picture;
		this.isActive = isActive;
		this.sizeChartId = sizeChartId;
		this.retailerAccount = retailerAccount;
	}

	public Item(ItemDto itemDto){		
		super();
		if(itemDto.getId() == null || itemDto.getId().isEmpty()){
			this.id = null;
		}else{
			this.id = UUID.fromString(itemDto.getId());
		}

		//this.productId = itemDto.getProductId();
		/*this.positionx = itemDto.getpositionx;
		this.positiony = itemDto.getpositiony;*/
		//this.apparelType = itemDto.getApparelType();

		//NOTE:  the consumer should never be able to overwrite sizeGroup property

		/*this.sizeGroup = ( itemDto.getSizeGroupDto() == null || itemDto.getSizeGroupDto().getId().isEmpty() ) ? 
			UUID.fromString(itemDto.getSizeGroupDto().getId()) : 
			null;*/
		//this.link = itemDto.getLink();
		//this.picture = itemDto.getPicture();
	}

	public Item(ProductDto productDto){		
		super();
		this.id = UUID.fromString(productDto.getId());
		//this.productId = productDto.getProductId();
		/*this.positionx = productDto.getpositionx;
		this.positiony = productDto.getpositiony;*/
		//this.apparelType = productDto.getApparelType();
		this.sizeGroupId = UUID.fromString(productDto.getSizeGroupId());
		//this.link = productDto.getLink();
		//this.isRetailPicture = productDto.getIsRetailPicture();
		this.picture = new Picture(productDto.getPictureDto());
		this.isActive = productDto.getIsActive();
		this.sizeChart = new SizeChart(productDto.getSizeChartDto());
		this.sizeChartId = UUID.fromString(productDto.getSizeChartDto().getId());
	}
	
	//Setters
	//@Override
	public void setId(UUID id){this.id = id;}
	public void setPlatform(String platform){this.platform = platform;}
	public void setProduct(String product){this.product = product;}
	//public void setProductId(String productId){this.productId = productId;}
	//public void setProductOwner(String productOwner){this.productOwner = productOwner;}
	//public void setIdText(String idText){this.idText = idText;}
	/*public void setPositionx(Float posx){this.positionx = posx;}
	public void setPositiony(Float posy){this.positiony = posy;}*/
	//public void setLink(String link){this.link = link;}	
	public void setApparelType(Integer apparelType){this.apparelType = apparelType;}
	public void setSizeGroupId(UUID sizeGroupId){this.sizeGroupId = sizeGroupId;}
	//public void setBrand(UUID brand){this.brand = brand;}
	//public void setRetailer(UUID retailer){this.retailer = retailer;}	
	//public void setProfile(Profile profile){this.profile = profile;}

	public void setPicture(Picture picture){this.picture = (Picture)this.setManyToOneParent(picture, this.picture, this);}	
	//public void setSizeChart(UUID sizeChartId){this.sizeChartId = (UUID)this.setManyToOneParent(sizeChartId, this.sizeChartId, this);}
	public void setSizeChart(SizeChart sizeChart){this.sizeChart = sizeChart;}
	public void setSizeChartId(UUID sizeChartId){this.sizeChartId = sizeChartId;}
	public void setRetailerAccount(RetailerAccount retailerAccount){this.retailerAccount = (RetailerAccount)this.setManyToOneParent(retailerAccount, this.retailerAccount, this);}

	//public void setIsRetailPicture(boolean isRetailPicture){this.isRetailPicture = isRetailPicture;}
	public void setIsActive(boolean isActive){this.isActive = isActive;}
	/*public void setContents(List<Content> contents){
		//this.contents = (List<Content>)(Object)this.setManyToManyParents(contents, this.contents, this);
		this.contents = this.<Content, Item>setManyToManyParents(contents, this.contents, this);
		//add this Item to profile items list
	}
	
	public void addContent(Content content){
		this.contents.add(content);
		if(!this.contents.contains(content)) content.addItem(this);
	}*/
	

	//Getters
	//@Override
	public UUID getId(){return this.id;}
	public String getPlatform(){return this.platform;}
	public String getProduct(){return this.product;}
	//public String getProductId(){return this.productId;}
	//public String getProductOwner(){return this.productOwner;}
	public String getIdText(){return this.idText;}
	//public String getSize(){return this.size;}
	public UUID getSizeGroupId(){return this.sizeGroupId;}
	public String getSizeGroupIdText(){return this.sizeGroupIdText;}
	//public String getLink(){return this.link;}
	public SizeChart getSizeChart(){return this.sizeChart;}
	public UUID getSizeChartId(){return this.sizeChartId;}
	public String getSizeChartIdText(){return this.sizeChartIdText;}
	public RetailerAccount getRetailerAccount(){return this.retailerAccount;}

	//public UUID getBrand(){return this.brand;}
	//public String getBrandText(){return this.brandText;}
	//public UUID getRetailer(){return this.retailer;}
	//public String getRetailerText(){return this.retailerText;}
	public Integer getApparelType(){return this.apparelType;}
	public List<ItemContent> getContents(){return this.contents;}
	public Picture getPicture(){return this.picture;}
	//public boolean getIsRetailPicture(){return this.isRetailPicture;}
	public boolean getIsActive(){return this.isActive;}
	//public Profile getProfile(){return this.profile;}
	
	@Override 
	public <T extends Relational> void internalAddChild(T targetChild){
		/*if(this.contents == null){
			logger.debug("instantiating new List<T>");
			this.contents = new ArrayList<Content>();
		}
		this.contents.add((Content)targetChild);*/
	}
	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		//this.contents.remove((Content)targetChild);
	}
	
	@Override
	public String toString(){
		logger.debug("building Item string");
        StringBuilder sb = new StringBuilder("\nid: ").append(this.id)
			.append("\ntype:").append(this.apparelType);
			//.append("\nsize:").append(this.size);
		//sb.append("\nprofile: ").append(((profile.getId() == null) ? "null" : profile.getId().toString()));
        return sb.toString();		
	}

	@Override
	public boolean equals(Object object){
		if(this == object) return true;
		if(this == null || getClass() != object.getClass()) return false;
		Item item = (Item) object;
		return Objects.equals(id, item.getId());
	}

	@Override
	public int hashCode(){
		return Objects.hash(id);
	}
}