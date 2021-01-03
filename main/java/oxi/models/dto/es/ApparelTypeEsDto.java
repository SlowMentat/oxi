package oxi.models.dto.es;

import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.*;

import org.springframework.hateoas.server.core.*;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.Identifiable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.core.*;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;


@Document(indexName = "apparel_type"/*, type = "doc"*/)
@JsonRootName(value = "apparelTypeAvailable")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ApparelTypeEsDto.class)
public class ApparelTypeEsDto implements Serializable/*Identifiable<Integer>*/
{
	@Id
	private Integer id;
	private String name;


	public ApparelTypeEsDto(){
		
	}

	public ApparelTypeEsDto(Integer id, String name){
		this.id = id;
		this.name = name;
	}

	//Setters
	public void setId(Integer id){this.id = id;}
	public void setName(String name){this.name = name;}
	
	//Getters
	
	public Integer getId(){return this.id;}
	public String getName(){return this.name;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id))
			.append(indent).append("name : ").append(((this.name == null) ? "null" : this.name));
        return sb.toString();		
	}
}
