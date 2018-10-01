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
@Table(name="retailer")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Retailer.class)
public class Retailer extends RelatedEntity implements Serializable, Identifiable<UUID>{
	@Transient
	private static final Logger logger = LogManager.getLogger(Retailer.class);
	
	@Id
	//@JsonProperty("id")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;
	private String name;
	private String link;
	private Integer red;
	private Integer blue;
	private Integer green;
	
	//Constructor
	public Retailer(){

	}
	
	//Setters
	//@Override
	public void setId(UUID id){this.id = id;}
	public void setLink(String link){this.link = link;}	
	public void setName(String name){this.name = name;}
	public void setRed(Integer red){this.red = red;}
	public void setBlue(Integer blue){this.blue = blue;}
	public void setGreen(Integer green){this.green = green;}
	
	//Getters
	//@Override
	public UUID getId(){return this.id;}
	public String getLink(){return this.link;}	
	public String getName(){return this.name;}
	public String getIdText(){return this.idText;}
	public Integer getRed(){return this.red;}
	public Integer getBlue(){return this.blue;}
	public Integer getGreen(){return this.green;}
	
	/*@Override 
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
	}*/
	
	@Override
	public String toString(){
		logger.debug("building Retailer string");
        StringBuilder sb = new StringBuilder("\nID: ").append(this.idText)
			.append("\nlink:").append(this.link)
			.append("\nname:").append(this.name)
			.append("\nred:").append((this.red != null) ? this.red.toString() : "null")
			.append("\nblue:").append((this.blue != null) ?  this.blue.toString() : "null")
			.append("\ngreen:").append((this.green != null) ?  this.green.toString() : "null");
        return sb.toString();		
	}
}