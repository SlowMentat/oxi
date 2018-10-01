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
@Table(name="size")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Size.class)
public class Size extends RelatedEntity implements Serializable, Identifiable<UUID>{
	@Transient
	private static final Logger logger = LogManager.getLogger(Size.class);
	
	@Id
	//@JsonProperty("id")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;
	private String size;
	
	//Constructor
	public Size(){

	}
	
	//Setters
	//@Override
	public void setId(UUID id){this.id = id;}
	public void setSize(String size){this.size = size;}
	
	//Getters
	//@Override
	public UUID getId(){return this.id;}
	public String getSize(){return this.size;}
	public String getIdText(){return this.idText;}
	
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
		logger.debug("building Size string");
        StringBuilder sb = new StringBuilder("\nId: ").append(this.idText)
			.append("\nsize:").append(this.size);
        return sb.toString();		
	}
}