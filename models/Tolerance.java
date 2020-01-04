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
@Table(name="tolerance")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Tolerance.class)
public class Tolerance extends RelatedEntity implements Serializable, Identifiable<UUID>
{
	@Transient
	private static final Logger logger = LogManager.getLogger(Tolerance.class);
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	//@JsonProperty("id")
	private UUID id;

	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;
	
	private float height;
	private float minNeck;
	private float minFullShoulder;
	private float minHalfShoulder;
	private float minChest;
	private float minWaist;
	private float minHip;
	private float minSleeve;
	private float minFrontLength;
	private float minBackLength;
	private float minPantOutseam;
	private float minPantInseam;
	private float minThigh;
	private float minCalf;

	private float maxNeck;
	private float maxFullShoulder;
	private float maxHalfShoulder;
	private float maxChest;
	private float maxWaist;
	private float maxHip;
	private float maxSleeve;
	private float maxFrontLength;
	private float maxBackLength;
	private float maxPantOutseam;
	private float maxPantInseam;
	private float maxThigh;
	private float maxCalf;

	@OneToOne(cascade=CascadeType.ALL)
	@JsonIdentityReference(alwaysAsId=true)
	private Profile profile;
	
	
	//Constructor
	public Tolerance(){
	}

	public Tolerance(
		UUID id,
		float height,
		float minNeck,
		float minFullShoulder,
		float minHalfShoulder,
		float minChest,
		float minWaist,
		float minHip,
		float minSleeve,
		float minFrontLength,
		float minBackLength,
		float minPantOutseam,
		float minPantInseam,
		float minThigh,
		float minCalf,
		float maxNeck,
		float maxFullShoulder,
		float maxHalfShoulder,
		float maxChest,
		float maxWaist,
		float maxHip,
		float maxSleeve,
		float maxFrontLength,
		float maxBackLength,
		float maxPantOutseam,
		float maxPantInseam,
		float maxThigh,
		float maxCalf,
		Profile profile
		){
		this.id = id;
		this.height = height;
		this.minNeck = minNeck;
		this.minFullShoulder = minFullShoulder;
		this.minHalfShoulder = minHalfShoulder;
		this.minChest = minChest;
		this.minWaist = minWaist;
		this.minHip = minHip;
		this.minSleeve = minSleeve;
		this.minFrontLength = minFrontLength;
		this.minBackLength = minBackLength;
		this.minPantOutseam = minPantOutseam;
		this.minPantInseam = minPantInseam;
		this.minThigh = minThigh;
		this.minCalf = minCalf;
		this.maxNeck = maxNeck;
		this.maxFullShoulder = maxFullShoulder;
		this.maxHalfShoulder = maxHalfShoulder;
		this.maxChest = maxChest;
		this.maxWaist = maxWaist;
		this.maxHip = maxHip;
		this.maxSleeve = maxSleeve;
		this.maxFrontLength = maxFrontLength;
		this.maxBackLength = maxBackLength;
		this.maxPantOutseam = maxPantOutseam;
		this.maxPantInseam = maxPantInseam;
		this.maxThigh = maxThigh;
		this.maxCalf = maxCalf;
		this.profile = profile;
	}

	public Tolerance(ToleranceDto toleranceDto){
		this.id = toleranceDto.getId() == null || toleranceDto.getId().isEmpty() ? null : UUID.fromString(toleranceDto.getId());
		this.height = toleranceDto.getHeight();
		this.minNeck = toleranceDto.getMinNeck();
		this.minFullShoulder = toleranceDto.getMinFullShoulder();
		this.minHalfShoulder = toleranceDto.getMinHalfShoulder();
		this.minChest = toleranceDto.getMinChest();
		this.minWaist = toleranceDto.getMinWaist();
		this.minHip = toleranceDto.getMinHip();
		this.minSleeve = toleranceDto.getMinSleeve();
		this.minFrontLength = toleranceDto.getMinFrontLength();
		this.minBackLength = toleranceDto.getMinBackLength();
		this.minPantOutseam = toleranceDto.getMinPantOutseam();
		this.minPantInseam = toleranceDto.getMinPantInseam();
		this.minThigh = toleranceDto.getMinThigh();
		this.minCalf = toleranceDto.getMinCalf();
		this.maxNeck = toleranceDto.getMaxNeck();
		this.maxFullShoulder = toleranceDto.getMaxFullShoulder();
		this.maxHalfShoulder = toleranceDto.getMaxHalfShoulder();
		this.maxChest = toleranceDto.getMaxChest();
		this.maxWaist = toleranceDto.getMaxWaist();
		this.maxHip = toleranceDto.getMaxHip();
		this.maxSleeve = toleranceDto.getMaxSleeve();
		this.maxFrontLength = toleranceDto.getMaxFrontLength();
		this.maxBackLength = toleranceDto.getMaxBackLength();
		this.maxPantOutseam = toleranceDto.getMaxPantOutseam();
		this.maxPantInseam = toleranceDto.getMaxPantInseam();
		this.maxThigh = toleranceDto.getMaxThigh();
		this.maxCalf = toleranceDto.getMaxCalf();
	}
	
