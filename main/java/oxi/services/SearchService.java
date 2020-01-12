package oxi.services;

import oxi.repositories.es.*;
import oxi.models.dto.es.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
//import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.hateoas.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.*;
import org.springframework.data.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.core.convert.converter.*;
import org.springframework.http.HttpHeaders;

import javax.mail.internet.MimeMessage;
import java.io.IOException;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Service
public class SearchService{

	private static final Logger logger = LogManager.getLogger(SearchService.class);

	@Autowired private ApparelTypeEsRepository apparelRep;
	@Autowired private ItemEsRepository itemRep;
	@Autowired private RetailerEsRepository retailerRep;
	@Autowired private SizeLabelEsRepository sizeLabelRep;
	//@Autowired private UserDefinedRetailerEsRepository udrRep;	

	@Autowired private SuggestEsRepositoryCustom suggestRep;

	@Autowired private PagedResourcesAssembler<ApparelTypeEsDto> apparelTypePRAP;
	@Autowired private PagedResourcesAssembler<ItemEsDto> itemPRAP;
	@Autowired private PagedResourcesAssembler<RetailerEsDto> retailerPRAP;
	@Autowired private PagedResourcesAssembler<SizeLabelEsDto> sizeLabelPRAP;
	//@Autowired private PagedResourcesAssembler<UserDefinedRetailerEsDto> udrPRAP;

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	private <T extends Identifiable<?>> ResourceSupport toResource(T dto){
		return new Resource<T>(dto);
	}

	public List<SuggestRetailerEsDto> suggestRetialerNames(String prefix){
		List<SuggestRetailerEsDto> result = new ArrayList<SuggestRetailerEsDto>();
		if(prefix != null && !prefix.isEmpty()){
			result = suggestRep.retailerNameSuggest(prefix);
		}
		return result;
	}

	public List<SuggestItemEsDto> suggestItems(String prefix, String retailer){
		List<SuggestItemEsDto> result = new ArrayList<SuggestItemEsDto>();
		if(prefix != null && retailer != null && !prefix.isEmpty()){
			result = suggestRep.itemSuggest(prefix, retailer);
		}
		return result;
	}

	public List<SuggestUdrEsDto> suggestUserDefinedRetailerNames(String prefix){
		List<SuggestUdrEsDto> result = new ArrayList<SuggestUdrEsDto>();
		if(prefix != null && !prefix.isEmpty()){
			result = suggestRep.udrNameSuggest(prefix);
		}
		return result;		
	}

	public List<SuggestUdsEsDto> suggestUserDefinedSizeLabels(String prefix){
		List<SuggestUdsEsDto> result = new ArrayList<SuggestUdsEsDto>();
		if(prefix != null && !prefix.isEmpty()){
			result = suggestRep.udsLabelSuggest(prefix);
		}
		return result;		
	}

	public List<SuggestApparelTypeEsDto> suggestApparelType(String prefix){
		List<SuggestApparelTypeEsDto> result = new ArrayList<SuggestApparelTypeEsDto>();
		if(prefix != null && !prefix.isEmpty()){
			result = suggestRep.apparelTypeSuggest(prefix);
		}
		return result;		
	}

	public PagedResources<?> getApparelTypes(String name, Pageable pageable){
		Page<ApparelTypeEsDto> apparelTypes = null;
		//if name is empty or null, query ApparelTypeEsRep for all names
		if(name != null && !name.isEmpty()){
			apparelTypes = apparelRep.findByName(name, pageable);
		}
		//otherwise query by name		
		else{ apparelTypes = apparelRep.findAll(pageable); }
		return apparelTypePRAP.toResource(apparelTypes, this::toResource);
	}


	public PagedResources<?> getSizeLabels(String name, Pageable pageable){
		Page<SizeLabelEsDto> sizeLabels = null;
		//if name is empty or null, query SizeLabelEsDto for all names
		if(name != null && !name.isEmpty()){
			sizeLabels = sizeLabelRep.findByName(name, pageable);
		}
		//otherwise query by name		
		else{ sizeLabels = sizeLabelRep.findAll(pageable); }
		return sizeLabelPRAP.toResource(sizeLabels, this::toResource);
	}
}

