package oxi.models.retailer;

import oxi.models.RelatedEntity;
import oxi.models.Relational;
//import oxi.models.retailer.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.hateoas.*;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "SizeChartSizeGroup")
@Table(name="size_chart_size_group")
public class SizeChartSizeGroup extends RelatedEntity{
	@Transient
	private static final Logger logger = LogManager.getLogger(SizeChartSizeGroup.class);
	
	@EmbeddedId
	private SizeChartSizeGroupId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("sizeGroupId")
	@JoinColumn(name = "size_group_id")
	private SizeGroup sizeGroup;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("sizeChartId")
	@JoinColumn(name = "size_chart_id")
	private SizeChart sizeChart;	

	@Column(name = "created_on")
	private Date createdOn = new Date();

	//Constructors 
	private SizeChartSizeGroup(){}

	public SizeChartSizeGroup(SizeGroup sizeGroup, SizeChart sizeChart){
		this.sizeGroup = sizeGroup;
		this.sizeChart = sizeChart;
		this.id  = new SizeChartSizeGroupId(sizeGroup.getId(), sizeChart.getId());
		this.createdOn = new Date();
	}

	//Getters
	public SizeGroup getSizeGroup(){return this.sizeGroup;}
	public SizeChart getSizeChart(){return this.sizeChart;}
	public Date getCreatedOn(){return this.createdOn;}

	//Setters
	public void setSizeGroup(SizeGroup sizeGroup){this.sizeGroup = sizeGroup;}
	public void setSizeChart(SizeChart sizeChart){this.sizeChart = sizeChart;}
	public void setCreatedOn(Date createdOn){this.createdOn = createdOn;}


	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building SizeChart string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append("{")
        	.append(((this.id == null) ? "null" : this.id.toString(indents + 1)))
        	.append(indent).append("}")
        	.append(indent).append("sizeGroup: ").append(this.sizeGroup == null ? "null" : sizeGroup.toString(indents + 1))
			.append(indent).append("sizeChart: ").append(this.sizeChart == null ? "null" : sizeChart.toString(indents + 1));
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}

	@Override
	public boolean equals(Object object){
		//test object reference equivalence
		if(this == object) return true;
		//test for class association equivalence
		if(object == null || getClass() != object.getClass()) return false;
		//test further for equivalence by calling equals method of sizeGroup and sizeCharts properties
		SizeChartSizeGroup that = (SizeChartSizeGroup) object;
		return Objects.equals(sizeGroup, that.sizeGroup) && Objects.equals(sizeChart, that.sizeChart);
	}

	@Override
	public int hashCode(){
		return Objects.hash(sizeGroup, sizeChart);
	}
}