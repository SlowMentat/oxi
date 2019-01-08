package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.Objects;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators.*;
import org.springframework.hateoas.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.hibernate.annotations.GenericGenerator;

import oxi.models.projection.ContentProjection;

@Entity
@Table(name="content")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="content_id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Content.class)
public class Content extends RelatedEntity implements Serializable, Identifiable<UUID>{
	@Transient
	private static final Logger logger = LogManager.getLogger(Content.class);

	@Id
	@JsonProperty("id")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;	
	private String coverpicuri;
	
	@ManyToOne(cascade=CascadeType.ALL)
	//@JoinColumn(name="outfit_id")
	@RestResource(rel="outfit")
	@JoinColumn(name="outfit_id")
	@JsonIdentityReference(alwaysAsId=true)
	private Outfit outfit;	
	
	//@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, mappedBy="contents")	//Item entity is the owner
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, mappedBy = "content")
	//@MapKeyColumn(name="clothing_type")
	@RestResource(rel="vendor_0")
	@JsonProperty("items")
	@JsonIdentityReference(alwaysAsId=true)
	//private Map<String, Item> items;*
	private List<ItemContent> items = new ArrayList<>();
	
	@OneToOne(cascade=CascadeType.MERGE, mappedBy="content")
	@RestResource(rel="vendor_1")
	@JsonProperty("picture")
	@JsonIdentityReference(alwaysAsId=true)	
	//@JsonBackReference
	private Picture picture;
	
	
	//Constructor
	public Content(){
	}

	public Content(UUID id, String coverpicuri, Picture picture, List<ItemContent> items){
		super();
		this.id = id;
		this.coverpicuri = coverpicuri;
		this.picture = picture;
		this.items = items;
	}
	
	//Setters==========================================================================	
	public void setId(UUID id){this.id = id;}
	//public void setIdText(String idText){this.idText = idText;}
	public void setCoverpicuri(String uri){this.coverpicuri = uri;}	
	public void setOutfit(Outfit outfit){
		/*logger.warn("SETTING OUTFIT");
		this.outfit = outfit;
		if(!this.outfit.getContents().contains(this)){
			logger.warn("LINKING OUTFIT TO CONTENT");
			this.outfit.getContents().add(this);
		}*/
		this.outfit = (Outfit)this.setManyToOneParent(outfit, this.outfit, this);
	}	
	public void setPicture(Picture picture){
		logger.warn("SETTING PICURE");
		this.picture = picture;
		if (this.picture != null){		
			logger.warn("Picture POJO Not NULL");
			if(this.picture.getContent() != this){
				logger.warn("LINKING PICTURE TO CONTENT");
				picture.setContent(this);
			}
		}
		else{
			logger.warn("!!PICTURE IS NULL!!");
		}
	}	
	/*public void setItems(List<Item> items){
		logger.debug("Setting items list in Content entity");
		this.items = this.<Item, Content>setManyToManyParents(items, this.items, this);
	}*/
	
	/*public void setItems(List<Item> items){
		logger.debug("adding items to itemContent jointable");
		if(!items.isEmpty()){
			for(Item item : items){
				addItem(item);
			}
		}
	}*/

	/*public void addItem(Item item){
		logger.debug("ADDING ITEM TO items[]");
		this.items.add(item);
		if(item.getContents() == null) item.setContents(new ArrayList<Content>());
		if(!item.getContents().contains(this)) item.addContent(this);
	}*/

	public void addItem(Item item, Float positionx, Float positiony){
		ItemContent itemContent = new ItemContent(item, this);
		itemContent.setPositionx(positionx);
		itemContent.setPositiony(positiony);
		this.items.add(itemContent);
		item.getContents().add(itemContent);		
	}

	public void addItem(ItemContent itemContent){
		this.items.add(itemContent);
		itemContent.getItem().getContents().add(itemContent);
	}

	public void removeItem(Item item){
		for(Iterator<ItemContent> iterator = items.iterator(); iterator.hasNext();){
			ItemContent itemContent = iterator.next();

			if(itemContent.getContent().equals(this) && itemContent.getItem().equals(item)){
				iterator.remove();
				itemContent.getItem().getContents().remove(itemContent);  //Needed for bi-directional mapping
				itemContent.setContent(null);
				itemContent.setItem(null);
			}
		}
	}

	public void removeItem(String itemId){
		for(Iterator<ItemContent> iterator = items.iterator(); iterator.hasNext();){
			ItemContent itemContent = iterator.next();
			logger.debug("Content#removeItem: content equal = " + itemContent.getContent().equals(this));
			logger.debug("\nContent#removeItem:  itemContent#getItem() = \"" + itemContent.getItem().getId() + "\", \nitemId parameter = \"" + itemId + "\"\n");
			logger.debug("Content#removeItem: item ids equal = " + itemContent.getItem().getId().toString().equalsIgnoreCase(itemId));
			if(itemContent.getContent().equals(this) && itemContent.getItem().getId().toString().equals(itemId)){
				logger.debug("Content#removeItem: itemContent = \n" + itemContent.toString());
				iterator.remove();
				itemContent.getItem().getContents().remove(itemContent);  //Needed for bi-directional mapping
				itemContent.setContent(null);
				itemContent.setItem(null);
			}
		}
	}

	public void removeItems(List<Integer> excludedIndices){
		int ind = 0;
		for(ItemContent itemContent : items){
			if(excludedIndices.contains(ind)) break;
			items.remove(ind);
			itemContent.getItem().getContents().remove(itemContent);  //Needed for bi-directional mapping
			itemContent.setContent(null);
			itemContent.setItem(null);
			ind++;
		}
	}
	
	//Getters==========================================================================	
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public String getCoverpicuri(){return this.coverpicuri;}
	public Outfit getOutfit(){
		logger.warn("GETTING OUTFIT");
		return this.outfit;
	}
	public Picture getPicture(){
		logger.warn("GETTING PICURE");
		return this.picture;
	}
	/*
	public List<Item> getItems(){
		logger.warn("GETTING items MAP");
		return this.items;
	}*/
	
	public List<ItemContent> getItems(){
		logger.warn("GETTING items MAP");
		return this.items;
	}
	
	//Auxillary methods	
	/*@Override
	public void internalAddChild(Relational targetChild){
	Content currentParent = (Content)this;
	Item child = (Item)targetChild;
		if(currentParent.items == null){
			logger.debug("instantiating new List<Outfit> outfits");
			currentParent.items = new ArrayList<Item>();
		}
		currentParent.items.add(child);
	}
	@Override
	public void internalRemoveChild(Relational targetChild){
		((Content)this).items.remove((Item)targetChild);
	}*/
	@Override 
	public <T extends Relational> void internalAddChild(T targetChild){
		logger.debug("internalAddChild invoked");
		/*if(this.items == null){
			logger.debug("instantiating new List<T>");
			this.items = new ArrayList<ItemContent>();
		}
		this.items.add((ItemContent)targetChild);*/

		Item childItem = (Item) targetChild;
		ItemContent itemContent = new ItemContent(childItem, this);
		this.items.add(itemContent);
		childItem.getContents().add(itemContent);
	}

	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		this.items.remove((Item)targetChild);
	}

	
	//@Override 
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Content string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append((this.id == null ? "null" : this.id))
        	.append(indent).append("outfit: ").append(outfit == null ? "null" : outfit.getId() == null ? "null" : outfit.getId().toString())
			.append(indent).append("coverpicuri: ").append(this.coverpicuri)
			.append(indent).append("items: [");
			for (ItemContent itemContent: this.items) {
				sb.append(indent).append("{")
					.append((itemContent == null ? "null" : itemContent.toString(indents + 1))).append(", ")
					.append(indent).append("},");
			}
			sb.append(indent).append("],")
				.append(indent).append("picture: {")
				.append(picture == null ? "null" : picture.toString(indents + 1))
				.append(indent).append("}");
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}


	@Override
	public boolean equals(Object object){
		if(this == object) return true;
		if(this == null || getClass() != object.getClass()) return false;
		Content content = (Content) object;
		return Objects.equals(id, content.getId());
	}

	@Override
	public int hashCode(){
		return Objects.hash(id);
	}
}