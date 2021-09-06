package oxi.controllers;

import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Enumeration;

import java.lang.Exception;
import java.lang.IllegalStateException;

import javax.servlet.http.*;
import javax.servlet.*;
import javax.persistence.*;
import java.security.Principal;
//import javax.annotation.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.*; 

import org.springframework.context.ApplicationEventPublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.*;
import org.springframework.web.bind.annotation.*;

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
import oxi.services.SearchService;
import oxi.services.ValueService;
import oxi.services.UserAccountService;
import oxi.events.OnRegistrationCompleteEvent;
import oxi.errors.UserAlreadyExistException;

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

	//private MAX_PAGE_SIZE = 50;

	//Services
	@Autowired
	private ConsumerService consumerService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private ValueService valueService;
	@Autowired
	private SearchService searchService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	private static final Logger logger = LogManager.getLogger(ConsumerController.class);
	private static StringWriter stringWriter = new StringWriter();
	private static PrintWriter printWriter = new PrintWriter(stringWriter);


	/*@RequestMapping(value="*", method=RequestMethod.OPTIONS)
	public ResponseEntity<?> preflight(){
		logger.debug("yo!");
		return new ResponseEntity<>(null, HttpStatus.OK);
	}*/

	//@Secured ({"ROLE_USER"})

	//@Secured({"ROLE_USER"})
	//@RequestMapping(value="/uploadPhoto", method=RequestMethod.POST)
	//public ResponseEntity<?> uploadImage(MultipartHttpServletRequest requestData){
	//	StringWriter stringWriter = new StringWriter();
	//	PrintWriter printWriter = new PrintWriter(stringWriter);
