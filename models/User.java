package oxi.models;

import javax.persistence.*;
import java.util.List;
import java.io.Serializable;
import java.lang.*;
import com.fasterxml.jackson.annotation.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.Logger;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.LogManager;

@Entity
@Table(name="user")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=User.class)
public class User extends RelatedEntity implements Serializable{
	@Transient
	private static final Logger logger = LogManager.getLogger(User.class);
	
	@Id
	//@JsonProperty("id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long Id;
	
	//private byte[] picture;
	private String email;
	private String password;
	private String username;
	private boolean enabled;
	
	@OneToOne(mappedBy="user")
	@RestResource(rel="client_0")
	private Profile profile;
	
	//Constructor
	public User(){
	}
	
	//Setters
	public void setId(Long id){
		this.Id = id;
	}
	public void setEmail(String email){
		this.email = email;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public void setUsername(String username){
		this.username = username;
	}

	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}

	public void setProfile(Profile profile){
		this.profile = profile;
	}

	
	//Getters
	public Long getId(){
		return this.Id;
	}
	public String getEmail(){
		return this.email;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public String getUsername(){
		return this.username;
	}

	public boolean getEnabled(){
		return this.enabled;
	}
	
	public Profile getProfile(){
		return this.profile;
	}
}