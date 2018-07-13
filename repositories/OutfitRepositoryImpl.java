package oxi.repositories;

import oxi.models.*;
import oxi.models.dto.OutfitDto;
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
		List<OutfitDto> outfitDtos = new ArrayList<OutfitDto>();
		//HashMap used to build OutfitDTO with nested List<Content>
		HashMap<Outfit, ArrayList<Content>> outfitToContentMap = new HashMap<Outfit, ArrayList<Content>>();
		//Example 553. Hibernate native query selecting entities with joined one-to-many association
		String query = "select {o.*}, {c.*} from outfit o, content c where o.profile_id=:id and c.outfit_id=o.id order by o.id";
		String countQ = "select count(o.id) as cnt from outfit o where o.profile_id = :id";
		Long outfitCount = (Long)session.createSQLQuery(countQ)
			.addScalar("cnt", LongType.INSTANCE)
			.setParameter("id", id)
			.uniqueResult();
		int pageSize = pageable.getPageSize();
		int lastPageNumber = (int) (Math.ceil(outfitCount / pageSize));

		List<Object[]> tuples = session.createSQLQuery(query)
			.addEntity("o", Outfit.class)
			.addEntity("c", Content.class)
			.setFirstResult(pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.setParameter("id", id)
			.list();
	
		for(Object[] tuple : tuples){
			logger.debug("Outfit Tuple = " + tuple[0]);
			logger.debug("Content Tuple " + tuple[1]);

			//put <key, value> into HasMap
			outfit = (Outfit)tuple[0];
			if(!outfitToContentMap.containsKey(outfit)){
				ArrayList<Content> contents = new ArrayList<Content>(7);
				contents.add((Content)tuple[1]);				
				outfitToContentMap.put(outfit, contents);
			}else{
				ArrayList<Content> c = outfitToContentMap.get(outfit);
				c.add((Content)tuple[1]);
				outfitToContentMap.put(outfit, c);
			}
		}
		//Instantiate and return List<ObjectDto> with embedded Content Entities	
		for(Outfit o : outfitToContentMap.keySet()){
			outfitDtos.add(new OutfitDto(o.getId(), o.getLikes(), o.getComments(), outfitToContentMap.get(o), o.getCoverpicuri()));
		}

		Page<OutfitDto> pagedOutfitDtos = new PageImpl<OutfitDto>(outfitDtos, pageable, outfitCount);
		return pagedOutfitDtos;//outfitDtos;
	}
}