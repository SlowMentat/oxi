package oxi.models.dto.es;

import oxi.models.*;
import oxi.models.retailer.SizeGroup;
import oxi.models.dto.retailer.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

import org.springframework.hateoas.server.core.*;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.Identifiable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import com.fasterxml.jackson.annotation.*;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;



/*
* 
* 
*/
@JsonRootName(value = "suggestItem")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=SuggestItemEsDto.class)
public class SuggestItemEsDto implements Serializable/*Identifiable<String>*/
{
	@Id
	@JsonProperty("id")
	private String id;
	//@JsonAnyGetter
	private Map<String, Object> itemSnippet = new HashMap<String, Object>();


	public SuggestItemEsDto(){}

	public SuggestItemEsDto(String id, Map<String, Object> itemSnippet){
		this.id = id;
		this.itemSnippet = itemSnippet;
	}

	/*public SuggestItemEsDto(suggestItem suggestItem){
		this.id = suggestItem.getId();
		this.name = suggestItem.getName();
	}

	public SuggestItemEsDto(Integer id, String name){
		this.id = id;
		this.name = name;
	}*/

	//Setters
	public void setId(String id){this.id = this.id;}
	public void setItemSnippet(Map<String, Object> itemSnippet){this.itemSnippet = this.itemSnippet;}
	//@JsonAnySetter
	//public void set(String name, Object value){
	//	this.itemSnippet.put(name, value);
	//}

	//Getters
	
	public String getId(){return this.id;}
	public Map<String, Object> getItemSnippet(){return this.itemSnippet;}
	//@JsonAnyGetter
	//public Map<String, Object> any(){
	//	return this.itemSnippet;
	//}

	//@Override
	/*public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id))
			.append(indent).append("name : ").append(((this.name == null) ? "null" : this.name));
        return sb.toString();		
	}*/
}