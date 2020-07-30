package oxi.models;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
import java.util.Collection;
import java.io.Serializable;
import java.lang.*;
import javax.persistence.GeneratedValue;
import com.fasterxml.jackson.annotation.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.*;
import org.hibernate.annotations.GenericGenerator;


//@Entity 
//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public abstract class BaseAccount extends RelatedEntity /*implements Identifiable<UUID>*/{

	@Id
	//@JsonProperty("id")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	
	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;
	//@Email(message = "Invalid email")
	private String email;
	private String password;
	private boolean enabled;
	//@ManyToMany
    //@JoinTable( 
    //    name = "users_roles", 
    //    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
    //    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) 
    //private Collection<Role> roles;

    public BaseAccount(){

	}

	public BaseAccount(String email, String password){
		this.email = email;
		this.password = password;
		this.enabled = enabled;
	}


	//Setters
	//@Override
	public void setId(UUID id){this.id = id;}
	//public void setIdText(String idText){this.idText = idText;}
	public void setEmail(String email){this.email = email;}	
	public void setPassword(String password){this.password = password;}	
	public void setEnabled(boolean enabled){this.enabled = enabled;}
	//public void setRoles(Collection<Role> roles){this.roles = roles;}

	
	//Getters
	//@Override
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public String getEmail(){return this.email;}	
	public String getPassword(){return this.password;}
	public boolean getEnabled(){return this.enabled;}
	//public Collection<Role> getRoles(){return this.roles;}
}