package oxi.models;

import oxi.models.dto.UserDto;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
import java.util.Collection;
import java.io.Serializable;
import java.lang.*;
import com.fasterxml.jackson.annotation.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

//import org.springframework.security.core.userdetails;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="user")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=User.class)
public class User extends BaseAccount/*RelatedEntity*/ /*implements Serializable,  Identifiable<UUID>*/{
	@Transient
	private static final Logger logger = LogManager.getLogger(User.class);
	
	//@Id
	////@JsonProperty("id")
	//@GeneratedValue(generator = "uuid2")
	//@GenericGenerator(name = "uuid2", strategy = "uuid2")
	//@Column(columnDefinition = "BINARY(16)")
	//private UUID id;
	//
	//@Column(name = "id_text", updatable = false, insertable = false)
	//private String idText;	
	////private byte[] picture;
	//private String email;
	//private String password;
	private String username;
	//private boolean enabled;
    ////private boolean tokenExpired;
	
	@OneToOne(mappedBy="user")
	@RestResource(rel="client_0")
	private Profile profile;
	
	@ManyToMany
    @JoinTable( 
        name = "users_roles", 
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) 
    private Collection<Role> roles;

	//Constructor
	public User(){
		super();
		//this.enabled = false;
	}

	public User(UserDto userDto){
		super();
		this.username = userDto.getUsername();
	}


	public User(User user){
		super(user.getUsername(), user.getPassword());
		this.profile = user.getProfile();
	}
	
	
	//Setters
	//@Override
	//public void setId(UUID id){this.id = id;}
	////public void setIdText(String idText){this.idText = idText;}
	//public void setEmail(String email){this.email = email;}	
	//public void setPassword(String password){this.password = password;}	
	public void setUsername(String username){this.username = username;}
	//public void setEnabled(boolean enabled){this.enabled = enabled;}
	public void setProfile(Profile profile){this.profile = profile;}
	//public void setTokenExpired(boolean isExpired){this.tokenExpired = isExpired};
	public void setRoles(Collection<Role> roles){this.roles = roles;}
	/*public void setRoles(List<Role> roles){
		this.roles = this.<Role, User>setManyToManyParents(roles, this.roles, this);
	}*/

	
	//Getters
	//@Override
	//public UUID getId(){return this.id;}
	//public String getIdText(){return this.idText;}
	//public String getEmail(){return this.email;}	
	//public String getPassword(){return this.password;}	
	public String getUsername(){return this.username;}
	//public boolean getEnabled(){return this.enabled;}	
	public Profile getProfile(){return this.profile;}
	//public boolean getTokenExpired(){return this.tokenExpired;}
	public Collection<Role> getRoles(){return this.roles;}
	
	//Add User instance to Profile's parent reference
	/*@Override 
	public <T extends Relational> void internalAddChild(T targetChild){
		if(this.profile == null){
			logger.debug("instantiating new Profile");
			this.profile = new ArrayList<Role>();
		}
		this.profile.add((User)targetChild);
	}
	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		this.profile.remove((User)targetChild);
	}*/
}