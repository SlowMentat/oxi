package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.List;
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
public class AccountId implements Serializable{

	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "company_id")
	private UUID companyId;

	private AccountId(){}

	public AccountId(UUID userId, UUID companyId){
		this.userId = userId;
		this.companyId = companyId;
	}

	//Getters
	public UUID getUserId(){return this.userId;}
	public UUID getCompanyId(){return this.companyId;}

	//Setters
	public void setUserId(UUID userId){this.userId = userId;}
	public void setCompanyId(UUID companyId){this.companyId = companyId;}

	
	
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("userId: ").append(((this.userId == null) ? "null" : this.userId.toString()))
        	.append(indent).append("companyId: ").append(((this.companyId == null) ? "null" : this.companyId.toString()));
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
		AccountId that = (AccountId)object;
		return Objects.equals(userId, that.userId) && Objects.equals(companyId, that.companyId);
	}

	@Override 
	public int hashCode(){
		return Objects.hash(userId, companyId);
	}
}