package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.dto.retailer.SizeGroupDto;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;


public class ItemUpdateDto extends ItemDto
{
	private String contentId;

	public ItemUpdateDto(String id, Float positionx, Float positiony, String type, SizeGroupDto SizeGroupDto, String retailer, String brand){
		super(id, positionx, positiony, type, SizeGroupDto, retailer, brand);
	}

	//Setters
	public void setId(String contentId){this.contentId = contentId;}
	
	//Getters
	@Override
	public String getId(){return (this.contentId == null) ? this.contentId : this.contentId.toLowerCase();}
}
