package oxi.util.assemblers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import oxi.models.Item;
import oxi.models.dto.ItemDto;
import oxi.controllers.ConsumerController;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.*;
import org.springframework.data.domain.*;


@Component
public class ItemResourceAssembler extends ResourceAssemblerSupport<Item, ItemDto> {

	private static final Logger logger = LogManager.getLogger(ItemResourceAssembler.class);

	public ItemResourceAssembler(){
		super(ConsumerController.class, ItemDto.class);
	}

	@Override
	public ItemDto toResource(Item item){
		ItemDto resource = createResourceWithId(item.getId(), item);
		logger.debug("ItemDto resource:  " + resource);

		//TDO:  map properties
		resource.getPositionx(item.getPositionx());
		resource.getPositiony(item.getPositiony());
		resource.setLink(item.getLink());
		resource.setType(item.getType());
		resource.setSize(item.getSize());

		//TODO:  add paged resource linkes
		return resource;
	}
}