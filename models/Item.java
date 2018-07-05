package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.hateoas.*;

@Entity
@Table(name="item")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Item.class)
public class Item extends RelatedEntity implements Serializable{
	@Transient
	private static final Logger logger = LogManager.getLogger(Item.class);
	
	@Id
	//@JsonProperty("id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long Id;
	private long positionx;
	private long positiony;
	private String link;
	private String type;
	private String size;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@RestResource(rel="client_0")
	@JsonIdentityReference(alwaysAsId=true)
	@JoinTable(
		name="item_content",
		joinColumns=@JoinColumn(name="item_id", referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="content_id", referencedColumnName="id")
	)
	private List<Content> contents;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@RestResource(rel="client_1")
	@JsonIdentityReference(alwaysAsId=true)
	private Profile profile;
	
	//Constructor
	public Item(){

	}
	
	//Setters
	public void setId(Long id){this.Id = id;}
	public void setLocationx(long posx){this.positionx = posx;}
	public void setLocationy(long posy){this.positiony = posy;}
	public void setLink(String link){this.link = link;}	
	public void setType(String type){this.type = type;}	
	public void setSize(String size){this.size = size;}	
	public void setProfile(Profile profile){
		this.profile = profile;
	}
	
	public void setContents(List<Content> contents){
		//this.contents = (List<Content>)(Object)this.setManyToManyParents(contents, this.contents, this);
		this.contents = this.<Content, Item>setManyToManyParents(contents, this.contents, this);
		//add this Item to profile items list
	}
	
	public void addContent(Content content){
		this.contents.add(content);
		if(!this.contents.contains(content)) content.addItem(this);
	}
	
	//Getters
	public Long getId(){return this.Id;}
	public long getPositionx(){return this.positionx;}
	public long getPositiony(){return this.positiony;}
	public String getLink(){return this.link;}	
	public String getType(){return this.type;}	
	public String getSize(){return this.size;}
	public List<Content> getContents(){return this.contents;}
	public Profile getProfile(){return this.profile;}
	
	@Override 
	public <T extends Relational> void internalAddChild(T targetChild){
		if(this.contents == null){
			logger.debug("instantiating new List<T>");
			this.contents = new ArrayList<Content>();
		}
		this.contents.add((Content)targetChild);
	}
	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		this.contents.remove((Content)targetChild);
	}
	
	@Override
	public String toString(){
		logger.debug("building Item string");
        StringBuilder sb = new StringBuilder("\nID: ").append(this.Id)
			.append("\npositionx: ").append(this.positionx)
			.append("\npositiony:").append(this.positiony)
			.append("\nlink:").append(this.link)
			.append("\ntype:").append(this.type)
			.append("\nsize:").append(this.size)
		.append("\ncontents: [");
		if(this.contents != null){
			for (Content content: this.contents) {
				logger.debug("building content string");
				sb.append("\n	").append(content.getId());
			}
			sb.append("]");
		}else{
			logger.debug("Item.contents is null");
		}
		sb.append("\nprofile: ");
		if(this.profile != null){
			sb.append(profile.getId());
		}else{
			logger.debug("Item.profile is null");
		}
        return sb.toString();		
	}
}