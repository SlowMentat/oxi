/*package oxi.util.assemblers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import oxi.models.Content;
import oxi.models.dto.ContentDto;
import oxi.controllers.ConsumerController;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.*;
import org.springframework.data.domain.*;


@Component
public class ContentResourceAssembler extends ResourceAssemblerSupport<Content, ContentDto> {

	private static final Logger logger = LogManager.getLogger(ContentResourceAssembler.class);

	public ContentResourceAssembler(){
		super(ConsumerController.class, ContentDto.class);
	}

	@Override
	public ContentDto toResource(Content content){
		ContentDto resource = createResourceWithId(content.getId(), content);
		logger.debug("ContentDto resource:  " + resource);

		//TDO:  map properties
		resource.setCoverpicuri(content.getCoverpicuri());
		resource.setOutfit(content.getOutfit());
		resource.setPicture(content.getPicture());
		resource.setItems(content.getItems());

		//TODO:  add paged resource linkes
		return resource;
	}
}*/