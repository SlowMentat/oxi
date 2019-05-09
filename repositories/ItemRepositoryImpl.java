package oxi.repositories;

import oxi.models.*;
import oxi.models.dto.*;
import oxi.models.dto.retailer.*;
import oxi.models.retailer.*;
import oxi.repositories.ItemRepositoryCustom;

//import org.hibernate.Query;
import org.hibernate.transform.*;
import org.hibernate.SQLQuery;
import org.hibernate.*;
import org.hibernate.type.UUIDBinaryType;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.*;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.Transient;
//import javax.persistence.Query;

import java.lang.Long;
import static java.lang.Math.toIntExact;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.type.LongType;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


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
} 