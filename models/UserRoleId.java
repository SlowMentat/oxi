package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
/*import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.util.Objects;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.hateoas.*;

import org.hibernate.annotations.GenericGenerator;

@Embeddable
public class UserRoleId implements Serializable{

	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "role_id")
	private Long roleId;

	private UserRoleId(){}

	public UserRoleId(UUID userId, Long roleId){
		this.userId = userId;
		this.roleId = roleId;
	}

	//Getters
	public UUID getUserId(){return this.userId;}
	public Long getRoleId(){return this.roleId;}

	//Setters
	public void setUserId(UUID userId){this.userId = userId;}
	public void setRoleId(Long roleId){this.roleId = roleId;}

	
	
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("userId: ").append(((this.userId == null) ? "null" : this.userId.toString()))
        	.append(indent).append("roleId: ").append(((this.roleId == null) ? "null" : this.roleId.toString()));
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}

	@Override
	public boolean equals(Object object){
		if(this == object) return true;
		if(object == null || getClass() != object.getClass()) return false;
		UserRoleId that = (UserRoleId)object;
		return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
	}

	@Override 
	public int hashCode(){
		return Objects.hash(userId, roleId);
	}
}*/