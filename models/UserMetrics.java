package oxi.models;

import oxi.models.dto.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
//import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;
//import oxi.jackson.*;
import org.apache.logging.log4j.Logger;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.LogManager;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="user_metrics")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=UserMetrics.class)
public class UserMetrics implements Serializable, Identifiable<UUID>
{
	@Transient
	private static final Logger logger = LogManager.getLogger(UserMetrics.class);
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	//@JsonProperty("id")
	private UUID id;

	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;
	private String bodyShape;
	@Column(nullable=false, columnDefinition="BOOLEAN default false")
	private boolean mens;
	@Column(nullable=false, columnDefinition="BOOLEAN default false")
	private boolean womens;
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

	@OneToOne(cascade=CascadeType.ALL)
	@JsonIdentityReference(alwaysAsId=true)
	private Profile profile;
	
	//Constructor
	public UserMetrics(){
	}

	public UserMetrics(UserMetricsDto userMetricsDto){
		this.bodyShape = userMetricsDto.getBodyShape();
		this.mens = userMetricsDto.getMens();
		this.womens = userMetricsDto.getWomens();
		this.height = userMetricsDto.getHeight();
		this.neck = userMetricsDto.getNeck();
		this.fullShoulder = userMetricsDto.getFullShoulder();
		this.halfShoulder = userMetricsDto.getHalfShoulder();
		this.chest = userMetricsDto.getChest();
		this.waist = userMetricsDto.getWaist();
		this.hip = userMetricsDto.getHip();
		this.sleeve = userMetricsDto.getSleeve();
		this.frontLength = userMetricsDto.getFrontLength();
		this.backLength = userMetricsDto.getBackLength();
		this.pantOutseam = userMetricsDto.getPantOutseam();
		this.pantInseam = userMetricsDto.getPantInseam();
		this.thigh = userMetricsDto.getThigh();
		this.calf = userMetricsDto.getCalf();
	}
	
	//Setters
	public void setId(UUID id){this.id = id;}
	
	public void setBodyShape(String bodyShape){this.bodyShape = bodyShape;}

	public void setMens(boolean mens){this.mens = mens;}
	
	public void setWomens(boolean womens){this.womens = womens;}

	public void setHeight(float height){this.height = height;}
	
	public void setNeck(float neck){this.neck = neck;}

	public void setFullShoulder(float fullShoulder){this.fullShoulder = fullShoulder;}

	public void setHalfShoulder(float halfShoulder){this.halfShoulder = halfShoulder;}

	public void setChest(float chest){this.chest = chest;}
	
	public void setWaist(float waist){this.waist = waist;}
	
	public void setHip(float hip){this.hip = hip;}
	
	public void setSleeve(float sleeve){this.sleeve = sleeve;}

	public void setFrontLength(float FrontLength){this.frontLength = frontLength;}

	public void setBackLength(float BackLength){this.backLength = backLength;}

	public void setPantOutseam(float pantOutseam){this.pantOutseam = pantOutseam;}

	public void setPantInseam(float pantInseam){this.pantInseam = pantInseam;}

	public void setThigh(float thigh){this.thigh = thigh;}

	public void setCalf(float calf){this.calf = calf;}

	public void setProfile(Profile profile){
		logger.warn("SETTING PROFILE");
		this.profile = profile;
		if (this.profile != null){		
			logger.warn("Profile POJO Not NULL");
			if(this.profile.getUserMetrics() != this){
				logger.warn("Linking Profile to UserMetrics");
				profile.setUserMetrics(this);
			}
		}
		else{
			logger.warn("!!Profile IS NULL!!");
		}
	}
	
	//Getters
	@Override
	public UUID getId(){return this.id;}

	public String getIdText(){return this.idText;}
	
	public String getBodyShape(){return this.bodyShape;}

	public boolean getMens(){return this.mens;}
	
	public boolean getWomens(){return this.womens;}

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

	public Profile getProfile(){return this.profile;}

	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building UserMetrics string");
        StringBuilder sb = new StringBuilder("\nid: ").append(((this.id == null) ? "null" : this.id.toString()))
			.append(indent).append("\nidText:").append(this.idText)
			.append(indent).append("\nbodyShape:").append(this.bodyShape)
			.append(indent).append("\nmens:").append(this.mens)
			.append(indent).append("\nwomens:").append(this.womens)
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
			.append(indent).append("\ncalf:").append(this.calf)
			.append(indent).append("\nprofile: {\n")
				.append(indent).append("    ").append("id: ")
				.append( ((this.profile != null && this.profile.getId() != null) ?  profile.getId().toString() : "null") )
				.append("\n").append(indent).append("}");
        return sb.toString();		
	}

	@Override
	public String toString(){
		return toString(0);
	}
}
