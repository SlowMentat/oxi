package oxi.models;

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
import org.springframework.hateoas.*;
import org.apache.logging.log4j.LogManager;

import org.hibernate.annotations.GenericGenerator;


//@Entity
//@Table(name="privilege")
@MappedSuperclass
public class BasePrivilege implements Serializable, Identifiable<Long>{
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; 
    private String name;
 
    //@ManyToMany(mappedBy = "privileges")
    //private Collection<Role> roles;

    public BasePrivilege(){

    }

    public BasePrivilege(String name){
    	this.name = name;
    }
    //Getter
	public Long getId(){return this.id;}
    public String getName(){return this.name;}
	//public Collection<Role> getRoles(){return this.roles;}

    //Setter
	public void setId(Long id){this.id = id;}
    public void setName(String name){this.name = name;}
    /*public void setRoles(List<Role> roles){
		this.roles = this.<Role, BasePrivilege>setManyToManyParents(roles, this.roles, this);
	}


	@Override 
	public <T extends Relational> void internalAddChild(T targetChild){
		if(this.roles == null){
			logger.debug("instantiating new List<T>");
			this.roles = new ArrayList<Role>();
		}
		this.roles.add((Role)targetChild);
	}
	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		this.roles.remove((Role)targetChild);
	}*/
}