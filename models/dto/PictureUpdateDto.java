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


public class PictureUpdateDto extends PictureDto
{
	private String contentId;
	
	public PictureUpdateDto(String id, String thumbnailuri, String smalluri, String largeuri){
		super(id, thumbnailuri, smalluri, largeuri);
	}
	
	//Getters
	public String getContentId(){return this.contentId;}
	//public Content getContent(){return this.content;}
	
	//Setters
	public void setContentId(String contentId){this.contentId = contentId;}
	//public void setContent(Content content){this.content = content;}

	@Override
	public String toString(){
		//TODO:  below causing StackOverflowError
        /*StringBuilder sb = new StringBuilder(this.toString()).append("\ncontentId: ").append(this.contentId);
        return sb.toString();*/
        return "Place holder string for PictureUpdateDto entity";
	}
}