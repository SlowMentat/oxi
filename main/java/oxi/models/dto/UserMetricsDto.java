package oxi.models.dto;

import oxi.models.UserMetrics;
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


public class UserMetricsDto implements Serializable/*Identifiable<String>*/
{
	@Transient
	private static final Logger logger = LogManager.getLogger(UserMetricsDto.class);
	
	@JsonProperty("id")
	private String id;
	private String bodyShape;
	private boolean mens;
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

	
	//Constructor
	public UserMetricsDto(){
	}

	public UserMetricsDto(UserMetrics userMetrics){
		this.id = userMetrics.getIdText();
		this.bodyShape = userMetrics.getBodyShape();
		this.mens = userMetrics.getMens();
		this.womens = userMetrics.getWomens();
		this.height = userMetrics.getHeight();
		this.neck = userMetrics.getNeck();
		this.fullShoulder = userMetrics.getFullShoulder();
		this.halfShoulder = userMetrics.getHalfShoulder();
		this.chest = userMetrics.getChest();
		this.waist = userMetrics.getWaist();
		this.hip = userMetrics.getHip();
		this.sleeve = userMetrics.getSleeve();
		this.frontLength = userMetrics.getFrontLength();
		this.backLength = userMetrics.getBackLength();
		this.pantOutseam = userMetrics.getPantOutseam();
		this.pantInseam = userMetrics.getPantInseam();
		this.thigh = userMetrics.getThigh();
		this.calf = userMetrics.getCalf();
	}
	
	//Setters
	public void setId(String id){this.id = id;}
	
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

	public void setFrontLength(float frontLength){this.frontLength = frontLength;}

	public void setBackLength(float backLength){this.backLength = backLength;}

	public void setPantOutseam(float pantOutseam){this.pantOutseam = pantOutseam;}

	public void setPantInseam(float pantInseam){this.pantInseam = pantInseam;}

	public void setThigh(float thigh){this.thigh = thigh;}

	public void setCalf(float calf){this.calf = calf;}

	/*public void setProfile(Profile profile){
		logger.warn("SETTING PROFILE");
		this.profile = profile;
		if (this.profile != null){		
			logger.warn("Profile POJO Not NULL");
			if(this.profile.getUserMetrics() != this){
				logger.warn("Linking Profile to UserMetricsDto");
				profile.setUserMetrics(this);
			}
		}
		else{
			logger.warn("!!Profile IS NULL!!");
		}
	}*/
	
	//Getters
	
	public String getId(){return this.id;}
	
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

	/*public Profile getProfile(){return this.profile;}*/
}
