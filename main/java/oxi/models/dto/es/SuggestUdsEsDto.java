package oxi.models.dto.es;

import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.retailer.SizeGroup;
import oxi.models.dto.retailer.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;
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
*Suggest User Defined Size Elasticsearch Data Transfer Object
*/

@Document(indexName = "uds", type = "doc")
@JsonRootName(value = "uds")
public class SuggestUdsEsDto implements Serializable, Identifiable<String>
{
	@Id
	private String id;
	private Map<String, Object> data;

	public SuggestUdsEsDto(){}

	public SuggestUdsEsDto(String id, Map<String, Object> data){
		this.id = id;
		this.data = data;
	}

	//Setters
	public void setId(String id){this.id = id;}
	public void setData(Map<String, Object> data){this.data = data;}
	
	//Getters
	@Override
	public String getId(){return this.id;}
	public Map<String, Object> getData(){return this.data;}

	//@Override
	//public String toString(int indents){
	//	String indent = "\n";
	//	for(int i = 0; i < indents; i++){
	//		indent += "    ";
	//	}
    //    StringBuilder sb = new StringBuilder(indent)/*.append("id: ").append(((this.id == null) ? "null" : this.id))*/
	//		.append(indent).append("size : ").append(((this.size == null) ? "null" : this.size));
    //    return sb.toString();		
	//}
}
