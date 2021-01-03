package oxi.models.dto;

import oxi.models.retailer.RetailerAccount;
import oxi.components.UUIDKeyset;
import oxi.components.HALEntityModel;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.Calendar;
import java.sql.Timestamp;
import java.io.Serializable;
import javax.persistence.Transient;
import org.springframework.hateoas.server.core.*;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.Identifiable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.blazebit.persistence.PagedList;

import com.blazebit.persistence.KeysetPage;
import com.blazebit.persistence.Keyset;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/*
*  DTO model used during seek type pageination
*/
public class CursorDto extends HALEntityModel implements KeysetPage
{
	@Transient
	private static final Logger logger = LogManager.getLogger(CursorDto.class);
	static final Integer MAX_SIZE = 50;

	
	@JsonIgnore
	private Keyset highest;
	@JsonIgnore
	private Keyset lowest;
	@JsonIgnore
	private List<Keyset> keysets;
	@JsonIgnore
	private Integer nextFirstResult;

	private Integer firstResult = 0;
	private Integer maxResults = MAX_SIZE;
	private String date;
	private String firstId;
	private String lastId;

	/* -1 page up
	*  0 first page
	*  1 page down
	*/
	private Integer direction = 0;

	public CursorDto(
		Integer direction, 
		Integer firstResult, 
		Integer maxResults, 
		String lowestId, 
		String highestId, 
		String date )
	{
		// Convert to passed string ids to UUIDs
		UUID lowestUUID = lowestId != null && !lowestId.isEmpty() ? UUID.fromString(lowestId) : null;
		UUID highestUUID = highestId != null && !highestId.isEmpty() ? UUID.fromString(highestId) : null;

		this.firstResult = firstResult != null ? firstResult : 0;
		logger.debug("Checking maxResults cap:  maxResults = " + maxResults);
		this.maxResults = capSize(maxResults);
		this.date = date;

		// Set highest and lowest Keyset, then build keysets from there
		this.lowest = (Keyset)new UUIDKeyset(lowestUUID, date);
		this.highest = (Keyset)new UUIDKeyset(highestUUID, date);
		this.keysets = Arrays.asList(lowest, highest);
		this.direction = direction;

		// Page down
		if(this.direction == 1){
			this.nextFirstResult = this.firstResult + this.maxResults;
		}
		// Page up
		else if(this.direction == -1){
			this.nextFirstResult = this.firstResult - this.maxResults;
		}
		else{
			this.nextFirstResult = this.firstResult;
		}
	}

	private static Integer capSize(Integer size){
		logger.debug("capSize# size parameter = " + size);
		return size <= MAX_SIZE ? size : MAX_SIZE;
	}

	/*
	*	Build query paramters for page link
	*		first:  	pos of the first element in the previous PagedList
	*		size:		max number of results from previous PagedList
	*		date:		date filter
	*		firstId:	the first entry from the previous PagedList
	*		lastId:		the last entry from the preivous PagedList
	*/
	public static String getNextURI(PagedList<?> nextPage, CursorDto cursor){

		String uri = String.format(
			"&direction=%d&firstResult=%d&maxResults=%d&lowestId=%s&highestId=%s&date=%s", 
			cursor.getDirection(),
			cursor.getFirstResult(), 
			cursor.getMaxResults(), 
			cursor.getLowest(), 
			cursor.getHighest(),
			cursor.getDate()
		);
		
		if(nextPage.getKeysetPage() != null){
			UUID lowest = (UUID)nextPage.getKeysetPage().getLowest().getTuple()[0];
			UUID highest = (UUID)nextPage.getKeysetPage().getHighest().getTuple()[0];

			uri = String.format(
				"&direction=%d&firstResult=%d&maxResults=%d&lowestId=%s&highestId=%s&date=%s", 
				(cursor.getDirection() == 0 ? 1 : cursor.getDirection()),
				nextPage.getKeysetPage().getFirstResult(), 
				nextPage.getKeysetPage().getMaxResults(), 
				(lowest != null ? lowest.toString() : ""), 
				(highest != null ? highest.toString() : ""),
				cursor.getDate()
			);
		}

		return uri;
	}


	//Getters
	@Override
	public int getFirstResult(){return this.firstResult;}
	@Override
	public int getMaxResults(){return this.maxResults;}
	@Override
	public Keyset getLowest(){return this.lowest;}
	@Override
	public Keyset getHighest(){return this.highest;}
	//@Override
	public List<Keyset> getKeysets(){return this.keysets;}
	public String getDate(){return this.date;}
	public String getFirstId(){return this.firstId;}
	public String getLastId(){return this.lastId;}
	public Integer getNextFirstResult(){return this.nextFirstResult;}
	public Integer getDirection(){return this.direction;}

	//Setters
	public void setFirstResult(Integer firstResult){this.firstResult = firstResult;}
	public void setMaxResults(Integer maxResults){this.maxResults = maxResults;}
	public void setDate(String date){this.date = date;}
	//public void setSize(Integer size){this.size = capSize(size);}
}