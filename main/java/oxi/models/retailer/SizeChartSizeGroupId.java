package oxi.models.retailer;

import oxi.models.RelatedEntity;
import oxi.models.Relational;
//import oxi.models.retailer.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.util.Objects;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.hateoas.*;

import org.hibernate.annotations.GenericGenerator;

@Embeddable
public class SizeChartSizeGroupId implements Serializable{
	@Transient
	private static final Logger logger = LogManager.getLogger(SizeChartSizeGroupId.class);

	@Column(name = "size_group_id", columnDefinition = "BINARY(16)")
	private UUID sizeGroupId;

	@Column(name = "size_chart_id", columnDefinition = "BINARY(16)")
	private UUID sizeChartId;

	private SizeChartSizeGroupId(){}

	public SizeChartSizeGroupId(UUID sizeGroupId, UUID sizeChartId){
		this.sizeGroupId = sizeGroupId;
		this.sizeChartId = sizeChartId;
	}

	//Getters
	public UUID getSizeId(){return this.sizeGroupId;}
	public UUID getSizeChartId(){return this.sizeChartId;}

	//Setters
	public void setSizeId(UUID sizeGroupId){this.sizeGroupId = sizeGroupId;}
	public void setSizeChartId(UUID sizeChartId){this.sizeChartId = sizeChartId;}

	
	
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("sizeGroupId: ").append(((this.sizeGroupId == null) ? "null" : this.sizeGroupId.toString()))
        	.append(indent).append("sizeChartId: ").append(((this.sizeChartId == null) ? "null" : this.sizeChartId.toString()));
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}

	@Override
	public boolean equals(Object object){
		if(this == object) return true;
		if(object == null || getClass() != object.getClass()) return false;
		SizeChartSizeGroupId that = (SizeChartSizeGroupId)object;
		return Objects.equals(sizeGroupId, that.sizeGroupId) && Objects.equals(sizeChartId, that.sizeChartId);
	}

	@Override 
	public int hashCode(){
		return Objects.hash(sizeGroupId, sizeChartId);
	}
}