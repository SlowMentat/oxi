package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.annotation.*;
//import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import lombok.Data;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=OutfitCoverpicDto.class)
public class OutfitCoverpicDto implements Serializable, Identifiable<String>
{
	private static final Logger logger = LogManager.getLogger(OutfitCoverpicDto.class);

	@JsonProperty("id")
	private String id;
	private String coverpicuri;

	public OutfitCoverpicDto(){}

	public OutfitCoverpicDto(String id, String coverpicuri){
		//super();
		this.id = id;
		this.coverpicuri = coverpicuri;
	}

	//Getters
	@Override
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}
	public String getCoverpicuri(){return this.coverpicuri;}

	//Setters
	public void setId(String id){this.id = id;}
	public void setCoverpicuri(String coverpicuri){this.coverpicuri = coverpicuri;}


	public String toString(int indents) {
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : id))
			.append(indent).append("coverpic:").append(this.coverpicuri);
		
		return sb.toString();
	}	

    @Override
    public String toString(){
    	return toString(0);
    }
}
