package oxi.services;

import java.lang.*;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Map;
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
public class ConsumerService implements ClientService{
	//Repositories
	@Autowired private OutfitRepository outfitRep;
	@Autowired private ContentRepository contentRep;
	@Autowired private ItemRepository itemRep;
	@Autowired private PictureRepository pictureRep;
	@Autowired private PictureDeleteRepository pictureDeleteRep;
	@Autowired private ProfileRepository profileRep;
	@Autowired private UserRepository userRep;
	@Autowired private RoleRepository roleRep;
	@Autowired private BrandRepository brandRep;
	@Autowired private RetailerRepository retailerRep;
	@Autowired private SizeRepository sizeRep;

	//Resource Assemblers
	//@Autowired 
	//private OutfitResourceAssembler outfitRA;
	//@Autowired private ContentResourceAssembler contentRA;
	//@Autowired private ItemResourceAssembler itemRA;
	//@Autowired private PictureResourceAssembler pictureRA;
	//@Autowired private ProfileResourceAssembler profileRA;
	//@Autowired private UserResourceAssembler userRA;

	//Paged Resource Assemblers 
	@Autowired private PagedResourcesAssembler<Outfit> outfitPRA;
	@Autowired private PagedResourcesAssembler<OutfitDto> outfitPRAP;
	@Autowired private PagedResourcesAssembler<Item> itemPRA;
	@Autowired private PagedResourcesAssembler<Brand> brandPRA;
	@Autowired private PagedResourcesAssembler<BrandDto> brandPRAP;
	@Autowired private PagedResourcesAssembler<Retailer> retailerPRA;
	@Autowired private PagedResourcesAssembler<RetailerDto> retailerPRAP;

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

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;


	public ConsumerService(){

	}
	//=====================================================================================

	private static ProfileDto copyToProfileDto(Profile profile){
		return new ProfileDto(
			null,
			profile.getUsername(),
			profile.getCountry(),
			profile.getDateOfBirth(),
			profile.getBodyShape(),
			profile.getMens(),
			profile.getWomens(),
			profile.getHeight(),
			profile.getNeck(),
			profile.getFullShoulder(),
			profile.getHalfShoulder(),
			profile.getChest(),
			profile.getWaist(),
			profile.getHip(),
			profile.getSleeve(),
			profile.getFrontLength(),
			profile.getBackLength(),
			profile.getPantOutseam(),
			profile.getPantInseam(),
			profile.getThigh(),
			profile.getCalf()
			); 
	}

	private static Profile copyToProfile(ProfileDto profileDto){
		Profile profile = new Profile();
		logger.debug("starting copy.");
		if(!profileDto.getId().isEmpty() && profileDto.getId() != null) profile.setId(UUID.fromString(profileDto.getId()));
		profile.setUsername(profileDto.getUsername());
		profile.setCountry(profileDto.getCountry());
		profile.setDateOfBirth(profileDto.getDateOfBirth());
		profile.setBodyShape(profileDto.getBodyShape());
		profile.setMens(profileDto.getMens());
		profile.setWomens(profileDto.getWomens());
		profile.setHeight(profileDto.getHeight());
		profile.setNeck(profileDto.getNeck());
		profile.setFullShoulder(profileDto.getFullShoulder());
		profile.setHalfShoulder(profileDto.getHalfShoulder());
		profile.setChest(profileDto.getChest());
		profile.setWaist(profileDto.getWaist());
		profile.setHip(profileDto.getHip());
		profile.setSleeve(profileDto.getSleeve());
		profile.setFrontLength(profileDto.getFrontLength());
		profile.setBackLength(profileDto.getBackLength());
		profile.setPantOutseam(profileDto.getPantOutseam());
		profile.setPantInseam(profileDto.getPantInseam());
		profile.setThigh(profileDto.getThigh());
		profile.setCalf(profileDto.getCalf());
		logger.debug("finished copy.");
		return profile;
	}

