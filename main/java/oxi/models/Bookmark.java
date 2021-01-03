package oxi.models;

import javax.persistence.*;
import javax.persistence.CascadeType;
import java.util.Objects;
import java.util.Date;
import java.util.UUID;
import java.io.Serializable;
import java.lang.*;
import org.springframework.data.rest.core.annotation.*;
import com.fasterxml.jackson.annotation.*;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators.*;
import org.springframework.hateoas.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.hibernate.annotations.GenericGenerator;

////import oxi.models.projection.BookmarkProjection;

@Entity
@Table(name="bookmark")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="Bookmark_id")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Bookmark.class)
public class Bookmark extends RelatedEntity implements Serializable/*Identifiable<UUID>*/{
	@Transient
	private static final Logger logger = LogManager.getLogger(Bookmark.class);

	@Id
	@JsonProperty("id")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "id_text", updatable = false, insertable = false)
	private String idText;
	private String username;
	@Column(name="item_id", columnDefinition = "BINARY(16)")
	private UUID itemId;
	@Column(name = "item_id_text", updatable = false, insertable = false)
	private String itemIdText;
	@Column(name = "created_on")
	private Date createdOn = new Date();
	
	
	//Constructor
	public Bookmark(){
	}

	public Bookmark(UUID id, String username, UUID itemId){
		super();
		this.id = id;
		this.username = username;
		this.itemId = itemId;
		this.createdOn = new Date();
	}
	
	//Setters==========================================================================	
	public void setId(UUID id){this.id = id;}
	public void setUsername(String username){this.username = username;}	
	public void setItemId(UUID itemId){this.itemId = itemId;}
	public void setCreatedOn(Date createdOn){this.createdOn = createdOn;}
	
	
	//Getters==========================================================================	
	public UUID getId(){return this.id;}
	public String getIdText(){return this.idText;}
	public String getUsername(){return this.username;}
	public UUID getItemId(){return this.itemId;}
	public String getItemIdText(){return this.itemIdText;}
	public Date getCreatedOn(){return this.createdOn;}

	//@Override 
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		logger.debug("building Bookmark string");
        StringBuilder sb = new StringBuilder(indent).append("id: ").append((this.id == null ? "null" : this.id))
			.append(indent).append("username: ").append(this.username)
			.append(indent).append("itemId: ").append(this.itemId);
        return sb.toString();
	}

	@Override
	public String toString(){
		return toString(0);
	}


	@Override
	public boolean equals(Object object){
		if(this == object) return true;
		if(this == null || getClass() != object.getClass()) return false;
		Bookmark bookmark = (Bookmark) object;
		return Objects.equals(id, bookmark.getId());
	}

	@Override
	public int hashCode(){
		return Objects.hash(id);
	}
}