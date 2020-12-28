package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;



@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=PictureDto.class)
public class PictureDto implements Serializable, Identifiable<String>
{
	@JsonProperty("id")
	private String id;	
	private String thumbnailuri;
	private String smalluri;
	private String mediumuri;
	private String largeuri;
	private String originaluri;
	@JsonRawValue
	private String crop;
	//private String contentId;
	
	public PictureDto(){}

	public PictureDto(String id, String thumbnailuri, String smalluri, String largeuri){
		this.id = id;
		this.thumbnailuri = thumbnailuri;
		this.smalluri = smalluri;
		this.largeuri = largeuri;
	}

	public PictureDto(String id, String thumbnailuri, String smalluri, String mediumuri, String largeuri){
		this.id = id;
		this.thumbnailuri = thumbnailuri;
		this.smalluri = smalluri;
		this.mediumuri = mediumuri;
		this.largeuri = largeuri;
	}

	public PictureDto(String id, String thumbnailuri, String smalluri, String mediumuri, String largeuri, String originaluri, String crop){
		this.id = id;
		this.thumbnailuri = thumbnailuri;
		this.smalluri = smalluri;
		this.mediumuri = mediumuri;
		this.largeuri = largeuri;
		this.originaluri = originaluri;
		this.crop = crop;
	}

	public PictureDto(Picture picture){
		this.id = picture.getIdText();
		this.thumbnailuri = picture.getThumbnailuri();
		this.smalluri = picture.getSmalluri();
		this.mediumuri = picture.getMediumuri();
		this.largeuri = picture.getLargeuri();
		this.originaluri = picture.getOriginaluri();
		this.crop = picture.getCrop();
	}

	public PictureDto(BasePicture basePicture){		
		if(basePicture != null){
			this.id = basePicture.getIdText();
			this.thumbnailuri = basePicture.getThumbnailuri();
			this.smalluri = basePicture.getSmalluri();
			this.mediumuri = basePicture.getMediumuri();
			this.largeuri = basePicture.getLargeuri();
			this.originaluri = basePicture.getOriginaluri();
			this.crop = basePicture.getCrop();
		}
	}

	//public PictureDto(PictureUpdateDto pictureUpdateDto){		
	//	if(pictureUpdateDto != null){
	//		this.id = pictureUpdateDto.getId();
	//		this.thumbnailuri = pictureUpdateDto.getThumbnailuri();
	//		this.smalluri = pictureUpdateDto.getSmalluri();
	//		this.mediumuri = pictureUpdateDto.getMediumuri();
	//		this.largeuri = pictureUpdateDto.getLargeuri();
	//		this.originaluri = pictureUpdateDto.getOriginaluri();
	//		this.crop = pictureUpdateDto.getCrop();
	//	}
	//}

	public PictureDto(PictureDto pictureDto){		
		if(pictureDto != null){
			this.id = pictureDto.getId();
			this.thumbnailuri = pictureDto.getThumbnailuri();
			this.smalluri = pictureDto.getSmalluri();
			this.mediumuri = pictureDto.getMediumuri();
			this.largeuri = pictureDto.getLargeuri();
			this.originaluri = pictureDto.getOriginaluri();
			this.crop = pictureDto.getCrop();
		}
	}

	/*@JsonCreator
	public PictureDto(
		@JsonProperty("id")
		String id, 
		?
		
		@JsonProperty("thumbnailuri")
		String thumbnailuri, 
		@JsonProperty("smalluri")
		String smalluri, 
		@JsonProperty("largeuri")
		String largeuri){
		this.id = id;
		this.thumbnailuri = thumbnailuri;
		this.smalluri = smalluri;
		this.largeuri = largeuri;
	}*/
	
	//Getters
	@Override
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}
	public String getSmalluri(){return this.smalluri;}
	public String getMediumuri(){return this.mediumuri;}
	public String getLargeuri(){return this.largeuri;}
	public String getThumbnailuri(){return this.thumbnailuri;}
	public String getOriginaluri(){return this.originaluri;}
	public String getCrop(){return this.crop;}
	//public String getContentId(){return this.contentId;}
	//public Content getContent(){return this.content;}
	
	//Setters
	public void setId(String id){this.id = id;}
	public void setSmalluri(String smalluri){this.smalluri = smalluri;}
	public void setMediumuri(String mediumuri){this.mediumuri = mediumuri;}
	public void setLargeuri(String largeuri){this.largeuri = largeuri;}
	public void setThumbnailrui(String thumbnailuri){this.thumbnailuri = thumbnailuri;}
	public void setOriginaluri(String originaluri){this.originaluri = originaluri;}
	public void setCrop(String crop){this.crop = crop;}
	//public void setContentId(String contentId){this.contentId = contentId;}
	//public void setContent(Content content){this.content = content;}

	//@Override
	public String toString(int indents){
		String indent = "\n";
		for(int i = 0; i < indents; i++){
			indent += "    ";
		}
        StringBuilder sb = new StringBuilder("\nid: ").append(this.id == null ? "null" : this.id)
			.append(indent).append("\nsmalluri: ").append(this.smalluri)
			.append(indent).append("\nmediumuri:").append(this.mediumuri)
			.append(indent).append("\nlargeuri:").append(this.largeuri)
			.append(indent).append("\nthumbnailuri: ").append(this.thumbnailuri)
			.append(indent).append("\noriginal:").append(this.originaluri)
			.append(indent).append("\ncrop: ").append(this.crop);
			//.append("\ncontentId: ").append(this.contentId);
        return sb.toString();
	}
}