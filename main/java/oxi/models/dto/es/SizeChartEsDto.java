package oxi.models.dto.es;

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



@Document(indexName = "size_chart", type = "doc")
@JsonRootName(value = "availableSizeGroup")
//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=SizeChartEsDto.class)
public class SizeChartEsDto implements Serializable, Identifiable<Integer>
{
	@Id
	@JsonProperty("id")
	private Integer id;
	@JsonRawValue
	private String metric;
	private SizeLabelEsDto sizeLabel;


	public SizeChartEsDto(){}

	public SizeChartEsDto(Integer id, String metric, SizeLabelEsDto sizeLabel){
		this.id = id;
		this.metric = metric;
		this.sizeLabel = sizeLabel;
	}

	//Setters
	public void setId(Integer id){this.id = id;}
	public void setMetric(String metric){this.metric = metric;}
	public void setSizeLabelEsDto(SizeLabelEsDto sizeLabel){this.sizeLabel = sizeLabel;}
	
	//Getters
	@Override
	public Integer getId(){return this.id;}
	public String getMetric(){return this.metric;}
	public SizeLabelEsDto getSizeLabelEsDto(){return this.sizeLabel;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id))
			.append(indent).append("metric : ").append(((this.metric == null) ? "null" : this.metric))
			.append(indent).append("sizeLabelEsDto : ").append(((this.sizeLabel == null) ? "null" : this.sizeLabel.toString(indents + 1))) ;
        return sb.toString();		
	}
}
