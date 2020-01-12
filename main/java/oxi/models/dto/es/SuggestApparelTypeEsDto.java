package oxi.models.dto.es;

import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.*;

import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;


@JsonRootName(value = "suggestApparelType")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ApparelTypeEsDto.class)
public class SuggestApparelTypeEsDto implements Serializable, Identifiable<Integer>
{
	@Id
	private Integer id;
	private Map<String, Object> data;


	public SuggestApparelTypeEsDto(){
		
	}

	public SuggestApparelTypeEsDto(Integer id, Map<String, Object> data){
		this.id = id;
		this.data = data;
	}

	//Setters
	public void setId(Integer id){this.id = id;}
	public void setData(Map<String, Object> data){this.data = data;}
	
	//Getters
	@Override
	public Integer getId(){return this.id;}
	public Map<String, Object> getData(){return this.data;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id));
		//	.append(indent).append("data : ").append(((this.data == null) ? "null" : this.data));
        return sb.toString();		
	}
}
