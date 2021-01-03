package oxi.models.dto.retailer;

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
import com.fasterxml.jackson.annotation.*;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;


@JsonRootName(value = "sizeGroup")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=SizeGroupDto.class)
public class SizeGroupDto implements Serializable/*Identifiable<String>*/
{
	private String id;
	private String sizeLabel;
	@JsonRawValue
	private String metric;

	//private float neck;
	//private float fullShoulder;
	//private float halfShoulder;
	//private float chest;
	//private float waist;
	//private float hip;
	//private float sleeve;
	//private float frontLength;
	//private float backLength;
	//private float pantOutseam;
	//private float pantInseam;
	//private float thigh;
	//private float calf;
	//private SizeChartDto sizeChartDto;

	public SizeGroupDto(){}

	public SizeGroupDto(String id){
		this.id = id;
	}

	public SizeGroupDto(
		String id, 
		String sizeLabel,
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
		float calf)
	{
		//super();
		this.id = id;
		this.sizeLabel = sizeLabel;

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
	}

	public SizeGroupDto(SizeGroup sizeGroup){
		this.id = sizeGroup.getIdText();
		//this.sizeLabel = sizeGroup.getSizeLabel();
		this.metric = sizeGroup.getMetric();
		this.sizeLabel = sizeGroup.getSizeLabel();
		//this.neck = sizeGroup.getNeck();
		//this.fullShoulder = sizeGroup.getFullShoulder();
		//this.halfShoulder = sizeGroup.getHalfShoulder();
		//this.chest = sizeGroup.getChest();
		//this.waist = sizeGroup.getWaist();
		//this.hip = sizeGroup.getHip();
		//this.sleeve = sizeGroup.getSleeve();
		//this.frontLength = sizeGroup.getFrontLength();
		//this.backLength = sizeGroup.getBackLength();
		//this.pantOutseam = sizeGroup.getPantOutseam();
		//this.pantInseam = sizeGroup.getPantInseam();
		//this.thigh = sizeGroup.getThigh();
		//this.calf = sizeGroup.getCalf();
	}

	//Setters
	public void setId(String id){this.id = id;}
	public void setSizeLabel(String sizeLabel){this.sizeLabel = sizeLabel;}	
	public void setMetric(String metric){this.metric = metric;}
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
	
	//Getters
	
	public String getId(){return this.id;}
	public String getSizeLabel(){return this.sizeLabel;}	
	public String getMetric(){return this.metric;}
	//public float getNeck(){return this.neck;}
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

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : this.id))
			.append(indent).append("sizeLabel : ").append(((this.sizeLabel == null) ? "null" : this.sizeLabel))
			.append(indent).append("metric : ").append(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(this.metric)));
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
        return sb.toString();		
	}
}
