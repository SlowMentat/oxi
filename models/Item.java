package oxi.models;

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
	//@JsonProperty("id")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;
	/*private Float positionx;
	private Float positiony;*/
	//private String link;
	private String type;

	/*@ManyToOne(cascade=CascadeType.ALL)
	@RestResource(rel="vendor_1")
	@JsonProperty("size")
	@JsonIdentityReference(alwaysAsId=true)	
	@JsonBackReference*/
	private String size;
	
	/*@ManyToOne(cascade=CascadeType.ALL)
	@RestResource(rel="vendor_1")
	@JsonProperty("brand")
	@JsonIdentityReference(alwaysAsId=true)	*/
	//@JsonBackReference
	@Column(name="brand_id", columnDefinition = "BINARY(16)")
	private UUID brand;
	@Column(name = "brand_id_text", updatable = false, insertable = false)
	private String brandText;

	/*@ManyToOne(cascade=CascadeType.ALL)
	@RestResource(rel="vendor_1")
	@JsonProperty("retailer")
	@JsonIdentityReference(alwaysAsId=true)	*/
	//@JsonBackReference
	@Column(name="retailer_id", columnDefinition = "BINARY(16)")
	private UUID retailer;
	@Column(name = "retailer_id_text", updatable = false, insertable = false)
	private String retailerText;

	//@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval= true, mappedBy = "item")
	@RestResource(rel="client_0")
	@JsonIdentityReference(alwaysAsId=true)
	/*@JoinTable(
		name="item_content",
		joinColumns=@JoinColumn(name="item_id", referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="content_id", referencedColumnName="id")
	)*/
	private List<ItemContent> contents = new ArrayList<>();
	
	@ManyToOne(cascade=CascadeType.ALL)
	@RestResource(rel="client_1")
	@JsonIdentityReference(alwaysAsId=true)
	private Profile profile;
	
	//Constructor
	public Item(){

	}

	public Item(UUID id, String type, String size, UUID retailer, UUID brand){
		super();
		this.id = id;
		/*this.positionx = positionx;
		this.positiony = positiony;*/
		this.type = type;
		this.size = size;
		this.retailer = retailer;
		this.brand = brand;
	}
	
	//Setters
	//@Override
	public void setId(UUID id){this.id = id;}
	//public void setIdText(String idText){this.idText = idText;}
	/*public void setPositionx(Float posx){this.positionx = posx;}
	public void setPositiony(Float posy){this.positiony = posy;}*/
	//public void setLink(String link){this.link = link;}	
	public void setType(String type){this.type = type;}
	public void setSize(String size){this.size = size;}
	public void setBrand(UUID brand){this.brand = brand;}
	public void setRetailer(UUID retailer){this.retailer = retailer;}	
	public void setProfile(Profile profile){
		this.profile = profile;
	}
	
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
	public String getIdText(){return this.idText;}
	/*public Float getPositionx(){return this.positionx;}
	public Float getPositiony(){return this.positiony;}*/
	//public String getLink(){return this.link;}
	public String getSize(){return this.size;}
	public UUID getBrand(){return this.brand;}
	public String getBrandText(){return this.brandText;}
	public UUID getRetailer(){return this.retailer;}
	public String getRetailerText(){return this.retailerText;}
	public String getType(){return this.type;}
	public List<ItemContent> getContents(){return this.contents;}
	public Profile getProfile(){return this.profile;}
	
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
			.append("\ntype:").append(this.type)
			.append("\nsize:").append(this.size);
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