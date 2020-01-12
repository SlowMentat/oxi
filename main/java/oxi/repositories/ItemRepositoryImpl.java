package oxi.repositories;

import oxi.models.*;
import oxi.models.dto.ItemDto;
import oxi.models.dto.CursorDto;
import oxi.models.dto.retailer.*;
import oxi.models.retailer.*;
import oxi.repositories.ItemRepositoryCustom;

//import org.hibernate.Query;
import org.hibernate.transform.*;
import org.hibernate.SQLQuery;
import org.hibernate.*;
import org.hibernate.type.UUIDBinaryType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import oxi.components.CursorImpl;
import org.springframework.transaction.annotation.*;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.Transient;
import javax.xml.bind.DatatypeConverter;
//import javax.persistence.Query;

import java.lang.Long;
import static java.lang.Math.toIntExact;
import java.lang.NoSuchMethodException;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.UUID;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.type.LongType;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PagedArrayList;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
//import com.blazebit.persistence.deltaspike.data.KeysetAwarePage;
import com.blazebit.persistence.spring.data.base.query.KeysetAwarePageImpl;


public class ItemRepositoryImpl implements ItemRepositoryCustom{
	
	//@RestResource(exported=true, path="byid", rel="SearchAllById")
	//@Query(value = "SELECT * FROM item WHERE profile_id = ?1", nativeQuery = true)
	//List<Item> findByProfileId(long profileid/*, Pageable pageable*/);

	//Item findById(UUID id);
	@Transient
	private static final Logger logger = LogManager.getLogger(ItemRepositoryImpl.class);

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	//CriteriaBuilderConfiguration cbf;
	CriteriaBuilderFactory cbf;