	private static OutfitDto copyToOutfitDto(Outfit outfit){
		if(outfit != null){
			int contentLength = outfit.getContents().size();
			List<ContentDto> contentDtos = new ArrayList<ContentDto>(contentLength);
			for(Content content: outfit.getContents()){
				contentDtos.add(copyToContentDto(content));
			}
			return new OutfitDto(outfit.getId().toString().toUpperCase(), outfit.getLikes(), outfit.getComments(), contentDtos, outfit.getCoverpicuri());
		}
		return null;
	}

	private static ContentDto copyToContentDto(Content content){
		if(content != null){
			int itemLength = content.getItems().size();
			List<ItemDto> itemDtos = new ArrayList(itemLength);
			for(Item item : content.getItems()){
				itemDtos.add(copyToItemDto(item));
			}
			PictureDto pictureDto = copyToPictureDto(content.getPicture());
			return new ContentDto(content.getId().toString().toUpperCase(), content.getCoverpicuri(), pictureDto, itemDtos);
		}
		return null;
	}

	private static ItemDto copyToItemDto(Item item){
		if(item != null){
			return new ItemDto(item.getId().toString().toUpperCase(), item.getPositionx(), item.getPositiony(), item.getType(), item.getSize(), item.getRetailer().toString().toUpperCase(), item.getBrand().toString().toUpperCase());
		}
		return null;
	}

	private static PictureDto copyToPictureDto(Picture picture){
		if(picture != null){
			return new PictureDto(picture.getId().toString().toUpperCase(), picture.getThumbnailuri(), picture.getSmalluri(), picture.getLargeuri());
		}
		return null;
	}

