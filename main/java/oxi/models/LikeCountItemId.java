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
public class LikeCountItemId implements Serializable{
	//@Transient
	//private static final Logger logger = LogManager.getLogger(Item.class);

	@Column(name = "like_count_id")
	private UUID likeCountId;

	@Column(name = "item_id")
	private UUID itemId;

	private LikeCountItemId(){}

	public LikeCountItemId(UUID likeCountId, UUID itemId){
		this.likeCountId = likeCountId;
		this.itemId = itemId;
	}

	//Getters
	public UUID getLikeCountId(){return this.likeCountId;}
	public UUID getItemId(){return this.itemId;}

	//Setters
	public void setLikeCountId(UUID likeCountId){this.likeCountId = likeCountId;}
	public void setItemId(UUID itemId){this.itemId = itemId;}

	
	
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("likeCountId: ").append(((this.likeCountId == null) ? "null" : this.likeCountId.toString()))
        	.append(indent).append("itemId: ").append(((this.itemId == null) ? "null" : this.itemId.toString()));
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
		LikeCountItemId that = (LikeCountItemId)object;
		return Objects.equals(likeCountId, that.likeCountId) && Objects.equals(itemId, that.itemId);
	}

	@Override 
	public int hashCode(){
		return Objects.hash(likeCountId, itemId);
	}
}