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

//mport org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
import com.fasterxml.jackson.databind.*; 

import java.io.*;
import java.util.ArrayList;
import java.io.Serializable;

import oxi.models.*;
import oxi.repositories.*;
import oxi.dao.*;
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
	//Services
	@Autowired
	private ConsumerService consumerService;
	private static final Logger logger = LogManager.getLogger(ConsumerController.class);

	//@Secured ({"ROLE_USER"})
	@RequestMapping(value="/uploadPhoto", method=RequestMethod.POST)
	public void uploadImage(MultipartHttpServletRequest requestData){
		String imageUrl = consumerService.savePhoto(requestData);
		//create new Picture Record with imageUrl
		logger.debug("Image filename = " + imageUrl);
	}
	
	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for OUTFIT resource
	******************************************************************
	*/
	//@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/outfit", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void uploadOutfit(@RequestBody Outfit outfit){
		logger.debug("Request Body Received: " + outfit);
		consumerService.saveOutfit(outfit);
	}

	//@PreAuthorize("#name == principal.username")
	//@Secured({"ROLE_ANONYMOUS"})
	@RestResource(exported = true)
	@RequestMapping(value="/outfit/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<?> readOutfit(@PathVariable("id") Long id){
		return new ResponseEntity<>(consumerService.readOutfit(id), HttpStatus.OK);
	}

	/*@RestResource(exported = true)
	@RequestMapping(value = "/outfits/{profileId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<?>> readOutfits(@PathVariable("profileId") Long profileId, @PageableDefault Pageable pageable){
		return new ResponseEntity<>(consumerService.readOutfits(profileId, pageable), HttpStatus.OK);
	}*/
	@RestResource(exported = true)
	@RequestMapping(value = "/outfits/{profileId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readOutfits(@PathVariable("profileId") Long profileId, @PageableDefault Pageable pageable){
		return new ResponseEntity<PagedResources<?>>(consumerService.readOutfits(profileId, pageable), HttpStatus.OK);
	}

	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for CONTENT resource
	******************************************************************
	*/
	//@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/content", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void uploadContent(@RequestBody Content content){
		logger.debug("Request Body Received: " + content);
		consumerService.saveContent(content);
	}

	//@PreAuthorize("#name == principal.username")
	//@Secured({"ROLE_ANONYMOUS"})
	/*@RestResource(exported = true)
	@RequestMapping(value="/content/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<ContentDto> readContent(@PathVariable("id") Long id){
		return new ResponseEntity<>(consumerService.readContent(id), HttpStatus.OK);
	}*/

	@RestResource(exported = true)
	@RequestMapping(value = "/contents/{outfitId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> readOutfits(@PathVariable("outfitId") Long outfitId/*, @PageableDefault Pageable pageable*/){
		return new ResponseEntity<List<?>>(consumerService.readContents(outfitId), HttpStatus.OK);
	}


	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for ITEM resource
	******************************************************************
	*/
	//@Secured({"ROLE_USER"})
	@Transactional
	@RestResource(exported = true)
	@RequestMapping(value="/item", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void uploadItem(@RequestBody Item item){
		logger.debug("Request Body Received: " + item);
		consumerService.saveItem(item);
	}

	//@PreAuthorize("#name == principal.username")
	//@Secured({"ROLE_ANONYMOUS"})
	/*@RestResource(exported = true)
	@RequestMapping(value="/item/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<ItemDto> readItem(@PathVariable("id") Long id){
		return new ResponseEntity<>(consumerService.readItem(id), HttpStatus.OK);
	}

	@RestResource(exported = true)
	@RequestMapping(value = "/items", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedResources<ItemDto>> readItems(@PageableDefault Pageable pageable){
		return new ResponseEntity<>(consumerService.readItems(pageable), HttpStatus.OK);
	}*/


	/*
	******************************************************************
	HTTP Request handling methods (GET and POST) for PROFILE resource
	******************************************************************
	*/
	//@Secured({"ROLE_USER"})
	@RestResource(exported = true)
	@RequestMapping(value="/profile", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void uploadProfile(@RequestBody Profile profile){
		logger.debug("Request Body Received: " + profile);
		consumerService.saveProfile(profile);
	}

	//@PreAuthorize("#name == principal.username")
	//@Secured({"ROLE_ANONYMOUS"})
	@RestResource(exported = true)
	@RequestMapping(value="/profile/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<ProfileDto> readProfile(@PathVariable("id") Long id){
		return new ResponseEntity<>(consumerService.readProfile(id), HttpStatus.OK);
	}

}