	private static Picture copyToPicture(PictureDto pictureDto){
		if(pictureDto != null){
			if(pictureDto.getId() != null) return new Picture(UUID.fromString(pictureDto.getId()), pictureDto.getThumbnailuri(), pictureDto.getSmalluri(), pictureDto.getLargeuri());	
			else return new Picture(null, pictureDto.getThumbnailuri(), pictureDto.getSmalluri(), pictureDto.getLargeuri());
		}
		return null;
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
				//List<UUID> contentIds = new ArrayList<UUID>(contentsCount);
				/*List<Content> contents = new ArrayList<Content>(contentsCount);
				for(Content c : outfit.getContents()){
					List<Item> items = c.getItems();
					Picture p = c.getPicture();
					if(p != null){
						if(p.getId() != null){
							pictureRep.save(entityManager.merge(p));
						}else{
							entityManager.persist(p);
						}
						logger.debug("picture after persist = " + p);
						c.setPicture(p);
					}else{
						logger.debug("picture property from Content object is null");
					}*/
					/*if(c.getId() != null){
						logger.debug("content id is not null");
						logger.debug("content id = " + c.getId());
						//contentRep.save(entityManager.merge(c));
						contentRep.save(entityManager.persist(c));
					}else{
						logger.debug("content id is null");
						entityManager.persist(c);
					}
					logger.debug("content after persist = " + c);
					List<ItemDto> itemDtos = new ArrayList<ItemDto>(c.getItems().size());
					for(Item i : c.getItems()){
						if(i.getId() != null) itemRep.save(entityManager.merge(i));
						else entityManager.persist(i);
						itemDtos.add(new ItemDto(i.getId().toString().toUpperCase(), i.getPositionx(), i.getPositiony(), i.getType(), i.getSize(), i.getRetailer().toString().toUpperCase(), i.getBrand().toString().toUpperCase()));
					}
					contents.add(c);
					Picture picture = c.getPicture();
					PictureDto pictureDto = picture == null ? null : new PictureDto(picture.getId().toString().toUpperCase(), picture.getThumbnailuri(), picture.getSmalluri(), picture.getLargeuri());
					contentDtos.add(new ContentDto(c.getId().toString().toUpperCase(), c.getCoverpicuri(), pictureDto, itemDtos));
				}
				//DOTO:  see about getting rid of this loop
				for(Content c : contents){
					outfit.addContent(c);
				}*/
				outfit.setProfile(profile);
				/*if(outfit.getId() != null) outfitRep.save(entityManager.merge(outfit));
				else entityManager.persist(outfit);*/
				//profile.setOutfit(outfit);
				//entityManager.persist(profile);
				outfitDto = copyToOutfitDto(entityManager.merge(outfit));
				//outfit.setProfile(profile);
				logger.debug("outfit after persist = ");
				logger.debug(outfit);
			}else{
				logger.warn("No profile exists for user!");
			}
			//outfitDto = new OutfitDto(outfit.getId().toString().toUpperCase(), outfit.getLikes(), outfit.getComments(), contentDtos, outfit.getCoverpicuri());
		}else{
			logger.warn("Rejecting Outfit entity. Outfit entity contains no Content childeren. Outfit entity must have at least 1 Content child entity");
		}
		return outfitDto;
	}

	@Transactional
	public OutfitDto addOutfit(Outfit outfit, String username){
		OutfitDto outfitDto = null;
		int contentsCount = outfit.getContents().size();
		Profile profile = profileRep.findByUsername(username);
		
		for(Content c : outfit.getContents()){
			Picture picture = pictureRep.findById(c.getPicture().getId());
			entityManager.persist(picture);
			picture.setContent(c);
		}
		entityManager.persist(outfit);
		for(Content c : outfit.getContents()){
			c.setOutfit(outfit);
		}
		outfit.setProfile(profile);
		outfitDto = copyToOutfitDto(outfit);
		
		return outfitDto;
	}

	@Transactional
	public OutfitDto addContents(List<Content> contents, String username, String outfitId){
		OutfitDto outfitDto = null;
		Outfit outfit = outfitRep.findById(UUID.fromString(outfitId));
		//Verify that client is owner of outfitId
		Profile profile = outfit.getProfile();
		if(profile.getUsername().equals(username)){
			//Add new contents to outfit.contents list
			for(Content c : contents){
				Picture picture = pictureRep.findById(c.getPicture().getId());
				entityManager.persist(picture);
				picture.setContent(c);
				c.setOutfit(outfit);
				entityManager.persist(c);
			}
			entityManager.persist(outfit);
			outfitDto = copyToOutfitDto(outfit);
		}else{
			logger.warn("User does not have permissions to edit outfit with provided Id");
		}
		return outfitDto;
	}


	//EXPERIMENT
	/*
	Makes call to DAL retreiving outfit resource
	@param Long specifying id of outfit to retreive
	@return OutfitDto which extends ResourceSupport
	*/
	public ResourceSupport readOutfit(String outfitId){
		//TODO:  ???need a customized json serialization to replace HAL links with raw data.
		logger.debug("Reading Outfit (id=" + outfitId + ")");
		//logger.debug("OutfitResource:  " + outfitResource);
		return null;//this.toResource(outfitRep.findById(outfitId));
	}

	public PagedResources<?> readOutfits(String username, Pageable pageable){
		logger.debug("username = " + username);
		Profile profile = profileRep.findByUsername(username);
		Page<OutfitDto> outfits = outfitRep.findByProfileId(profile.getId(), pageable);
		logger.debug("outfits return from repository: \n" + outfits);
		// Tell PAR to use the user assembler for individual items.
		PagedResources<?> pagedOutfitResource = outfitPRAP.toResource(outfits, this::toResource);
		return pagedOutfitResource;
	}

	public PagedResources<?> readFilteredOutfits(String filter, Pageable pageable){
		switch(filter){
			case "all":
				Page<OutfitDto> outfits = outfitRep.findAll(pageable).map(new Converter<Outfit, OutfitDto>(){
					@Override
					public OutfitDto convert(Outfit outfit){
						return new OutfitDto(outfit.getIdText(), outfit.getLikes(), outfit.getComments(), new ArrayList<ContentDto>(5), outfit.getCoverpicuri());
					}	
				});
				return outfitPRAP.toResource(outfits, this::toResource);
			default:
				return null;
		}		
	}

	public List<?> readContents(String outfitId){
		List<ContentDto> contents = contentRep.findByOutfitId(UUID.fromString(outfitId));
		return contents.stream().map(this::toResource).collect(Collectors.toList());
	}

	/*
	Makes call to data access layer (DAL) inserting new content resource into database and adds it to the specifed outiftId.
	@param Content 
	@return void
	*/
	@Transactional
	public ContentDto saveContent(Content content, String parentId) throws Exception{
		logger.debug("saving Content");

		//Find Outfit by parentId
		Outfit parentOutfit = outfitRep.findById(UUID.fromString(parentId));

		//Build content Entity copying over the properties of content
		//Content content = new Content(null, content.getCoverpicuri(), null, null);
		Picture p = content.getPicture();
		PictureDto pDto = null;
		List<ItemDto> itemsDto  = new ArrayList<ItemDto>(content.getItems().size());
		for(Item i : content.getItems()){
			entityManager.persist(i);
			itemsDto.add(new ItemDto(i.getId().toString(), i.getPositionx(), i.getPositiony(), i.getType(), i.getSize(), i.getRetailer().toString(), i.getBrand().toString()));
		}
		//content.setItems(items);
		if(p != null){
			//p = new Picture(null, pDto.getThumbnailuri(), pDto.getSmalluri(), pDto.getLargeuri());
			entityManager.persist(p);
			logger.debug("picture after persist = " + p);
			pDto = new PictureDto(p.getId().toString(), p.getThumbnailuri(), p.getSmalluri(), p.getLargeuri());
			//content.setPicture(p);
		}else{
			throw new Exception("content property, picture must not be null");
		}
		entityManager.persist(content);
		content.setOutfit(parentOutfit);
		entityManager.persist(parentOutfit);
		return new ContentDto(content.getId().toString(), content.getCoverpicuri(), pDto, itemsDto);
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

	//=====================================================================================
	/*
	Makes call to DAL retreiving profile resource
	@param Long specifying id of profile to retreive
	@return ProfileDto which extends ResourceSupport
	*/



	public ProfileDto readProfile(String username) throws Exception{
		logger.debug("Reading Profile (id=" + username + ")");
		Profile profile = profileRep.findByUsername(username);
		if (profile == null) throw new Exception("The spicified user does cannot be found");
		return copyToProfileDto(profile);
	}

	public ProfileDto readMetric(String outfitId) throws Exception{
		logger.debug("Reading PRofile (outfit id = " + outfitId + ")");
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

	@Transactional
	public ProfileDto updateProfile(ProfileDto profileDto, String username) throws Exception{
		if (profileDto.getUsername() == null || profileDto.getUsername().isEmpty() || !profileDto.getUsername().equals(username)){
			throw new Exception("request invalid");
		}else{
			profileDto.setId(profileRep.findByUsername(username).getId().toString());
			Profile profile = copyToProfile(profileDto);
			profile.setUser(userRep.findByUsername(profile.getUsername()));
			profile = profileRep.save(entityManager.merge(profile));
			return copyToProfileDto(profile);
		}
	}


	@Transactional
	public ResponseEntity<?> provisionUser(UserDto userDto){
		try{
			String conflicts = "";
			//check if data exists
			conflicts += (userRep.findByEmail(userDto.getEmail()) != null) ? "email " : "";
			conflicts += (userRep.findByUsername(userDto.getUsername()) != null) ? "username " : "";
			if(conflicts.compareTo("") != 0){
				return new ResponseEntity("Fields: " + conflicts, HttpStatus.CONFLICT);
			}

			User user = new User();
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
			user.setEmail(userDto.getEmail());
			user.setUsername(userDto.getUsername());
			user.setRoles(Arrays.asList(roleRep.findByName("ROLE_USER")));
			user.setEnabled(true);
			entityManager.persist(user);
			logger.debug("User object persisted.  user.id: " + user.getId().toString());
			//Create an empty Profile linked to this user
			Profile profile = new Profile();
			profile.setUser(user);
			profile.setUsername(user.getUsername());
			entityManager.persist(profile);
			logger.debug("Profile object persisted.  profile.user.id: " + profile.getUser().getId().toString());
			//userRep.saveAndFlush(user);	

			//Set User roles
						
			return new ResponseEntity(user.getUsername(), HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity("Our servers seem to have freyed a bit.\nPlease wait a moment and try your request agian.", HttpStatus.INTERNAL_SERVER_ERROR);
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

	public PagedResources<?> readBrands(Pageable pageable){
		Page<BrandDto> brands = brandRep.findAll(pageable).map(new Converter<Brand, BrandDto>(){
			@Override
			public BrandDto convert(Brand brand){
				return new BrandDto(brand.getIdText(), brand.getName(), brand.getLink(), brand.getRed(), brand.getGreen(), brand.getBlue());
			}	
		});
		logger.debug("brands return from repository: \n" + brands);
		return brandPRAP.toResource(brands, this::toResource);
	}

	public PagedResources<?> readRetailers(Pageable pageable){
		Page<RetailerDto> retailers = retailerRep.findAll(pageable).map(new Converter<Retailer, RetailerDto>(){
			@Override
			public RetailerDto convert(Retailer retailer){
				return new RetailerDto(retailer.getIdText(), retailer.getName(), retailer.getLink(), retailer.getRed(), retailer.getGreen(), retailer.getBlue());
			}	
		});
		logger.debug("retailers return from repository: \n" + retailers);
		return retailerPRAP.toResource(retailers, this::toResource);
	}

	/*public List<Size> readSizes(){
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
	public PictureDto saveImage(MultipartHttpServletRequest data){
		logger.debug("ConsumerService.saveImage() invoked");
		PictureUpdateDto pictureUpdateDto = imageService.saveImage(data);
		Picture picture = copyToPicture(pictureUpdateDto);
		entityManager.persist(picture);
		pictureUpdateDto.setId(picture.getId().toString());
		return pictureUpdateDto;
	}
	
	/*
	saves jpeg data to filesystem, and creates an entry in PictureDeleted with associating picture entity.
	TODO:  Finiah implementing multiple files identified by a multipart contentId parameter
	@param MulitpartHttpServletRequest data 
	@param String contentId
	@returns json picture object of existing picture entity having properties modified with new image filnames.
	*/
	@Transactional
	public List<PictureUpdateDto> updateImage(MultipartHttpServletRequest data, String contentId){
		//send existing image to the PictureDeleteTable
		Content content = contentRep.findById(UUID.fromString(contentId));
		Picture picture = content.getPicture();
		//Picture picture = pictureRep.findById(UUID.fromString(pictureId));
		PictureDelete pd = new PictureDelete(picture);
		logger.debug("PictureDelete created = " + pd.toString());
		pictureDeleteRep.saveAndFlush(pd);
		//save the new image data to fs and return in picture json with existing id.
		PictureUpdateDto pictureUpdateDto = imageService.saveImage(data);
		pictureUpdateDto.setId(picture.getId().toString());
		pictureUpdateDto.setContentId(contentId);
		List<PictureUpdateDto> pictureUpdateDtos = new ArrayList<PictureUpdateDto>();
		pictureUpdateDtos.add(pictureUpdateDto);
		return pictureUpdateDtos;		
	}

	public byte[] getImage(String filename/*, HttpServletResponse response*/){
		//Path file = Paths.get(imgfolder+filename);
		byte[] image = null;
		logger.debug("in ConsumerService.getImage()");
		String subfolder = "";
		switch(filename.substring(0, Math.min(filename.length(), 3))){
			case "sml":
				subfolder = "small/";
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

	private <T extends Identifiable<String>> ResourceSupport toResource(T dto){		
		//Link outfitLink = null;// links.linkForSingleResource(dto).withRel("outfit");
		//SLink selfLink = links.linkForSingleResource(dto).withSelfRel();
		return new Resource<T>(dto/*, null, selfLink*/);
	}
}