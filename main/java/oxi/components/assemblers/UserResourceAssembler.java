/*package oxi.util.assemblers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import oxi.models.User;
import oxi.models.dto.UserDto;
import oxi.controllers.ConsumerController;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.*;
import org.springframework.data.domain.*;


/*@Component
public class UserResourceAssembler extends ResourceAssemblerSupport<User, UserDto> {

	private static final Logger logger = LogManager.getLogger(UserResourceAssembler.class);

	public UserResourceAssembler(){
		super(ConsumerController.class, UserDto.class);
	}

	@Override
	public UserDto toModel(User user){
		UserDto resource = createResourceWithId(user.getId(), user);
		logger.debug("UserDto resource:  " + resource);

		//TDO:  map properties
		/*resource.setEmail(user.getEmail());
		resource.setProfile(user.getProfile());*/

		//TODO:  add paged resource linkes
		/*return resource;
	}
}*/