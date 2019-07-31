/*package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.Objects;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.hateoas.*;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "UserRole")
@Table(name="users_roles")
public class UserRole extends RelatedEntity{
	@Transient
	private static final Logger logger = LogManager.getLogger(User.class);
	
	@EmbeddedId
	private UserRoleId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("id")
	private Role role;	

	@Column(name = "created_on")
	private Date createdOn = new Date();

	//Constructors 
	private UserRole(){}

	public UserRole(User user, Role role){
		this.user = user;
		this.role = role;
		this.id  = new UserRoleId(user.getId(), role.getId());
		this.createdOn = new Date();
	}

	//Getters
	public User getItem(){return this.user;}
	public Role getContent(){return this.role;}
	public Date getCreatedOn(){return this.createdOn;}

	//Setters
	public void setItem(User user){this.user = user;}
	public void setContent(Role role){this.role = role;}
	public void setCreatedOn(Date createdOn){this.createdOn = createdOn;}


	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Role string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append("{")
        	.append(((this.id == null) ? "null" : this.id.toString(indents + 1)))
        	.append(indent).append("}")
        	.append(indent).append("user: ").append(user.getId().toString())
			.append(indent).append("role: ").append(role.getId().toString());
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}

	@Override
	public boolean equals(Object object){
		//test object reference equivalence
		if(this == object) return true;
		//test for class association equivalence
		if(object == null || getClass() != object.getClass()) return false;
		//test further for equivalence by calling equals method of user and contents properties
		UserRole that = (UserRole) object;
		return Objects.equals(user, that.user) && Objects.equals(role, that.role);
	}

	@Override
	public int hashCode(){
		return Objects.hash(user, role);
	}
}*/