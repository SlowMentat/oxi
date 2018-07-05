package oxi.services;

import java.lang.*;
import java.util.Iterator;
import java.io.*;
import java.util.ArrayList;
import java.io.Serializable;
import org.apache.commons.io.IOUtils;

import oxi.models.*;
import oxi.repositories.*;
import oxi.util.assemblers.*;
import oxi.models.dto.*;

import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.data.domain.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@Service
public class ConsumerService implements ClientService{
	//Repositories
	@Autowired
	private OutfitRepository outfitRep;
	@Autowired
	private ContentRepository contentRep;
	@Autowired
	private ItemRepository itemRep;
	@Autowired
	private PictureRepository pictureRep;
	@Autowired
	private ProfileRepository profileRep;
	
	//Resource Assemblers
	@Autowired 
	private OutfitResourceAssembler outfitRA;
	@Autowired 
	private ContentResourceAssembler contentRA;
	@Autowired 
	private ItemResourceAssembler itemRA;
	@Autowired 
	private PictureResourceAssembler pictureRA;
	@Autowired 
	private ProfileResourceAssembler profileRA;
	@Autowired 
	private UserResourceAssembler userRA;

	//Paged Resource Assemblers 
	@Autowired
	private PagedResourcesAssembler<Outfit> outfitPRA;
	@Autowired 
	private PagedResourcesAssembler<Item> itemPRA;

	private static final Logger logger = LogManager.getLogger(ConsumerService.class);
	private static String imgfolder = "/usr/images/";

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
		outfitRep.saveAndFlush(outfit);
	}

	/*
	Makes call to DAL retreiving outfit resource
	@param Long specifying id of outfit to retreive
	@return OutfitDto which extends ResourceSupport
	*/
	public OutfitDto readOutfit(Long id){
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
	}
	//=====================================================================================

	/*
	Makes call to DAL retreiving content resource
	@param Long specifying id of content to retreive
	@return ContentDto which extends ResourceSupport
	*/
	public ContentDto readContent(Long id){
		//TODO:  ???need a customized json serialization to replace HAL links with raw data.
		logger.debug("Reading Content (id=" + id + ")");
		ContentDto contentResource = contentRA.toResource(contentRep.getOne(id));
		logger.debug("OutfitResource:  " + contentResource);
		return contentResource;
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
	public ItemDto readItem(Long id){
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
	}
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
	public ProfileDto readProfile(Long id){
		//TODO:  ???need a customized json serialization to replace HAL links with raw data.
		logger.debug("Reading Profile (id=" + id + ")");
		ProfileDto profileResource = profileRA.toResource(profileRep.getOne(id));
		logger.debug("profileResource:  " + profileResource);
		return profileResource;
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
	/*
	saves jpeg data to filesystem
	@param MulitpartHttpServletRequest data 
	@return String indicating generated filename.
	*/
	public String savePhoto(MultipartHttpServletRequest data){
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
}