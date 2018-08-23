package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
//import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;


@Relation(value = "profile", collectionRelation = "profiles")
public class ProfileDto implements ProfileProjection 
{
	private String id;
	private String username;
	private String country;
	private String dateOfBirth;
	private String bodyShape;
	private boolean mens;
	private boolean womens;
	private float height;		//inches or cm
	private float neck;			//inches or cm
	private float fullShoulder;	//inches or cm
	private float halfShoulder;	//inches or cm
	private float chest;		//inches or cm
	private float waist;		//inches or cm
	private float hips;			//inches or cm
	private float sleeve;		//inches or cm
	private float frontLength;	//inches or cm
	private float backLength;	//inches or cm
	private float pantOutseam;	//inches or cm
	private float pantInseam;	//inches or cm
	private float thigh;			//inches or cm
	private float calf;			//inches or cm
	private List<OutfitDto> outfits;
	private List<ItemDto> items;
	private UserDto user;

	public ProfileDto(){
		super();
	}
	
	//Getters
	@Override
	public String getId(){return this.id;}

	public String getUsername(){return this.username;}

	public String getCountry(){return this.country;}
	
	public String getDateOfBirth(){return this.dateOfBirth;}
	
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

	public <T extends OutfitProjection> List<T> getOutfitsDto(){return this.outfits;}

	public <T extends ItemProjection> List<T> getItemsDto(){return this.items;}

	//Setters

	public void setId(String id){this.id = id;}
 
	public void setUsername(String username){this.username = username;}

	public void setCountry(String country){this.country = country;}
	
	public void setDateOfBirth(String dateOfBirth){this.dateOfBirth = dateOfBirth;}
	
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

	public <T extends OutfitProjection> void setOutfits(List<T> outfits){this.outfits = outfits;}

	public <T extends ItemProjection> void setItems(List<T> items){this.items = items;}
}