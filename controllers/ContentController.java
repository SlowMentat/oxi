package oxi.controllers;

import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.Iterator;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.persistence.*;
//import javax.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.*; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.*;

//import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.*;
import org.springframework.data.domain.*;

import org.springframework.http.MediaType;
/*import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.ResponseEntity<T>;*/
import org.springframework.http.*;
import org.springframework.http.converter.*;

import org.springframework.security.access.annotation.*;

//import java.io.BufferedOutputStream;
//import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
//import java.io.FileOutputStream;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;


import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import oxi.repositories.*;
import oxi.dao.*;
//import oxi.services.*;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.apache.commons.io.IOUtils;

import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.access.prepost.*;
import org.springframework.security.access.method.P;
//import org.springframework.util.Log4jConfigurer.*;

import org.springframework.data.rest.webmvc.*;

//@RestController
//@RequestMapping
@RepositoryRestController
public class ContentController{
	//Add Image
	//@Autowired
	//private JpaRepository<Content, Long> contentRep;
	//private UserRepository userRep;
	
	private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
	
	private static final Logger logger = LogManager.getLogger(ContentController.class);
	private static String imgfolder = "/usr/images/";
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private ServletContext context;
	private ProfileRepository profileRep;
	private OutfitRepository outfitRep;	
	private PictureRepository pictureRep;	
	private UserRepository userRep;	
	private ItemRepository itemRep;	
	private HttpSession httpsession;	
	public static EntityManager em;

	//@Autowired
	//private OutfitManagementService oms;
	//@Autowired
	//private PersistentEntityResourceAssembler entityAssembler;
	/*
	@Autowired
	//@Resource(name="userResource")
	private Resource<User> userResource;
	@Autowired
	//@Resource(name="userResourceAssembler")
	private ResourceAssembler<oxi.models.User, Resource<oxi.models.User>> resourceAssembler;
	*/
	private UserDAO userDao;
	

	@Autowired
    public void setServletContext(ServletContext context){
        this.context=context;
    }
	@Autowired
    public void setOutfitRepository(OutfitRepository outfitRep){
        this.outfitRep=outfitRep;
    }
    @Autowired
    public void setProfileRepository(ProfileRepository profileRep){
        this.profileRep=profileRep;
    }
    @Autowired
    public void setPictureRepository(PictureRepository pictureRep){
        this.pictureRep=pictureRep;
    }
    @Autowired
    public void setUserRepository(UserRepository userRep){
        this.userRep=userRep;
    }
    @Autowired
    public void setItemRepository(ItemRepository itemRep){
        this.itemRep=itemRep;
    }
    @Autowired
    public void setHttpSession(HttpSession httpsession){
        this.httpsession=httpsession;
    }
    @Autowired
    public void setEntityManager(EntityManager em){
        this.em=em;
    }


	private class OutfitDataWrapper{
		private Content content;
		private Picture picture;
		private Outfit outfit;
		private Item[] items;
		
		public Content getContent(){return this.content;}
		public Picture getPicture(){return this.picture;}
		public Outfit getOutfit(){return this.outfit;}
		public Item[] getItems(){return this.items;}
	}

	//@PathVariable("img_id") int id,	
	
	/*@PreAuthorize("#name == principal.username")
	public long getProfileId(@P("name") String name){
		logger.debug("||||||||||||||||||||invoking getProfileID() with parameter name:" + name + "||||||||||||||||||||");
		return profileRep.findByFirstname(name);
	}*/
	
	@Secured ({"ROLE_USER"})
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public @ResponseBody String uploadImage(MultipartHttpServletRequest requestData){
		logger.debug("Hitting uploadImage method!");
		logger.warn("Hitting uploadImage method!");
		Iterator<String> itr = requestData.getFileNames();
		MultipartFile file = requestData.getFile(itr.next());
		if(!file.isEmpty()){
			File imgpath = null;
			try{
				imgpath = File.createTempFile("lrg",".jpg", new File(imgfolder));
			}catch(Exception e){
				return e.toString();
			}
			//tring fullfilename = imgpath + filename;			
			//insert new image entry in database
			try{
				byte[] bytes = file.getBytes();
				BufferedOutputStream ostream = new BufferedOutputStream(new FileOutputStream(imgpath));
				ostream.write(bytes);
				ostream.close();
				
				//update image database
				return "file \"" + imgpath.toString() + "\" successfully uploaded";
			}catch(Exception e){
				return "Failed to upload file " + imgpath.toString();
			}
		}else{
			return "File is empty";
		}
	}
	
