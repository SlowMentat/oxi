package oxi.models;

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

@Entity(name = "LikeCountItem")
@Table(name="like_count_item")
public class LikeCountItem extends RelatedEntity{
	@Transient
	private static final Logger logger = LogManager.getLogger(LikeCountItem.class);
	
	@EmbeddedId
	private LikeCountItemId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("likeCountId")
	private LikeCount likeCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("itemId")
	private Item item;	

	//@Column(name = "created_on")
	//private Date createdOn = new Date();

	//Constructors 
	private LikeCountItem(){}

	public LikeCountItem(Item item, LikeCount likeCount){
		this.likeCount = likeCount;
		this.item = item;
		this.id  = new LikeCountItemId(likeCount.getId(), item.getId());
		//this.createdOn = new Date();
	}

	//Getters
	public LikeCount getLikeCount(){return this.likeCount;}
	public Item getItem(){return this.item;}
	//public Date getCreatedOn(){return this.createdOn;}

	//Setters
	public void setLikeCount(LikeCount likeCount){this.likeCount = likeCount;}
	public void setItem(Item item){this.item = item;}
	//public void setCreatedOn(Date createdOn){this.createdOn = createdOn;}


	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Item string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append("{")
        	.append(((this.id == null) ? "null" : this.id.toString(indents + 1)))
        	.append(indent).append("}")
        	.append(indent).append("likeCount: ").append(likeCount.getId().toString())
			.append(indent).append("item: ").append(item.getId().toString());
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
		//test further for equivalence by calling equals method of likeCount and contents properties
		LikeCountItem that = (LikeCountItem) object;
		return Objects.equals(likeCount, that.likeCount) && Objects.equals(item, that.item);
	}

	@Override
	public int hashCode(){
		return Objects.hash(likeCount, item);
	}
}