package oxi.services;

import java.lang.*;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Arrays;
import java.util.Optional;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.lang.IllegalStateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FilenameUtils;

import oxi.models.*;
import oxi.models.retailer.*;
import oxi.repositories.*;
import oxi.repositories.retailer.*;
//import oxi.util.assemblers.*;
import oxi.models.dto.*;
import oxi.models.projection.*;
import oxi.models.dto.retailer.*;
import oxi.controllers.ConsumerController;

import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.hateoas.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.*;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.core.convert.converter.*;
import org.springframework.http.HttpHeaders;
//import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.DatatypeConverter;

import org.hibernate.SessionFactory;
import org.hibernate.Session;

import static oxi.security.SecurityConfiguration.*;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PagedArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class ConsumerService implements ClientService{
	//Repositories
	@Autowired private OutfitRepository outfitRep;
	@Autowired private MyContentRepository contentRep;
	@Autowired private ItemRepository itemRep;
	@Autowired private PictureRepository pictureRep;
	@Autowired private PictureDeleteRepository pictureDeleteRep;

	@Autowired private BookmarkRepository bookmarkRep;
	@Autowired private LikeRepository likeRep;
	@Autowired private FollowingRepository followRep;

	@Autowired private ProfileRepository profileRep;
	@Autowired private UserRepository userRep;
	@Autowired private RoleRepository roleRep;

	@Autowired private BrandRepository brandRep;
	@Autowired private RetailerRepository retailerRep;
	@Autowired private SizeLabelRepository sizeLabelRep;

	@Autowired private ApparelTypeRepository apparelTypeRep;

	@Autowired private SizeChartRepository sizeChartRep;

	@Autowired private SizeGroupRepository sizeGroupRep;
	@Autowired private ItemContentRepository itemContentRep;
	@Autowired private LikeCountProfileRepository likeCountProfileRep;
	@Autowired private LikeCountRepository likeCountRep;

	@Autowired private PictureProfileRepository pictureProfileRep;

	//EntityModel Assemblers
	//@Autowired 
	//private OutfitResourceAssembler outfitRA;
	//@Autowired private ContentResourceAssembler contentRA;
	//@Autowired private ItemResourceAssembler itemRA;
	//@Autowired private PictureResourceAssembler pictureRA;
	//@Autowired private ProfileResourceAssembler profileRA;
	//@Autowired private UserResourceAssembler userRA;

	//Paged EntityModel Assemblers 
	@Autowired private PagedResourcesAssembler<Outfit> outfitPRA;
	@Autowired private PagedResourcesAssembler<OutfitDto> outfitPRAP;

	@Autowired private PagedResourcesAssembler<Content> contentPRA;
	@Autowired private PagedResourcesAssembler<ContentDto> contentPRAP;
	
	//@Autowired private PagedResourcesAssembler<Content> contentWithOutfitPRA;
	@Autowired private PagedResourcesAssembler<ContentWithOutfitDto> contentWithOutfitPRAP;

	@Autowired private PagedResourcesAssembler<Item> itemPRA;
	@Autowired private PagedResourcesAssembler<ItemDto> itemPRAP;

	@Autowired private PagedResourcesAssembler<Brand> brandPRA;
	@Autowired private PagedResourcesAssembler<BrandDto> brandPRAP;

	@Autowired private PagedResourcesAssembler<Retailer> retailerPRA;
	@Autowired private PagedResourcesAssembler<RetailerDto> retailerPRAP;

	@Autowired private PagedResourcesAssembler<ApparelType> apparelTypePRA;
	@Autowired private PagedResourcesAssembler<ApparelTypeDto> apparelTypePRAP;

	@Autowired 
	private RepositoryEntityLinks links;

	@Autowired 
	private ImageService imageService;

	private static final Logger logger = LogManager.getLogger(ConsumerService.class);
	private static String imgfolder = "/usr/images/";

	//private final FileOutputStream fos = null;
	private InputStream iStream = null;

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	/*@Autowired
	SessionFactory sessionFactory;*/

	//@Autowired
	//private BCryptPasswordEncoder userPasswordEncoder;
	@Autowired
	private PasswordEncoder userPasswordEncoder;


	public ConsumerService(){

	}

	//@Override
	private <T extends Object> RepresentationModel toModel(T dto){		
		//Link outfitLink = null;// links.linkForItemResource(dto).withRel("outfit");
		//SLink selfLink = links.linkForItemResource(dto).withSelfRel();
		return new EntityModel<T>(dto/*, null, selfLink*/);
	}

	private void logStackTrace(Exception e){
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		logger.debug(stringWriter.toString());
	}
	/*
	* Converter functions for maping Paged entities to their respective Dtos
	*/

	private OutfitDto convertToOutfitDto(final Outfit outfit){
		return new OutfitDto(outfit);
		//return new OutfitDto(outfit.getIdText(), outfit.getLikes(), outfit.getComments(), new ArrayList<ContentDto>(5), outfit.getCoverPictureId(), outfit.getUsername());
	}

	private ContentDto convertToContentDto(final Content content){
		//return new ContentDto(content.getIdText(), content.getCoverpicuri(), new PictureDto(content.getPicture()), null, content.getOutfit().getIdText());
		return new ContentDto(content);
	}

	private ItemDto convertToItemDto(final Item item){
		String coverpicuri = (item.getPicture() != null) ? item.getPicture().getSmalluri() : "";
		return new ItemDto(item);
	}

	private BrandDto convertToBrandDto(final Brand brand){
		return new BrandDto(brand.getIdText(), brand.getName(), brand.getLink(), brand.getRed(), brand.getGreen(), brand.getBlue());
	}

	private ApparelTypeDto convertToApparelTypeDto(final ApparelType apparelType){
		return new ApparelTypeDto(apparelType);
	}

	//public RetailerDto convertToRetailerDto(final Retailer retailer){
	//	return new RetailerDto(retailer.getIdText(), retailer.getName(), retailer.getLink(), retailer.getRed(), retailer.getGreen(), retailer.getBlue());
	//}


	/*
	* Helpler functions for copying DTOs to DAOs and vice versa.  
	* TODO:  investigate leveraging map() or stream() 
	*/
	private static ProfileDto copyToProfileDto(Profile profile){
		return new ProfileDto(profile);
		//return new ProfileDto(
		//	null,
		//	profile.getUsername(),
		//	profile.getCountry(),
		//	profile.getDateOfBirth(),
		//	//profile.getBodyShape(),
		//	//profile.getMens(),
		//	//profile.getWomens(),
		//	//profile.getHeight(),
		//	//profile.getNeck(),
		//	//profile.getFullShoulder(),
		//	//profile.getHalfShoulder(),
		//	//profile.getChest(),
		//	//profile.getWaist(),
		//	//profile.getHip(),
		//	//profile.getSleeve(),
		//	//profile.getFrontLength(),
		//	//profile.getBackLength(),
		//	//profile.getPantOutseam(),
		//	//profile.getPantInseam(),
		//	//profile.getThigh(),
		//	//profile.getCalf(),
		//	copyToUserMetricsDto(profile.getUserMetrics()),
		//	copyToToleranceDto(profile.getTolerance()),
		//	copyToProfileStatsDto(profile.getProfileStats())
		//	); 
	}

	private static Profile copyToProfile(ProfileDto profileDto){
		Profile profile = new Profile();
		logger.debug("starting copy.");
		if(!profileDto.getId().isEmpty() && profileDto.getId() != null) profile.setId(UUID.fromString(profileDto.getId()));
		profile.setUsername(profileDto.getUsername());
		profile.setCountry(profileDto.getCountry());
		profile.setDateOfBirth(profileDto.getDateOfBirth());
		//profile.setBodyShape(profileDto.getBodyShape());
		//profile.setMens(profileDto.getMens());
		//profile.setWomens(profileDto.getWomens());
		//profile.setHeight(profileDto.getHeight());
		//profile.setNeck(profileDto.getNeck());
		//profile.setFullShoulder(profileDto.getFullShoulder());
		//profile.setHalfShoulder(profileDto.getHalfShoulder());
		//profile.setChest(profileDto.getChest());
		//profile.setWaist(profileDto.getWaist());
		//profile.setHip(profileDto.getHip());
		//profile.setSleeve(profileDto.getSleeve());
		//profile.setFrontLength(profileDto.getFrontLength());
		//profile.setBackLength(profileDto.getBackLength());
		//profile.setPantOutseam(profileDto.getPantOutseam());
		//profile.setPantInseam(profileDto.getPantInseam());
		//profile.setThigh(profileDto.getThigh());
		//profile.setCalf(profileDto.getCalf());
		profile.setUserMetrics(copyToUserMetrics(profileDto.getUserMetricsDto()));
		profile.setTolerance(copyToTolerance(profileDto.getToleranceDto()));
		profile.setProfileStats(copyToProfileStats(profileDto.getProfileStatsDto()));
		logger.debug("finished copy.");
		return profile;
	}

	private static UserMetrics copyToUserMetrics(UserMetricsDto userMetricsDto){
		return userMetricsDto != null ? (new UserMetrics(userMetricsDto)) : null;
	}

	private static UserMetricsDto copyToUserMetricsDto(UserMetrics userMetrics){
		UserMetricsDto userMetricsDto = null;
		if(userMetrics == null){
			logger.debug("ConsumerService # copyToUserMetrics:  userMetrics parameter is null");
		}else{
			logger.debug("ConsumerService # copyToUserMetrics:  userMetrics parameter is NOT null");
			userMetricsDto = new UserMetricsDto(userMetrics);
		}
		//return userMetrics != null ? (new UserMetricsDto(userMetrics)) : null;
		return userMetricsDto;
	}

	private static Tolerance copyToTolerance(ToleranceDto toleranceDto){
		return toleranceDto != null ? (new Tolerance(toleranceDto)) : null;
	}

	private static ToleranceDto copyToToleranceDto(Tolerance tolerance){
		return tolerance != null ? (new ToleranceDto(tolerance)) : null;
	}

	private static ProfileStats copyToProfileStats(ProfileStatsDto profileStatsDto){
		return profileStatsDto != null ? (new ProfileStats(profileStatsDto)) : null;
	}

	private static ProfileStatsDto copyToProfileStatsDto(ProfileStats profileStats){
		return profileStats != null ? (new ProfileStatsDto(profileStats)) : null;
	}

	private static OutfitDto copyToOutfitDto(Outfit outfit){
		if(outfit != null){
			int contentLength = outfit.getContents().size();
			List<ContentDto> contentDtos = new ArrayList<ContentDto>(contentLength);

			for(Content content: outfit.getContents()){
				contentDtos.add(copyToContentDto(content));
			}

			OutfitDto outfitDto = new OutfitDto(outfit);
			outfitDto.setContents(contentDtos);
			return outfitDto;
			//return new OutfitDto(outfit.getId().toString(), outfit.getLikes(), outfit.getComments(), contentDtos, outfit.getCoverPictureId().toString(), outfit.getUsername());
		}
		return null;
	}

	//Coppies full outfit object including child entities.  This should only be called when all child entities are new
	private static Outfit copyToOutfit(OutfitDto outfitDto){
		if(outfitDto != null){
			List<Content> contents = new ArrayList<Content>(outfitDto.getContents().size());

			for(ContentDto contentDto : outfitDto.getContents()){
				
				Content content = copyToContent(contentDto);

				for(ItemDto itemDto : contentDto.getItems()){
					Item item = copyToItem(itemDto);
					ItemContent itemContent = new ItemContent(item, content);
					itemContent.setPositionx(itemDto.getPositionx());
					itemContent.setPositiony(itemDto.getPositiony());
					content.addItem(itemContent);
				}

				contents.add(content);
			}

			return new Outfit(
				null, 
				outfitDto.getLikes(), 
				outfitDto.getComments(), 
				contents, 
				UUID.fromString(outfitDto.getCoverPictureId()));
		}
		return null;
	}

	//Coppies only non-entity properties.  Instantiats ArrayList for one-to-many child entities.
	private static Outfit copyToOutfitExcludingEntities(OutfitDto outfitDto){
		if(outfitDto != null){
			return new Outfit(
				null, 
				outfitDto.getLikes(), 
				outfitDto.getComments(), 
				new ArrayList<Content>(outfitDto.getContents().size()), 
				UUID.fromString(outfitDto.getCoverPictureId()),
				outfitDto.getUsername(),
				null,
				outfitDto.getCoverpicuri());
		}
		return null;		
	}

	private static ContentDto copyToContentDto(Content content){
		logger.debug("test: content = " + content.toString());

		if(content != null){
			//int itemLength = content.getItems().size();
			//List<ItemDto> itemDtos = new ArrayList(itemLength);

			//for(ItemContent itemContent : content.getItems()){
			//	//itemDtos.add(copyToItemDto(itemContent));
			//	itemDtos.add(new ItemDto(itemContent));
			//}

			//PictureDto pictureDto = copyToPictureDto(content.getPicture());
			//String outfitId = null;

			//if(content.getOutfit()  != null){
			//	outfitId = content.getOutfit().getIdText();
			//}
			
			//return new ContentDto(content.getId().toString(), content.getCoverpicuri(), pictureDto, itemDtos, outfitId);
			return new ContentDto(content);
		}
		return null;
	}

	private static Content copyToContent(ContentDto contentDto){
		if(contentDto != null){
			if(contentDto.getId() == null){
				return new Content(
					null,
					contentDto.getCoverpicuri(),
					copyToPicture(contentDto.getPicture()),
					new HashSet<ItemContent>(contentDto.getItems().size()));
			}

			return new Content(
				UUID.fromString(contentDto.getId()),
				contentDto.getCoverpicuri(),
				copyToPicture(contentDto.getPicture()),
				new HashSet<ItemContent>(contentDto.getItems().size()));
		}
		return null;
	}

	private static ItemDto copyToItemDto(ItemContent itemContent){
		logger.debug("test: itemContent = " + itemContent.toString());

		if(itemContent != null){
			ItemDto itemDto = new ItemDto(itemContent.getItem());
			itemDto.setPositiony(itemContent.getPositiony());
			itemDto.setPositionx(itemContent.getPositionx());
			return itemDto;
			/*return new ItemDto(
				itemContent.getItem().getId().toString(), 
				itemContent.getPositionx(), 
				itemContent.getPositiony(), 
				itemContent.getItem().getApparelType(), 
				null,//itemContent.getItem().getSizeGroupId(), 
				itemContent.getItem().getRetailer().toString(), 
				itemContent.getItem().getBrand().toString());*/
		}
		return null;
	}

	private static Item copyToItem(ItemDto itemDto){
		if(itemDto != null){
			if(itemDto.getId() != null && !itemDto.getId().isEmpty()){
				return new Item(itemDto);
				/*return new Item(
					UUID.fromString(itemDto.getId()), 
					itemDto.getApparelType(), 
					UUID.fromString(itemDto.getSizeGroupDto().getId()), 
					UUID.fromString(itemDto.getRetailer()), 
					UUID.fromString(itemDto.getBrand()));*/
			}
			Item item = new Item(itemDto);
			/*Item item = new Item(
				null, 
				itemDto.getApparelType(), 
				UUID.fromString(itemDto.getSizeGroupDto().getId()),  
				UUID.fromString(itemDto.getRetailer()), 
				UUID.fromString(itemDto.getBrand()));*/

			//item.setContents(new ArrayList<ItemContent>());
			return item;
		}
		return null;
	}

	private static PictureDto copyToPictureDto(Picture picture){
		if(picture != null){
			return new PictureDto(picture);
			//return new PictureDto(
			//	picture.getId().toString(), 
			//	picture.getThumbnailuri(), 
			//	picture.getSmalluri(), 
			//	picture.getMediumuri(),
			//	picture.getLargeuri(),
			//	picture.getOriginaluri(),
			//	picture.getCrop());
		}
		return null;
	}

	private static Picture copyToPicture(PictureDto pictureDto){
		if(pictureDto != null){
			return new Picture(pictureDto);
			//if(pictureDto.getId() != null){
			//	return new Picture(pictureDto);
			//	//return new Picture(
			//	//	UUID.fromString(pictureDto.getId()), 
			//	//	pictureDto.getThumbnailuri(), 
			//	//	pictureDto.getSmalluri(), 
			//	//	pictureDto.getMediumuri(),
			//	//	pictureDto.getLargeuri(),
			//	//	pictureDto.getOriginaluri(),
			//	//	pictureDto.getCrop());	
			//}
			//else{
			//	return new Picture(
			//		null, 
			//		pictureDto.getThumbnailuri(), 
			//		pictureDto.getSmalluri(), 
			//		pictureDto.getMediumuri(),
			//		pictureDto.getLargeuri(),
			//		pictureDto.getOriginaluri(),
			//		pictureDto.getCrop());
			//}
		}
		return null;
	}

	private Item buildExistingItem(ItemDto itemDto) throws IllegalStateException{
		ObjectMapper mapper = new ObjectMapper();
		ItemDto.Product productObject = null;

		try{
			productObject = mapper.readValue(itemDto.getProduct(), ItemDto.Product.class);
		}
		catch(Exception e){
			logStackTrace(e);
			throw new IllegalStateException(e.toString());
		}

		Item existingItem = itemRep.findByExistingCustomItem(productObject.getUdr(), productObject.getUds(), itemDto.getApparelType());

		if(existingItem != null) return existingItem;

		return copyToItem(itemDto);
	}


	@Transactional
	public List<ApparelType> getAllApparelTypes(){
		return apparelTypeRep.findAll();
	}


	/*
	Makes call to data access layer (DAL) inserting new outfit resource into database.
	@param Outfit 
	@return
	*/
	@Transactional
	public OutfitDto saveOutfit(Outfit outfit, String username){
		logger.debug("saving Outfit");
		//Session session = sessionFactory.getCurrentSession();
		OutfitDto outfitDto = null;
		int contentsCount = outfit.getContents().size();
		if(contentsCount > 0){
			ArrayList<ContentDto> contentDtos = new ArrayList<ContentDto>(contentsCount);
			logger.debug("outfit = ");
			logger.debug(outfit);
			Profile profile = profileRep.findByUsername(username);
			if(profile != null){
				outfit.setProfile(profile);
				outfitDto = copyToOutfitDto(entityManager.merge(outfit));
				//outfit.setProfile(profile);
				logger.debug("outfit after persist = ");
				logger.debug(outfit);
			}else{
				logger.warn("No profile exists for user!");
			}
		}else{
			logger.warn("Rejecting Outfit entity. Outfit entity contains no Content childeren. Outfit entity must have at least 1 Content child entity");
		}
		return outfitDto;
	}

	@Transactional
	public OutfitDto addOutfit(OutfitDto outfitDto, String username){
		OutfitDto result = null;
		//Outfit outfit = copyToOutfit(outfitDto);
		Outfit outfit  = copyToOutfitExcludingEntities(outfitDto);
		int contentsCount = outfitDto.getContents().size();
		Profile profile = profileRep.findByUsername(username);
		LikeCount likeCount = new LikeCount();

		if(profile.getPictureProfile() != null){
			outfit.setPictureProfileId(profile.getPictureProfile().getId());
		}
		
		entityManager.persist(likeCount);

		for(ContentDto contentDto : outfitDto.getContents()){
			Content content = copyToContent(contentDto);
			logger.debug("contentDto = " + contentDto.toString());

			entityManager.persist(content);

			for(ItemDto itemDto : contentDto.getItems()){
				//Item item = null;

				///*
				//* If id exists in itemDto then create a new Item object with itemDto's id value as UUID.  
				//* This object will be merged into the persistence context without overriding existing Item properties.
				//* Normally when adding existing items the user agent should send Item with only id field specified.  Precausions
				//* are taken here ti aviud overriding existing item entities during a merge operation.
				//*/
				//if(itemDto.getId() != null){
				//	item = new Item();
				//	item.setId(UUID.fromString(itemDto.getId()));
				//	//entityManager.merge(item);
				//}else{
				//	item = copyToItem(itemDto);
				//	entityManager.persist(item);
				//}
				Item item = buildExistingItem(itemDto);

				// item is new so add the picture id associated with this outfit
				if(item.getId() == null){
					item.setPicture(pictureRep.findById(outfit.getCoverPictureId()).get());
					entityManager.persist(item);
				}
				//else{
				//	item = (Item)entityManager.merge(item);
				//}
				
				//Item mergedItem = null;
				//if(item.getId() != null){
				//	/*
				//	* Consumer service should not be able to modify item db directly,
				//	* so make shure all fields relating to item db are cleared in itemDto
				//	*/
				//	item.setProduct(null);
				//	mergedItem = entityManager.merge(item);
				//	logger.debug("product = " + mergedItem.getProduct());
				//}else{
				//	entityManager.persist(item);
				//}

				ItemContent ic = new ItemContent(item, content);
				ic.setPositiony(itemDto.getPositiony());
				ic.setPositionx(itemDto.getPositionx());
				content.addItem(ic);
				//content.addItem(mergedItem, itemDto.getPositionx(), itemDto.getPositiony());
			}

			Picture picture = pictureRep.findById(content.getPicture().getId()).get();
			picture.setContent(content);
			// copy crop data from picture dto to picture dao
			picture.setCrop(contentDto.getPicture().getCrop());
			entityManager.merge(picture);
			//content.setOutfit(outfit);
			outfit.addContent(content);
			logger.debug("ConsumerService#addOutfit:  content object = \n" + content.toString());
		}
		
		entityManager.persist(outfit);
		outfit.setProfile(profile);
		outfit.setLikeCount(likeCount);
		outfit.setUsername(username);
		logger.debug("ConsumerService#addOutfit:  outfit object = \n" + outfit.toString());
		result = copyToOutfitDto(outfit);
		logger.debug("ConsumerService#addOutfit: result = \n" + result.toString());
		return result;
	}

	@Transactional
	public OutfitDto addContents(List<Content> contents, String username, String outfitId){
		OutfitDto outfitDto = null;
		Outfit outfit = outfitRep.findById(UUID.fromString(outfitId)).get();
		//Verify that client is owner of outfitId
		Profile profile = outfit.getProfile();
		if(profile.getUsername().equals(username)){
			//Add new contents to outfit.contents list
			for(Content c : contents){
				Picture picture = pictureRep.findById(c.getPicture().getId()).get();
				entityManager.persist(picture);
				picture.setContent(c);
				c.setOutfit(outfit);
				entityManager.persist(c);
			}
			entityManager.persist(outfit);
			outfitDto = copyToOutfitDto(outfit);
		}else{
			logger.warn("User, " + username + " does not have permissions to edit outfit with provided Id");
		}
		return outfitDto;
	}


	//=====================================================================================
	//Item
	//TODO:  this needs some serious optimizing
	@Transactional
	public OutfitDto updateItems(HashMap<String, ArrayList<ItemDto>> contentIdToItemMap, String username, String outfitId) throws IllegalStateException{
		Outfit outfit = outfitRep.findById(UUID.fromString(outfitId)).get();
		Profile profile = outfit.getProfile();
		OutfitDto outfitDto;
		List<ContentDto> contentDtos = new ArrayList<ContentDto>();
		List<ItemDto> itemDtos;
		List<SizeGroupDto> sizeGroupDtos = new ArrayList<SizeGroupDto>();

		//HashSet<UUID> itemIds = new HashSet<UUID>();

		if(profile.getUsername().equals(username)){
			//logger.debug("content Ids:");
			//entityManager.merge(outfit);
			try{
				for(String contentId : contentIdToItemMap.keySet()){
					//logger.debug(contentId + ": " + "{\n");
					Content content = contentRep.findById(UUID.fromString(contentId)).get();				
					//List<ItemContent> itemContents = content.getItems();
					//entityManager.persist(content);
					Set<ItemContent> itemContents = content.getItems();
					//itemDtos = new ArrayList<ItemDto>();
	
					//
					for(ItemDto itemDto : contentIdToItemMap.get(contentId)){
						int ind = -1;
						boolean isAssociated = false;
	
						//itemIds.add(UUID.fromString(itemDto.getId()));
	
						// Check each content/item combination exists in the item_content join table. 
						// If so, then merge changes to existing record
						for(ItemContent itemContent : itemContents){
							ind++;
	
							if(content.getId().equals(itemContent.getContent().getId()) && itemDto.getId().equals(itemContent.getItem().getId().toString())){
								logger.debug("ConsumerService#updateItems: itemContents = \n" + itemContent.toString());
								itemContent.setPositionx(itemDto.getPositionx()); 
								itemContent.setPositiony(itemDto.getPositiony());
								//itemContent.getItem().setApparelType(itemDto.getApparelType()); 
	
								//TODO: make sure this isn't needed
								//itemContent.getItem().setSizeGroupId(UUID.fromString(itemDto.getSizeGroupId()));
								
								//itemContent.getItem().setRetailer(UUID.fromString(itemDto.getRetailer()));
								//itemContent.getItem().setBrand(UUID.fromString(itemDto.getBrand()));
								entityManager.merge(itemContent);
								//itemContents.set(ind, itemContent);
								itemContents.add(itemContent);
								logger.debug("ConsumerService#updateItems: updated itemContent = \n" + itemContent.toString());
								isAssociated = true;
	
								break;
							}
						}
	
						// This handles newly added retailer items.  It creates a new content/item record in the item_content database
						if(!isAssociated){
							Item item = itemRep.findById(UUID.fromString(itemDto.getId())).get();
							ItemContent ic = new ItemContent(item, content);
							ic.setPositiony(itemDto.getPositiony());
							ic.setPositionx(itemDto.getPositionx());
							content.addItem(ic);
						}
	
						//itemDtos.add(itemDto);
					}
	
					content = entityManager.merge(content);
					
					//ContentDto contentDto = new ContentDto(content);
					//contentDto.setItems(itemDtos);
					//contentDtos.add(contentDto);
					
					//Note: more than one content entity may have been updated
					outfit.getContents().set(outfit.getContents().indexOf(content), content);
				}
			}
			catch(Exception e){
				logStackTrace(e);
				throw new IllegalStateException(e.toString());
			}

			////batch select sizeGroups associated with each itemDto id
			//HashMap<UUID, SizeGroupDto> itemIdToSizeGroupDto = sizeGroupRep.customFindByIds(itemIds);

			////loop through all constructed ItemDtos adding the corresponding sizeGroupDto
			//for(ContentDto contentDto : contentDtos){
			//	for(ItemDto itemDto : contentDto.getItems()){
			//		itemDto.setSizeGroupDto(itemIdToSizeGroupDto.get(UUID.fromString(itemDto.getId())));
			//	}
			//}
		}else{
			logger.warn("User, " + username + " does not have permissions to edit item");
		}

		//outfitDto = new OutfitDto(outfit);
		//outfitDto.setContents(contentDtos);

		//return outfitDto;
		return copyToOutfitDto(outfit);
	}

	//TODO  modify to reuse exisitng items
	@Transactional
	public OutfitDto addItems(HashMap<String, ArrayList<ItemDto>> contentIdToItemMap, String username, String outfitId) throws IllegalStateException{
		Outfit outfit = outfitRep.findById(UUID.fromString(outfitId)).get();
		Profile profile = outfit.getProfile();
		PictureProfile pictureProfile = null;

		ObjectMapper mapper = new ObjectMapper();

		if(profile.getUsername().equals(username)){
			//entityManager.persist(outfit);
			//work on each content id key provided in the payload received
			ItemDto.Product productObject = null;
			Item existingItem = null;
			Item item = null;
			pictureProfile = pictureProfileRep.findById(outfit.getPictureProfileId()).get();

			for(String contentId : contentIdToItemMap.keySet()){
				Content content = contentRep.findById(UUID.fromString(contentId)).get();
				entityManager.persist(content);

				try{
					for(ItemDto itemDto : contentIdToItemMap.get(contentId)){
						//Item item = copyToItem(itemDto);
						
						// Determine if item exists by searching db on product.uds, product.udr, appartelType id
						productObject = mapper.readValue(itemDto.getProduct(), ItemDto.Product.class);
						item = buildExistingItem(itemDto);
						//existingItem = itemRep.findByExistingCustomItem(productObject.getUdr(), productObject.getUds(), itemDto.getApparelType());
						//item = existingItem != null ? existingItem : copyToItem(itemDto);
	
						if(item.getId() == null){
							item.setPicture(pictureRep.findById(outfit.getCoverPictureId()).get());
							entityManager.persist(item);
						}

						content.addItem(item, itemDto.getPositionx(), itemDto.getPositiony());
					}
				}
				catch(Exception e){
					logStackTrace(e);
					throw new IllegalStateException(e.toString());
				}
			}
		}
		else{
			logger.warn("User, " + username + " does not have permissions to edit item");
		}

		OutfitDto outfitDto = copyToOutfitDto(outfit);
		if(pictureProfile != null) outfitDto.setProfilePictureUri(pictureProfile.getSmalluri());

		return outfitDto;
	}


	//EXPERIMENT
	/*
	Makes call to DAL retreiving outfit resource
	@param Long specifying id of outfit to retreive
	@return OutfitDto which extends RepresentationModel
	*/
	public OutfitDto readOutfit(String outfitId){
		return outfitRep.getOutfitById(UUID.fromString(outfitId));
	}

	// TDOD:  generalize this method for getContentsByItemId
	private <T> EntityModel<CursorDto> buildPagedResponse(CursorDto cursor, PagedList<T> pagedDtoList, String filterQueryParam){
		String queryParams = cursor.getNextURI(pagedDtoList, cursor);

		Link afterLink = WebMvcLinkBuilder.linkTo(ConsumerController.class)
			.slash("outfits?filter=" + filterQueryParam + queryParams)
			.withRel("after");

		cursor.embedResource("outfitDtoes", new CollectionModel<T>(pagedDtoList));

		return new EntityModel<CursorDto>(cursor, afterLink);
	}

	public EntityModel<CursorDto> readOutfitsByUsername(String username, String callerName, CursorDto cursor, String filterQueryParam) throws Exception{
		//logger.debug("username = " + username);
		//Profile profile = profileRep.findByUsername(username);
		////Page<OutfitDto> outfits = outfitRep.findByProfileId(profile.getId(), pageable);
		//List<OutfitDto> outfitDtos = outfitRep.findByProfileId(callerName, cursor, profile.getId().toString());
		////logger.debug("outfits return from repository: \n" + outfitDtos);
		////// Tell Paged EntityModel Assembler to use the user assembler for individual items.
		////PagedModel<?> pagedOutfitResource = outfitPRAP.toModel(outfits, this::toModel);
		////return pagedOutfitResource;
		PagedList<OutfitDto> pagedOutfitDtos = null;

		try{
			Profile profile = profileRep.findByUsername(username);
			pagedOutfitDtos = (PagedList)outfitRep.findByProfileId(callerName, cursor, profile.getId());
		}
		catch(Exception e){
			logStackTrace(e);
			throw new Exception("Error getting outfits.");
		}

		return buildPagedResponse(cursor, pagedOutfitDtos, filterQueryParam);

	}

	public ResponseEntity<?> readOutfitsByIds(List<String> ids){
		List<Outfit> outfits;

		try{
			outfits = outfitRep.getOutfitsByIds(ids);
		}
		catch(Exception e){
			List<String> exception = new ArrayList(1);
			exception.add(e.toString());
			return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>( outfits.stream().map(this::toModel).collect(Collectors.toList()), HttpStatus.OK );
	}

	//public PagedModel<?> readPagedOutfits(String filter, String callerName, Pageable pageable) throws Exception{
	public EntityModel<CursorDto> readPagedOutfits(String filter, String callerName, CursorDto cursor, String filterQueryParam) throws Exception{
		//Page<OutfitDto> outfits = outfitRep.customFindAll(callerName, pageable);//outfitRep.getAllOutfitsWithUsername(pageable).map(this::convertToOutfitDto);

		PagedList<OutfitDto> pagedOutfitDtos = null;

		try{
			pagedOutfitDtos = (PagedList)outfitRep.customFindAll(callerName, cursor, null);
		}
		catch(Exception e){
			logStackTrace(e);
			throw new Exception("Error getting outfits.");
		}

		return buildPagedResponse(cursor, pagedOutfitDtos, filterQueryParam);
	}

	@Transactional
	public void/*ResponseEntity<?>*/ updateOutfitCoverpic(String id, String username, OutfitCoverpicDto outfitCoverpicDto) throws Exception, IllegalStateException{
		//try{
			UUID pictureId = UUID.fromString(outfitCoverpicDto.getCoverPictureId());
			Profile profile = profileRep.findByUsername(username);
	
			if(pictureId == null) throw new IllegalStateException("The provided picture id does not exist.");
			Picture picture = pictureRep.findById(pictureId).get();

			Outfit outfit = outfitRep.findById(UUID.fromString(id)).get();
			if(!profile.getId().toString().equalsIgnoreCase(picture.getProfileId().toString())) throw new Exception("User does not have permissions to edit this resouce.");
			//if(profile.getUsername().equals(username)) throw new Exception("User does not have permissions to edit this resouce.");
	
	
			Outfit mergedOutfit = entityManager.merge(outfit);
			mergedOutfit.setCoverPictureId(pictureId);
			mergedOutfit.setCoverpicuri(picture.getMediumuri());
	}

	public List<?> readContents(String outfitId){
		List<ContentDto> contents = contentRep.findByOutfitId(UUID.fromString(outfitId));
		return contents.stream().map(this::toModel).collect(Collectors.toList());
	}

	//public PagedModel<?> getContentsByItemId(String itemId, Pageable pageable){
	//	Page<ContentWithOutfitDto> contentWithOutfitDto = contentRep.getContentWithOutfitByItemId(UUID.fromString(itemId), pageable);
	//	return contentWithOutfitPRAP.toModel(contentWithOutfitDto, this::toModel);
	//}

	public EntityModel<CursorDto> getContentsByItemId(String itemId, CursorDto cursor) throws Exception{
		PagedList<ContentWithOutfitDto> pagedContentWithOutfitDto = null;

		try{
			pagedContentWithOutfitDto = (PagedList)contentRep.getContentWithOutfitByItemId(UUID.fromString(itemId), cursor);
		}
		catch(Exception e){
			logStackTrace(e);
			throw new Exception("Error getting outfits.");
		}
		
		//return buildPagedResponse(cursor, pagedOutfitDtos, null);
		String queryParams = cursor.getNextURI(pagedContentWithOutfitDto, cursor);

		Link afterLink = WebMvcLinkBuilder.linkTo(ConsumerController.class)
			.slash("contents/items/" + itemId + queryParams)
			.withRel("after");

		cursor.embedResource("contentWithOutfitDtoes", new CollectionModel<ContentWithOutfitDto>(pagedContentWithOutfitDto));

		return new EntityModel<CursorDto>(cursor, afterLink);
	}


	private void persistContent(Outfit parentOutfit, ContentDto contentDto) throws Exception{
		Content content = copyToContent(contentDto);
		Picture picture = content.getPicture();
		PictureDto pictureDto = null;
		List<ItemDto> itemsDto  = new ArrayList<ItemDto>(content.getItems().size());

		for(ItemDto itemDto : contentDto.getItems()){
			Item item = copyToItem(itemDto);
			entityManager.persist(item);
			content.addItem(item, itemDto.getPositionx(), itemDto.getPositiony());
			itemsDto.add(new ItemDto(item));
			/*itemsDto.add(new ItemDto(
				item.getId().toString(), 
				itemDto.getPositionx(), 
				itemDto.getPositiony(), 
				item.getApparelType(), 
				item.getSizeGroupId().toString(), 
				item.getRetailer().toString(), 
				item.getBrand().toString()));*/
		}
		if(picture != null){
			picture.setContent(content);
			entityManager.merge(picture);
			logger.debug("picture after persist = " + picture);
			pictureDto = copyToPictureDto(picture);
		}else{
			throw new Exception("content property, picture must not be null");
		}
		entityManager.persist(content);
		content.setOutfit(parentOutfit);	
	}


	/*
	Makes call to data access layer (DAL) inserting new content resource into database and adds it to the specifed outiftId.
	@param Content 
	@return void
	*/
	//TODO  modify to reuse exisitng items
	@Transactional
	public ContentDto createContent(ContentDto contentDto, String parentId) throws Exception{
		logger.debug("Creating Content");
		Outfit parentOutfit = outfitRep.findById(UUID.fromString(parentId)).get();

		//return persistContent(parentOutfit, contentDto);

		Content content = copyToContent(contentDto);
		Picture picture = content.getPicture();
		PictureDto pictureDto = null;
		List<ItemDto> itemsDto  = new ArrayList<ItemDto>(content.getItems().size());

		for(ItemDto itemDto : contentDto.getItems()){
			Item item = copyToItem(itemDto);
			entityManager.persist(item);
			content.addItem(item, itemDto.getPositionx(), itemDto.getPositiony());
			itemsDto.add(new ItemDto(item));
			/*itemsDto.add(new ItemDto(
				item.getId().toString(), 
				itemDto.getPositionx(), 
				itemDto.getPositiony(), 
				item.getApparelType(), 
				item.getSizeGroupId().toString(), 
				item.getRetailer().toString(), 
				item.getBrand().toString()));*/
		}
		if(picture != null){
			picture.setContent(content);
			entityManager.merge(picture);
			logger.debug("picture after persist = " + picture);
			pictureDto = copyToPictureDto(picture);
		}else{
			throw new Exception("content property, picture must not be null");
		}
		entityManager.persist(content);
		content.setOutfit(parentOutfit);
		entityManager.persist(parentOutfit);
		//return new ContentDto(content.getId().toString(), content.getCoverpicuri(), pictureDto, itemsDto, null);
		return new ContentDto(content);
	}


	//TODO  modify to reuse exisitng items
	@Transactional
	public ArrayList<ContentDto> createContents(ArrayList<ContentDto> contentDtos, String parentId){// throws Exception{
		logger.debug("Creating Contents");
		Outfit parentOutfit = outfitRep.findById(UUID.fromString(parentId)).get();
		ArrayList<ContentDto> contentDtosResult = new ArrayList<ContentDto>(contentDtos.size());

		for(ContentDto contentDto : contentDtos){
			Content content = copyToContent(contentDto);
			content.setId(null);
			entityManager.persist(content);
			PictureDto pictureDtoResult = null;
			List<ItemDto> itemsDtosResult  = new ArrayList<ItemDto>(content.getItems().size());
			//content.setItems(items); 
			logger.debug("ConsumerService#createContents: content after persist() = " + content.toString());

			for(ItemDto itemDto : contentDto.getItems()){
				//Item item = copyToItem(itemDto);

				Item item = buildExistingItem(itemDto);

				logger.debug("ConsumerService#createContents: item before persist/merge =" + item.toString() + "\n");
				logger.debug("ConsumerService#createContents: item#contents before persist/merge =" + item.getContents().toString() + "\n");

				// Item is new so add the picture id associated with its outfit
				if(item.getId() == null){
					item.setPicture(pictureRep.findById(parentOutfit.getCoverPictureId()).get());
					entityManager.persist(item);
				}				
				//else{
				//	item = (Item)entityManager.merge(item);
				//}

				logger.debug("ConsumerService#createContents: item after persist/merge = " + item.toString() + "\n");
				logger.debug("ConsumerService#createContents: item#contents after persist/merge =" + item.getContents().toString() + "\n");
				content.addItem(item, itemDto.getPositionx(), itemDto.getPositiony());

				ItemDto iDto = new ItemDto(item);
				iDto.setPositiony(itemDto.getPositiony());
				iDto.setPositionx(itemDto.getPositionx());
				itemsDtosResult.add(iDto);
				/*itemsDtosResult.add(new ItemDto(
					item.getId().toString(), 
					itemDto.getPositionx(), 
					itemDto.getPositiony(), 
					item.getApparelType(), 
					item.getSizeGroupId().toString(), 
					item.getRetailer().toString(), 
					item.getBrand().toString()));*/
			}

			Picture picture = pictureRep.findById(content.getPicture().getId()).get();
			picture.setContent(content);
			entityManager.merge(picture);
			logger.debug("picture after persist = " + picture);
			pictureDtoResult = copyToPictureDto(picture);
			content.setOutfit(parentOutfit);
			contentDtosResult.add(copyToContentDto(content));
			logger.debug("ConsumerService#createContents: parentOutfit = " + parentOutfit.toString());
			//contentDtosResult.add(new ContentDto(content.getId().toString(), content.getCoverpicuri(), pictureDtoResult, itemsDtosResult));
		}
		entityManager.merge(parentOutfit);
		
		return contentDtosResult;
	}

	//TODO  modify to reuse exisitng items
	//This is called mainly when Content's picture property or its items property (mapping changed, ie adding/removing association to EXISTING items) are modified
	//@Transactional
	//public ContentDto updateContent(ContentDto contentDto, String outfitId) /*throws Exception*/{
	//	logger.debug("Updating Content");
	//	Outfit outfit = outfitRep.findById(UUID.fromString(outfitId)).get();
	//	Content content = copyToContent(contentDto);
	//	Picture picture = content.getPicture();
	//	PictureDto pictureDto = null;
	//	List<ItemDto> itemDtos  = new ArrayList<ItemDto>(content.getItems().size());
	//	List<Integer> exlcudedIndices = new ArrayList<Integer>(content.getItems().size());
	//	
	//	Content mergedContent = entityManager.merge(content);
//
	//	if(picture != null){
	//		entityManager.merge(picture);
	//		logger.debug("picture after persist = " + picture);
	//		pictureDto = new PictureDto(picture.getId().toString(), picture.getThumbnailuri(), picture.getSmalluri(), picture.getLargeuri());
	//	}
	//	else{
	//		//pictureDto = new PictureDto(mergedContent.getPicture());
	//	}
//
	//	//List<Item> items = mergedContent.getItems();
	//	//mergedContent.setItems(items);
	//	mergedContent.setOutfit(outfit);
	//	logger.debug("ConsumerService#updateContent: mergedContent = " + mergedContent.toString());
	//	mergedContent.getItems().clear();
//
	//	for(ItemDto itemDto : contentDto.getItems()){
	//		Item item = copyToItem(itemDto);
	//		ItemContent itemContent = new ItemContent(item, mergedContent);
	//		//add new itemContent association
	//		itemContent.setPositionx(itemDto.getPositionx());
	//		itemContent.setPositiony(itemDto.getPositiony());
	//		mergedContent.addItem(itemContent);	
//
	//		ItemDto iDto = new ItemDto(item);
	//		iDto.setPositiony(itemDto.getPositiony());
	//		iDto.setPositionx(itemDto.getPositionx());
	//		itemDtos.add(iDto);		
	//	}
//
	//	logger.debug("mergedContent after calls persist and setItems: " + mergedContent.toString());
	//	return new ContentDto(mergedContent.getId().toString(), mergedContent.getCoverpicuri(), pictureDto, itemDtos, null);
	//}

	// Handles CRUD operations on content excluding picture.
	@Transactional
	public ContentDto updateContent(ContentDto contentDto, String outfitId) /*throws Exception*/{
		Content content = contentRep.findById(UUID.fromString(contentDto.getId())).get();
		
		// Update content coverpicuri
		content.setCoverpicuri(contentDto.getCoverpicuri());
		logger.debug("ConsumerService#updateContent: content = " + content.toString());

		List<ItemDto> itemDtos  = new ArrayList<ItemDto>(content.getItems().size());
		
		// Update content items
		if(contentDto.getItems() != null && contentDto.getItems().size() > 0){
			content.getItems().clear();

			for(ItemDto itemDto : contentDto.getItems()){
				Item item = copyToItem(itemDto);
				ItemContent itemContent = new ItemContent(item, content);
				
				//add new itemContent association
				itemContent.setPositionx(itemDto.getPositionx());
				itemContent.setPositiony(itemDto.getPositiony());
				content.addItem(itemContent);	
	
				ItemDto iDto = new ItemDto(item);
				iDto.setPositiony(itemDto.getPositiony());
				iDto.setPositionx(itemDto.getPositionx());
				itemDtos.add(iDto);		
			}
		}

		content = entityManager.merge(content);

		logger.debug("content after calls persist and setItems: " + content.toString());
		PictureDto pictureDto = new PictureDto(content.getPicture());
		//pictureDto.setId(null);

		//return new ContentDto(content.getId().toString(), content.getCoverpicuri(), pictureDto, itemDtos, null);
		return new ContentDto(content);
	}

	@Transactional
	public ArrayList<ContentDto> updateContents(ArrayList<ContentDto> contentDtos, String outfitId) throws Exception{
		//TODO  Hanlde multipled content updates
		int count = contentDtos.size();		
		if(count < 7){
			ArrayList<ContentDto> result = new ArrayList(count);

			for(ContentDto contentDto : contentDtos){
				result.add(updateContent(contentDto, outfitId));
			}

			return result;
		}else{
			throw new Exception("Parameter collection size greater than maximum allowed size.");
		}
	}

	@Transactional
	public OutfitDto removeItemsFromContent(HashMap<String, ArrayList<String>> contentIdToItemMap, String username, String outfitId){
		Outfit outfit = outfitRep.findById(UUID.fromString(outfitId)).get();
		Profile profile = outfit.getProfile();
		List<ContentDto> contentDtos;

		if(profile.getUsername().equals(username)){
			//logger.debug("content Ids:");
			//entityManager.merge(outfit);
			for(String contentId : contentIdToItemMap.keySet()){
				//logger.debug(contentId + ": " + "{\n");
				//Content content = contentRep.findById(UUID.fromString(contentId));				
				//List<ItemContent> itemContents = content.getItems();
				//entityManager.persist(content);

				int ind = -1;
				for(Content c : outfit.getContents()){
					ind++;
					if(c.getId().toString().equals(contentId)){
						break;
					}
				}

				Content content = outfit.getContents().get(ind);
				for(String itemId : contentIdToItemMap.get(contentId)){
					content.removeItem(itemId);
				}
				logger.debug("ConsumerService#removeItemsFromContent:  content = \n" + content.toString());
				//content = entityManager.merge(content);
				outfit.getContents().set(outfit.getContents().indexOf(content), content);
			}
			outfit = entityManager.merge(outfit);
		}else{
			logger.warn("User, " + username + " does not have permissions to edit item");
		}
		return copyToOutfitDto(outfit);		
	}

	//=====================================================================================

	/*
	Makes call to data access layer (DAL) inserting new item resource into database.
	@param Item 
	@return void
	*/

	@Transactional
	public void saveItem(Item item){
		logger.debug("saving Item");
		itemRep.saveAndFlush(item);
	}

	@Transactional
	//public PagedModel<?> getFilteredItems(String username, String filter, Pageable pageable){
	//public PagedModel<?> getFilteredItems(String username, String filter, CursorDto cursor){
	public EntityModel<CursorDto> getFilteredItems(String username, String filter, CursorDto cursor){
		switch(filter){
			case "all":
				/*Page<ItemDto> itemDtos = itemRep.findAll(pageable).map(new Converter<Item, ItemDto>(){
					@Override
					public ItemDto convert(Item item){
						String coverpicuri = (item.getPicture() != null) ? item.getPicture().getSmalluri() : "";
						return new ItemDto(item);
					}	
				});*/
				//Page<ItemDto> itemDtos = itemRep.findAll(pageable).map(this::convertToItemDto);
				logger.debug("point1");
				//Page<ItemDto> itemDtos = itemRep.getAllItemsWithCoverpicUri(pageable);
				//List<ItemDto> itemDtos = itemRep.getAllItemsWithCoverpicUri(cursor);
				PagedList<ItemDto> pagedItemDtos = null;
				try{
					pagedItemDtos = (PagedList)itemRep.getAllItemsWithCoverpicUri(cursor);
				}
				catch(NoSuchMethodException e){
					logger.error(e.toString());
				}
				//logger.debug("PageImpl = {}", itemDtos.toString());

				//logger.debug("itemDtos = " + itemDtos.toString());
				//logger.debug("itemDtos.getSize() = " + itemDtos.size());
				//logger.debug("ConsumerController.class = " + ConsumerController.class);

				//// Build "after" link.
				//Link afterLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConsumerController.class))
				//	.slash(itemDtos.getContent().get(itemDtos.getSize()))
				//	//.toURI( .getId() )
				//	.withRel("after");

				// Build "after" link.

				//Link afterLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConsumerController.class, itemDtos.get(itemDtos.size()-1).getId() ))
				//	//.slash(itemDtos.get(itemDtos.size()-1))
				//	//.toURI( .getId() )
				//	.withRel("after");

				/*logger.debug("lowest = " + pagedItemDtos.getKeysetPage().getLowest().getTuple()[0].toString());
				logger.debug("highest = " + pagedItemDtos.getKeysetPage().getHighest().getTuple()[0].toString());*/

				//int direction = cursor.getDirection() == 0 ? 1 : cursor.getDirection();
				///*
				//*	Build query paramters for page link
				//*		first:  	pos of the first element in the previous PagedList
				//*		size:		max number of results from previous PagedList
				//*		date:		date filter
				//*		firstId:	the first entry from the previous PagedList
				//*		lastId:		the last entry from the preivous PagedList
				//*/
				//String queryParams = String.format(
				//	"?&direction=%d&first=%d&size=%d&firstId=%s&lastId=%s&date=%s", 
				//	direction,
				//	pagedItemDtos.getKeysetPage().getFirstResult(), 
				//	pagedItemDtos.getKeysetPage().getMaxResults(), 
				//	pagedItemDtos.getKeysetPage().getLowest().getTuple()[0].toString(), 
				//	pagedItemDtos.getKeysetPage().getHighest().getTuple()[0].toString(),
				//	cursor.getDate()
				//);

				String queryParams = cursor.getNextURI(pagedItemDtos, cursor);

				//String queryParams = String.format(
				//	"?first=%d&size=%d&date=%s&firstId=%s&lastId=%s", 
				//	cursor.getSize(), 
				//	cursor.getDate(), 
				//	itemDtos.get(itemDtos.size()-1).getId()
				//);

				Link afterLink = WebMvcLinkBuilder.linkTo(ConsumerController.class)
					.slash("items?filter=all" + queryParams)
				//Link afterLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConsumerController.class).getFilteredItems(null, new CursorDto(), null))
					//.slash(itemDtos.get(itemDtos.size()-1).getId())
					//.addParameters(
					//	QueryParameters.required("size"),
					//	QueryParameters.required("prevId"),
					//	QueryParameters.required("date")
					//)
					.withRel("after");

				//CursorDto prevCursor = cursor;new Resourc<>(cursor, afterLink);
				//cursor.add(afterLink);
				cursor.embedResource("items", new CollectionModel<ItemDto>(pagedItemDtos));

				// Build "after" link.
				//Link beforLink = linkTo(methodOn(ConsumerController.class)).getFilteredItems( itemDtos.get(0).getId() ).withRel("before");
				
				//return new CollectionModel<ItemDto>(pagedItemDtos, afterLink);
				return new EntityModel<CursorDto>(cursor, afterLink);

				//return itemPRAP.toModel(itemDtos, this::toModel, afterLink);
			//case "bookmarked":

			default:
				return null;
		}		
	}


	//=====================================================================================
	//  SizeChart

	@Transactional
	public SizeChartDto getSizeChartByItemId(String itemId){
		//TODO: fix getSizeChartByItemId
		return sizeChartRep.customGetSizeChartWithItemId(UUID.fromString(itemId));

		//Item item = (Item)itemRep.findById(UUID.fromString(itemId)).get();
		//return (SizeChart)sizeChartRep.findById(item.getSizeChartId()).get();
	}


	//=====================================================================================
	//  Bookmark


	public HashMap<String, Date> getBookmarkedItemsByUsername(String username){
		//return bookmarkRep.findItemIdsByUsername(username);
		return bookmarkRep.customfindItemIdsByUsername(username);
	}
	
	@Transactional
	public Date bookmarkItem(String username, String itemId){
		UUID uuidItemId = UUID.fromString(itemId);
		Bookmark bookmark = bookmarkRep.findByUsernameAndItemId(username, uuidItemId);

		if(bookmark == null){
			bookmark = new Bookmark(null, username, uuidItemId);
			entityManager.persist(bookmark);
			return bookmark.getCreatedOn();
		}

		return null;
	}

	@Transactional
	public void bookmarkItems(String username, ArrayList<String> itemIds){
		List<UUID> uuidItemIds = new ArrayList<UUID>(itemIds.size());
		for(String itemId : itemIds){
			uuidItemIds.add(UUID.fromString(itemId));
		}
		logger.debug("ConsumerService#bookmarkItems:  uuidItemIds List size = " + uuidItemIds.size());
		List<Bookmark> bookmarks = bookmarkRep.findByUsernameAndItemIds(username, uuidItemIds);
		logger.debug("ConsumerService#bookmarkItems:  bookmarks List size = " + bookmarks.size());
		boolean exists;
		for(String itemId : itemIds){
			exists = false;
			for(Bookmark bookmark : bookmarks){
				if(bookmark.getItemId().toString().equals(itemId)){
					exists = true;
					break;
				}
			}
			if(!exists){
				logger.debug("ConsumerService#bookmarkItems:  itemId not found in bookmarks List = " + itemId);
				entityManager.persist(new Bookmark(null, username, UUID.fromString(itemId)));
			}
		}
	}

	//@Transactional
	public void removeBookmarkItem(String username, String itemId){
		bookmarkRep.deleteByUsernameAndItemId(username, UUID.fromString(itemId));
	}

	@Transactional
	public void removeBookmarkItems(String username, ArrayList<String> itemIds){
		List<UUID> uuidItemIds = new ArrayList<UUID>(itemIds.size());
		for(String itemId : itemIds){
			uuidItemIds.add(UUID.fromString(itemId));
		}
		bookmarkRep.deleteByUsernameAndItemIds(username, uuidItemIds);
	}


	//=====================================================================================
	//  Like

	//@Transactional
	//public void likeOutfit(String username, String outfitId){
	//	Like like = likeRep.findByUsernameAndOutfitId(username, UUID.fromString(outfitId));
	//	if(like == null){
	//		//LIKE OUTFIT
	//		entityManager.persist(new Like(null, username, UUID.fromString(outfitId)));
	//		Outfit outfit = outfitRep.findById(UUID.fromString(outfitId)).get();
	//		outfit.setLikes((outfit.getLikes() + 1));
	//		entityManager.merge(outfit);
	//	}
	//}
//
	//@Transactional
	//public void unlikeOutfit(String username, String outfitId){
	//	likeRep.deleteByUsernameAndOutfitId(username, UUID.fromString(outfitId));
	//}


	//=====================================================================================
	//  follow

	/*@Transactional
	public void followUser(String followerUsername, String followedUsername){
		Follow follow = followRep.findByFollowerUsernameAndFollowedUsername(followerUsername, followedUsername);
		if(follow == null){
			entityManager.persist(new Following(null, followerUsername, followedUsername));
		}
	}

	@Transactional
	public void unfollowUser(String followerUsername, String followedUsername){
		followRep.deleteByFollowerUsernameAndFollowedUsername(followerUsername, followedUsername);
	}*/


	//=====================================================================================
	/*
	Makes call to DAL retreiving profile resource
	@param Long specifying id of profile to retreive
	@return ProfileDto which extends RepresentationModel
	*/

	public ProfileDto readProfile(String username, String owner) throws Exception{
		logger.debug("Reading Profile (id=" + username + ")");
		Profile profile = profileRep.findByUsername(username);
		ProfileDto profileDto = null;

		//logger.debug("profile object ofter findByUsername call to profile repository:");
		//logger.debug(profile.toString());
		if (profile == null) throw new Exception("The spicified user does cannot be found");
		
		//set points property to null if user does not own the profile resource
		if (username.equals(owner)){
			profileDto = copyToProfileDto(profile);
		}else{			
			profileDto = copyToProfileDto(profile);
			profileDto.getProfileStatsDto().setPoints(-1);
		}

		return profileDto;
	}

	public ProfileDto readMetric(String outfitId) throws Exception{
		logger.debug("Reading Profile (outfit id = " + outfitId + ")");
		Profile profile = profileRep.findByOutfitId(UUID.fromString(outfitId));
		return copyToProfileDto(profile);
	}

	@Transactional
	public ProfileDto createProfile(ProfileDto profileDto, String username) throws Exception{
		logger.debug("Creating new profile");
		Profile existingProfile = profileRep.findByUsername(username);
		//check if profile exists for user.  If so, insert id field into profileDto before copying to profile DAO
		if(existingProfile == null) throw new Exception("User does not exist");
		//logger.debug(profile.toString());
		//entityManager.persist(profile);
		Profile profile = copyToProfile(profileDto);
		User user = userRep.findByUsername(username);
		logger.debug("User object queried.  Properties [id_text, username] = " + user.getId().toString() + " " + user.getUsername());		
		profile.setUser(userRep.findByUsername(username));
		logger.debug("Profile Object Created.  Properties [username, user.username, user.id_text] = " + profile.getUsername() + " " + profile.getUser().getId().toString());
		//entityManager.persist(profile);
		//Profile idProfile = profileRep.saveAndFlush(profile);//entityManager.flush();
		//if(profile == null) throw new Exception("Could not create profile");
		return copyToProfileDto(profileRep.saveAndFlush(profile));
	}

	// TODO: Possibly break this method down into operations on individual child entities
	@Transactional
	public ProfileDto updateProfile(ProfileDto profileDto, String username) throws Exception{
		//if (profileDto.getUsername() == null || profileDto.getUsername().isEmpty() || !profileDto.getUsername().equals(username)){
		//	throw new Exception("request invalid");
		//}else{
			Profile existingProfile = profileRep.findByUsername(username);

			if(existingProfile == null) throw new Exception("User does not exist");

			PictureProfile pictureProfile = null;
			// Initialize mergedPP with current PictureProfile.  This will be updated with profileDto.PictureProfile if it exists.
			PictureProfile mergedPP = existingProfile.getPictureProfile();
			// Instantiate returned profile object with values from profileDto
			Profile profile = copyToProfile(profileDto);

			// UpdateProfile is invoked under two cases:  ALL updates to profile INCLUDING PictureProfile, and ANY updates to profile EXCLUDING PictureProfile.
			// The condition below handles either case.
			if(profileDto.getPictureDto() != null){
				pictureProfile = new PictureProfile(profileDto.getPictureDto());
				// Change pictureProfile originaluri from null to the persisted value
				pictureProfile.setOriginaluri(existingProfile.getPictureProfile().getOriginaluri());
			}

			profile.setId(existingProfile.getId());
			profile.setUsername(username);
			profile.getUserMetrics().setId(existingProfile.getUserMetrics().getId());
			profile.getTolerance().setId(existingProfile.getTolerance().getId());

			//if picture entity is modified
			if(pictureProfile != null && pictureProfile.getId() != null && pictureProfile.getId().toString().compareTo(existingProfile.getPictureProfile().getId().toString()) == 0){
				logger.debug("About to create cropped images.");
				// crop and optimize images
				PictureDto croppedPP = imageService.updateCroppedImages(pictureProfile);

				pictureProfile.setThumbnailuri(croppedPP.getThumbnailuri());
				pictureProfile.setSmalluri(croppedPP.getSmalluri());
				pictureProfile.setMediumuri(croppedPP.getMediumuri());
				pictureProfile.setLargeuri(croppedPP.getLargeuri());
				pictureProfile.setCrop(croppedPP.getCrop());

				mergedPP = entityManager.merge(pictureProfile);
			}

			profile.setUser(userRep.findByUsername(username));

			//Ensure ProfileStats is not updated via this method
			profile.setProfileStats(existingProfile.getProfileStats());
			logger.debug("about to merge profile");
			profile.setPictureProfile(mergedPP);
			Profile mergedProfile = entityManager.merge(profile);

			return copyToProfileDto(mergedProfile);
		//}
	}

	//
	@Transactional
	public PictureUpdateDto updateCrop(PictureUpdateDto pictureUpdateDto, String username, String type) throws Exception, IllegalStateException{
		// Check if profile exists for username
		Profile profile = profileRep.findByUsername(username);
		if(profile == null) throw new Exception("User does not exist");
		BasePicture picture = null;

		// Verify user is the owner of the picture resource
		logger.debug("ConsumerService#updateCrop: type = " + type);
		if(type.equals("profile")){
			picture = pictureProfileRep.findByOriginaluri(pictureUpdateDto.getOriginaluri());
			if(!((PictureProfile)picture).getProfile().getId().toString().equalsIgnoreCase(profile.getIdText())) throw new IllegalStateException("Unauthorized attempt to modify resource.");	
		}
		else{
			picture = pictureRep.findByOriginaluri(pictureUpdateDto.getOriginaluri());	
			if(!((Picture)picture).getProfileId().toString().equalsIgnoreCase(profile.getIdText())) throw new IllegalStateException("Unauthorized attempt to modify resource.");		
		}

		//logger.debug("ConsumerService#updateCrop: picture = " + picture.toString());
		//logger.debug("ConsumerService#updateCrop: picture -> profileId = " + picture.getProfileId().toString());
		//logger.debug("ConsumerService#updateCrop: profile -> idText = " + profile.getIdText());

		try{
			// Update picture crop with the crop passed in pcitureDto
			picture.setCrop(pictureUpdateDto.getCrop());
			PictureUpdateDto completePUDto = imageService.updatePictureEntity(picture);
			completePUDto.setContentId(pictureUpdateDto.getContentId());
			return completePUDto;
		}
		catch(IllegalStateException e){
			logStackTrace(e);
			throw new IllegalStateException("failed to update picture");
		}
		catch(Exception e){
			logStackTrace(e);
			throw new Exception("failed to update picture");
		}
	}

	



	/*F
	Makes call to data access layer (DAL) inserting new profile resource into database.
	@param Profile 
	@return void
	*/
	@Transactional
	public void saveProfile(Profile profile){
		logger.debug("saving Profile");
		profileRep.saveAndFlush(profile);
	}

	//=====================================================================================

	public PagedModel<?> readBrands(Pageable pageable){
		/*Page<BrandDto> brands = brandRep.findAll(pageable).map(new Converter<Brand, BrandDto>(){
			@Override
			public BrandDto convert(Brand brand){
				return new BrandDto(brand.getIdText(), brand.getName(), brand.getLink(), brand.getRed(), brand.getGreen(), brand.getBlue());
			}	
		});*/
		Page<BrandDto> brands = brandRep.findAll(pageable).map(this::convertToBrandDto);
		logger.debug("brands return from repository: \n" + brands);
		return brandPRAP.toModel(brands);
	}

	public PagedModel<?> getApparelTypes(Pageable pageable){		
		Page<ApparelTypeDto> apparelTypes = apparelTypeRep.findAll(pageable).map(this::convertToApparelTypeDto);
		logger.debug("apparelTypes return from repository: \n" + apparelTypes);
		return apparelTypePRAP.toModel(apparelTypes);
	}

	//public PagedModel<?> readRetailers(Pageable pageable){
	//	/*Page<RetailerDto> retailers = retailerRep.findAll(pageable).map(new Converter<Retailer, RetailerDto>(){
	//		@Override
	//		public RetailerDto convert(Retailer retailer){
	//			return new RetailerDto(retailer.getIdText(), retailer.getName(), retailer.getLink(), retailer.getRed(), retailer.getGreen(), retailer.getBlue());
	//		}	
	//	});*/
	//	Page<RetailerDto> retailers = retailerRep.findAll(pageable).map(this::convertToRetailerDto);
	//	logger.debug("retailers return from repository: \n" + retailers);
	//	return retailerPRAP.toModel(retailers, this::toModel);
	//}

	/*public List<SizeLabel> readSizes(){
		List<SizeDto> sizes = sizesRep.findAll();
		logger.debug("sizes return from repository: \n" + sizes);
		return sizes;
	}*/

	@Transactional
	private void savePicture(String imgFileName){
		if(imgFileName != null){
			Picture picture = new Picture();
			picture.setLargeuri(imgFileName);
			pictureRep.saveAndFlush(picture);
		}else{
			return;
		}
	}

	/*
	saves jpeg data to filesystem
	@param MulitpartHttpServletRequest data 
	@return String indicating generated filename.
	*/
	@Transactional
	public PictureDto saveImage(String username, MultipartHttpServletRequest data){
		Profile profile = profileRep.findByUsername(username);
		logger.debug("ConsumerService.saveImage() invoked");
		PictureDto pictureDto = imageService.saveImage(data);
		Picture picture = copyToPicture(pictureDto);
		picture.setProfileId(profile.getId());
		entityManager.persist(picture);
		pictureDto.setId(picture.getId().toString());
		return pictureDto;
	}
	
	/**
	*saves jpeg data to filesystem, and creates an entry in PictureDeleted with associating picture entity.
	*TODO:  Finiah implementing multiple files identified by a multipart contentId parameter
	*@param MulitpartHttpServletRequest data 
	*@param String contentId
	*@returns json picture object of existing picture entity having properties modified with new image filnames.
	**/
	@Transactional
	public List<PictureUpdateDto> updateImage(MultipartHttpServletRequest data, String contentId){
		//send existing image to the PictureDeleteTable
		Content content = contentRep.findById(UUID.fromString(contentId)).get();
		Picture picture = content.getPicture();

		//Picture picture = pictureRep.findById(UUID.fromString(pictureId));
		PictureDelete pd = new PictureDelete(picture);
		logger.debug("PictureDelete created = " + pd.toString());
		pictureDeleteRep.saveAndFlush(pd);

		//save the new image data to fs and return in picture json with existing id.
		PictureUpdateDto pictureUpdateDto = (PictureUpdateDto)imageService.saveImage(data);
		pictureUpdateDto.setId(picture.getId().toString());
		pictureUpdateDto.setContentId(contentId);

		List<PictureUpdateDto> pictureUpdateDtos = new ArrayList<PictureUpdateDto>();
		pictureUpdateDtos.add(pictureUpdateDto);

		return pictureUpdateDtos;		
	}

	/**
	*deletes a one or more images identified by the user
	*<p>
	*\/user\/sendVerificationEmail
	*</p>
	*@param token Current expired token sent in request parameter
	*@return ResponseEntity<?> with Status 400 contentId does not belong to user, otherwise status 200
	*/
	@Transactional
	public void deleteImages(String username, ArrayList<String> contentIds) {

		List<Content> contents = contentRep.getContentsByIds(contentIds);
		boolean containsIllegalId = false;		

		// Check if each content entity belongs to the user
		for(Content content : contents){			
			if(!content.getOutfit().getUsername().equals(username)){
				throw new IllegalStateException("Unauthorized action");
			}
		}

		// If at least one content entity does not belong to the user, thrwo an exception
		contentRep.deleteInBatch(contents);

		return;
	}

	/**
	*Deletes one or more outfits identified by the user.
	*Note: Child picture entities are marked for later deletion by creating deletePicutre entity.  
	*<p>
	*\/user\/sendVerificationEmail
	*</p>
	*@param token Current expired token sent in request parameter
	*@return ResponseEntity<?> with Status 400 contentId does not belong to user, otherwise status 200
	*/
	@Transactional
	public void deleteOutfits(String username, List<String> outfitIds) {
		logger.debug("in deleteOutfits");
		if(outfitIds.size() > 0){
			try{	
				if(outfitIds.size() > 50) throw new IllegalStateException("delete request limit reached.");
	
				List<PictureDelete> pictureDeletes = new ArrayList<PictureDelete>();
				List<Content> contents = new ArrayList<Content>();
				List<Picture> pictures = new ArrayList<Picture>();
				List<Item> items = null;
				List<UUID> contentIds = new ArrayList<UUID>();
				List<UUID> pictureIds = new ArrayList<UUID>();
				Set<ItemContent> itemContents = new HashSet<ItemContent>();
				List<Outfit> outfits = outfitRep.getOutfitsByIds(outfitIds);
				Profile profile = outfits.get(0).getProfile();
				List<LikeCount> likeCounts = likeCountRep.findByOutfitIds(outfitIds);				
				List<LikeCountProfileId> likeCountProfileIds = likeCounts.stream().map(likeCount -> new LikeCountProfileId(likeCount.getId(), profile.getId())).collect(Collectors.toList());				
				List<LikeCountProfile> likeCountProfiles = likeCountProfileRep.findByIds(likeCountProfileIds);
				int count = 0;

				for(Outfit outfit : outfits){
					logger.debug("Iteration {}", count);
					
					if(!outfit.getUsername().equals(username)) throw new IllegalStateException("Unauthorized action.");
					contents.addAll(outfit.getContents());
		
					for(Content content : outfit.getContents()){
						// Copy exiting Picture entity to a PictureDelete entity
						PictureDelete pd = new PictureDelete(content.getPicture());
						pictureDeletes.add(pd);

						logger.debug("ConsumerService#deleteOutfits: content item count = " + content.getItems().size());

						if(content.getItems() != null && content.getItems().size() > 0){
							//itemContentRep.deleteInBatch(content.getItems());
							itemContents.addAll(content.getItems());
						}

						if(content.getPicture() != null){
							pictures.add(content.getPicture());
							pictureIds.add(content.getPicture().getId());
						}

						//content.setOutfit(null);
					}

					//outfit.setProfile(null);
					outfit.getContents().clear();
					count++;
				}

				items = itemRep.findByPictureIds(pictureIds);
				items.stream().forEach(item -> item.setPicture(null));
				likeCountProfiles.stream().forEach(likeCountProfile -> likeCountProfile.setLikeCount(null));
				likeCounts.stream().forEach(likeCount -> likeCount.setOutfit(null));
				
				entityManager.flush();
				
				itemContentRep.deleteInBatch(itemContents);
				//likeCountProfileRep.deleteInBatch(likeCountProfiles);				
				//likeCountRep.deleteInBatch(likeCounts);
				pictureRep.deleteInBatch(pictures);
				contentRep.deleteInBatch(contents);
				outfitRep.deleteInBatch(outfits);
				pictureDeleteRep.saveAll(pictureDeletes);
	
				return;
			}
			catch(Exception e){
				logger.debug("Something went wrong: " + e.toString());
			}
		}
	}

	@Transactional
	public PictureDto updateProfileImage(MultipartHttpServletRequest data, String username) throws Exception{
		//send existing image to the PictureDeleteTable
		Profile profile = profileRep.findByUsername(username);

		if(profile == null) throw new Exception("User does not exist");

		PictureProfile pp = profile.getPictureProfile();
		PictureDto pictureDto = imageService.saveImage(data);

		// First time submitting a profile picture
		if(pp == null){
			pp = new PictureProfile(pictureDto);
			profile.setPictureProfile(pp);
			entityManager.merge(profile);
		}
		// Existing profile picture updated.
		else {
			pp.setThumbnailuri(pictureDto.getThumbnailuri());
			pp.setSmalluri(pictureDto.getSmalluri());
			pp.setMediumuri(pictureDto.getMediumuri());
			pp.setLargeuri(pictureDto.getLargeuri());
			pp.setOriginaluri(pictureDto.getOriginaluri());
			entityManager.merge(pp);
		}

		return pictureDto;
	}

	public byte[] getImage(String filename/*, HttpServletResponse response*/){
		//Path file = Paths.get(imgfolder+filename);
		byte[] image = null;
		logger.debug("in ConsumerService.getImage()");
		String subfolder = "";
		switch(filename.substring(0, Math.min(filename.length(), 3))){
			case "ogl":
				subfolder = "original/";
				break;

			case "sml":
				subfolder = "small/";
				break;

			case "med":
				subfolder = "medium/";
				break;

			case "lrg":
				subfolder = "large/";
				break;

			case "tnl":
				subfolder = "thumbnail/";
				break;
				
			default:
				logger.debug("no matching prefix in filename");
				break;
		}
		try{
			iStream = new FileInputStream(imgfolder + subfolder + filename + ".jpg");
			image = IOUtils.toByteArray(iStream);
			logger.debug("image byte[] length = " + image.length);
			return image;
			/*Files.copy(file, response.getOutputStream());
			response.getOutputStream.flush();*/
		}catch(IOException e){
			logger.debug(e);
		}
		return image;
	}
}