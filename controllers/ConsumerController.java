package oxi.controllers;

import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.*;
import javax.servlet.*;
import javax.persistence.*;
import java.security.Principal;
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

//mport org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
import com.fasterxml.jackson.databind.*; 

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;

import oxi.models.*;
import oxi.repositories.*;
import oxi.models.dto.*;
import oxi.models.projection.*;
import oxi.services.ConsumerService;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.apache.commons.io.IOUtils;

import org.springframework.stereotype.*;

import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.access.prepost.*;
import org.springframework.security.access.method.P;

import org.springframework.data.rest.webmvc.*;
import org.springframework.data.rest.webmvc.support.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.*;

import org.springframework.http.*;


//@RestController
//@Controller
@RequestMapping("/consumer")
@RepositoryRestController
public class ConsumerController{
	public static final String defaultExceptionResponse = "Our servers seem to have freyed a bit.\nPlease wait a moment and try your request agian.";

	//Services
	@Autowired
	private ConsumerService consumerService;
	private static final Logger logger = LogManager.getLogger(ConsumerController.class);

	//@Secured ({"ROLE_USER"})
	@RequestMapping(value="/uploadPhoto", method=RequestMethod.POST)
	public ResponseEntity<?> uploadImage(MultipartHttpServletRequest requestData){
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		//try{
			logger.debug("/uploadPhoto content length = " + requestData.getContentLength() + " bytes");
			PictureDto pictureDto = consumerService.saveImage(requestData);
			return new ResponseEntity<PictureDto>(pictureDto, HttpStatus.CREATED);
		/*}catch(Exception e){
			e.printStackTrace(printWriter);
			logger.debug(stringWriter.toString()); //stack trace as stirng		
		}finally{			
			return new ResponseEntity<String>(defaultExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);	
		}*/
	}