	@Override
	public Page<Item> getAllItemsWithRetailerAccount(UUID retailerAccountId, Pageable pageable){
		Item item;
		RetailerAccount retailerAccount;
		List<Item> items = new ArrayList<Item>();		
		Session session = entityManager.unwrap(Session.class);	
		String itemQ = "select {i.*} from item i where i.retailer_account_id = :retailerAccountId";
		String countQ = "select count(i.id) as cnt from item i where i.retailer_account_id = :retailerAccountId";//TODO:  may be expensive

		Long itemCount = (Long)session.createSQLQuery(countQ)
			.addScalar("cnt", LongType.INSTANCE)
			.setParameter("retailerAccountId", retailerAccountId, UUIDBinaryType.INSTANCE)
			.uniqueResult();

		List<Item> itemRetailerAccountTuples = session.createSQLQuery(itemQ)
			.addEntity("i", Item.class)
			.setParameter("retailerAccountId", retailerAccountId, UUIDBinaryType.INSTANCE)
			.setFirstResult((int)pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.list();

		for(Item i : itemRetailerAccountTuples){
			//item = (Item)tuple[0];
			//retailerAccount = (RetailerAccount)tuple[1];
			//if(retailerAccount != null){ 
			//	String getCompanyName = retailerAccount.getCompanyName();
			//	if(getCompanyName != null){
			//		logger.debug("retailerAccount.companyName = " + getCompanyName);
			//	}else{
			//		logger.debug("retailerAccount.companyName is null");
			//	}
			//}else{
			//	logger.debug("retailerAccount is null");
			//}
			items.add(i);
		}

		return new PageImpl<Item>(items, pageable, itemCount);
	}
	//List<Item> findByProductOwner(String productOwner);

//	@Override
//	//public Page<ItemDto> getAllItemsWithCoverpicUri(Pageable pageable){
//	public List<ItemDto> getAllItemsWithCoverpicUri(CursorDto cursor){
//		Item item = null;
//		Outfit outfit = null;
//		UUID prevId = null;
//		Date date = DatatypeConverter.parseDateTime(cursor.getDate()).getTime();
//		//Date date = new Date(cursor.getDate());
//		logger.debug("point2");
//		List<ItemDto> itemDtos = new ArrayList<ItemDto>();
//		int count = 0;
//		Session session = entityManager.unwrap(Session.class);	
//
//		//String itemQ = "select {i.*}, {o.*} from item i join outfit o on o.id = i.outfit_id";
//
//		/*
//		*  Uses seek method with index ordered by for pagination instead of offset.
//		*  See:
//		*		https://use-the-index-luke.com/no-offset
//		*		https://use-the-index-luke.com/sql/partial-results/fetch-next-page
//		*/
//		//String itemQ = null;
//
//		//// If prevId is null, query using offset pagination
//		//if(cursor.getId == null){
//		//	itemQ = "SELECT {i.*}, {o.*} " +
//		//	"FROM item i " +
//		//	"JOIN outfit o ON o.id = i.outfit_id " +
//		//	"ORDER BY i.createdOn DESC NULLS LAST, i.id DESC NULLS LAST " +
//		//	"LIMIT :size OFFSET 0";
//		//}
//		//// If prevId is provided, query using keyset pagination
//		//else{
//		String firstItemQ = "SELECT i.id_text FROM item i WHERE i.created_on <= :date ORDER BY i.created_on DESC LIMIT 1";
//
//		if(cursor.getId() == null || cursor.getId().isEmpty()){
//			prevId = UUID.fromString((String)session.createSQLQuery(firstItemQ).setParameter("date", date, DateType.INSTANCE).list().get(0));
//		}
//		else{
//			prevId = UUID.fromString(cursor.getId());//UUID.fromString(cursor.getId());
//		}
//
//		//String itemQ = "SELECT * " +
//		//	"FROM (SELECT {i.*}, {o.*} " +
//		//		"FROM item i " +
//		//		"JOIN outfit o ON o.id = i.outfit_id " +
//		//		"WHERE ((i.created_on <= :date) AND ((i.created_on < :date) OR (i.created_on = :date AND i.id < :itemId))) " +
//		//		"ORDER BY i.created_on DESC, i.id DESC" +
//		//	") as pi " +
//		//	"LIMIT :size";
//
//		String itemQ = "SELECT {i.*}, {o.*} " +
//			"FROM item i " +
//			"JOIN outfit o ON o.id = i.outfit_id " +
//			"WHERE ((i.created_on <= :date) AND ((i.created_on < :date) OR (i.created_on = :date AND i.id < :itemId))) " +
//			"ORDER BY i.created_on DESC, i.id DESC " +
//			"LIMIT :size";
//		//}
//
//		List<Object[]> itemCoverpicTuples = session.createSQLQuery(itemQ)
//			.addEntity("i", Item.class)
//			.addEntity("o", Outfit.class)
//			//.setFirstResult((int)pageable.getOffset())
//			//.setMaxResults(pageable.getPageSize())
//			.setParameter("date", date, DateType.INSTANCE)
//			.setParameter("itemId", prevId, UUIDBinaryType.INSTANCE)
//			.setParameter("size", cursor.getSize(), IntegerType.INSTANCE)
//			.list();
//
//		for(Object[] tuple : itemCoverpicTuples){
//			item = (Item)tuple[0];
//			outfit = (Outfit)tuple[1];
//			logger.debug("outfti coverpicuri = " + outfit.getCoverpicuri());
//			itemDtos.add(new ItemDto(item, outfit.getCoverpicuri()));
//			//count++;
//		}
//
//		//Build links from cursor parameter
//		//CursorImpl<ItemDto> cursorImpl = new CursorImpl<ItemDto>(itemDtos, pageable, lastId.toString());
//		//cursorImpl.setBeforeId();
//		//cursorImpl.setAfterId();
//		//
//		//return cursorImpl;
//		//return new PageImpl<ItemDto>(itemDtos, pageable, count);
//		return itemDtos;
//
//	}

	@Override
	//public Page<ItemDto> getAllItemsWithCoverpicUri(Pageable pageable){
	public List<ItemDto> getAllItemsWithCoverpicUri(CursorDto cursor) throws NoSuchMethodException{
		Item item = null;
		Outfit outfit = null;
		Date date = DatatypeConverter.parseDateTime(cursor.getDate()).getTime();
		logger.debug("point2");
		List<ItemDto> itemDtos = new ArrayList<ItemDto>();
		int count = 0;
		Session session = entityManager.unwrap(Session.class);	
		logger.debug("nextFirstResult = " + cursor.getNextFirstResult());
		//String itemQ = "select {i.*}, {o.*} from item i join outfit o on o.id = i.outfit_id";

		/*
		*  2 things preventing me from using this:
		*    Don't know how to perform join on 
		*    Don't know how to handle KeySet page between calls
		*/
		//PagedList<ItemDto> itemPage = cbf.create(entityManager, ItemDto.class)
		PagedList<ItemDto> itemPage = cbf.create(entityManager, ItemDto.class)
			.from(Item.class, "i")
			.leftJoinOn(Outfit.class, "o")
				.on("i.outfitId").eqExpression("o.id")
			.end()
			//.select("i")
			//.select("o.coverpicuri")
			.selectNew(ItemDto.class.getConstructor(Item.class, String.class))
				.with("i")
				.with("o.coverpicuri")
			.end()
			.orderByDesc("i.id")
			.orderByDesc("i.createdOn")
			.page((cursor.getFirstId() == null || cursor.getLastId() == null ? null : cursor), cursor.getNextFirstResult(), cursor.getMaxResults())
			.getResultList();
		
		//int direction = cursor.getDirection() == 0 ? 1 : cursor.getDirection();
		//
		///*
		//*	Build query paramters for page link
		//*		first:  	pos of the first element in the previous PagedList
		//*		size:		max number of results from previous PagedList
		//*		date:		date filter
		//*		firstId:	the first entry from the previous PagedList
		//*		lastId:		the last entry from the preivous PagedList
		//*/
		//String queryParams = String.format(
		//	"\ndirection=%d\nfirst=%d\nsize=%d\nfirstId=%s\nlastId=%s\ndate=%s", 
		//	direction,
		//	itemPage.getKeysetPage().getFirstResult(), 
		//	itemPage.getKeysetPage().getMaxResults(), 
		//	itemPage.getKeysetPage().getLowest().getTuple()[0].toString(), 
		//	itemPage.getKeysetPage().getHighest().getTuple()[0].toString(),
		//	cursor.getDate()
		//);

		//logger.debug("\n\nPagedList properties = " + queryParams + "\n");

		//Page<ItemDto> kaPal = new KeysetAwarePageImpl<ItemDto>(itemPage);
		//return new PageImpl<ItemDto>(itemDtos, pageable, count);
		//PagedArrayList<ItemDto> pal = new PagedArrayList<ItemDto>(/*itemPage.toArray(new ItemDto[Math.toIntExact(itemPage.getTotalSize())])*/itemPage, itemPage.getKeysetPage(), itemPage.getTotalSize(), itemPage.getFirstResult(), itemPage.getMaxResults());
		logger.debug("pal instantiated");
		//return kaPal;
		return itemPage;
	}
} 