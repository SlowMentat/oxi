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
import oxi.models.dto.retailer.*;
import oxi.services.RetailerService;

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
//@RequestMapping("/retailer")
//@RepositoryRestController
//public class RetailerAPIController{
//	public static final String defaultExceptionResponse = "Our servers seem to have ran into a problem.\nPlease wait a moment and try your request agian.";
//
//	//Services
//	@Autowired
//	private RetailerService retailerService;
//	private static final Logger logger = LogManager.getLogger(RetailerAPIController.class);
//	
//	@RequestMapping(value="/image/{filename}", method=RequestMethod.GET)
//	public @ResponseBody byte[] getImage(@PathVariable String filename) throws IOException{
//		return retailerService.getImage(filename);
//	}
//
//	//
//	//******************************************************************
//	//HTTP Request handling methods (GET and POST) for ITEM resource
//	//******************************************************************
//	//
//
//	@Secured({"ROLE_RETAILER_USER"})
//	@RestResource(exported = true)
//	@RequestMapping(value="/items", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> getFilteredItems(final Principal principal, @PageableDefault Pageable pageable, @RequestParam(value="filter", required=false) String filter){
//		try{
//			filter = filter == null ? "" : filter;
//			String username = principal.getName();		
//			return new ResponseEntity<PagedResources<?>>(retailerService.getProducts(username, filter, pageable), HttpStatus.OK);
//		}catch(Exception e){
//			return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//
//	@Secured({"ROLE_RETAILER_USER"})
//	@RestResource(exported = true)
//	@RequestMapping(value="/sizeCharts", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> getFilteredItems(final Principal principal, @RequestBody SizeChartDto sizeChartDto){
//		try{
//			String username = principal.getName();		
//			return new ResponseEntity<>(retailerService.setSizeChartsByProductId(username, sizeChartDto), HttpStatus.OK);
//		}catch(Exception e){
//			return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//
//
//	/*
//	*	/fit/username?product={itemId}
//	*	Request Body:
//	*		hmac token
//	*		session token
//	*/
//
//}