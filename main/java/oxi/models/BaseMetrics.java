package oxi.models;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
import java.util.Collection;
import java.io.Serializable;
import java.lang.*;
import com.fasterxml.jackson.annotation.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.Logger;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.LogManager;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public class BaseMetrics implements Serializable/*, Identifiable<.*>*/
{
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	//@JsonProperty("id")
	private UUID id;

	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;

	private float height;
	private float neck;
	private float fullShoulder;
	private float halfShoulder;
	private float chest;
	private float waist;
	private float hip;
	private float sleeve;
	private float frontLength;
	private float backLength;
	private float pantOutseam;
	private float pantInseam;
	private float thigh;
	private float calf;
	
	//Constructor
	public BaseMetrics(){
	}
	
	//Setters
	public void setId(UUID id){this.id = id;}

	public void setHeight(float height){this.height = height;}
	
	public void setNeck(float neck){this.neck = neck;}

	public void setFullShoulder(float fullShoulder){this.fullShoulder = fullShoulder;}

	public void setHalfShoulder(float halfShoulder){this.halfShoulder = halfShoulder;}

	public void setChest(float chest){this.chest = chest;}
	
	public void setWaist(float waist){this.waist = waist;}
	
	public void setHip(float hip){this.hip = hip;}
	
	public void setSleeve(float sleeve){this.sleeve = sleeve;}

	public void setFrontLength(float frontLength){this.frontLength = frontLength;}

	public void setBackLength(float backLength){this.backLength = backLength;}

	public void setPantOutseam(float pantOutseam){this.pantOutseam = pantOutseam;}

	public void setPantInseam(float pantInseam){this.pantInseam = pantInseam;}

	public void setThigh(float thigh){this.thigh = thigh;}

	public void setCalf(float calf){this.calf = calf;}
	
	//Getters
	//@Override
	public UUID getId(){return this.id;}

	public String getIdText(){return this.idText;}

	public float getHeight(){return this.height;}
	
	public float getNeck(){return this.neck;}

	public float getFullShoulder(){return this.fullShoulder;}

	public float getHalfShoulder(){return this.halfShoulder;}

	public float getChest(){return this.chest;}
	
	public float getWaist(){return this.waist;}
	
	public float getHip(){return this.hip;}
	
	public float getSleeve(){return this.sleeve;}

	public float getFrontLength(){return this.frontLength;}

	public float getBackLength(){return this.backLength;}

	public float getPantOutseam(){return this.pantOutseam;}

	public float getPantInseam(){return this.pantInseam;}

	public float getThigh(){return this.thigh;}

	public float getCalf(){return this.calf;}


	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder("\nid: ").append(((this.id == null) ? "null" : this.id.toString()))
			.append(indent).append("\nidText:").append(this.idText)
			.append(indent).append("\nheight:").append(this.height)
			.append(indent).append("\nneck:").append(this.neck)
			.append(indent).append("\nfullShoulder:").append(this.fullShoulder)
			.append(indent).append("\nhalfShoulder:").append(this.halfShoulder)
			.append(indent).append("\nchest:").append(this.chest)
			.append(indent).append("\nwaist:").append(this.waist)
			.append(indent).append("\nhip:").append(this.hip)
			.append(indent).append("\nsleeve:").append(this.sleeve)
			.append(indent).append("\nfrontLength:").append(this.frontLength)
			.append(indent).append("\nbackLength:").append(this.backLength)
			.append(indent).append("\npantOutseam:").append(this.pantOutseam)
			.append(indent).append("\npantInseam:").append(this.pantInseam)
			.append(indent).append("\nthigh:").append(this.thigh)
			.append(indent).append("\ncalf:").append(this.calf);
        return sb.toString();		
	}

	@Override
	public String toString(){
		return toString(0);
	}
}