	//Setters

	public void setId(UUID id){this.id = id;}

	public void setHeight(float height){this.height = height;}

	public void setProfile(Profile profile){
		logger.warn("SETTING PROFILE");
		this.profile = profile;
		if (this.profile != null){		
			logger.warn("Profile POJO Not NULL");
			if(this.profile.getTolerance() != this){
				logger.warn("LINKING Profile TO Tolerance");
				profile.setTolerance(this);
			}
		}
		else{
			logger.warn("!!Profile IS NULL!!");
		}
	}	

	
	public void setMinNeck(float minNeck){this.minNeck = minNeck;}

	public void setMinFullShoulder(float minFullShoulder){this.minFullShoulder = minFullShoulder;}

	public void setMinHalfShoulder(float minHalfShoulder){this.minHalfShoulder = minHalfShoulder;}

	public void setMinChest(float minChest){this.minChest = minChest;}
	
	public void setMinWaist(float minWaist){this.minWaist = minWaist;}
	
	public void setMinHip(float minHip){this.minHip = minHip;}
	
	public void setMinSleeve(float minSleeve){this.minSleeve = minSleeve;}

	public void setMinFrontLength(float minFrontLength){this.minFrontLength = minFrontLength;}

	public void setMinBackLength(float minBackLength){this.minBackLength = minBackLength;}

	public void setMinPantOutseam(float minPantOutseam){this.minPantOutseam = minPantOutseam;}

	public void setMinPantInseam(float minPantInseam){this.minPantInseam = minPantInseam;}

	public void setMinThigh(float minThigh){this.minThigh = minThigh;}

	public void setMinCalf(float minCalf){this.minCalf = minCalf;}

	
	public void setMaxNeck(float maxNeck){this.maxNeck = maxNeck;}

	public void setMaxFullShoulder(float maxFullShoulder){this.maxFullShoulder = maxFullShoulder;}

	public void setMaxHalfShoulder(float maxHalfShoulder){this.maxHalfShoulder = maxHalfShoulder;}

	public void setMaxChest(float maxChest){this.maxChest = maxChest;}
	
	public void setMaxWaist(float maxWaist){this.maxWaist = maxWaist;}
	
	public void setMaxHip(float maxHip){this.maxHip = maxHip;}
	
	public void setMaxSleeve(float maxSleeve){this.maxSleeve = maxSleeve;}

	public void setMaxFrontLength(float maxFrontLength){this.maxFrontLength = maxFrontLength;}

	public void setMaxBackLength(float maxBackLength){this.maxBackLength = maxBackLength;}

	public void setMaxPantOutseam(float maxPantOutseam){this.maxPantOutseam = maxPantOutseam;}

	public void setMaxPantInseam(float maxPantInseam){this.maxPantInseam = maxPantInseam;}

	public void setMaxThigh(float maxThigh){this.maxThigh = maxThigh;}

	public void setMaxCalf(float maxCalf){this.maxCalf = maxCalf;}

	
	//Getters
	@Override
	public UUID getId(){return this.id;}

	public String getIdText(){return this.idText;}

	public float getHeight(){return this.height;}

	public Profile getProfile(){return this.profile;}

	
	public float getMinNeck(){return this.minNeck;}

