package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
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

@Entity
@Table(name="item")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Item.class)
public class Item extends RelatedEntity implements ItemDao 
{
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
	private Long positionx;
	private Long positiony;
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
	private Content contents;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@RestResource(rel="client_1")
	@JsonIdentityReference(alwaysAsId=true)
	private Profile profile;
	
	//Constructor
	public Item(){

	}
	
	//Setters

	public void setId(UUID id){this.id = id;}
	//public void setIdText(String idText){this.idText = idText;}

	public void setLocationx(Long posx){this.positionx = posx;}

	public void setLocationy(Long posy){this.positiony = posy;}

	public void setLink(String link){this.link = link;}	

	public void setType(String type){this.type = type;}	

	public void setSize(String size){this.size = size;}	

	public <T extends ProfileDao> void setProfile(T profile){
		this.profile = profile;
	}	

	public <T extends ContentDao> void setContents(List<T> contents){
		this.contents = this.<T, Item>setManyToManyParents(contents, this.contents, this);
	}	

	public <T extends contentDao> void addContent(T content){
		this.contents.add(content);
		if(!this.contents.contains(content)) content.addItem(this);
	}
	
	//Getters
	@Override
	public UUID getId(){return this.id;}

	public String getIdText(){return this.idText;}

	public Long getPositionx(){return this.positionx;}

	public Long getPositiony(){return this.positiony;}

	public String getLink(){return this.link;}	

	public String getType(){return this.type;}	

	public String getSize(){return this.size;}

	public <T extends ContentDao> List<T> getContents(){return this.contents;}

	public <T extends ProfileDao> T getProfile(){return this.profile;}
	
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
        StringBuilder sb = new StringBuilder("\nID: ").append(this.id)
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