package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.Date;
import java.util.Objects;
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

//import oxi.models.projection.LikeProjection;

@Entity
@Table(name="apparel_type")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=ApparelType.class)
public class ApparelType extends RelatedEntity implements Serializable, Identifiable<Integer>{
	@Transient
	private static final Logger logger = LogManager.getLogger(ApparelType.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	private String name;	

	//@OneToMany(cascade=CascadeType.ALL, mappedBy="apparelType")
	//@RestResource(rel="items")
	//@JsonIdentityReference(alwaysAsId=true)
	//List<Item> items;
	
	//Constructors
	public ApparelType(){
	}

	public ApparelType(Integer id, String name){
		this.name = name;
	}
	
	//Setters==========================================================================	
	public void setId(Integer id){this.id = id;}
	public void setName(String name){this.name = name;}
	//public void setItems(List<Item> items){
	//	logger.debug("Setting items list in Outfit entity");
	//	this.items = items;
	//}	
	//@JsonAnySetter
	//public void addItem(Item item){
	//	this.items.add(content);
	//	if (content.getOutfit() != this) content.setOutfit(this);
	//}
	
	
	//Getters==========================================================================	
	public Integer getId(){return this.id;}
	public String getName(){return this.name;}
	//public List<Item> getItems(){return this.items;}


	//@Override 
	//public <T extends Relational> void internalAddChild(T targetChild){
	//	if(this.items == null){
	//		logger.debug("instantiating new List<T>");
	//		this.items = new ArrayList<Item>();
	//	}
	//	if (!this.items.contains((Item)targetChild)) this.items.add((Item)targetChild);
	//}
	//@Override
	//public <T extends Relational> void internalRemoveChild(T targetChild){
	//	this.items.remove((Item)targetChild);
	//}

	//@Override 
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building ApparelType string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append((this.id == null ? "null" : this.id))
			.append(indent).append("name: ").append(this.name);
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}
}