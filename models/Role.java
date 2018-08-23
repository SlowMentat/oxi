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

@Entity
@Table(name="role")
public class Role extends RelatedEntity implements RoleDao
{  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    private String name;

    public Role(){

    }
    public Role(String name){
    	this.name = name;
    }

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
	    
	@ManyToMany
	@JoinTable(
		name = "roles_privileges", 
		joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
	private Collection<Privilege> privileges;   

	//Getter
	@Override
	public Long getId(){return this.id;}
	public String getName(){return this.name;}
	public <T extends PrivilegeDao> Collection<T> getPrivileges(){return this.privileges;}

	//Setter
	public void setId(Long id){this.id = id;}
	public void setName(String name){this.name = name;}
	public <T extends PrivilegeDao> void setPrivileges(Collection<T> privileges){this.privileges = privileges;}
	/*public void setContents(List<Privilege> privileges){
		this.privileges = this.<Privilege, Role>setManyToManyParents(privileges, this.privileges, this);
	}
	public void setUsers(List<User> users){
		this.users = this.<User, Role>setManyToManyParents(users, this.users, this);
	}

	//
	@Override 
	public <T extends Relational> void internalAddChild(T targetChild){
		if(this.contents == null){
			logger.debug("instantiating new List<T>");
			this.contents = new ArrayList<Content>();
		}
		this.contents.add((Content)targetChild);
	}
	@Override
	public <T extends Relational> void internalRemoveChild(T targetChild){
		this.contents.remove((Content)targetChild);
	}*/
}