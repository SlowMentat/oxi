package oxi.services;

import java.lang.*;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Arrays;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FilenameUtils;

import oxi.models.*;
import oxi.repositories.*;
//import oxi.util.assemblers.*;
import oxi.models.dto.*;
import oxi.models.projection.*;

import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.hateoas.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.*;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.core.convert.converter.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.DatatypeConverter;

import org.hibernate.SessionFactory;
import org.hibernate.Session;

import org.springframework.security.crypto.bcrypt.*;



@Service
public class ItemManagementService{
	//Repositories
	@Autowired private ItemRepository itemRep;
	@Autowired private BookmarkRepository bookmarkRep;
	@Autowired private LikeRepository likeRep;
	@Autowired private FollowingRepository followRep;

	//Paged Resource Assemblers 
	@Autowired private PagedResourcesAssembler<Bookmark> bookmarPRA;
	//@Autowired private PagedResourcesAssembler<BookmarkDto> bookmarkPRAP;  //TODO:  create this class

	@Autowired private PagedResourcesAssembler<Like> likePRA;
	//@Autowired private PagedResourcesAssembler<LikeDto> likePRAP; //TODO:  create this class

	@Autowired private PagedResourcesAssembler<Following> followPRA;
	//@Autowired private PagedResourcesAssembler<FollowDto> followPRAP; //TODO:  create this class

	@Autowired 
	private RepositoryEntityLinks links;

	@Autowired 
	private ValueService valueService;
	private ImageService ImageService;

	private static final Logger logger = LogManager.getLogger(ItemManagementService.class);
	private static String imgfolder = "/usr/images/";

	//private final FileOutputStream fos = null;
	private InputStream iStream = null;

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	/*@Autowired
	SessionFactory sessionFactory;*/

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;


	public ItemManagementService(){

	}
	//=====================================================================================

	//every 24 hrs, update all items' coverpic not set by retailers to the coverpicuri of the leading content post containing said item.
	//leading is defined as the content post beloning to the outfit with the most likes
	private void cycleItemCoverpic(){
		//valueService.find
	}

	@Transactional
	public ItemDto createItem(ItemDto itemDto){
		return new ItemDto();
	}

	@Transactional
	public void updateItemPicture(String itemId, String retailPictureId){

		//Item item = itemRep.findById(UUID.fromString(itemId));

		//item.setPctureId(UUID.fromString(retailPictureid))

		//item.setIsRetailerPicture(true);

		//entityManage.merge(item);
	}

	@Transactional
	public void removeItemPicture(String itemId){

		//Item item = itemRep.findById(UUID.fromString(itemId));

		//item.setPctureId(null)

		//item.setIsRetailerPicture(false);

		//entityManage.merge(item);
	}

	private <T extends Identifiable<String>> ResourceSupport toResource(T dto){		
		//Link outfitLink = null;// links.linkForSingleResource(dto).withRel("outfit");
		//SLink selfLink = links.linkForSingleResource(dto).withSelfRel();
		return new Resource<T>(dto/*, null, selfLink*/);
	}
}