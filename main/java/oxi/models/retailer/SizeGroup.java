package oxi.models.retailer;

import oxi.models.RelatedEntity;
import oxi.models.Relational;
//import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.dto.retailer.SizeGroupDto;
//import oxi.models.retailer.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.server.core.*;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.Date;


@Entity
@Table(name="size_group")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=SizeGroup.class)
public class SizeGroup extends RelatedEntity implements Serializable/*Identifiable<UUID>*/
{
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	@Column(name = "id_text", columnDefinition="VARCHAR(36)", updatable = false, insertable = false)
	private String idText;
	@Column(name="size_label_id", columnDefinition = "int")
	private Integer sizeLabelId;
	@Column(name = "created_on")
	private Date createdOn = new Date();
	@Column(name = "metric", columnDefinition="JSON")
	private String metric;
	@Column(name = "size_label", columnDefinition="VARCHAR(16)")
	private String sizeLabel;

	//private float neck;
	//@Column(name = "full_shoulder", columnDefinition="float", nullable = true)
	//private float fullShoulder;
	//@Column(name = "half_shoulder", columnDefinition="float", nullable = true)
	//private float halfShoulder;
	//@Column(nullable = true)
	//private float chest;
	//@Column(nullable = true)
	//private float waist;
	//@Column(nullable = true)
	//private float hip;
	//@Column(nullable = true)
	//private float sleeve;
	//@Column(name = "front_length", columnDefinition="float", nullable = true)
	//private float frontLength;
	//@Column(name = "back_length", columnDefinition="float", nullable = true)
	//private float backLength;
	//@Column(name = "pant_outseam", columnDefinition="float", nullable = true)
	//private float pantOutseam;
	//@Column(name = "pant_inseam", columnDefinition="float", nullable = true)
	//private float pantInseam;
	//@Column(nullable = true)
	//private float thigh;
	//@Column(nullable = true)
	//private float calf;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval= true, mappedBy = "sizeGroup")
	@RestResource(rel="client_0")	
	@JsonIdentityReference(alwaysAsId=true)
	private List<SizeChartSizeGroup> sizeCharts = new ArrayList<>();

	public SizeGroup(){}

	public SizeGroup(
		UUID id, 
		Integer sizeLabelId,
		float neck,
		float fullShoulder,
		float halfShoulder,
		float chest,
		float waist,
		float hip,
		float sleeve,
		float frontLength,
		float backLength,
		float pantOutseam,
		float pantInseam,
		float thigh,
		float calf,
		List<SizeChartSizeGroup> sizeCharts){
		//super();
		this.id = id;
		this.sizeLabelId = sizeLabelId;
		//this.neck = neck;
		//this.fullShoulder = fullShoulder;
		//this.halfShoulder = halfShoulder;
		//this.chest = chest;
		//this.waist = waist;
		//this.hip = hip;
		//this.sleeve = sleeve;
		//this.frontLength = frontLength;
		//this.backLength = backLength;
		//this.pantOutseam = pantOutseam;
		//this.pantInseam = pantInseam;
		//this.thigh = thigh;
		//this.calf = calf;
		this.sizeCharts = sizeCharts;
	}

	public SizeGroup(SizeGroupDto sizeGroupDto){
		//super();
		this.id = sizeGroupDto.getId() != null ? UUID.fromString(sizeGroupDto.getId()) : null;
		//this.sizeLabelId = sizeGroupDto.getSizeLabelId();
		//this.neck = sizeGroupDto.getNeck();
		//this.fullShoulder = sizeGroupDto.getFullShoulder();
		//this.hal//fShoulder = sizeGroupDto.getHalfShoulder();
		//this.chest = sizeGroupDto.getChest();
		//this.waist = sizeGroupDto.getWaist();
		//this.hip = sizeGroupDto.getHip();
		//this.sleeve = sizeGroupDto.getSleeve();
		//this.frontLength = sizeGroupDto.getFrontLength();
		//this.backLength = sizeGroupDto.getBackLength();
		//this.pantOutseam = sizeGroupDto.getPantOutseam();
		//this.pantInseam = sizeGroupDto.getPantInseam();
		//this.thigh = sizeGroupDto.getThigh();
		//this.calf = sizeGroupDto.getCalf();
		this.sizeLabel = sizeGroupDto.getSizeLabel();
		this.metric = sizeGroupDto.getMetric();
		this.sizeCharts = null;
	}

