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
//import org.apache.spark.sql.types.LongType;

//import org.springframework.data.domain.Page;

public class BrandRepositoryImpl implements BrandRepositoryCustom {	
	/*@Transient
	private static final Logger logger = LogManager.getLogger(OutfitRepositoryImpl.class);
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Outfit> findAll(Pageable pageable){
		Session session = entityManager.unwrap(Session.class);	
		String outfitQ = "select {o.*} from outfit o";
		//values used as a Parameter List in the cotnentItemQ SQL query
		//List<Long> outfitIds = new ArrayList(toIntExact(outfitCount));
		String countQ = "select count(o.id) as cnt from outfit o";
 
		Long outfitCount = (Long)session.createSQLQuery(countQ)
			.addScalar("cnt", LongType.INSTANCE)
			.uniqueResult();

		List<Outfit> outfitTuples = session.createSQLQuery(outfitQ)
			.addEntity("o", Outfit.class)
			.setFirstResult((int)pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.list();
		logger.debug("outfitTuples Size = ");
		logger.debug(outfitTuples.size());
		return new PageImpl<Outfit>(outfitTuples, pageable, outfitCount);
	}*/
}