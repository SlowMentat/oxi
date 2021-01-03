package oxi.models.dto;

//import oxi.models.projection.*;
import oxi.models.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.server.core.*;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.Identifiable;


public class PictureUpdateDto extends PictureDto
{
	private String contentId;

	public PictureUpdateDto(){
		super();
	}
	
	public PictureUpdateDto(String id, String thumbnailuri, String smalluri, String mediumuri, String largeuri){
		super(id, thumbnailuri, smalluri, mediumuri, largeuri);
	}

	public PictureUpdateDto(String id, String thumbnailuri, String smalluri, String mediumuri, String largeuri, String originaluri, String crop){
		super(id, thumbnailuri, smalluri, mediumuri, largeuri, originaluri, crop);
	}

	public PictureUpdateDto(String id, String thumbnailuri, String smalluri, String mediumuri, String largeuri, String originaluri, String crop, String contentId){
		super(id, thumbnailuri, smalluri, mediumuri, largeuri, originaluri, crop);
		this.contentId = contentId.toLowerCase();
	}

	public PictureUpdateDto(PictureDto pictureDto){
		super(pictureDto);
	}
	
	//Getters
	public String getContentId(){return (this.contentId == null) ? this.contentId : this.contentId.toLowerCase();}
	//public Content getContent(){return this.content;}
	
	//Setters
	public void setContentId(String contentId){this.contentId = (contentId == null) ? contentId : contentId.toLowerCase();}
	//public void setContent(Content content){this.content = content;}

	@Override
	public String toString(){
		//TODO:  below causing StackOverflowError 
        /*StringBuilder sb = new StringBuilder(this.toString()).append("\ncontentId: ").append(this.contentId);
        return sb.toString();*/
        return "Place holder string for PictureUpdateDto entity";
	}
}