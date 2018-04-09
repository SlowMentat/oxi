package oxi.models;

import javax.persistence.*;
import java.util.List;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="user_id", scope=User.class)
public class User extends ResourceSupport implements Serializable{
	@Transient
	private static final Logger logger = LogManager.getLogger(User.class);
	
	@Id
	@JsonProperty("user_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long user_id;
	
	//private byte[] picture;
	private String email;
	private String password;
	private String username;
	
	@OneToOne(mappedBy="user")
	@RestResource(rel="client_0")
	private Profile profile;
	
	//Constructor
	public User(){
	}
	
	//Setters
	public void setEmail(String email){
		this.email = email;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public void setProfile(Profile profile){
		this.profile = profile;
	}
	
	public void setUsername(String username){
		this.username = username;
	}

	
	//Getters
	public String getEmail(){
		return this.email;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public Profile getProfile(){
		return this.profile;
	}
	
	public String getUsername(){
		return this.username;
	}
}