	//Setters
	public void setId(UUID id){this.id = id;}
	public void setSizeLabelId(Integer sizeLabelId){this.sizeLabelId = sizeLabelId;}	
	//public void setNeck(float neck){this.neck = neck;}
	//public void setFullShoulder(float fullShoulder){this.fullShoulder = fullShoulder;}
	//public void setHalfShoulder(float halfShoulder){this.halfShoulder = halfShoulder;}
	//public void setChest(float chest){this.chest = chest;}	
	//public void setWaist(float waist){this.waist = waist;}	
	//public void setHip(float hip){this.hip = hip;}	
	//public void setSleeve(float sleeve){this.sleeve = sleeve;}
	//public void setFrontLength(float FrontLength){this.frontLength = frontLength;}
	//public void setBackLength(float BackLength){this.backLength = backLength;}
	//public void setPantOutseam(float pantOutseam){this.pantOutseam = pantOutseam;}
	//public void setPantInseam(float pantInseam){this.pantInseam = pantInseam;}
	//public void setThigh(float thigh){this.thigh = thigh;}
	//public void setCalf(float calf){this.calf = calf;}
	public void setMetric(String metric){this.metric = metric;}
	public void setSizeLabel(String sizeLabel){this.sizeLabel = sizeLabel;}
	
	//Getters
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public Integer getSizeLabelId(){return this.sizeLabelId;}
	public List<SizeChartSizeGroup> getSizeCharts(){return this.sizeCharts;}	
	//public float //(){return this.neck;}
	//public float getFullShoulder(){return this.fullShoulder;}
	//public float getHalfShoulder(){return this.halfShoulder;}
	//public float getChest(){return this.chest;}	
	//public float getWaist(){return this.waist;}	
	//public float getHip(){return this.hip;}	
	//public float getSleeve(){return this.sleeve;}
	//public float getFrontLength(){return this.frontLength;}
	//public float getBackLength(){return this.backLength;}
	//public float getPantOutseam(){return this.pantOutseam;}
	//public float getPantInseam(){return this.pantInseam;}
	//public float getThigh(){return this.thigh;}
	//public float getCalf(){return this.calf;}
	public Date getCreatedOn(){return this.createdOn;}
	public String getMetric(){return this.metric;}
	public String getSizeLabel(){return this.sizeLabel;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id))
			.append(indent).append("sizeLabelId:").append(((this.sizeLabelId == null) ? "null" : this.sizeLabelId))
			.append(indent).append("sizeLabel:").append(((this.sizeLabel == null) ? "null" : this.sizeLabel));
			//.append(indent).append("neck:").append(this.neck)
			//.append(indent).append("fullShoulder:").append(this.fullShoulder)
			//.append(indent).append("halfShoulder:").append(this.halfShoulder)
			//.append(indent).append("chest:").append(this.chest)
			//.append(indent).append("waist:").append(this.waist)
			//.append(indent).append("hip:").append(this.hip)
			//.append(indent).append("sleeve:").append(this.sleeve)
			//.append(indent).append("frontLength:").append(this.frontLength)
			//.append(indent).append("backLength:").append(this.backLength)
			//.append(indent).append("pantOutseam:").append(this.pantOutseam)
			//.append(indent).append("pantInseam:").append(this.pantInseam)
			//.append(indent).append("thigh:").append(this.thigh)
			//.append(indent).append("calf:").append(this.calf);
			//.append(indent).append("sizeCharts:").append(((this.sizeCharts == null) ? "null" : this.sizeCharts));
        return sb.toString();		
	}
}
