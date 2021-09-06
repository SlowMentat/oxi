package oxi.models.dto.es;

import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.retailer.SizeGroup;
import oxi.models.dto.retailer.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.io.Serializable;

//import org.springframework.hateoas.core.*;
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
* This is the dto returned when user agent selects the retailer drop down in the add-item modal.
* The add-item modal is displayed when a consumer is tagging uploaded photos.
*/
@JsonRootName(value = "suggestRetailerName")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="name", scope=SuggestRetailerEsDto.class)
public class SuggestRetailerEsDto implements Serializable/*, Identifiable<.*>*/
{
	@Id
	private Integer id;
	private Map<String, Object> data;


	public SuggestRetailerEsDto(){}

	public SuggestRetailerEsDto(Integer id, Map<String, Object> data)
	{
		this.id = id;
		this.data = data;
	}

	//Setters
	public void setId(Integer id){this.id = id;}
	public void setData(Map<String, Object> data){this.data = data;}
	
	//Getters
	//@Override
	public Integer getId(){return this.id;}
	public Map<String, Object> getData(){return this.data;}

	//@Override
	//public String toString(int indents){
	//	String indent = "\n";
	//	for(int i = 0; i < indents; i++){
	//		indent += "    ";
	//	}
    //    StringBuilder sb = new StringBuilder(indent).append("name: ").append(((this.name == null) ? "null" : this.name))
	//		.append(indent).append("logoUrl : ").append(((this.logoUrl == null) ? "null" : this.logoUrl))
	//		.append(indent).append("homePageUrl : ").append(this.homePageUrl == null ?  "null" : this.homePageUrl);
    //    return sb.toString();		
	//}
}
