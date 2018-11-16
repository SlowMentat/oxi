package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
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
	
	@ManyToMany(cascade=CascadeType.ALL, mappedBy="contents")	//Item entity is the owner
	//@MapKeyColumn(name="clothing_type")
	@RestResource(rel="vendor_0")
	@JsonProperty("items")
	@JsonIdentityReference(alwaysAsId=true)
	//private Map<String, Item> items;	
	private List<Item> items;
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy="content")
	@RestResource(rel="vendor_1")
	@JsonProperty("picture")
	@JsonIdentityReference(alwaysAsId=true)	
	//@JsonBackReference
	private Picture picture;
	
	
	//Constructor
	public Content(){
	}

	public Content(UUID id, String coverpicuri, Picture picture, List<Item> items){
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
	public void setItems(List<Item> items){
		logger.debug("Setting items list in Content entity");
		this.items = this.<Item, Content>setManyToManyParents(items, this.items, this);
	}
	
	public void addItem(Item item){
		logger.debug("ADDING ITEM TO items[]");
		this.items.add(item);
		if (!item.getContents().contains(this)) item.addContent(this);
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
	
	public List<Item> getItems(){
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
		if(this.items == null){
			logger.debug("instantiating new List<T>");
			this.items = new ArrayList<Item>();
		}
		this.items.add((Item)targetChild);
	}
	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		this.items.remove((Item)targetChild);
	}
	
	@Override 
	public String toString(){
		logger.debug("building Content string");
        StringBuilder sb = new StringBuilder("\nId: ").append(this.id)
			.append("\ncoverpicuri: ").append(this.coverpicuri)
			.append("\noutfits: [");
		if(this.outfit != null){
			logger.debug("building outfit string");
			sb.append(outfit.getId());
		}else{
			logger.debug("profile.outfits is null");
		}
		sb.append("]");
		sb.append("\nitems: [");
		if(this.items != null){
			for (Item item: this.items) {
				logger.debug("building item string");
				sb.append("	").append(item.getId());
			}
		}else{
			logger.debug("Content.items is null");
		}
		sb.append("]");
		sb.append("\npicture: {").append(this.picture).append("\n}");
        return sb.toString();
	}
}