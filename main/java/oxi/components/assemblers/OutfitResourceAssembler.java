/*package oxi.util.assemblers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import oxi.models.Outfit;
import oxi.models.dto.OutfitDto;
import oxi.controllers.ConsumerController;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.*;
import org.springframework.data.domain.*;


@Component
public class OutfitResourceAssembler extends ResourceAssemblerSupport<Outfit, OutfitDto> {

	private static final Logger logger = LogManager.getLogger(OutfitResourceAssembler.class);

	public OutfitResourceAssembler(){
		super(ConsumerController.class, OutfitDto.class);
	}

	@Override
	public OutfitDto toResource(Outfit outfit){
		OutfitDto resource = createResourceWithId(outfit.getId(), outfit);
		logger.debug("OutfitDto resource:  " + resource);

		//TDO:  map properties
		//resource.setLikes(outfit.getLikes());
		//resource.setComments(outfit.getComments());
		//resource.setContents(outfit.getContents());
		//resource.setCoverpicuri(outfit.getCoverpicuri());

		//TODO:  add paged resource linkes
		return resource;
	}
}*/

