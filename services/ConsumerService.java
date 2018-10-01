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

	@Autowired private RepositoryEntityLinks links;

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
				int n=0;
				//List<UUID> contentIds = new ArrayList<UUID>(contentsCount);
				List<Content> contents = new ArrayList<Content>(contentsCount);
				for(Content c : outfit.getContents()){
					entityManager.persist(c);
					contents.add(c);
					List<ItemDto> itemDtos = new ArrayList<ItemDto>(c.getItems().size());
					for(Item i : c.getItems()){
						entityManager.persist(i);
						itemDtos.add(new ItemDto(i.getId().toString().toUpperCase(), i.getPositionx(), i.getPositiony(), i.getType(), i.getSize(), i.getRetailer().toString().toUpperCase(), i.getBrand().toString().toUpperCase()));
					}
					contentDtos.add(new ContentDto(c.getId().toString().toUpperCase(), c.getCoverpicuri(), itemDtos));
				}
				//DOTO:  see about getting rid of this loop
				for(Content c : contents){
					outfit.addContent(c);
				}
				outfit.setProfile(profile);
				entityManager.persist(outfit);
				logger.debug("outfit after persist = ");
				logger.debug(outfit);
			}else{
				logger.warn("No profile exists for user!");
			}
			outfitDto = new OutfitDto(outfit.getId().toString().toUpperCase(), outfit.getLikes(), outfit.getComments(), contentDtos, outfit.getCoverpicuri());
		}else{
			logger.warn("Rejecting Outfit entity. Outfit entity contains no Content childeren. Outfit entity must have at least 1 Content child entity");
		}
		return outfitDto;
	}

	/*
	Makes call to DAL retreiving outfit resource
	@param Long specifying id of outfit to retreive
	@return OutfitDto which extends ResourceSupport
	*/
	/*public OutfitDto readOutfit(Long id){
		//TODO:  ???need a customized json serialization to replace HAL links with raw data.
		logger.debug("Reading Outfit (id=" + id + ")");
		OutfitDto outfitResource = outfitRA.toResource(outfitRep.getOne(id));
		logger.debug("OutfitResource:  " + outfitResource);
		return outfitResource;
	}

	public PagedResources<OutfitDto> readOutfits(Pageable pageable){
		Page<Outfit> outfits = outfitRep.findAll(pageable);
		// Tell PAR to use the user assembler for individual items.
		PagedResources<OutfitDto> pagedOutfitResource = outfitPRA.toResource(outfits, outfitRA);
		return pagedOutfitResource;
	}*/


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

	/*public List<?> readOutfits(Long profileId, Pageable pageable){
		List<OutfitDto> outfits = outfitRep.findByProfileId(profileId);
		logger.debug("outfits return from repository: \n" + outfits);
		// Tell PAR to use the user assembler for individual items.
		List<?> outfitResources = outfits.stream().map(this::toResource).collect(Collectors.toList());
		//List<?> pagedOutfitResources = outfitPRAP.toResource(outfits, this::toResource);
		logger.debug("outfitResources = " + outfitResources);
		return outfitResources;
	}*/

	/*private ResourceSupport toResource(OutfitProjection projection){
		//OutfitDto dto = new OutfitDto(projection.getId(), projection.getLikes(), projection.getComments(), projection.getContents(), projection.getCoverpicuri());
		OutfitDto dto = new OutfitDto(projection.getId(), projection.getLikes(), projection.getComments(), projection.getContents(), projection.getCoverpicuri());		
		Link outfitLink = links.linkForSingleResource(projection).withRel("outfit");
		Link selfLink = links.linkForSingleResource(projection).withSelfRel();
		return new Resource<>(dto, outfitLink, selfLink);
		/*OutfitDto dto = new OutfitDto(projection.getOutfit());		
		Link outfitLink = links.linkForSingleResource(projection.getOutfit()).withRel("outfit");
		Link selfLink = links.linkForSingleResource(projection.getOutfit()).withSelfRel();
		return new Resource<>(dto, outfitLink, selfLink);
	}*/
	//=====================================================================================

	/*
	Makes call to DAL retreiving content resource
	@param Long specifying id of content to retreive
	@return ContentDto which extends ResourceSupport
	*/
	/*public ContentDto readContent(Long id){
		//TODO:  ???need a customized json serialization to replace HAL links with raw data.
		logger.debug("Reading Content (id=" + id + ")");
		ContentDto contentResource = contentRA.toResource(contentRep.getOne(id));
		logger.debug("OutfitResource:  " + contentResource);
		return contentResource;
	}*/

	public List<?> readContents(String outfitId){
		List<ContentDto> contents = contentRep.findByOutfitId(UUID.fromString(outfitId));
		return contents.stream().map(this::toResource).collect(Collectors.toList());
	}

	/*
	Makes call to data access layer (DAL) inserting new content resource into database.
	@param Content 
	@return void
	*/
	@Transactional
	public void saveContent(Content content){
		logger.debug("saving Content");
		contentRep.saveAndFlush(content);
	}
	//=====================================================================================

	/*
	Makes call to DAL retreiving item resource
	@param Long specifying id of item to retreive
	@return ItemDto which extends ResourceSupport
	*/
	/*public ItemDto readItem(Long id){
		//TODO:  ???need a customized json serialization to replace HAL links with raw data.
		logger.debug("Reading Item (id=" + id + ")");
		ItemDto itemResource = itemRA.toResource(itemRep.getOne(id));
		logger.debug("ItemRsource:  " + itemResource);
		return itemResource;
	}

	public PagedResources<ItemDto> readItems(Pageable pageable){
		Page<Item> items = itemRep.findAll(pageable);
		// Tell PAR to use the user assembler for individual items.
		PagedResources<ItemDto> pagedItemResource = itemPRA.toResource(items, itemRA);
		return pagedItemResource;
	}*/
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
	public String saveImage(MultipartHttpServletRequest data){
		logger.debug("Hitting uploadImage method!");
		
		//Iterator<String> itr = data.getFileNames();
		//MultipartFile file = data.getFile("imageFile");

		Enumeration<String> parameters = data.getParameterNames();
		String[] multipartPayload = data.getParameterValues(parameters.nextElement());
		String file = multipartPayload[0].split(",")[1];

		//logger.debug(file);
		//build file string
		/*for(String s : multipartPayload){
			file += s;
		}*/

		//Todo:	the below snippet returns an empty Iterator.  Why does this happen?  it was working before.
		//		If I call getParameterNames below It returns the requst parameter imageFile from which I can extract the file data.
		/*if(!itr.hasNext()){logger.debug("no parameter names returned from calling getFileNames() on MultipartHttpServletRequest, data");}
		else{
			while(itr.hasNext()){
				String paramName = itr.next();
				logger.debug("parameter name: " + paramName);
			}
		}*/

		//Note:	getParameterNames implementation works
		/*Enumeration<String> parameters = data.getParameterNames();
		if(!parameters.hasMoreElements()){logger.debug("no parameter names found in MultipartHttpServletRequest, data.");}
		else{
			while(parameters.hasMoreElements()){
				String paramName = parameters.nextElement();
				logger.debug("parameter name: " + paramName);
				//logger.debug(paramName + " value length:  " + data.getParameterValues(paramName)[0]);
			}
		}*/

		if(!file.isEmpty()){
			File imgpath = null;
			try{
				imgpath = File.createTempFile("lrg",".jpg", new File(imgfolder));
			}catch(Exception e){
				return e.toString();
			}		
			//insert new image entry in database
			try{
				//byte[] bytes = file.getBytes();
				byte[] bytes = DatatypeConverter.parseBase64Binary(file);
				BufferedOutputStream oStream = new BufferedOutputStream(new FileOutputStream(imgpath));
				oStream.write(bytes);
				oStream.close();
				
				//update image database
				return FilenameUtils.getBaseName(imgpath.getName());
			}catch(Exception e){
				return null;
			}
		}else{
			return null;
		}
	}

	/*public Resource getThumbnail(String filename){

	}*/

	public byte[] getImage(String filename/*, HttpServletResponse response*/){
		//Path file = Paths.get(imgfolder+filename);
		byte[] image = null;
		logger.debug("in ConsumerService.getImage()");
		try{
			iStream = new FileInputStream(imgfolder + filename + ".jpg");
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