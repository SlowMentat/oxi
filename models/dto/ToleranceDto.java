package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import javax.persistence.*;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.UUID;



public class ToleranceDto extends RelatedEntity implements Serializable, Identifiable<String>
{
	@Transient
	private static final Logger logger = LogManager.getLogger(ToleranceDto.class);

	@JsonProperty("id")
	private String id;

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

	private ProfileDto profileDto;
	
	
	//Constructor
	public ToleranceDto(){
	}

	public ToleranceDto(
		String id,
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
		ProfileDto profileDto
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
		this.profileDto = profileDto;
	}

	public ToleranceDto(Tolerance tolerance){
		this.id = tolerance.getId() == null ? null : tolerance.getId().toString();
		this.height = tolerance.getHeight();
		this.minNeck = tolerance.getMinNeck();
		this.minFullShoulder = tolerance.getMinFullShoulder();
		this.minHalfShoulder = tolerance.getMinHalfShoulder();
		this.minChest = tolerance.getMinChest();
		this.minWaist = tolerance.getMinWaist();
		this.minHip = tolerance.getMinHip();
		this.minSleeve = tolerance.getMinSleeve();
		this.minFrontLength = tolerance.getMinFrontLength();
		this.minBackLength = tolerance.getMinBackLength();
		this.minPantOutseam = tolerance.getMinPantOutseam();
		this.minPantInseam = tolerance.getMinPantInseam();
		this.minThigh = tolerance.getMinThigh();
		this.minCalf = tolerance.getMinCalf();
		this.maxNeck = tolerance.getMaxNeck();
		this.maxFullShoulder = tolerance.getMaxFullShoulder();
		this.maxHalfShoulder = tolerance.getMaxHalfShoulder();
		this.maxChest = tolerance.getMaxChest();
		this.maxWaist = tolerance.getMaxWaist();
		this.maxHip = tolerance.getMaxHip();
		this.maxSleeve = tolerance.getMaxSleeve();
		this.maxFrontLength = tolerance.getMaxFrontLength();
		this.maxBackLength = tolerance.getMaxBackLength();
		this.maxPantOutseam = tolerance.getMaxPantOutseam();
		this.maxPantInseam = tolerance.getMaxPantInseam();
		this.maxThigh = tolerance.getMaxThigh();
		this.maxCalf = tolerance.getMaxCalf();
	}
	
	//Setters

	public void setId(String id){this.id = id;}

	public void setHeight(float height){this.height = height;}

	public void setProfileDto(ProfileDto profileDto){
		logger.warn("SETTING ProfileDto");
		this.profileDto = profileDto;
		if (this.profileDto != null){		
			logger.warn("ProfileDto POJO Not NULL");
			if(this.profileDto.getToleranceDto() != this){
				logger.warn("LINKING Profile TO Tolerance");
				profileDto.setToleranceDto(this);
			}
		}
		else{
			logger.warn("!!PICTURE IS NULL!!");
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
	public String getId(){return this.id;}

	public float getHeight(){return this.height;}

	public ProfileDto getProfileDto(){return this.profileDto;}

	
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
}