package oxi.repositories;

import oxi.models.*;
import oxi.models.dto.OutfitDto;
import oxi.models.dto.ContentDto;
import oxi.models.dto.ItemDto;
import oxi.models.projection.OutfitProjection;
import oxi.models.projection.ContentProjection;
import oxi.repositories.OutfitRepositoryCustom;

//import org.hibernate.Query;
import org.hibernate.transform.*;
import org.hibernate.SQLQuery;
import org.hibernate.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.Transient;
//import javax.persistence.Query;

import java.lang.Long;
import static java.lang.Math.toIntExact;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.type.LongType;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
//import org.apache.spark.sql.types.LongType;

//import org.springframework.data.domain.Page;

public class OutfitRepositoryImpl implements OutfitRepositoryCustom {	
	@Transient
	private static final Logger logger = LogManager.getLogger(OutfitRepositoryImpl.class);
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	/*private static String buildPagedQuery(String query, Pageable page){

	}*/

	public Page<OutfitDto> findByProfileId(Long id, Pageable pageable){
		Session session = entityManager.unwrap(Session.class);	
		Outfit outfit;
		Content content;
		Item item;
		List<OutfitDto> outfitDtos = new ArrayList<OutfitDto>();
		List<ContentDto> contentDtos = new ArrayList<ContentDto>();
		ArrayList<ItemDto> items;
		//HashMap used to build OutfitDTO with nested List<Content>
		HashMap<Outfit, ArrayList<Content>> outfitToContentMap = new HashMap<Outfit, ArrayList<Content>>();
		HashMap<Long, OutfitDto> idToOutfitDtoMap = new HashMap<Long, OutfitDto>();
		HashMap<Content, ArrayList<ItemDto>> contentToItemMap = new HashMap<Content, ArrayList<ItemDto>>();
		//Example 553. Hibernate native query selecting entities with joined one-to-many association
		String outfitQ = "select {o.*} from outfit o where o.profile_id = :id";
		//String contentItemQ = "select {c.*}, {i.*} from item i, content c join item_content ic on ic.content_id=c.id where i.id = ic.item_id and c.outfit_id in (:outfitIdList)";
		String contentItemQ = "select {c.*}, {i.*} "+
			"from item i, content c " +
			"join item_content ic on ic.content_id=c.id " +
			"where i.id = ic.item_id " +
			"and c.outfit_id in (:outfitIdList) " +
			"union all select c.*, null, null, null, null, null, null, null " +
			"from content c, item_content ic " +
			"where c.outfit_id in (:outfitIdList) " +
			"and not c.id=ic.content_id";
		String countQ = "select count(o.id) as cnt from outfit o where o.profile_id = :id";
 
		Long outfitCount = (Long)session.createSQLQuery(countQ)
			.addScalar("cnt", LongType.INSTANCE)
			.setParameter("id", id)
			.uniqueResult();

		//values used as a Parameter List in the cotnentItemQ SQL query
		List<Long> outfitIds = new ArrayList(toIntExact(outfitCount));

		List<Outfit> outfitTuples = session.createSQLQuery(outfitQ)
			.addEntity("o", Outfit.class)
			.setFirstResult(pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.setParameter("id", id)
			.list();

		//Populates hastable with id columns as key and outfitDto object as value.
		//Assumes rows with unique id's to be returned from query
		for(Outfit o : outfitTuples){
			//Outfit o = (Outfit)tuple[0];
			idToOutfitDtoMap.put(o.getId(), new OutfitDto(o.getId(), o.getLikes(), o.getComments(), new ArrayList<ContentDto>(5), o.getCoverpicuri()));
		}

		List<Object[]> contentItemTuples = session.createSQLQuery(contentItemQ)
			.addEntity("c", Content.class)
			.addEntity("i", Item.class)
			.setParameterList("outfitIdList", idToOutfitDtoMap.keySet())
			.list();

		for(Object[] tuple : contentItemTuples){
			//put <key, value> into HasMap
			content = (Content)tuple[0];
			item = (Item)tuple[1];
			if(!contentToItemMap.containsKey(content)){
				items = new ArrayList<ItemDto>(9);
			}else{
				items = contentToItemMap.get(content);
			}			
			if(item != null) items.add(new ItemDto(item.getId(), item.getPositionx(), item.getPositiony(), item.getLink(), item.getType(), item.getSize()));
			contentToItemMap.put(content, items);
		}
		
		for(Content c : contentToItemMap.keySet()){
			idToOutfitDtoMap.get(c.getOutfit().getId()).getContents().add(new ContentDto(c.getId(), c.getCoverpicuri(), contentToItemMap.get(c)));
			//contentDtos.add(new ContentDto(c.getId(), c.getCoverpicuri(), contentToItemMap.get(c)));
			logger.debug("content.outfit = " + c.getOutfit());
			//outfitDtos.add
		}
		Page<OutfitDto> pagedOutfitDtos = new PageImpl<OutfitDto>(new ArrayList<OutfitDto>(idToOutfitDtoMap.values()), pageable, outfitCount);


		return pagedOutfitDtos;//outfitDtos;
	}
}