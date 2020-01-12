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

@Entity(name = "ItemContent")
@Table(name="item_content")
public class ItemContent extends RelatedEntity{
	@Transient
	private static final Logger logger = LogManager.getLogger(Item.class);
	
	@EmbeddedId
	private ItemContentId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("itemId")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("contentId")
	private Content content;	

	@Column(name = "created_on")
	private Date createdOn = new Date();
	@Column(name = "size_label_id")
	private Integer sizeLabelId;
	private Float positionx;
	private Float positiony;

	//Constructors 
	private ItemContent(){}

	public ItemContent(Item item, Content content){
		this.item = item;
		this.content = content;
		this.id  = new ItemContentId(item.getId(), content.getId());
		this.createdOn = new Date();
	}

	//Getters
	public Item getItem(){return this.item;}
	public Content getContent(){return this.content;}
	public Date getCreatedOn(){return this.createdOn;}
	public Integer getSizeLabelId(){return this.sizeLabelId;}
	public Float getPositionx(){return this.positionx;}
	public Float getPositiony(){return this.positiony;}

	//Setters
	public void setItem(Item item){this.item = item;}
	public void setContent(Content content){this.content = content;}
	public void setCreatedOn(Date createdOn){this.createdOn = createdOn;}
	public void setSizeLabelId(Integer sizeLabelId){this.sizeLabelId = sizeLabelId;}
	public void setPositionx(Float positionx){this.positionx = positionx;}
	public void setPositiony(Float positiony){this.positiony = positiony;}


	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Content string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append("{")
        	.append(((this.id == null) ? "null" : this.id.toString(indents + 1)))
        	.append(indent).append("}")
        	.append(indent).append("item: ").append(item.getId().toString())
			.append(indent).append("content: ").append(content.getId().toString())
			.append(indent).append("sizeLabelId: ").append(sizeLabelId == null ? "null" : sizeLabelId.toString())
        	.append(indent).append("positionx: ").append(positionx.toString())
			.append(indent).append("positiony: ").append(positionx.toString());
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
		//test further for equivalence by calling equals method of item and contents properties
		ItemContent that = (ItemContent) object;
		return Objects.equals(item, that.item) && Objects.equals(content, that.content);
	}

	@Override
	public int hashCode(){
		return Objects.hash(item, content);
	}
}