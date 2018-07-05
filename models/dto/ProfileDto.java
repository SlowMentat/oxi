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


@Relation(value = "profile", collectionRelation = "profiles")
public class ProfileDto extends ResourceSupport
{
	//private Long Id;
	private String alias;
	private String country;
	//private String city;
	//private String birthday;
	private int height;
	//private int chest;
	//private int waist;
	//private int hip;
	private List<Outfit> outfits;
	private List<Item> items;
	//private User user;

	public ProfileDto(){
		super();
	}
	
	//Getters
	//public Long getId(){return this.Id;}
	public String getAlias(){return this.alias;}	
	public String getCountry(){return this.country;}	
	//public String getCity(){return this.city;}	
	//public String getBirthday(){return this.birthday;}
	public int getHeight(){return this.height;}	
	//public int getChest(){return this.chest;}	
	//public int getWaist(){return this.waist;}	
	//public int getHip(){return this.hip;}
	public List<Outfit> getOutfits(){return this.outfits;}
	public List<Item> getItems(){return this.items;}
	//public User getUser(){return this.user;}

	//Setters
	//public void setId(Long id){this.Id = id;}
	public void setAlias(String alias){this.alias = alias;}	
	public void setCountry(String country){this.country = country;}	
	//public void setCity(String city){this.city = city;}	
	//public void setBirthday(String birthday){this.birthday = birthday;}	
	public void setHeight(int height){this.height = height;}	
	//public void setChest(int chest){this.chest = chest;}	
	//public void setWaist(int waist){this.waist = waist;}	
	//public void setHip(int hip){this.hip = hip;}	
	public void setOutfits(List<Outfit> outfits){this.outfits = outfits;}
	public void setItems(List<Item> item){this.items = items;}	
	//public void setUser(User user){	this.user = user;}
}