package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
//import org.springframework.hateoas.core.*;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.Identifiable;


public class UserDto implements Serializable/*, Identifiable<.*>*/
{
	private String email;
	private String password;
	private String username;
	private Profile profile;

	public UserDto(){
	}

	public UserDto(String email, String password, String username){
		this.email = email;
		this.password = password;
		this.username = username;
	}

	//Getters
	//@Override
	public String getId(){return null;}
	public String getEmail(){return this.email;}
	public String getPassword(){return this.password;}
	public String getUsername(){return this.username;}
	public Profile getProfile(){return this.profile;}

	//Setters
	//public void setId(UUID id){this.Id = id;}
	public void setEmail(String email){this.email = email;}
	public void setPassword(String password){this.password = password;}
	public void setUsername(String username){this.username = username;}
	public void setProfile(Profile profile){this.profile = profile;}
}