package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;


@Relation(value = "user", collectionRelation = "users")
public class UserDto implements Serializable
{
	private String email;
	private String password;
	private String username;
	private Profile profile;

	public UserDto(){
		super();
	}

	public UserDto(String email, String password, String username){
		this.email = email;
		this.password = password;
		this.username = username;
	}

	//Getters
	//public UUID getId(){return this.Id;}
	public String getEmail(){return this.email;}
	public String getPassword(){return this.password;}
	public String getUsername(){return this.username;}
	public Profile getProfile(){return this.profile;}

	//Setters
	//public void setId(UUID id){this.Id = id;}
	/*public void setEmail(String email){this.email = email;}
	public void setPassword(String password){this.password = password;}
	public void setUsername(String username){this.username = username;}
	public void setProfile(Profile profile){this.profile = profile;}*/
}