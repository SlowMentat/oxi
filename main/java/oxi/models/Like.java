//	package oxi.models;
//	
//	import javax.persistence.*;
//	import javax.persistence.CascadeType;
//	import java.util.Date;
//	import java.util.Objects;
//	import java.util.UUID;
//	import java.io.Serializable;
//	import java.lang.*;
//	import org.springframework.data.rest.core.annotation.*;
//	import com.fasterxml.jackson.annotation.*;
//	//import com.fasterxml.jackson.annotation.ObjectIdGenerators.*;
//	import org.springframework.hateoas.*;
//	
//	import org.apache.logging.log4j.Logger;
//	import org.apache.logging.log4j.LogManager;
//	
//	import org.hibernate.annotations.GenericGenerator;
//	
//	//import oxi.models.projection.LikeProjection;
//	
//	@Entity
//	@Table(name="likes")
//	//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="Like_id")
//	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Like.class)
//	public class Like extends RelatedEntity implements Serializable/*, Identifiable<.*>*/{
//		@Transient
//		private static final Logger logger = LogManager.getLogger(Like.class);
//	
//		@Id
//		@JsonProperty("id")
//		@GeneratedValue(generator = "uuid2")
//		@GenericGenerator(name = "uuid2", strategy = "uuid2")
//		@Column(columnDefinition = "BINARY(16)")
//		private UUID id;
//	
//		@Column(name = "id_text", updatable = false, insertable = false)
//		private String idText;
//		private String username;
//		@Column(name="outfit_id", columnDefinition = "BINARY(16)")
//		private UUID outfitId;
//		@Column(name = "outfit_id_text", updatable = false, insertable = false)
//		private String outfitIdText;
//		@Column(name = "created_on")
//		private Date createdOn = new Date();
//		
//		
//		//Constructor
//		public Like(){
//		}
//	
//		public Like(UUID id, String username, UUID outfitId){
//			super();
//			this.id = id;
//			this.username = username;
//			this.outfitId = outfitId;
//			this.createdOn = new Date();
//		}
//		
//		//Setters==========================================================================	
//		public void setId(UUID id){this.id = id;}
//		public void setUsername(String username){this.username = username;}	
//		public void setItemId(UUID outfitId){this.outfitId = outfitId;}
//		public void setCreatedOn(Date createdOn){this.createdOn = createdOn;}
//		
//		
//		//Getters==========================================================================	
//		public UUID getId(){return this.id;}
//		public String getIdText(){return this.idText;}
//		public String getUsername(){return this.username;}
//		public UUID getOutfitId(){return this.outfitId;}
//		public String getOutfitIdText(){return this.outfitIdText;}
//		public Date getCreatedOn(){return this.createdOn;}
//	
//		//@Override 
//		public String toString(int indents){
//			String indent = "\n";
//			for(int i = 0; i < indents; i++){
//				indent += "    ";
//			}
//			logger.debug("building Like string");
//	        StringBuilder sb = new StringBuilder(indent).append("id: ").append((this.id == null ? "null" : this.id))
//				.append(indent).append("username: ").append(this.username)
//				.append(indent).append("outfitId: ").append(this.outfitId);
//	        return sb.toString();
//		}
//	
//		@Override
//		public String toString(){
//			return toString(0);
//		}
//	
//	
//		@Override
//		public boolean equals(Object object){
//			if(this == object) return true;
//			if(this == null || getClass() != object.getClass()) return false;
//			Like like = (Like) object;
//			return Objects.equals(id, like.getId());
//		}
//	
//		@Override
//		public int hashCode(){
//			return Objects.hash(id);
//		}
//	}