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
import java.util.Date;

@Entity
@Table(name="size_label")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=SizeLabel.class)
public class SizeLabel extends RelatedEntity implements Serializable/*, Identifiable<.*>*/{
	@Transient
	private static final Logger logger = LogManager.getLogger(SizeLabel.class);
	
	//@Id
	////@JsonProperty("id")
	//@GeneratedValue(generator = "uuid2")
	//@GenericGenerator(name = "uuid2", strategy = "uuid2")
	//@Column(columnDefinition = "BINARY(16)")
	//private UUID id;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Column(columnDefinition = "INT")
	private Integer id;
	//@Column(name = "id_text", updatable = false, insertable = false)
	//private String idText;
	@Column(name = "name", columnDefinition = "VARCHAR(12)")
	private String name;

	
	//Constructor
	public SizeLabel(){

	}
	
	//Setters
	//@Override
	//public void setId(UUID id){this.id = id;}
	public void setId(Integer id){this.id = id;}
	public void setName(String name){this.name = name;}
	
	//Getters
	//@Override
	//public UUID getId(){return this.id;}
	public Integer getId(){return this.id;}
	//public Date getCreatedOn(){return this.createdOn;}
	public String getName(){return this.name;}
	//public String getIdText(){return this.idText;}
	
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
		logger.debug("building SizeLabel string");
        StringBuilder sb = new StringBuilder("\nId: ").append(this.id)
			.append("\nname:").append(this.name);
        return sb.toString();		
	}
}