	@RequestMapping(value="/thumbnail", method=RequestMethod.GET/*, produces={MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE}*/)
	public ResponseEntity<byte[]> downloadImage(@RequestParam("filename") String filename) throws IOException{
		logger.warn("||||||||||||||||||||deven  :" + this.imgfolder+filename + "||||||||||||||||||||");
		//InputStream is = context.getResourceAsStream(this.imgfolder+filename);
		File file = new File(this.imgfolder+filename);
		InputStream is = new FileInputStream(file);
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		
		return new ResponseEntity<byte[]>(IOUtils.toByteArray(is), headers, HttpStatus.OK);
	
	
		/*Path filepath = null;
		try{
			filepath = Paths.get(imgfolder+filename);
			return Files.readAllBytes(filepath);
		}catch(Exception e){
			return new byte[] {(byte)0xEA, (byte)0xAE};
		}*/	
	}
	
	//@PreAuthorize("#name == principal.username")
	//@RequestMapping(value="outfits",method=RequestMethod.GET, produces="application/json")
	//public @ResponseBody List<Outfit> getUserOutfits(@RequestParam("u") String name/*, @RequestBody Outfit outfit*/){
	//	SecurityContext ctx=SecurityContextHolder.getContext();
	//	Authentication auth=ctx.getAuthentication();
	//	User user= (User)(auth.getPrincipal());
	//	String username = user.getUsername();
	//	logger.warn("||||||||||||||||||||The getPrincipal().toString() return:   " + username + "   ||||||||||||||||||||");
	//	long profileId = this.profileRep.findByFirstname(username);
	//	//logger.warn("||||||||||||||||||||profile_id:   " + outfit + "   ||||||||||||||||||||");
	//	//oms.addOutfit(outfit);
	//	List<Outfit> outfits = this.outfitRep.findByProfileId(profileId);
	//	return outfits;
	//}
	
	//@PreAuthorize("#name == principal.username")
	@RequestMapping(value="MyItems",method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<Item> getUserOutfits(/*@RequestParam("u") String name*/){
		SecurityContext ctx=SecurityContextHolder.getContext();
		Authentication auth=ctx.getAuthentication();
		User user= (User)(auth.getPrincipal());
		String username = user.getUsername();
		long profId = 3;//profileRep.findByFirstname(username);
		return itemRep.findByProfileId(profId/*, new PageRequest(0,1,Sort.Direction.DESC)*/);

		//return userRep.findAll();
		//return this.resourceAssembler.toResource(users.get(0));
		
		/*Pageable pageable = new PageRequest(1,5,new Sort("id"));
		Page<oxi.models.User> pageResults = userRep.findAll(pageable);
		return new PagedResources<oxi.models.User>(pageResults, 1, 5);*/
		
		
		/*logger.warn("||||||||||||||||||||The getPrincipal().toString() return:   " + username + "   ||||||||||||||||||||");
		return user;*/
	}
	
	//User upload new Photo data to new outfit
	//@Transactional
	@RequestMapping(value="content2", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public void addContent(@ModelAttribute Content newcontent) throws IOException{
		logger.warn("--STARTING TO SAVE CONTENT");
		String contentString = this.mapper.writeValueAsString(newcontent);
		logger.warn("Contents of content :)");
		logger.warn(contentString);
		//this.contentRep.saveAndFlush(newcontent);
		logger.warn("--FINISHED SAVING CONTENT");
		/*//begin transaction
		em.getTransaction().begin();
	
		//Add data to the picture entity
		this.pictureRep.save(data.getPicture());
		for each(Item item in data.getItems()){
			this.itemRep.save(item);
		}
		this.contentRep.save(data.getContent());
		this.outfitRep.save(
		data.getContent();
		
		//end transaction
		em.close();*/
	}
	
	@RequestMapping(value="test", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addContent(@RequestBody Profile profile) throws IOException{
		//String json_content = httpEntity.getBody();
		logger.warn("/test entry point");
		String profile_str = profile.toString();
		logger.warn("--Received Profile data:  "+profile_str);
		logger.warn("--DESERIALIZING JSON BODY");
		//Profile content = mapper.reader(Content.class).readValue(json_content);
		//String contentString = this.mapper.writeValueAsString(content);
		//logger.warn("Contents of content :)");
		//logger.warn(contentString);
		
		//logger.warn("--FINISHED SAVING CONTENT");
		/*//begin transaction
		em.getTransaction().begin();
	
		//Add data to the picture entity
		this.pictureRep.save(data.getPicture());
		for each(Item item in data.getItems()){
			this.itemRep.save(item);
		}
		this.contentRep.save(data.getContent());
		this.outfitRep.save(
		data.getContent();
		
		//end transaction
		em.close();*/
	}
	
	/*@Transactional
	@RequestMapping(value="alluser", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<org.springframework.security.core.userdetails.User> getUser(){
		return userRep.findAll();
	}*/
}