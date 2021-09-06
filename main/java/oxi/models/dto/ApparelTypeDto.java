package oxi.models.dto;

import oxi.models.ApparelType;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators.*;
import org.springframework.hateoas.*;

//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;

import org.hibernate.annotations.GenericGenerator;

//import oxi.models.projection.LikeProjection;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=ApparelTypeDto.class)
public class ApparelTypeDto implements Serializable/*, Identifiable<.*>*/{
	//@Transient
	//private static final Logger logger = LogManager.getLogger(ApparelTypeDto.class);
	@JsonProperty("id")
    private Integer id;
	private String name;
	private String iconName;	

	//@OneToMany(cascade=CascadeType.ALL, mappedBy="apparelType")
	//@RestResource(rel="items")
	//@JsonIdentityReference(alwaysAsId=true)
	//List<Item> items;
	
	//Constructors
	public ApparelTypeDto(){
	}

		public ApparelTypeDto(Integer id){
		this.id = id;
	}

	public ApparelTypeDto(Integer id, String name, String iconName){
		this.id = id;
		this.name = name;
		this.iconName = iconName;
	}

	public ApparelTypeDto(ApparelType apparelType){
		this.id = apparelType.getId();
		this.name = apparelType.getName();
		this.iconName = apparelType.getIconName();
	}
	
	//Setters==========================================================================	
	public void setId(Integer id){this.id = id;}
	public void setName(String name){this.name = name;}
	public void setIconName(String iconName){this.iconName = iconName;}
	
	
	//Getters==========================================================================	
	//@Override
	public Integer getId(){return this.id;}
	public String getName(){return this.name;}
	public String getIconName(){return this.iconName;}

	////@Override 
	//public String toString(int indents){
	//	String indent = "\n";
	//	for(int i = 0; i < indents; i++){
	//		indent += "    ";
	//	}
	//	logger.debug("building ApparelTypeDto string");
   //     StringBuilder sb = new StringBuilder(indent).append("id: ").append((this.id == null ? "null" : this.id))
	//		.append(indent).append("name: ").append(this.name)
	//		.append(indent).append("iconName: ").append(this.iconName);
   //     return sb.toString();
	//}
//
	//@Override
	//public String toString(){
	//	return toString(0);
	//}
}