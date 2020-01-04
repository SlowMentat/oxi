package oxi.models;

import oxi.models.retailer.*;
import oxi.models.dto.ItemDto;
import oxi.models.dto.retailer.*; 

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.Objects;
import java.util.List;
import java.time.Instant;
import java.util.Date;
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

	@Column(name = "size_group_id_text", columnDefinition="VARCHAR(36)",  updatable = false, insertable = false)
	private String sizeGroupIdText;

	@Column(columnDefinition = "BINARY(16)", name = "outfit_id", nullable=true)
	private UUID outfitId;	

	@Column(name = "outfit_id_text", columnDefinition="VARCHAR(36)",  updatable = false, insertable = false)
	private String outfitIdText;

	@Column(name = "is_active", nullable=false, columnDefinition="BOOLEAN default true")
	private boolean isActive;

	@Column(name = "created_on", nullable=false)
	private Date createdOn = new Date();

	@Column(name = "updated_on", nullable=true)
	//private Instant updatedOn = createdOn;
	private Date updatedOn = null;

	//@Column(name = "udr",  columnDefinition="VARCHAR(36)")
	//private String userDefinedRetailer;
//
	//@Column(name = "uds",  columnDefinition="VARCHAR(12)")
	//private String userDefinedSize;

	@OneToMany(/*cascade = CascadeType.ALL,*/ orphanRemoval= true, mappedBy = "item")
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
		this.apparelType = apparelType == null ? 8 : apparelType;
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
		this.apparelType = apparelType == null ? 8 : apparelType;
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


		this.id = (itemDto.getId() == null || itemDto.getId().isEmpty()) ? null : UUID.fromString(itemDto.getId());
		this.apparelType = (itemDto.getApparelType() == null) ? new Integer(8) : itemDto.getApparelType(); //unknown apparel_type




		//this.productId = itemDto.getProductId();
		//this.positionx = itemDto.getPositionx();
		//this.positiony = itemDto.getPositiony();
		//this.apparelType = itemDto.getApparelType();
		//this.userDefinedSize = itemDto.getUserDefinedSize();
		//this.userDefinedRetailer = itemDto.getUserDefinedRetailer();
		this.product = itemDto.getProduct();
		this.platform = itemDto.getPlatform();
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
	public void setApparelType(Integer apparelType){this.apparelType = apparelType;}
	public void setSizeGroupId(UUID sizeGroupId){this.sizeGroupId = sizeGroupId;}
	public void setPicture(Picture picture){this.picture = (Picture)this.setManyToOneParent(picture, this.picture, this);}	
	public void setSizeChart(SizeChart sizeChart){this.sizeChart = sizeChart;}
	public void setSizeChartId(UUID sizeChartId){this.sizeChartId = sizeChartId;}
	public void setRetailerAccount(RetailerAccount retailerAccount){this.retailerAccount = (RetailerAccount)this.setManyToOneParent(retailerAccount, this.retailerAccount, this);}
	public void setIsActive(boolean isActive){this.isActive = isActive;}
	//public void setUpdatedOn(){this.updatedOn = Instant.now();}
	public void setUpdatedOn(Date updatedOn){this.updatedOn = updatedOn;}
	public void setOutfitId(UUID outfitId){this.outfitId = outfitId;}
	//public void setUserDefinedSize(String uds){this.userDefinedSize = uds;}
	//public void setUserDefinedRetailer(String udr){this.userDefinedRetailer = udr;}
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
	public String getIdText(){return this.idText;}
	public UUID getSizeGroupId(){return this.sizeGroupId;}
	public String getSizeGroupIdText(){return this.sizeGroupIdText;}
	public SizeChart getSizeChart(){return this.sizeChart;}
	public UUID getSizeChartId(){return this.sizeChartId;}
	public String getSizeChartIdText(){return this.sizeChartIdText;}
	public RetailerAccount getRetailerAccount(){return this.retailerAccount;}
	public Integer getApparelType(){return this.apparelType;}
	public List<ItemContent> getContents(){return this.contents;}
	public Picture getPicture(){return this.picture;}
	public boolean getIsActive(){return this.isActive;}
	//public Instant getUpdatedOn(){return this.updatedOn;}
	public Date getUpdatedOn(){return this.updatedOn;}
	public Date getCreatedOn(){return this.createdOn;}
	public UUID getOutfitId(){return this.outfitId;}
	public String getOutfitIdText(){return this.outfitIdText;}
	//public String getUserDefinedSize(){return this.userDefinedSize;}
	//public String getUserDefinedRetailer(){return this.userDefinedRetailer;}
	
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
			.append("\napparelType:").append(this.apparelType);
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