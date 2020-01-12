/*package oxi.util.assemblers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import oxi.models.Profile;
import oxi.models.dto.ProfileDto;
import oxi.controllers.ConsumerController;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.*;
import org.springframework.data.domain.*;


@Component
public class ProfileResourceAssembler extends ResourceAssemblerSupport<Profile, ProfileDto> {

	private static final Logger logger = LogManager.getLogger(ProfileResourceAssembler.class);

	public ProfileResourceAssembler(){
		super(ConsumerController.class, ProfileDto.class);
	}

	@Override
	public ProfileDto toResource(Profile profile){
		ProfileDto resource = createResourceWithId(profile.getId(), profile);
		logger.debug("ProfileDto resource:  " + resource);

		//TDO:  map properties
		resource.setAlias(profile.getAlias());
		resource.setCountry(profile.getCountry());
		resource.setHeight(profile.getHeight());
		resource.setOutfits(profile.getOutfits());
		resource.setItems(profile.getItems());

		//TODO:  add paged resource linkes
		return resource;
	}
}*/