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
public class ItemContentId implements Serializable{
	@Transient
	private static final Logger logger = LogManager.getLogger(Item.class);

	@Column(name = "item_id")
	private UUID itemId;

	@Column(name = "content_id")
	private UUID contentId;

	private ItemContentId(){}

	public ItemContentId(UUID itemId, UUID contentId){
		this.itemId = itemId;
		this.contentId = contentId;
	}

	//Getters
	public UUID getItemId(){return this.itemId;}
	public UUID getContentId(){return this.contentId;}

	//Setters
	public void setItemId(UUID itemId){this.itemId = itemId;}
	public void setContentId(UUID contentId){this.contentId = contentId;}

	
	
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder(indent).append("itemId: ").append(((this.itemId == null) ? "null" : this.itemId.toString()))
        	.append(indent).append("contentId: ").append(((this.contentId == null) ? "null" : this.contentId.toString()));
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
		ItemContentId that = (ItemContentId)object;
		return Objects.equals(itemId, that.itemId) && Objects.equals(contentId, that.contentId);
	}

	@Override 
	public int hashCode(){
		return Objects.hash(itemId, contentId);
	}
}