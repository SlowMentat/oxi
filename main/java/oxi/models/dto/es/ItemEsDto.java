package oxi.models.dto.es;

//import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.retailer.SizeGroup;
import oxi.models.dto.retailer.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
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
* This is the dto returned when user agent selects an item from the drop down in the add-item modal.
* The add-item modal is displayed when a consumer is tagging uploaded photos.
*/
@Document(indexName = "item"/*, type = "doc"*/)
@JsonRootName(value = "availableItem")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ItemEsDto.class)
public class ItemEsDto implements Serializable/*Identifiable<String>*/
{
	private String id;
	private ApparelTypeEsDto apparelType;
	private SizeGroupEsDto sizeGroup;
	private String sizeChartId;
	private String retailer;


	public ItemEsDto(){

	}

	public ItemEsDto(String id, ApparelTypeEsDto apparelType, String sizeChartId, SizeGroupEsDto sizeGroup){
		//super();
		this.id = id;
		this.apparelType = apparelType;
		this.sizeChartId = sizeChartId;
		this.sizeGroup = sizeGroup;
	}

	//Setters
	public void setId(String id){this.id = id;}
	public void setSizeChartId(String sizeChartId){this.sizeChartId = sizeChartId;}
	public void setSizeGroup(SizeGroupEsDto sizeGroup){this.sizeGroup = sizeGroup;}
	public void setApparelType(ApparelTypeEsDto apparelType){this.apparelType = apparelType;}
	
	//Getters
	
	public String getId(){return this.id;}
	public String getSizeChartId(){return this.sizeChartId;}
	public SizeGroupEsDto getSizeGroup(){return this.sizeGroup;}
	public ApparelTypeEsDto getApparelType(){return this.apparelType;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id))
			.append(indent).append("sizeChartId : ").append(((this.sizeChartId == null) ? "null" : this.sizeChartId))
			.append(indent).append("sizeGroup : ").append(( this.sizeGroup == null ? "null" : this.sizeGroup.toString(indents) ))
			.append(indent).append("apparelType : ").append(this.apparelType.toString(indents));
        return sb.toString();		
	}
}
