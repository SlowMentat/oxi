package oxi.models.dto.es;

import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.retailer.SizeGroup;
import oxi.models.dto.retailer.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
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
@Document(indexName = "retailer")
@JsonRootName(value = "availableRetailer")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="name", scope=RetailerEsDto.class)
public class RetailerEsDto implements Serializable/*, Identifiable<.*>*/
{
	@Id
	private String name;
	private String logoUrl;
	private String homePageUrl;


	public RetailerEsDto(){}

	public RetailerEsDto(String name, String logoUrl, String homePageUrl)
	{
		this.name = name;
		this.logoUrl = logoUrl;
		this.homePageUrl = homePageUrl;
	}

	//Setters
	public void setId(String name){this.name = name;}
	public void setLogoUrl(String logoUrl){this.logoUrl = logoUrl;}
	public void setHomePageUrl(String homePageUrl){this.homePageUrl = homePageUrl;}
	
	//Getters
	//@Override
	public String getId(){return this.name;}
	public String getLogoUrl(){return this.logoUrl;}
	public String getHomePageUrl(){return this.homePageUrl;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("name: ").append(((this.name == null) ? "null" : this.name))
			.append(indent).append("logoUrl : ").append(((this.logoUrl == null) ? "null" : this.logoUrl))
			.append(indent).append("homePageUrl : ").append(this.homePageUrl == null ?  "null" : this.homePageUrl);
        return sb.toString();		
	}
}
