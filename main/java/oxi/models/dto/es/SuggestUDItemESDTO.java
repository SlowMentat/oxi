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

import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;

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

@JsonRootName(value = "udItem")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=SuggestUDItemESDTO.class)
public class SuggestUDItemESDTO implements Serializable, Identifiable<String>
{
	@Id
	@JsonProperty("id")
	private String id;
	//@JsonAnyGetter
	private Map<String, Object> itemSnippet = new HashMap<String, Object>();


	public SuggestUDItemESDTO(){}

	public SuggestUDItemESDTO(String id, Map<String, Object> itemSnippet){
		this.id = id;
		this.itemSnippet = itemSnippet;
	}

	//Setters
	public void setId(String id){this.id = this.id;}
	public void setItemSnippet(Map<String, Object> itemSnippet){this.itemSnippet = this.itemSnippet;}

	//Getters
	@Override
	public String getId(){return this.id;}
	public Map<String, Object> getItemSnippet(){return this.itemSnippet;}
}