//
	//	//try{
	//		logger.debug("/uploadPhoto content length = " + requestData.getContentLength() + " bytes");
	//		PictureDto pictureDto = consumerService.saveImage(requestData);
	//		return new ResponseEntity<PictureDto>(pictureDto, HttpStatus.CREATED);
	//	/*}catch(Exception e){
	//		e.printStackTrace(printWriter);
	//		logger.debug(stringWriter.toString()); //stack trace as stirng		
	//	}finally{			
	//		return new ResponseEntity<String>(defaultExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);	
	//	}*/
	//}


	@Secured({"ROLE_USER"})
	@RequestMapping(value="/uploadPhoto", method=RequestMethod.POST) //TODO:  Override multipart resolver to allow for put requests
	public ResponseEntity<?> uploadImage(final Principal principal, MultipartHttpServletRequest formData){	

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		//try{
			//MultipartHttpServletRequest imageBody = (MultipartHttpServletRequest)formData.get("imageFile");
			//MultipartHttpServletRequest cropBody = (MultipartHttpServletRequest)formData.get("crop");
			//Enumeration<String> cropBodyParameters = cropBody.getParameterNames();
			//String[] cropData = cropBody.getParameterValues(cropBodyParameters.nextElement());

			String[] cropData = formData.getParameterValues("crop");

			logger.debug("/updatePhoto data length = " + formData.getContentLength() + " bytes");
			logger.debug("/crop = " + cropData[0]);

			PictureDto pictureDto = consumerService.saveImage(principal.getName(), formData);
			return new ResponseEntity<PictureDto>(pictureDto, HttpStatus.CREATED);
		//}
		//catch(Exception e){
		//	//e.printStackTrace(printWriter);
		//	logger.debug("Exception thrown in uploadImage");
		//	logger.error("", e);
		//	//logger.debug(stringWriter.toString()); //stack trace as stirng	
		//}
		//finally{			
		//	return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		//}
	}


	/**
	*deletes one or more images identified by the user
	*<p>
	*\/deletePhoto
	*</p>
	*@param principle 
	*@param contentIds ArrayList of content ids as Strings
	*@return ResponseEntity<?> with Status 400 if there exists a contentId that does not belong to the user, otherwise returns status 200
	*/
	@Secured({"ROLE_USER"})
	@RequestMapping(value="/images", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteImages(final Principal principal, @RequestBody ArrayList<String> contentIds){
		consumerService.deleteImages(principal.getName(), contentIds);
		return new ResponseEntity<String>(HttpStatus.OK);
	}


	/*@Secured({"ROLE_USER"})
	@RequestMapping(value="/updatePhoto/{contentId}", method=RequestMethod.POST) //TODO:  Override multipart resolver to allow for put requests
	public ResponseEntity<?> updateImage(MultipartHttpServletRequest requestData, @PathVariable String contentId){	
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		try{
			logger.debug("/updatePhoto content length = " + requestData.getContentLength() + " bytes");
			List<PictureUpdateDto> pictureUpdateDto = consumerService.updateImage(requestData, contentId);
			return new ResponseEntity<List<PictureUpdateDto>>(pictureUpdateDto, HttpStatus.OK);
		}
		catch(Exception e){
			e.printStackTrace(printWriter);
			logger.debug(stringWriter.toString()); //stack trace as stirng	
		}
		finally{			
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	@Secured({"ROLE_USER"})
	@RequestMapping(value="/updatePhoto/{contentId}", method=RequestMethod.POST) //TODO:  Override multipart resolver to allow for put requests
	public ResponseEntity<?> updateImage(@RequestBody MultiValueMap<String, Object> formData, @PathVariable String contentId){	

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		try{
			MultipartHttpServletRequest imageBody = (MultipartHttpServletRequest)formData.get("imageFile");
			MultipartHttpServletRequest cropBody = (MultipartHttpServletRequest)formData.get("crop");

			Enumeration<String> cropBodyParameters = cropBody.getParameterNames();
			String[] cropData = cropBody.getParameterValues(cropBodyParameters.nextElement());

			logger.debug("/updatePhoto data length = " + imageBody.getContentLength() + " bytes");
			logger.debug("/crop = " + cropData[0]);

			List<PictureUpdateDto> pictureUpdateDto = consumerService.updateImage(imageBody, contentId);
			return new ResponseEntity<List<PictureUpdateDto>>(pictureUpdateDto, HttpStatus.OK);
		}
		catch(Exception e){
			e.printStackTrace(printWriter);
			logger.debug(stringWriter.toString()); //stack trace as stirng	
		}
		finally{			
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	*Controller for creating a new profile picture or updating and existing one.
	*<p>
	*\/updateProfilePhoto
	*</p>
	*@param principle
	*@param formData MultiValueMap of String typed keys and Object typed values.
	*@return ResponseEntity<?> with Status 400 if there exists a contentId that does not belong to the user, otherwise returns status 200
	*/
	@Secured({"ROLE_USER"})
	@RequestMapping(value="/updateProfilePhoto", method=RequestMethod.POST) //TODO:  Override multipart resolver to allow for put requests
	public ResponseEntity<?> updateProfileImage(final Principal principal, MultipartHttpServletRequest formData /*MultipartHttpServletRequest requestData*/){	
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		try{
			String[] cropData = formData.getParameterValues("crop");
			logger.debug("/updatePhoto data length = " + formData.getContentLength() + " bytes");
			logger.debug("/crop = " + cropData[0]);

			PictureDto pictureDto = consumerService.updateProfileImage(formData, principal.getName());
			logger.debug("pictureDto = " + pictureDto);
			return new ResponseEntity<PictureDto>(pictureDto, HttpStatus.CREATED);
		}
		catch(Exception e){
			e.printStackTrace(printWriter);
			logger.debug(stringWriter.toString());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/crop", method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCrop(final Principal principal, @RequestBody PictureUpdateDto pictureUpdateDto, @RequestParam(value="type", required=false) String type) throws Exception, IllegalStateException{
		//StringWriter stringWriter = new StringWriter();
		//PrintWriter printWriter = new PrintWriter(stringWriter);

		//try{
			logger.debug("Request Body Received: " + pictureUpdateDto);
			return new ResponseEntity(consumerService.updateCrop(pictureUpdateDto, principal.getName(), type), HttpStatus.OK);
		//}
		//catch(Exception e){	
		//	e.printStackTrace(printWriter);
		//	logger.error(stringWriter.toString());		
		//	return new ResponseEntity(HttpStatus.BAD_REQUEST);
		//}
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
			return new ResponseEntity<PagedModel<?>>(consumerService.readBrands(pageable), HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(defaultExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	******************************************************************
	HTTP Request handling methodss for APPARELTYPE resource
	******************************************************************
	*/
	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/apparelTypes", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getApparelTypes(@PageableDefault Pageable pageable){
		try{
			return new ResponseEntity<PagedModel<?>>(consumerService.getApparelTypes(pageable), HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(defaultExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for RETAILER resource
	******************************************************************
	*/
	//@Secured({"ROLE_USER"})
	//@RestResource(exported = true)
	//@RequestMapping(value="/retailers", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	//public ResponseEntity<?> getRetailers(@PageableDefault Pageable pageable){
	//	try{
	//		return new ResponseEntity<PagedModel<?>>(consumerService.readRetailers(pageable), HttpStatus.OK);
	//	}catch(Exception e){
	//		logger.debug(e.toString());
	//		return new ResponseEntity<String>(defaultExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	//	}
	//}

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
			//long startTime = System.currentTimeMillis();

			//ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.saveOutfit(outfit, username), HttpStatus.CREATED);
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.addOutfit(outfitDto, username), HttpStatus.CREATED);

			//long endTime = System.currentTimeMillis();
			//logger.debug("ConsumerService execution time for call to saveOutfit: " + (endTime - startTime) + "ms");
			return responseEntity;
		/*}catch(Exception e){
			logger.debug("Could not save new outfit data from /outfit controller\n" + e);
			throw new Exception("Could not save new outfit data from /outfit controller", e);
		}finally{			
			return new ResponseEntity<String>(defaultExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);	
		}*/
	}

	@Secured({"ROLE_USER"})
	@RequestMapping(value="/outfits", method=RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteOutfits(final Principal principal, @RequestBody HashMap<String, List<String>> reqBodyMap){
		String username = principal.getName();
		
		if(reqBodyMap.get("outfitIds") == null){
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}

		consumerService.deleteOutfits(username, reqBodyMap.get("outfitIds"));
		return new ResponseEntity<>("", HttpStatus.OK);
	}

	//@PreAuthorize("#name == principal.username")

	@Secured({"READ_PRIVILEGE", "WRITE_PRIVILEGE"}) 
	@RestResource(exported = true)
	//@CrossOrigin(origins = "https://fitscene.app")
	@RequestMapping(value="/outfit/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> getSingleOutfit(@PathVariable("id") String id){
		return new ResponseEntity<>(consumerService.readOutfit(id), HttpStatus.OK); 
	}

	//@PreAuthorize("#name == principal.username")
	//@Secured({"READ_PRIVILEGE"})
	//@RestResource(exported = true)
	////@CrossOrigin(origins = "https://fitscene.app")
	//@RequestMapping(value="/outfits", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	//public HttpEntity<?> readOutfits(@RequestBody List<String> outfitIds){
	//	return consumerService.readOutfitsByIds(outfitIds);
	//}

	@RestResource(exported = true)
	//@CrossOrigin(origins = "https://fitscene.app")
	@RequestMapping(value = "/outfits/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getOutfitsByUsername(final Principal principal, @PathVariable("username") String username, CursorDto cursor/*@PageableDefault Pageable pageable*/) throws Exception{
		logger.debug("username = <" + username + ">");
		//return new ResponseEntity<PagedModel<?>>(consumerService.readOutfits(username, pageable), HttpStatus.OK);
		return new ResponseEntity<EntityModel<CursorDto>>(consumerService.readOutfitsByUsername(username, principal.getName(), cursor, ""), HttpStatus.OK);
	}

	@RestResource(exported = true)
	//@CrossOrigin(origins = "https://fitscene.app")
	@RequestMapping(value = "/outfits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<?> getFilteredOutfits(
		final Principal principal, 
		@PageableDefault Pageable pageable, 
		CursorDto cursor,
		@RequestParam(value="filter", required=false) String filter,
		@RequestBody(required=false) List<String> outfitIds
	) throws Exception{

		ResponseEntity<?> response;

		//if(filter == null || filter.isEmpty()){
			String username = principal.getName();
		//	logger.debug("username = <" + username + ">");
		//	logger.debug("Principal Name: " + principal.getName());
		//	response = new ResponseEntity<PagedModel<?>>(consumerService.readOutfits(username, pageable), HttpStatus.OK);
		//else{
		final String All_FILTER_PARAM = "all";
		final String IDS_FILTER_PARAM = "ids";
		final String EMPTY_FILTER_PARAM = "";

		switch (filter){
			case "all":
				response = new ResponseEntity<EntityModel<CursorDto>>(consumerService.readPagedOutfits(filter, principal.getName(), cursor, All_FILTER_PARAM), HttpStatus.OK);
				break;

			case "ids":
				response = consumerService.readOutfitsByIds(outfitIds);
				break;

			//Default behavior is retreive paged outfits
			default:
				//response = new ResponseEntity<PagedModel<?>>(consumerService.readOutfits(username, pageable), HttpStatus.OK);
				response = new ResponseEntity<EntityModel<CursorDto>>(consumerService.readOutfitsByUsername(username, principal.getName(), cursor, EMPTY_FILTER_PARAM), HttpStatus.OK);
				break;
		}
		//}
		
		return response;
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
		//try{
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.updateContent(contentDto, outfitId), HttpStatus.OK);
			return responseEntity;
		//}catch(Exception e){
		//	return new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		//}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/contents/{outfitId}", method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateContents(final Principal principal, @PathVariable String outfitId, @RequestBody ArrayList<ContentDto> contentDtos) throws Exception{
		String username = principal.getName();
		//try{
			ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.updateContents(contentDtos, outfitId), HttpStatus.OK);
			return responseEntity;
		//}catch(Exception e){
		//	return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		//}
	}

	@RestResource(exported = true)
	@RequestMapping(value = "/contents/{outfitId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getContents(@PathVariable("outfitId") String outfitId/*, @PageableDefault Pageable pageable*/){
		return new ResponseEntity<List<?>>(consumerService.readContents(outfitId), HttpStatus.OK);
	}

	//@RestResource(exported = true)
	//@RequestMapping(value = "/contents/items/{itemId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	//public ResponseEntity<?> getContents(final Principal principal, @PathVariable("itemId") String itemId, @PageableDefault Pageable pageable){
	//	return new ResponseEntity<PagedModel<?>>(consumerService.getContentsByItemId(itemId, pageable), HttpStatus.OK);
	//}
	@RestResource(exported = true)
	@RequestMapping(value = "/contents/items/{itemId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getContents(final Principal principal, @PathVariable("itemId") String itemId, CursorDto cursor) throws Exception{
		return new ResponseEntity<EntityModel<CursorDto>>(consumerService.getContentsByItemId(itemId, cursor), HttpStatus.OK);
	}

	@RestResource(exported = true)
	@RequestMapping(value = "/outfit/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<?> updateOutfitCoverpic(final Principal principal, @RequestBody OutfitCoverpicDto outfitCoverpicDto, @PathVariable("id") String id) throws Exception, IllegalStateException{
		String username = principal.getName();
		//try{
			consumerService.updateOutfitCoverpic(id, username, outfitCoverpicDto);
			return new ResponseEntity<String>("", HttpStatus.OK);
		//}catch(Exception e){
		//	return new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		//}
	}

	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for ITEM resource
	******************************************************************
	*/

	//@Secured({"ROLE_USER"})
	//@RestResource(exported = true)
	//@RequestMapping(value="/items", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	//public ResponseEntity<?> getFilteredItems(final Principal principal, @PageableDefault Pageable pageable, @RequestParam(value="filter", required=false) String filter){
	//	//try{
	//		filter = filter == null ? "" : filter;
	//		String username = principal.getName();	

	//		logger.debug(
	//			"pageable size = {}\npageable number = {}\npageable offset = {}", 
	//			pageable.getPageSize(), 
	//			pageable.getPageNumber(), 
	//			pageable.getOffset() 
	//		);

	//		return new ResponseEntity<PagedModel<?>>(consumerService.getFilteredItems(username, filter, pageable), HttpStatus.OK);
	//	/*}catch(Exception e){
	//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	//	}*/
	//}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/items", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getFilteredItems(final Principal principal, /*@ModelAttribute("cursor") */CursorDto cursor, @RequestParam(value="filter", required=false) String filter){
		//try{
			filter = filter == null ? "" : filter;
			String username = principal.getName();	

			logger.debug(
				"cursor firstResult = {}\ncursor maxResults = {}\ncursor date = {}", 
				cursor.getFirstResult(), 
				cursor.getMaxResults(), 
				cursor.getDate() 
			);

			//PagedModel<?> result = consumerService.getFilteredItems(username, filter, cursor);
			EntityModel<CursorDto> result = consumerService.getFilteredItems(username, filter, cursor);
			// Publish event to inject cursor data in response headers
			// TODO:  This should be injected into the respoonse body
			//eventPublisher.publishEvent(new CursorResultsRetrievedEvent<ItemDto>(ItemDto.class, uriBuilder, result, cursor));

			//return new ResponseEntity<PagedModel<?>>(result, HttpStatus.OK);
			return new ResponseEntity<EntityModel<CursorDto>>(result, HttpStatus.OK);
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
	@RequestMapping(value="/like/{outfitId}", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> like(final Principal principal, @PathVariable String outfitId){

		String username = principal.getName();
		ResponseEntity<?> response = null;
		LikeCountDto result = valueService.like(outfitId, username);

		if(result == null){
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}else{
			response = new ResponseEntity<>(result, HttpStatus.CREATED);
		}

		return response;
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/unlike/{outfitId}", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> unlike(final Principal principal, @PathVariable String outfitId){

		String username = principal.getName();
		ResponseEntity<?> response = null;
		LikeCountDto result = valueService.unlike(outfitId, username);

		if(result == null){
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}else{
			response = new ResponseEntity<>(result, HttpStatus.CREATED);
		}
		
		return response;
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/bookmark/{itemId}", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> bookmark(final Principal principal, @PathVariable String itemId){
		String username = principal.getName();
		//try{	
			//ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.bookmarkItem(username, itemId), HttpStatus.CREATED);
			Date createdOn = consumerService.bookmarkItem(username, itemId);

			if(createdOn != null){
				return new ResponseEntity<>(createdOn, HttpStatus.CREATED);
			}else{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		/*}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/bookmarks", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> bookmarks(final Principal principal, @RequestBody ArrayList<String> itemIds){

		String username = principal.getName();

		try{	
			//ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.bookmarkItem(username, itemId), HttpStatus.CREATED);
			consumerService.bookmarkItems(username, itemIds);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/bookmarks", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getBookmarks(final Principal principal){

		String username = principal.getName();

		try{	
			//ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.bookmarkItem(username, itemId), HttpStatus.CREATED);
			HashMap<String, Date> bookmarks = consumerService.getBookmarkedItemsByUsername(username);
			return new ResponseEntity<>(bookmarks, HttpStatus.OK);
		}catch(Exception e){
			logger.error("",e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/bookmark/{itemId}", method=RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteBookmark(final Principal principal, @PathVariable String itemId){

		String username = principal.getName();

		try{
			consumerService.removeBookmarkItem(username, itemId);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception e){
			logger.error("",e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured({"ROLE_USER"})
	//@RestResource(exported = true)
	//@RequestMapping(value="/follow/{recipientUsername}", method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	//public ResponseEntity<?> follow(final Principal principal, @PathVariable String recipientUsername){
	//	String username = principal.getName();
	//	//try{	
	//		//ResponseEntity<?> responseEntity = new ResponseEntity<>(consumerService.followUser(username, recipientUsername), HttpStatus.CREATED);
	//		consumerService.followUser(username, recipientUsername);
	//		return new ResponseEntity<>(HttpStatus.CREATED);
	//	/*}catch(Exception e){
	//		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	//	}*/
	//}

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
		//StringWriter stringWriter = new StringWriter();
		//PrintWriter printWriter = new PrintWriter(stringWriter);

		try{
			logger.debug("Request Body Received: " + profile);
			return new ResponseEntity(consumerService.updateProfile(profile, principal.getName()), HttpStatus.CREATED);
		}
		catch(Exception e){	
			e.printStackTrace(printWriter);
			logger.error(stringWriter.toString());		
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/profile/{username}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProfile(final Principal principal, @PathVariable("username") String username){
		try{
			return new ResponseEntity(consumerService.readProfile(username, principal.getName()), HttpStatus.OK);			
		}
		catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/profile", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getMetric(final Principal principal, @RequestParam(value="filter", required=false) String outfitId){
		try{
			if(outfitId == null || outfitId.isEmpty()){ 
				return new ResponseEntity(consumerService.readProfile(principal.getName(), principal.getName()), HttpStatus.OK);
			}
			else{//TODO:  Check if parameter is a valid UUID string
				return new ResponseEntity(consumerService.readMetric(outfitId), HttpStatus.OK);
			}
		}
		catch(Exception e){
			logger.error("",e);
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/follow", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> follow(final Principal principal, @RequestParam(value="username", required=true) String followee){
		try{
			FollowingDto followingDto = new FollowingDto(principal.getName(), followee);
			valueService.follow(followingDto);
			return new ResponseEntity(HttpStatus.OK);
		}
		catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/unfollow", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> unfollow(final Principal principal, @RequestParam(value="username", required=true) String followee){
		try{
			FollowingDto followingDto = new FollowingDto(principal.getName(), followee);
			valueService.unfollow(followingDto);
			return new ResponseEntity(HttpStatus.OK);
		}
		catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/following", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getFollowing(final Principal principal, @RequestParam(value="username", required=true) String followee){
		try{
			FollowingDto followingDto = new FollowingDto(principal.getName(), followee);
			valueService.unfollow(followingDto);
			return new ResponseEntity(HttpStatus.OK);
		}
		catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/sizeChart", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSizeChartByItemId(final Principal principal, @RequestParam(value="itemId", required=true) String itemId){
		try{
			return new ResponseEntity(consumerService.getSizeChartByItemId(itemId), HttpStatus.OK);
		}
		catch(Exception e){
			logger.error("", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	/*
	******************************************************************
	* HTTP Controller for consumer search
	******************************************************************
	*/

	@Secured({"ROLE_USER"})
	@RequestMapping(value="/searchUdr", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchUserDefinedRetailers(@RequestParam(value="retailer", required=true) String retailer){
		try{			
			return new ResponseEntity(searchService.suggestUserDefinedRetailerNames(retailer), HttpStatus.OK);
		}
		catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RequestMapping(value="/searchUds", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchUserDefinedSizes(@RequestParam(value="size", required=true) String size){
		try{			
			return new ResponseEntity(searchService.suggestUserDefinedSizeLabels(size), HttpStatus.OK);
		}
		catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RequestMapping(value="/allApparelTypes", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchApparelTypes(final Principal principal){
		try{			
			return new ResponseEntity(consumerService.getAllApparelTypes(), HttpStatus.OK);
		}
		catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RequestMapping(value="/searchCustomItems", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchUserDefinedItem(
		final Principal principal, 
		@RequestParam(value="term", required=true) String term, 
		@RequestParam(value="retailer", required=true) String retailer,
		@RequestParam(value="apparelTypeId", required=true) String apparelTypeId,
		@RequestParam(value="sizeLabel", required=true) String sizeLabel
	){
		try{			
			return new ResponseEntity(searchService.suggestUserDefinedItems(term, retailer, apparelTypeId, sizeLabel), HttpStatus.OK);
		}
		catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RequestMapping(value="/sizeLabels", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSizeLabels(final Principal principal, @PageableDefault Pageable pageable, @RequestParam(value="name", required=true) String name){
		try{			
			return new ResponseEntity(searchService.getSizeLabels(name, pageable), HttpStatus.OK);
		}
		catch(Exception e){
			logger.error(e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RequestMapping(value="/searchItems", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchItems(final Principal principal, @RequestParam(value="term", required=true) String term, @RequestParam(value="retailer", required=true) String retailer){
		try{
			return new ResponseEntity(searchService.suggestItems(term, retailer), HttpStatus.OK);
		}
		catch(Exception e){
			logger.error("Exception occured when invoking suggestItems: ", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_USER"})
	@RequestMapping(value="/searchRetailerNames", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchRetailerNames(final Principal principal, @RequestParam(value="term", required=true) String term){
		try{
			return new ResponseEntity(searchService.suggestRetialerNames(term), HttpStatus.OK);
		}
		catch(Exception e){
			logger.error("Exception occured when invoking suggestItems: ", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	* Deprecated 	
	*/
	@Secured({"ROLE_USER"})
	@RequestMapping(value="/searchApparelTypes", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchRetailerNames(final Principal principal, @RequestParam(value="type", required=true) String type, @PageableDefault Pageable pageable){
		try{
			if(type.isEmpty()){
				return new ResponseEntity(searchService.getApparelTypes(type, pageable), HttpStatus.OK);
			}else{
				return new ResponseEntity(searchService.suggestApparelType(type), HttpStatus.OK);
			}
		}
		catch(Exception e){
			logger.error("Exception occured when invoking suggestItems: ", e);
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}