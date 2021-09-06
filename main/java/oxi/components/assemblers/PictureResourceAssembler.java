/*package oxi.util.assemblers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import oxi.models.Picture;
import oxi.models.dto.PictureDto;
import oxi.controllers.ConsumerController;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.*;
import org.springframework.data.domain.*;


@Component
public class PictureResourceAssembler extends ResourceAssemblerSupport<Picture, PictureDto> {

	private static final Logger logger = LogManager.getLogger(PictureResourceAssembler.class);

	public PictureResourceAssembler(){
		super(ConsumerController.class, PictureDto.class);
	}

	@Override
	public PictureDto toModel(Picture picture){
		PictureDto resource = createResourceWithId(picture.getId(), picture);
		logger.debug("PictureDto resource:  " + resource);

		//TDO:  map properties
		resource.setSmalluri(picture.getSmalluri());
		resource.setLargeuri(picture.getLargeuri());
		resource.setContent(picture.getContent());

		//TODO:  add paged resource linkes
		return resource;
		
	}
}*/