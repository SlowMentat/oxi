package oxi.models.dto.retailer;

import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.dto.retailer.*;
import oxi.models.retailer.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;

@JsonRootName(value = "sizeChart")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=SizeChartDto.class)
public class SizeChartDto implements Serializable, Identifiable<String>
{
	@JsonProperty("id")
	private String id;
	private String name;
	private String apparelType;
	private List<SizeGroupDto> sizeGroupDtos;

	public SizeChartDto(){}

	public SizeChartDto(String id, String name, List<SizeGroupDto> sizeGroupDtos){
		//super();
		this.id = id;
		this.name = name;
		this.sizeGroupDtos = sizeGroupDtos;
	}

	//ugh
	public SizeChartDto(SizeChart sizeChart){
		id = sizeChart.getIdText() != null ? sizeChart.getIdText() : null;
		name = sizeChart.getChartName();
		sizeGroupDtos = new ArrayList<SizeGroupDto>(sizeChart.getSizeGroups().size());
		for(SizeChartSizeGroup sizeChartSizeGroup : sizeChart.getSizeGroups()){
			sizeGroupDtos.add(new SizeGroupDto(sizeChartSizeGroup.getSizeGroup()));
		}
	}

	//Setters
	public void setId(String id){this.id = id;}
	public void setName(String name){this.name = name;}
	public void setSizeGroupDtos(List<SizeGroupDto> sizeGroupDtos){this.sizeGroupDtos = sizeGroupDtos;}
	
	//Getters
	@Override
	public String getId(){return this.id;}
	public String getChartName(){return this.name;}
	public List<SizeGroupDto> getSizeGroupDtos(){return this.sizeGroupDtos;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id))
			.append(indent).append("name:").append(((this.name == null) ? "null" : this.name))
			.append(indent).append("sizeGroupDtos: {");
		if(this.sizeGroupDtos != null){
			for(SizeGroupDto sizeGroupDto : this.sizeGroupDtos){
				sb.append(indent).append(sizeGroupDto.toString(indents+1));
			}
		}else{
			sb.append(indent).append(indent).append("null");
		}
		sb.append(indent).append("}");
        return sb.toString();		
	}
}