	public float getMinFullShoulder(){return this.minFullShoulder;}

	public float getMinHalfShoulder(){return this.minHalfShoulder;}

	public float getMinChest(){return this.minChest;}
	
	public float getMinWaist(){return this.minWaist;}
	
	public float getMinHip(){return this.minHip;}
	
	public float getMinSleeve(){return this.minSleeve;}

	public float getMinFrontLength(){return this.minFrontLength;}

	public float getMinBackLength(){return this.minBackLength;}

	public float getMinPantOutseam(){return this.minPantOutseam;}

	public float getMinPantInseam(){return this.minPantInseam;}

	public float getMinThigh(){return this.minThigh;}

	public float getMinCalf(){return this.minCalf;}


	public float getMaxNeck(){return this.maxNeck;}

	public float getMaxFullShoulder(){return this.maxFullShoulder;}

	public float getMaxHalfShoulder(){return this.maxHalfShoulder;}

	public float getMaxChest(){return this.maxChest;}
	
	public float getMaxWaist(){return this.maxWaist;}
	
	public float getMaxHip(){return this.maxHip;}
	
	public float getMaxSleeve(){return this.maxSleeve;}

	public float getMaxFrontLength(){return this.maxFrontLength;}

	public float getMaxBackLength(){return this.maxBackLength;}

	public float getMaxPantOutseam(){return this.maxPantOutseam;}

	public float getMaxPantInseam(){return this.maxPantInseam;}

	public float getMaxThigh(){return this.maxThigh;}

	public float getMaxCalf(){return this.maxCalf;}


	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Tolerance string");
        StringBuilder sb = new StringBuilder("\nid: ").append(((id == null) ? "null" : id.toString()))
			.append(indent).append("\nheight:").append(this.height)
			.append(indent).append("\nminNeck:").append(this.minNeck)
			.append(indent).append("\nminFullShoulder:").append(this.minFullShoulder)
			.append(indent).append("\nminHalfShoulder:").append(this.minHalfShoulder)
			.append(indent).append("\nminChest:").append(this.minChest)
			.append(indent).append("\nminWaist:").append(this.minWaist)
			.append(indent).append("\nminHip:").append(this.minHip)
			.append(indent).append("\nminSleeve:").append(this.minSleeve)
			.append(indent).append("\nminFrontLength:").append(this.minFrontLength)
			.append(indent).append("\nminBackLength:").append(this.minBackLength)
			.append(indent).append("\nminPantOutseam:").append(this.minPantOutseam)
			.append(indent).append("\nminPantInseam:").append(this.minPantInseam)
			.append(indent).append("\nminThigh:").append(this.minThigh)
			.append(indent).append("\nminCalf:").append(this.minCalf)
			.append(indent).append("\nmaxNeck:").append(this.maxNeck)
			.append(indent).append("\nmaxFullShoulder:").append(this.maxFullShoulder)
			.append(indent).append("\nmaxHalfShoulder:").append(this.maxHalfShoulder)
			.append(indent).append("\nmaxChest:").append(this.maxChest)
			.append(indent).append("\nmaxWaist:").append(this.maxWaist)
			.append(indent).append("\nmaxHip:").append(this.maxHip)
			.append(indent).append("\nmaxSleeve:").append(this.maxSleeve)
			.append(indent).append("\nmaxFrontLength:").append(this.maxFrontLength)
			.append(indent).append("\nmaxBackLength:").append(this.maxBackLength)
			.append(indent).append("\nmaxPantOutseam:").append(this.maxPantOutseam)
			.append(indent).append("\nmaxPantInseam:").append(this.maxPantInseam)
			.append(indent).append("\nmaxThigh:").append(this.maxThigh)
			.append(indent).append("\nmaxCalf:").append(this.maxCalf)
			.append(indent).append("\nprofile: {\n")
				.append(indent).append("    ").append("id: ")
				.append( ((this.profile != null && this.profile.getId() != null) ?  profile.getId().toString() : "null") )
				.append("\n").append(indent).append("}");
		//sb.append("\nprofile: ").append(((profile.getId() == null) ? "null" : profile.getId().toString()));
        return sb.toString();		
	}

	@Override
	public String toString(){
		return toString(0);
	}

}


