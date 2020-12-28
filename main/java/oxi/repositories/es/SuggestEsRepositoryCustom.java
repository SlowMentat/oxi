package oxi.repositories.es;

import oxi.models.dto.es.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
//import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestEsRepositoryCustom{
	/*
	* performs field completion for sizeLabel input
	*/
	List<SizeLabelEsDto> sizeLabelSuggest(String prefix, String context);

	/*
	* performs field completion for item input
	*/
	List<SuggestItemEsDto> itemSuggest(String prefix, String context);

	List<SuggestUDItemESDTO> userDefinedItemSuggest(String prefix, String retailer, String apparelType, String sizeLabel);

	/*
	* performs field completion for retailer input
	*/
	List<SuggestRetailerEsDto> retailerNameSuggest(String prefix);

	/*
	* performs field completion for user defined retailer input
	*/
	List<SuggestUdrEsDto> udrNameSuggest(String prefix);

	List<SuggestUdsEsDto> udsLabelSuggest(String prefix);

	List<SuggestApparelTypeEsDto> apparelTypeSuggest(String prefix);
}
