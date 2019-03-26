package oxi.repositories.retailer;

import oxi.models.retailer.*;
import oxi.models.Item;
import oxi.models.dto.retailer.*;
import oxi.models.dto.ItemDto;
/*import oxi.models.dto.OutfitDto;
import oxi.models.dto.ContentDto;
import oxi.models.dto.ItemDto;
import oxi.models.dto.PictureDto;
import oxi.models.projection.OutfitProjection;
import oxi.models.projection.ContentProjection;
import oxi.repositories.OutfitRepositoryCustom;*/

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

public class SizeChartRepositoryImpl implements SizeChartRepositoryCustom {	
	@Transient
	private static final Logger logger = LogManager.getLogger(SizeChartRepositoryImpl.class);
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	public Page<SizeChartDto> findByRetailerAccountId(UUID id, Pageable pageable){
		Session session = entityManager.unwrap(Session.class);	
		SizeChart sizeChart;
		SizeGroup sizeGroup;
		SizeChartSizeGroup sizeChartSizeGroup;
		ArrayList<SizeChartDto> sizeChartDtos = new ArrayList<SizeChartDto>();
		ArrayList<SizeGroupDto> sizeGroupDtos = new ArrayList<SizeGroupDto>();
		HashMap<SizeChart, ArrayList<SizeGroupDto>> sizeChartToSizeGroupDtosMap = new HashMap<SizeChart, ArrayList<SizeGroupDto>>();
		HashMap<UUID, SizeChartDto> idToSizeChartDtoMap = new HashMap<UUID, SizeChartDto>();

		//Example 553. Hibernate native query selecting entities with joined one-to-many association
		String sizeChartQ = "select {sc.*} from size_chart sc where sc.retailer_account_id = :id";
		String sizeChartSizeGroupQ = "select {sc.*}, {sg.*}, {scsg.*}"+
			"from size_group sg, size_chart sc " +
			"join size_chart_size_group scsg on scsg.size_chart_id=sc.id " +
			"and sc.id in (:sizeChartIdList) ";

		String countSC = "select count(sc.id) as cnt from size_chart sc where sc.retailer_account_id = :id";
 
		Long sizeChartCount = (Long)session.createSQLQuery(countSC)
			.addScalar("cnt", LongType.INSTANCE)
			.setParameter("id", id)
			.uniqueResult();

		//values used as a Parameter List in the cotnentItemQ SQL query
		//List<Long> outfitIds = new ArrayList(toIntExact(sizeChartCount));
		List<SizeChart> sizeChartTuples = session.createSQLQuery(sizeChartQ)
			.addEntity("sc", SizeChart.class)
			.setFirstResult(pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.setParameter("id", id, UUIDBinaryType.INSTANCE)
			.list();
		logger.debug("sizeChartTuples Size = ");
		logger.debug(sizeChartTuples.size());

		//Populates hashtable with id columns as key and outfitDto object as value.
		//Assumes rows with unique id's to be returned from query
		for(SizeChart sc : sizeChartTuples){
			logger.debug("sizeChart id_text:");
			logger.debug(sc.getIdText());
			idToSizeChartDtoMap.put(sc.getId(), new SizeChartDto(sc));
		}
		logger.debug("idToSizeChartDtoMap keyset = ");
		logger.debug(idToSizeChartDtoMap.keySet());

		List<Object[]> sizeChartSizeGroupTuples = session.createSQLQuery(sizeChartSizeGroupQ)
			.addEntity("sc", SizeChart.class)
			.addEntity("sg", SizeGroup.class)
			.addEntity("scsg", SizeChartSizeGroup.class)
			//.setParameterList("sizeChartIdList", Arrays.asList(UUID.fromString("3a5f73ae-a986-11e8-8336-f23c9150975d"), UUID.fromString("078e417f-a986-11e8-8336-f23c9150975d")), UUIDBinaryType.INSTANCE)
			.setParameterList("sizeChartIdList", idToSizeChartDtoMap.keySet(), UUIDBinaryType.INSTANCE)
			.list();
		logger.debug("SizeChartRepositoryImpl#findByRetailerAccount: size of sizeChartSizeGroupTuples = " + sizeChartSizeGroupTuples.size());

		for(Object[] tuple : sizeChartSizeGroupTuples){
			sizeChart = (SizeChart)tuple[0];
			sizeGroup = (SizeGroup)tuple[1];
			sizeChartSizeGroup = (SizeChartSizeGroup)tuple[2];//Don't currently need this, but you will if you want to add creation/modificaiton timestamps to the dto

			if(!sizeChartToSizeGroupDtosMap.containsKey(sizeChart)){
				sizeGroupDtos = new ArrayList<SizeGroupDto>(9);
			}else{
				sizeGroupDtos = sizeChartToSizeGroupDtosMap.get(sizeChart);
			}			
			if(sizeGroup != null){
				sizeGroupDtos.add(new SizeGroupDto(
					sizeGroup.getIdText(), 
					sizeGroup.getSizeLabel(), 
					sizeGroup.getNeck(),
					sizeGroup.getFullShoulder(),
					sizeGroup.getHalfShoulder(),
					sizeGroup.getChest(),
					sizeGroup.getWaist(),
					sizeGroup.getHip(),
					sizeGroup.getSleeve(),
					sizeGroup.getFrontLength(),
					sizeGroup.getBackLength(),
					sizeGroup.getPantOutseam(),
					sizeGroup.getPantInseam(),
					sizeGroup.getThigh(),
					sizeGroup.getCalf()
				));
			}
			sizeChartToSizeGroupDtosMap.put(sizeChart, sizeGroupDtos);
		}

		for(SizeChart sc : sizeChartToSizeGroupDtosMap.keySet()){
			//get the sizeGroupIds associated with each sizeChart
			if(sc.getSizeGroups() != null){
				//add the sizeGroupDtos to the sizeChartDto associated with sizChart id
				idToSizeChartDtoMap
					.get(sc.getId())
					.setSizeGroupDtos(sizeChartToSizeGroupDtosMap.get(sc));			
			}
		}
		Page<SizeChartDto> pagedSizeChartDtos = new PageImpl<SizeChartDto>(new ArrayList<SizeChartDto>(idToSizeChartDtoMap.values()), pageable, sizeChartCount);
		return pagedSizeChartDtos;
	}


	@Override
	public Page<SizeChart> findAll(Pageable pageable){
		Session session = entityManager.unwrap(Session.class);	
		String sizeChartQ = "select {sc.*} from size_chart sc";
		//values used as a Parameter List in the cotnentItemQ SQL query
		//List<Long> outfitIds = new ArrayList(toIntExact(sizeChartCount));
		String countSC = "select count(sc.id) as cnt from size_chart sc";
 
		Long sizeChartCount = (Long)session.createSQLQuery(countSC)
			.addScalar("cnt", LongType.INSTANCE)
			.uniqueResult();

		List<SizeChart> sizeChartTuples = session.createSQLQuery(sizeChartQ)
			.addEntity("sc", SizeChart.class)
			.setFirstResult(pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.list();
		logger.debug("sizeChartTuples Size = ");
		logger.debug(sizeChartTuples.size());
		return new PageImpl<SizeChart>(sizeChartTuples, pageable, sizeChartCount);
	}

	@Override
	public Page<SizeChart> getAllSizeChartsWithCompanyName(Pageable pageable){
		SizeChart sizeChart;
		RetailerAccount retailerAccount;
		List<SizeChart> sizeCharts = new ArrayList<SizeChart>();
		Session session = entityManager.unwrap(Session.class);	
		String sizeChartQ = "select {sc.*}, {ra.*} from size_chart sc join retailer_account ra on ra.id=sc.retailer_account_id";
		String countSC = "select count(sc.id) as cnt from size_chart sc";
 
		Long sizeChartCount = (Long)session.createSQLQuery(countSC)
			.addScalar("cnt", LongType.INSTANCE)
			.uniqueResult();

		List<Object[]> sizeChartRetailerAccountTuples = session.createSQLQuery(sizeChartQ)
			.addEntity("sc", SizeChart.class)
			.addEntity("ra", RetailerAccount.class)
			.setFirstResult(pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.list();

		for(Object[] tuple : sizeChartRetailerAccountTuples){
			sizeChart = (SizeChart)tuple[0];
			retailerAccount = (RetailerAccount)tuple[1];
			if(retailerAccount != null){
				if(retailerAccount.getCompanyName() != null){
					logger.debug("retailerAccount.companyName = " + ((RetailerAccount)tuple[1]).getCompanyName());
				}else{
					logger.debug("retailerAccount.companyName is null!");
				}
			}else{
				logger.debug("retailerAccount is null");
			}
			//sizeChart.setUsername(retailerAccount.companyName());
			sizeCharts.add(sizeChart);
		}

		logger.debug("sizeChartRetailerAccountTuples Size = ");
		logger.debug(sizeChartRetailerAccountTuples.size());
		return new PageImpl<SizeChart>(sizeCharts, pageable, sizeChartCount);
	}
}