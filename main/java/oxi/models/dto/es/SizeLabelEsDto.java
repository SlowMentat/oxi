package oxi.models.dto.es;

import oxi.models.*;
import oxi.models.retailer.SizeGroup;
import oxi.models.dto.retailer.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
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
* This is the dto returned when user agent selects the size drop down in the add-item modal.
* The add-item modal is displayed when a consumer is tagging uploaded photos.
*/
@Document(indexName = "size_label")
@JsonRootName(value = "availableSizeLabel")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=SizeLabelEsDto.class)
public class SizeLabelEsDto implements Serializable, Identifiable<Integer>
{
	@Id
	@JsonProperty("id")
	private Integer id;
	private String name;


	public SizeLabelEsDto(){}

	public SizeLabelEsDto(SizeLabel sizeLabel){
		this.id = sizeLabel.getId();
		this.name = sizeLabel.getName();
	}

	public SizeLabelEsDto(Integer id, String name){
		this.id = id;
		this.name = name;
	}

	//Setters
	public void setId(Integer id){this.id = id;}
	public void setName(String name){this.name = name;}
	
	//Getters
	@Override
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
