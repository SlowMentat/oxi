package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;


public class ProfileDto extends RelatedEntity implements Serializable, Identifiable<String>
{
	//private Long Id;
	private String id;
	private String username;
	private String country;
	private String dateOfBirth;
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
	/*private List<OutfitDto> outfits;
	private List<ItemDto> items;
	private UserDto user;*/

	public ProfileDto(){
	}

	public ProfileDto(
		String id,
		String username,
		String country,
		String dateOfBirth,
		String bodyShape,
		boolean mens, 
		boolean womens,
		float heigh,
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
		float calf
		//List<OutfitDto> outfits,
		//List<ItemDto> items
		/*UserDto user*/){
		this.id = id;
		this.username = username;
		this.country = country;
		this.dateOfBirth = dateOfBirth;
		this.bodyShape = bodyShape;
		this.mens = mens;
		this.womens = womens;
		this.height = height;
		this.neck = neck;
		this.fullShoulder = fullShoulder;
		this.halfShoulder = halfShoulder;
		this.chest = chest;
		this.waist = waist;
		this.hip = hip;
		this.sleeve = sleeve;
		this.frontLength = frontLength;
		this.backLength = backLength;
		this.pantOutseam = pantOutseam;
		this.pantInseam = pantInseam;
		this.thigh = thigh;
		this.calf = calf;
		//this.outfits = outfits;
		//this.items = items;
		//this.user = user;
	}
	
	
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

	public void setFrontLength(float FrontLength){this.frontLength = frontLength;}

	public void setBackLength(float BackLength){this.backLength = backLength;}

	public void setPantOutseam(float pantOutseam){this.pantOutseam = pantOutseam;}

	public void setPantInSeam(float pantInseam){this.pantInseam = pantInseam;}

	public void setThigh(float thigh){this.thigh = thigh;}

	public void setCalf(float calf){this.calf = calf;}

	/*public void setItems(List<ItemDto> items){this.items = items;}	

	public void setUser(UserDto user){this.user = user;}

	public void setOutfits(List<OutfitDto> outfits){this.outfits = outfits;}*/
	
	
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

	/*public List<OutfitDto> getOutfits(){return this.outfits;}

	public UserDto getUser(){return this.user;}

	public List<ItemDto> getItems(){return this.items;}	*/	
}