	@RequestMapping(value="/updatePhoto/{contentId}", method=RequestMethod.POST) //TODO:  Override multipart resolver to allow for put requests
	public ResponseEntity<?> updateImage(MultipartHttpServletRequest requestData, @PathVariable String contentId){	
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		try{
			logger.debug("/updatePhoto content length = " + requestData.getContentLength() + " bytes");
			List<PictureUpdateDto> pictureUpdateDto = consumerService.updateImage(requestData, contentId);
			return new ResponseEntity<List<PictureUpdateDto>>(pictureUpdateDto, HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace(printWriter);
			logger.debug(stringWriter.toString()); //stack trace as stirng	
		}finally{			
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/image/{filename}", method=RequestMethod.GET/*, produces = MediaType.IMAGE_JPEG_VALUE*/)
	public @ResponseBody byte[] getImage(@PathVariable String filename) throws IOException{
		return consumerService.getImage(filename);
	}

	/*@RequestMapping(value="/image2/{filename}", method=RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getImageProduces(@PathVariable String filename) throws IOException{
		return consumerService.getImage(filename);
	}*/

	/*@RequestMapping(value="/image2/{filename}", method=RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImageByte(@PathVariable("filename") String filename) throws IOException{
		return consumerService.getImage(filename);
	}*/


	@RequestMapping(value="/image3/{filename}", method=RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> getImageResponseEntity(@PathVariable("filename") String filename) throws IOException{
		final HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(consumerService.getImage(filename), headers, HttpStatus.CREATED);
	}	

	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for BRAND resource
	******************************************************************
	*/
	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/brands", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBrands(@PageableDefault Pageable pageable){
		try{
			return new ResponseEntity<PagedResources<?>>(consumerService.readBrands(pageable), HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(defaultExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for RETAILER resource
	******************************************************************
	*/
	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/retailers", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRetailers(@PageableDefault Pageable pageable){
		try{
			return new ResponseEntity<PagedResources<?>>(consumerService.readRetailers(pageable), HttpStatus.OK);
		}catch(Exception e){
			logger.debug(e.toString());
			return new ResponseEntity<String>(defaultExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for SIZE resource
	******************************************************************
	*/

	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for OUTFIT resource
	******************************************************************
	*/
	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/outfit", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadOutfit(final Principal principal, @RequestBody OutfitDto outfitDto){
		String username = principal.getName();
		logger.debug("Request Body Received: " + outfitDto);
		//try{	
			long startTime = System.currentTimeMillis();

			//ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.saveOutfit(outfit, username), HttpStatus.CREATED);
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.addOutfit(outfitDto, username), HttpStatus.CREATED);

			long endTime = System.currentTimeMillis();
			logger.debug("ConsumerService execution time for call to saveOutfit: " + (endTime - startTime) + "ms");
			return responseEntity;
		/*}catch(Exception e){
			logger.debug("Could not save new outfit data from /outfit controller\n" + e);
			throw new Exception("Could not save new outfit data from /outfit controller", e);
		}finally{			
			return new ResponseEntity<String>(defaultExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);	
		}*/
	}

	//@PreAuthorize("#name == principal.username")
	@Secured({"READ_PRIVILEGE", "WRITE_PRIVILEGE"})
	@RestResource(exported = true)
	@RequestMapping(value="/outfit/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> readOutfit(@PathVariable("id") String id){
		return new ResponseEntity<>(consumerService.readOutfit(id), HttpStatus.OK);
	}

	/*@RestResource(exported = true)
	@RequestMapping(value = "/outfits/{profileId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<?>> readOutfits(@PathVariable("profileId") Long profileId, @PageableDefault Pageable pageable){
		return new ResponseEntity<>(consumerService.readOutfits(profileId, pageable), HttpStatus.OK);
	}*/
	@RestResource(exported = true)
	@RequestMapping(value = "/outfits/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readOutfits(@PathVariable("username") String username, @PageableDefault Pageable pageable){
		logger.debug("username = <" + username + ">");
		return new ResponseEntity<PagedResources<?>>(consumerService.readOutfits(username, pageable), HttpStatus.OK);
	}

	@RestResource(exported = true)
	@RequestMapping(value = "/outfits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readOutfits(final Principal principal, @PageableDefault Pageable pageable, @RequestParam(value="filter", required=false) String filter){
		if(filter == null || filter.isEmpty()){
			String username = principal.getName();
			logger.debug("username = <" + username + ">");
			logger.debug("Principal Name: " + principal.getName());
			return new ResponseEntity<PagedResources<?>>(consumerService.readOutfits(username, pageable), HttpStatus.OK);
		}else{
			return new ResponseEntity<PagedResources<?>>(consumerService.readFilteredOutfits(filter, pageable), HttpStatus.OK);
		}
	}


	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for CONTENT resource
	******************************************************************
	*/

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/contents/{outfitId}", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createContents(final Principal principal, @PathVariable("outfitId") String outfitId, @RequestBody ArrayList<ContentDto> contentDtos){
		String username = principal.getName();
		//ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.addContents(contents, username, outfitId), HttpStatus.CREATED);
		//try{
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.createContents(contentDtos, outfitId), HttpStatus.CREATED);
			return responseEntity;
		//}catch(Exception e){
		//	return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		//}
	}

	//@Secured({"ROLE_USER"})
	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/content/{outfitId}", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createContent(final Principal principal, @PathVariable String outfitId, @RequestBody ContentDto contentDto){
		String username = principal.getName();
		logger.debug("Request Body Received: " + contentDto);
		try{	
			long startTime = System.currentTimeMillis();
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.createContent(contentDto, outfitId), HttpStatus.CREATED);
			long endTime = System.currentTimeMillis();
			logger.debug("ConsumerService execution time for call to saveContent: " + (endTime - startTime) + "ms");
			return responseEntity;
		}catch(Exception e){
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/content/{outfitId}", method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateContent(final Principal principal, @PathVariable String outfitId, @RequestBody ContentDto contentDto){
		String username = principal.getName();
		try{
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.updateContent(contentDto, outfitId), HttpStatus.OK);
			return responseEntity;
		}catch(Exception e){
			return new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/contents/{outfitId}", method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateContents(final Principal principal, @PathVariable String outfitId, @RequestBody ArrayList<ContentDto> contentDtos){
		String username = principal.getName();
		try{
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.updateContents(contentDtos, outfitId), HttpStatus.OK);
			return responseEntity;
		}catch(Exception e){
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RestResource(exported = true)
	@RequestMapping(value = "/contents/{outfitId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getContents(@PathVariable("outfitId") String outfitId/*, @PageableDefault Pageable pageable*/){
		return new ResponseEntity<List<?>>(consumerService.readContents(outfitId), HttpStatus.OK);
	}

	@RestResource(exported = true)
	@RequestMapping(value = "/contents/items/{itemId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getContents(final Principal principa, @PathVariable("itemId") String itemId, @PageableDefault Pageable pageable){
		return new ResponseEntity<PagedResources<?>>(consumerService.getContentsByItemId(itemId, pageable), HttpStatus.OK);
	}


	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for ITEM resource
	******************************************************************
	*/

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/items", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getFilteredItems(final Principal principal, @PageableDefault Pageable pageable, @RequestParam(value="filter", required=false) String filter){
		//try{
			filter = filter == null ? "" : filter;
			String username = principal.getName();		
			return new ResponseEntity<PagedResources<?>>(consumerService.getFilteredItems(username, filter, pageable), HttpStatus.OK);
		/*}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/items/{outfitId}", method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadExistingItems(final Principal principal, @PathVariable String outfitId, @RequestBody HashMap<String, ArrayList<ItemDto>> payload){
		String username = principal.getName();
		//try{	
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.updateItems(payload, username, outfitId), HttpStatus.OK);
			return responseEntity;
		/*}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/removeItems/{outfitId}", method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> removeItemsFromContent(final Principal principal, @PathVariable String outfitId, @RequestBody HashMap<String, ArrayList<String>> payload){
		String username = principal.getName();
		//try{	
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.removeItemsFromContent(payload, username, outfitId), HttpStatus.OK);
			return responseEntity;
		/*}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/items/{outfitId}", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadNewItems(final Principal principal, @PathVariable String outfitId, @RequestBody HashMap<String, ArrayList<ItemDto>> payload){
		String username = principal.getName();
		//try{	
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.addItems(payload, username, outfitId), HttpStatus.CREATED);
			return responseEntity;
		/*}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/items/{outfitId}", method=RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteItems(final Principal principal, @PathVariable String outfitId, @RequestBody HashMap<String, ArrayList<ItemDto>> payload){
		String username = principal.getName();
		//try{	
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.addItems(payload, username, outfitId), HttpStatus.CREATED);
			return responseEntity;
		/*}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}


	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for LIKE and BOOKMARk resource
	******************************************************************
	*/

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/like/{recipientUsername}", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> like(final Principal principal, @PathVariable String recipientUsername){
		String username = principal.getName();
		//try{	
			//ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.likeOutfit(username, recipientUsername), HttpStatus.CREATED);
			consumerService.likeOutfit(username, recipientUsername);
			return new ResponseEntity<>(HttpStatus.CREATED);
		/*}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/bookmark/{itemId}", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> bookmark(final Principal principal, @PathVariable String itemId){
		String username = principal.getName();
		//try{	
			//ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.bookmarkItem(username, itemId), HttpStatus.CREATED);
			consumerService.bookmarkItem(username, itemId);
			return new ResponseEntity<>(HttpStatus.CREATED);
		/*}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/bookmarks", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> bookmark(final Principal principal, @RequestBody ArrayList<String> itemIds){
		String username = principal.getName();
		//try{	
			//ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.bookmarkItem(username, itemId), HttpStatus.CREATED);
			consumerService.bookmarkItems(username, itemIds);
			return new ResponseEntity<>(HttpStatus.CREATED);
		/*}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/follow/{recipientUsername}", method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> follow(final Principal principal, @PathVariable String recipientUsername){
		String username = principal.getName();
		//try{	
			//ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.followUser(username, recipientUsername), HttpStatus.CREATED);
			consumerService.followUser(username, recipientUsername);
			return new ResponseEntity<>(HttpStatus.CREATED);
		/*}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}


	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for PROFILE resource
	******************************************************************
	*/
	//@PreAuthorize("#name == principal.username")
	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/profile", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postProfile(final Principal principal, @RequestBody ProfileDto profile){
		try{
			logger.debug("Request Body Received: " + profile);
			return new ResponseEntity(consumerService.createProfile(profile, principal.getName()), HttpStatus.CREATED);
		}catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	//@PreAuthorize("#name == principal.username")
	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/profile", method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> putProfile(final Principal principal, @RequestBody ProfileDto profile){
		try{
			logger.debug("Request Body Received: " + profile);
			return new ResponseEntity(consumerService.updateProfile(profile, principal.getName()), HttpStatus.CREATED);
		}catch(Exception e){	
			logger.error(e);		
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/profile/{username}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProfile(final Principal principal, @PathVariable("username") String username){
		try{
			return new ResponseEntity(consumerService.readProfile(username), HttpStatus.OK);			
		}catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/profile", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMetric(final Principal principal, @RequestParam(value="filter", required=false) String outfitId){
		try{
			logger.debug("Principal Name:  " + principal.getName());
			if(outfitId == null || outfitId.isEmpty()){
				return new ResponseEntity(consumerService.readProfile(principal.getName()), HttpStatus.OK);
			}else{//TODO:  Check if parameter is a valid UUID string
				return new ResponseEntity(consumerService.readMetric(outfitId), HttpStatus.OK);
			}
		}catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}


	/*@RequestMapping(value="/createUser", method=RequestMethod.POST)
	public void createUser(@RequestParam String email, @RequestParam String password, @RequestParam(required = false) String username){
		consumerService.provisionUser(new UserDto(email, password, username));
	}*/
	@Secured({"ROLE_ANONYMOUS"})
	@RequestMapping(value="/createUser", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@RequestBody UserDto userDto){
		return consumerService.provisionUser(userDto);
	}


}