package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.dto.ContentDto;
import oxi.models.dto.OutfitDto;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ContentWithOutfitDto.class)
public class ContentWithOutfitDto implements Serializable, Identifiable<String>
{
	private static final Logger logger = LogManager.getLogger(ContentDto.class);
	@JsonProperty("id")
	private String id;
	private String coverpicuri;
	private String comments;
	private String username;
	private String outfitId;
	private LikeCountDto likeCount;
	//private List<ContentDto> contents;
	//private List<OutfitDto> outfits;

	public ContentWithOutfitDto(Content content, Outfit outfit, LikeCount likeCount){
		if(content != null && outfit != null){
			this.id = content.getIdText();
			this.coverpicuri = content.getPicture().getSmalluri();
			this.comments = outfit.getComments();
			this.username = outfit.getUsername();
			this.outfitId = outfit.getIdText();
			this.likeCount = new LikeCountDto(likeCount);
		}
	}

	//public ContentWithOutfitDto(List<ContentDto> contents, List<OutfitDto> outfits){
	//	this.contents = contents;
	//	this.outfits = outfits;
	//}

	//Setters==========================================================================	
	public void setId(String id){this.id = id;}
	public void setCoverpicUri(String coverpicuri){this.coverpicuri = coverpicuri;}
	public void setComments(String comments){this.comments = comments;}
	public void setUsername(String username){this.username = username;}
	public void setOutfitId(String outfitId){this.outfitId = outfitId;}
	
	//Getters==========================================================================	
	@Override
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}
	public String getCoverpicuri(){return this.coverpicuri;}
	public String getComments(){return this.comments;}
	public String getUsername(){return this.username;}
	public String getOutfitId(){return this.outfitId;}
	public LikeCountDto getLikeCount(){return this.likeCount;}

	public String toString(int indents) {
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
		StringBuilder sb = new StringBuilder(indent).append("id: ").append(((this.id == null) ? "null" : id))
			.append(indent).append("coverpicuri: ").append(this.coverpicuri)
			.append(indent).append("comments:").append(this.comments)
			.append(indent).append("username:").append(this.username)
			.append(indent).append("outfitId:").append(this.outfitId)
			.append(indent).append("likeCount:").append(this.likeCount);
		
		return sb.toString();
	}	

    @Override
    public String toString(){
    	return toString(0);
    }
}
