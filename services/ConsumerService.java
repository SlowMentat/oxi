package oxi.services;

import java.lang.*;
import java.util.Iterator;
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

import oxi.models.*;
import oxi.repositories.*;
import oxi.util.assemblers.*;
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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

	//Resource Assemblers
	//@Autowired 
	//private OutfitResourceAssembler outfitRA;
	//@Autowired private ContentResourceAssembler contentRA;
	//@Autowired private ItemResourceAssembler itemRA;
	@Autowired private PictureResourceAssembler pictureRA;
	@Autowired private ProfileResourceAssembler profileRA;
	//@Autowired private UserResourceAssembler userRA;

	//Paged Resource Assemblers 
	@Autowired private PagedResourcesAssembler<Outfit> outfitPRA;
	@Autowired private PagedResourcesAssembler<OutfitDto> outfitPRAP;
	@Autowired private PagedResourcesAssembler<Item> itemPRA;

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
	public void saveOutfit(Outfit outfit){
		logger.debug("saving Outfit");
		//Session session = sessionFactory.getCurrentSession();
		int contentsCount = outfit.getContents().size();
		if(contentsCount > 0){
			logger.debug("outfit = ");
			logger.debug(outfit);
			int n=0;
			//List<UUID> contentIds = new ArrayList<UUID>(contentsCount);
			List<Content> contents = new ArrayList<Content>(contentsCount);
			for(Content c : outfit.getContents()){
				entityManager.persist(c);
				contents.add(c);
			}
			for(Content c : contents){
				outfit.addContent(c);
			}
			entityManager.persist(outfit);
			logger.debug("outfit after persist = ");
			logger.debug(outfit);
		}else{
			logger.warn("Rejecting Outfit entity. Outfit entity contains no Content childeren. Outfit entity must have at least 1 Content child entity");
		}
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

	public PagedResources<?> readOutfits(String profileId, Pageable pageable){

		Page<OutfitDto> outfits = outfitRep.findByProfileId(UUID.fromString(profileId), pageable);
		logger.debug("outfits return from repository: \n" + outfits);
		// Tell PAR to use the user assembler for individual items.
		PagedResources<?> pagedOutfitResource = outfitPRAP.toResource(outfits, this::toResource);
		return pagedOutfitResource;
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
	public ProfileDto readProfile(String id){
		//TODO:  ???need a customized json serialization to replace HAL links with raw data.
		logger.debug("Reading Profile (id=" + id + ")");
		ProfileDto profileResource = profileRA.toResource(profileRep.getOne(UUID.fromString(id)));
		logger.debug("profileResource:  " + profileResource);
		return profileResource;
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
			userRep.saveAndFlush(user);	

			//Set User roles
						
			return new ResponseEntity(HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity("Our servers seem to have freyed a bit.\nPlease wait a moment and try your request agian.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
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
		Iterator<String> itr = data.getFileNames();
		MultipartFile file = data.getFile(itr.next());
		if(!file.isEmpty()){
			File imgpath = null;
			try{
				imgpath = File.createTempFile("lrg",".jpg", new File(imgfolder));
			}catch(Exception e){
				return e.toString();
			}		
			//insert new image entry in database
			try{
				byte[] bytes = file.getBytes();
				BufferedOutputStream oStream = new BufferedOutputStream(new FileOutputStream(imgpath));
				oStream.write(bytes);
				oStream.close();
				
				//update image database
				return imgpath.toString();
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