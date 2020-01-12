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
@Table(name="company_privilege")
public class CompanyPrivilege extends BasePrivilege{//} implements Serializable, Identifiable<Long>{
 
    @ManyToMany(mappedBy = "privileges")
    private Collection<CompanyRole> companyRoles;

    public CompanyPrivilege(){
    	super();
    }

    public CompanyPrivilege(String name){
    	super(name);
    }
    //Getter
	//public Long getId(){return this.id;}
    //public String getName(){return this.name;}
	public Collection<CompanyRole> getCompanyRoles(){return this.companyRoles;}

    //Setter
	//public void setId(Long id){this.id = id;}
    //public void setName(String name){this.name = name;}
    /*public void setRoles(List<Role> roles){
		this.roles = this.<Role, Privilege>setManyToManyParents(roles, this.roles, this);
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