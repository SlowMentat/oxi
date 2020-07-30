package oxi.repositories;

import oxi.models.*;
import oxi.models.dto.OutfitDto;
import oxi.models.dto.ContentDto;
import oxi.models.dto.ItemDto;
import oxi.models.dto.PictureDto;
import oxi.models.dto.retailer.SizeGroupDto;
import oxi.models.dto.retailer.SizeChartDto;
import oxi.models.retailer.SizeGroup;
import oxi.models.retailer.SizeChart;
import oxi.models.retailer.SizeChartSizeGroup;
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

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
//import org.apache.spark.sql.types.LongType;

//import org.springframework.data.domain.Page;

public class OutfitRepositoryImpl implements OutfitRepositoryCustom {

	private int MAX_PAGE_SIZE = 50;

	@Transient
	private static final Logger logger = LogManager.getLogger(OutfitRepositoryImpl.class);
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	/*private static String buildPagedQuery(String query, Pageable page){

	}*/

	private HashMap<UUID, OutfitDto> getCompleteOutfit(List<Outfit> outfitTuples, long outfitCount){
		Session session = entityManager.unwrap(Session.class);	
		Outfit outfit;
		Content content;
		Item item;
		ItemContent itemContent;
		Picture picture;
		SizeGroup sizeGroup;
		SizeChart sizeChart;
		SizeChartSizeGroup sizeChartSizeGroup;

		List<OutfitDto> outfitDtos = new ArrayList<OutfitDto>();
		List<ContentDto> contentDtos = new ArrayList<ContentDto>();
		ArrayList<ItemDto> items;
		ArrayList<SizeGroupDto> sizeGroups;
		//HashMap used to build OutfitDTO with nested List<Content>
		HashMap<Outfit, ArrayList<Content>> outfitToContentMap = new HashMap<Outfit, ArrayList<Content>>();
		HashMap<Content, ArrayList<ItemDto>> contentToItemMap = new HashMap<Content, ArrayList<ItemDto>>();
		HashMap<UUID, ArrayList<SizeGroupDto>> sizeChartIdToSizeGroupsMap = new HashMap<UUID, ArrayList<SizeGroupDto>>();
		//HashMap<UUID, SizeGroupDto> idToSizeGroupDtoMap = new HashMap<UUID, SizeGroupDto>();
		HashMap<UUID, OutfitDto> idToOutfitDtoMap = new HashMap<UUID, OutfitDto>();
		HashMap<UUID, Outfit> idToOutfitMap = new HashMap<UUID, Outfit>();
		HashMap<UUID, SizeChart> idToSizeChartMap = new HashMap<UUID, SizeChart>();
		//Example 553. Hibernate native query selecting entities with joined one-to-many association
		String outfitQ = "select {o.*} from outfit o where o.profile_id = :id";

		//String contentItemQ = "select {c.*}, {i.*}, {ic.*}, {p.*}, {*.sg} "+
		//	"from item i, picture p, content c size_group sg " +
		//	"join item_content ic on ic.content_id=c.id " +
		//	"where i.id = ic.item_id " +
		//	"and i.size_group_id = sg.id " +
		//	"and c.outfit_id in (:outfitIdList) " +
		//	"and p.content_id=c.id";/* +
		//	"union all select c.id, c.coverpicuri, c.outfit_id, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null " +
		//	"from content c, item_content ic " +
		//	"where c.outfit_id in (:outfitIdList) " +
		//	"and not c.id=ic.content_id";*/

		//String contentItemQ = 
		//	"select {c.*}, {i.*}, {ic.*}, {p.*} "+
		//	"from item i, picture p, content c " +
		//	"join item_content ic on ic.content_id=c.id " +
		//	"where i.id = ic.item_id " +
		//	"and c.outfit_id in (:outfitIdList) " +
		//	"and p.content_id=c.id";

		String contentItemQ = 
			"select {c.*}, {i.*}, {ic.*}, {p.*} "+
			"from item i, picture p, content c " +
			"join item_content ic on ic.content_id=c.id " +
			"where i.id = ic.item_id " +
			"and c.outfit_id in (:outfitIdList) " +
			"and p.content_id=c.id " +

			"union all select c.id, c.coverpicuri, c.outfit_id, " + 
				"null, null, null, null, null, null, null, null, null, null, " + 
				"null, null, null, null, null, null, null, null, null, null, " + 
				"null, null, null, null, null, null, null, null, null, null, " + 
				"null, null, null " + 
			"from content c, item_content ic " +
			"where c.outfit_id in (:outfitIdList) " +
			"and not c.id=ic.content_id";

		String sizeChartSizeGroupQ = 
			"select {sc.*}, {sg.*}, {scsg.*} " +
			"from size_chart sc, size_group sg " +
			"join size_chart_size_group scsg on scsg.size_group_id = sg.id " +
			"where sc.id = scsg.size_chart_id " +
			"and sc.id in (:sizeChartIdList)";

		if(outfitTuples.size() == 0){
			idToOutfitDtoMap.put(null, new OutfitDto());
			idToOutfitMap.put(null, new Outfit());
		}

		else{			
			//Populates HashMap with id columns as key and outfitDto object as value.
			//Assumes rows with unique id's to be returned from query
			for(Outfit o : outfitTuples){
				//Outfit o = (Outfit)tuple[0];
				logger.debug("outfit id_text:");
				logger.debug(o.getIdText());
				idToOutfitDtoMap.put(o.getId(), new OutfitDto(o));//new OutfitDto(o.getIdText(), o.getLikes(), o.getComments(), new ArrayList<ContentDto>(5), o.getCoverpicuri()));
				idToOutfitMap.put(o.getId(), o);
			}

			logger.debug("idToOutfitDtoMap keyset = ");
			logger.debug(idToOutfitDtoMap.keySet());
			
	
			List<Object[]> contentItemTuples = session.createSQLQuery(contentItemQ)
				.addEntity("c", Content.class)
				.addEntity("i", Item.class)
				.addEntity("ic", ItemContent.class)
				.addEntity("p", Picture.class)
				//.addEntity("sg", SizeGroup.class) TODO:  uncoment once sg table is filled
				//.setParameterList("outfitIdList", Arrays.asList(UUID.fromString("3a5f73ae-a986-11e8-8336-f23c9150975d"), UUID.fromString("078e417f-a986-11e8-8336-f23c9150975d")), UUIDBinaryType.INSTANCE)
				.setParameterList("outfitIdList", idToOutfitDtoMap.keySet(), UUIDBinaryType.INSTANCE)
				.list();
						
			logger.debug("OutfitRepositoryImpl#findByProfileId: size of contentItemTuples = " + contentItemTuples.size());
			
			// Handles the case for no item entities associated with existing content entities.
			// Builds the contentToItemMap from idToOutfitMap instead
			if(contentItemTuples.size() == 0 && idToOutfitMap.size() > 0){
				for(Outfit o : idToOutfitMap.values()){
					for(Content c : o.getContents()){
						contentToItemMap.put(c, null);
					}
				}
			}
			else{
				for(Object[] tuple : contentItemTuples){
					//put <key, value> into HasMap
					content = (Content)tuple[0];
					item = (Item)tuple[1];
		
					if(item != null){
						//add the item id as key to the idToSizeChartMap HashMap
						idToSizeChartMap.put(item.getSizeChartId(), null);
					}
				}
			}	

			logger.debug("idToSizeChartMap keyset = ");
			logger.debug(idToSizeChartMap.keySet());

			if(idToSizeChartMap.size() == 0){
				idToSizeChartMap.put(null, new SizeChart());
			}

			else{
	
				// Query size charts on sizeChartIdList
				List<Object[]> sizeChartSizeGroupTuples = session.createSQLQuery(sizeChartSizeGroupQ)
					.addEntity("sc", SizeChart.class)
					.addEntity("sg", SizeGroup.class)
					.addEntity("scsg", SizeChartSizeGroup.class)
					.setParameterList("sizeChartIdList", idToSizeChartMap.keySet(), UUIDBinaryType.INSTANCE)
					.list();
	
				// build sizechart to List<SizeGroup> HashMap
				for(Object[] tuple :  sizeChartSizeGroupTuples){
					sizeChart = (SizeChart)tuple[0];
					sizeGroup = (SizeGroup)tuple[1];
					sizeChartSizeGroup = (SizeChartSizeGroup)tuple[2]; //data may be available from this object in the future
		
					if(!sizeChartIdToSizeGroupsMap.containsKey(sizeChart.getId())){
						sizeGroups = new ArrayList<SizeGroupDto>(5);
					}else{
						sizeGroups = sizeChartIdToSizeGroupsMap.get(sizeChart.getId());
					}
		
					if(sizeGroup != null){
						sizeGroups.add(new SizeGroupDto(sizeGroup));
					}
					sizeChartIdToSizeGroupsMap.put(sizeChart.getId(), sizeGroups);
					//idToSizeGroupDtoMap.put(sizeGroup.getId(), new SizeGroupDto(sizeGroup));
					//update idToSizeChartMap with the queried SizeChart object associated with sizeChartId key
					idToSizeChartMap.put(sizeChart.getId(), sizeChart);
				}	
			}


			// Build <Content, List<Item>> HashMap.  
			// Set the fully constructed SizeChart Object to each Item object before adding to ArrayList<Item>			
			for(Object[] tuple : contentItemTuples){
				content = (Content)tuple[0];
				item = (Item)tuple[1];
				itemContent = (ItemContent)tuple[2];
				picture = (Picture)tuple[3];
		
				if(!contentToItemMap.containsKey(content)){
					items = new ArrayList<ItemDto>(9);
				}else{
					if(picture != null) picture.setContent(content);
					items = contentToItemMap.get(content);
				}			
				if(item != null){
					ItemDto itemDto = new ItemDto(
						item.getIdText(), 
						itemContent.getPositionx(), 
						itemContent.getPositiony(), 
						item.getApparelType(), 
						//idToSizeGroupDtoMap.get(item.getSizeGroupIdText()),
						item.getSizeGroupIdText(),
						new SizeChartDto(
							(item.getSizeChartId() != null ? item.getSizeChartId().toString() : null), 
							null,//idToSizeChartMap.get(item.getSizeChartId()).getChartName(), 
							sizeChartIdToSizeGroupsMap.get(item.getSizeChartId()) 		//retreive ArrayList<SizeGroupDto> associated with the sizeChart id
						),
						null,
						item.getProduct(),
						item.getPlatform()
					);
					items.add(itemDto);
				}
				contentToItemMap.put(content, items);
			}
			
	
			for(Content c : contentToItemMap.keySet()){
				if(c.getOutfit() != null){
					logger.debug("content.outfit = " + c.getOutfit());
					logger.debug("content.outfit.id = " + c.getOutfit().getId());
					//Picture picture = c.getPicture();
					PictureDto pictureDto = c.getPicture() == null ? 
						null : 
						//new PictureDto(c.getPicture().getIdText(), c.getPicture().getThumbnailuri(), c.getPicture().getSmalluri(), c.getPicture().getLargeuri());
						new PictureDto(c.getPicture());
					
					items = contentToItemMap.get(c) == null ? new ArrayList<ItemDto>(0) : contentToItemMap.get(c);
					idToOutfitDtoMap.get(c.getOutfit().getId()).getContents().add(new ContentDto(c.getIdText(), c.getCoverpicuri(), pictureDto, items, null));			
				}
			}
		}

		return idToOutfitDtoMap;
	}

	public OutfitDto getOutfitById(UUID id){
		Session session = entityManager.unwrap(Session.class);
		String outfitQ = "select {o.*} from outfit o where o.id = :id";

		//values used as a Parameter List in the cotnentItemQ SQL query
		List<Outfit> outfitTuples = session.createSQLQuery(outfitQ)
			.addEntity("o", Outfit.class)
			.setParameter("id", id, UUIDBinaryType.INSTANCE)
			.list();

		return getCompleteOutfit(outfitTuples, 1).get(id);

	}

	public Page<OutfitDto> findByProfileId(UUID id, Pageable pageable){
		Session session = entityManager.unwrap(Session.class);	
		Outfit outfit;
		Content content;
		Item item;
		ItemContent itemContent;
		Picture picture;
		SizeGroup sizeGroup;
		SizeChart sizeChart;
		SizeChartSizeGroup sizeChartSizeGroup;

		List<OutfitDto> outfitDtos = new ArrayList<OutfitDto>();
		List<ContentDto> contentDtos = new ArrayList<ContentDto>();
		ArrayList<ItemDto> items;
		ArrayList<SizeGroupDto> sizeGroups;
		//HashMap used to build OutfitDTO with nested List<Content>
		HashMap<Outfit, ArrayList<Content>> outfitToContentMap = new HashMap<Outfit, ArrayList<Content>>();
		HashMap<Content, ArrayList<ItemDto>> contentToItemMap = new HashMap<Content, ArrayList<ItemDto>>();
		HashMap<UUID, ArrayList<SizeGroupDto>> sizeChartIdToSizeGroupsMap = new HashMap<UUID, ArrayList<SizeGroupDto>>();
		//HashMap<UUID, SizeGroupDto> idToSizeGroupDtoMap = new HashMap<UUID, SizeGroupDto>();
		HashMap<UUID, OutfitDto> idToOutfitDtoMap = new HashMap<UUID, OutfitDto>();
		HashMap<UUID, Outfit> idToOutfitMap = new HashMap<UUID, Outfit>();
		HashMap<UUID, SizeChart> idToSizeChartMap = new HashMap<UUID, SizeChart>();
		//Example 553. Hibernate native query selecting entities with joined one-to-many association
		String outfitQ = "select {o.*} from outfit o where o.profile_id = :id";

		//String contentItemQ = "select {c.*}, {i.*}, {ic.*}, {p.*}, {*.sg} "+
		//	"from item i, picture p, content c size_group sg " +
		//	"join item_content ic on ic.content_id=c.id " +
		//	"where i.id = ic.item_id " +
		//	"and i.size_group_id = sg.id " +
		//	"and c.outfit_id in (:outfitIdList) " +
		//	"and p.content_id=c.id";/* +
		//	"union all select c.id, c.coverpicuri, c.outfit_id, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null " +
		//	"from content c, item_content ic " +
		//	"where c.outfit_id in (:outfitIdList) " +
		//	"and not c.id=ic.content_id";*/

		String contentItemQ = 
			"select {c.*}, {i.*}, {ic.*}, {p.*} "+
			"from item i, picture p, content c " +
			"join item_content ic on ic.content_id=c.id " +
			"where i.id = ic.item_id " +
			"and c.outfit_id in (:outfitIdList) " +
			"and p.content_id=c.id " +

			"union all select c.id, c.coverpicuri, c.outfit_id, " + 
				"null, null, null, null, null, null, null, null, null, null, " + 
				"null, null, null, null, null, null, null, null, null, null, " + 
				"null, null, null, null, null, null, null, null, null, null, " + 
				"null, null, null " + 
			"from content c, item_content ic " +
			"where c.outfit_id in (:outfitIdList) " +
			"and not c.id=ic.content_id";

		String countQ = "select count(o.id) as cnt from outfit o where o.profile_id = :id";

		String sizeChartSizeGroupQ = 
			"select {sc.*}, {sg.*}, {scsg.*} " +
			"from size_chart sc, size_group sg " +
			"join size_chart_size_group scsg on scsg.size_group_id = sg.id " +
			"where sc.id = scsg.size_chart_id " +
			"and sc.id in (:sizeChartIdList)";
 
		Long outfitCount = (Long)session.createSQLQuery(countQ)
			.addScalar("cnt", LongType.INSTANCE)
			.setParameter("id", id)
			.uniqueResult();

		//values used as a Parameter List in the cotnentItemQ SQL query
		List<Outfit> outfitTuples = session.createSQLQuery(outfitQ)
			.addEntity("o", Outfit.class)
			.setFirstResult((int)pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.setParameter("id", id, UUIDBinaryType.INSTANCE)
			.list();

		logger.debug("outfitTuples Size = ");
		logger.debug(outfitTuples.size());

		if(outfitTuples.size() == 0){
			idToOutfitDtoMap.put(null, new OutfitDto());
			idToOutfitMap.put(null, new Outfit());
		}

		else{			
			//Populates HashMap with id columns as key and outfitDto object as value.
			//Assumes rows with unique id's to be returned from query
			for(Outfit o : outfitTuples){
				//Outfit o = (Outfit)tuple[0];
				logger.debug("outfit id_text:");
				logger.debug(o.getIdText());
				idToOutfitDtoMap.put(o.getId(), new OutfitDto(o));//new OutfitDto(o.getIdText(), o.getLikes(), o.getComments(), new ArrayList<ContentDto>(5), o.getCoverpicuri()));
				idToOutfitMap.put(o.getId(), o);
			}

			logger.debug("idToOutfitDtoMap keyset = ");
			logger.debug(idToOutfitDtoMap.keySet());
			
	
			List<Object[]> contentItemTuples = session.createSQLQuery(contentItemQ)
				.addEntity("c", Content.class)
				.addEntity("i", Item.class)
				.addEntity("ic", ItemContent.class)
				.addEntity("p", Picture.class)
				//.addEntity("sg", SizeGroup.class) TODO:  uncoment once sg table is filled
				//.setParameterList("outfitIdList", Arrays.asList(UUID.fromString("3a5f73ae-a986-11e8-8336-f23c9150975d"), UUID.fromString("078e417f-a986-11e8-8336-f23c9150975d")), UUIDBinaryType.INSTANCE)
				.setParameterList("outfitIdList", idToOutfitDtoMap.keySet(), UUIDBinaryType.INSTANCE)
				.list();
						
			logger.debug("OutfitRepositoryImpl#findByProfileId: size of contentItemTuples = " + contentItemTuples.size());
			
			// Handles the case where there are no item entities associated with existing content entities.
			// Builds the contentToItemMap from idToOutfitMap instead
			if(contentItemTuples.size() == 0 && idToOutfitMap.size() > 0){
				for(Outfit o : idToOutfitMap.values()){
					for(Content c : o.getContents()){
						contentToItemMap.put(c, null);
					}
				}
			}
			else{
				for(Object[] tuple : contentItemTuples){
					//put <key, value> into HasMap
					content = (Content)tuple[0];
					item = (Item)tuple[1];
		
					if(item != null){
						//add the item id as key to the idToSizeChartMap HashMap
						idToSizeChartMap.put(item.getSizeChartId(), null);
					}
				}
			}	

			logger.debug("idToSizeChartMap keyset = ");
			logger.debug(idToSizeChartMap.keySet());

			if(idToSizeChartMap.size() == 0){
				idToSizeChartMap.put(null, new SizeChart());
			}

			else{
	
				// Query size charts on sizeChartIdList
				List<Object[]> sizeChartSizeGroupTuples = session.createSQLQuery(sizeChartSizeGroupQ)
					.addEntity("sc", SizeChart.class)
					.addEntity("sg", SizeGroup.class)
					.addEntity("scsg", SizeChartSizeGroup.class)
					.setParameterList("sizeChartIdList", idToSizeChartMap.keySet(), UUIDBinaryType.INSTANCE)
					.list();
	
				// build sizechart to List<SizeGroup> HashMap
				for(Object[] tuple :  sizeChartSizeGroupTuples){
					sizeChart = (SizeChart)tuple[0];
					sizeGroup = (SizeGroup)tuple[1];
					sizeChartSizeGroup = (SizeChartSizeGroup)tuple[2]; //data may be available from this object in the future
		
					if(!sizeChartIdToSizeGroupsMap.containsKey(sizeChart.getId())){
						sizeGroups = new ArrayList<SizeGroupDto>(5);
					}else{
						sizeGroups = sizeChartIdToSizeGroupsMap.get(sizeChart.getId());
					}
		
					if(sizeGroup != null){
						sizeGroups.add(new SizeGroupDto(sizeGroup));
					}
					sizeChartIdToSizeGroupsMap.put(sizeChart.getId(), sizeGroups);
					//idToSizeGroupDtoMap.put(sizeGroup.getId(), new SizeGroupDto(sizeGroup));
					//update idToSizeChartMap with the queried SizeChart object associated with sizeChartId key
					idToSizeChartMap.put(sizeChart.getId(), sizeChart);
				}	
			}


			// Build <Content, List<Item>> HashMap.  
			// Set the fully constructed SizeChart Object to each Item object before adding to ArrayList<Item>			
			for(Object[] tuple : contentItemTuples){
				content = (Content)tuple[0];
				item = (Item)tuple[1];
				itemContent = (ItemContent)tuple[2];
				picture = (Picture)tuple[3];
		
				if(!contentToItemMap.containsKey(content)){
					items = new ArrayList<ItemDto>(9);
				}else{
					if(picture != null) picture.setContent(content);
					items = contentToItemMap.get(content);
				}			
				if(item != null){
					ItemDto itemDto = new ItemDto(
						item.getIdText(), 
						itemContent.getPositionx(), 
						itemContent.getPositiony(), 
						item.getApparelType(), 
						//idToSizeGroupDtoMap.get(item.getSizeGroupIdText()),
						item.getSizeGroupIdText(),
						new SizeChartDto(
							(item.getSizeChartId() != null ? item.getSizeChartId().toString() : null), 
							null,//idToSizeChartMap.get(item.getSizeChartId()).getChartName(), 
							sizeChartIdToSizeGroupsMap.get(item.getSizeChartId()) 		//retreive ArrayList<SizeGroupDto> associated with the sizeChart id
						),
						null,
						item.getProduct(),
						item.getPlatform()
					);
					items.add(itemDto);
				}
				contentToItemMap.put(content, items);
			}
			
	
			for(Content c : contentToItemMap.keySet()){
				if(c.getOutfit() != null){
					logger.debug("content.outfit = " + c.getOutfit());
					logger.debug("content.outfit.id = " + c.getOutfit().getId());
					//Picture picture = c.getPicture();
					PictureDto pictureDto = c.getPicture() == null ? 
						null : 
						new PictureDto(
							c.getPicture().getIdText(), 
							c.getPicture().getThumbnailuri(), 
							c.getPicture().getSmalluri(), 
							c.getPicture().getMediumuri(),
							c.getPicture().getLargeuri(),
							c.getPicture().getOriginaluri(),
							c.getPicture().getCrop());
					
					items = contentToItemMap.get(c) == null ? new ArrayList<ItemDto>(0) : contentToItemMap.get(c);
					idToOutfitDtoMap.get(c.getOutfit().getId()).getContents().add(new ContentDto(c.getIdText(), c.getCoverpicuri(), pictureDto, items, null));
					//contentDtos.add(new ContentDto(c.getId(), c.getCoverpicuri(), contentToItemMap.get(c)));
					logger.debug("content.outfit = " + c.getOutfit());
					//outfitDtos.add				
				}
			}
		}

		Page<OutfitDto> pagedOutfitDtos = new PageImpl<OutfitDto>(new ArrayList<OutfitDto>(idToOutfitDtoMap.values()), pageable, outfitCount);
		return pagedOutfitDtos;
	}

	@Override
	public Page<OutfitDto> customFindAll(String callerName, Pageable pageable){
		Session session = entityManager.unwrap(Session.class);	

		String outfitQ = 
			"select {o.*}, {lc.*}, {pp.*} from outfit o " +
			"join like_count lc on lc.outfit_id=o.id " +
			"join picture_profile pp on pp.id = o.picture_profile_id";

		String lcpQuery =
			"select {lcp.*}, {p.*}, {lc.*} from like_count_profile lcp " +
			"join profile p on p.id=lcp.profile_id " +
			"join like_count lc on lc.id=lcp.likeCount_id " +
			"where lcp.likeCount_id in (:likeCountIds)";

		//String lcpQuery =
		//	"select {p.*}, {lcp.*} from profile p " +
		//	"join like_count_profile lcp on lcp.profile_id=p.id " +
		//	"join like_count lc on lc.id=lcp.likeCount_id " +
		//	"where lcp.likeCount_id in (:likeCountIds)";

		String countQ = "select count(o.id) as cnt from outfit o";
 		
		Long outfitCount = (Long)session.createSQLQuery(countQ)
			.addScalar("cnt", LongType.INSTANCE)
			.uniqueResult();

		List<Object[]> outfitTuples = session.createSQLQuery(outfitQ)
			.addEntity("o", Outfit.class)
			.addEntity("lc", LikeCount.class)
			.addEntity("pp", PictureProfile.class)
			.setFirstResult((int)pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.list();

		//build the likeCountId parameter list
		int size = outfitTuples.size();
		List<UUID> likeCountIds = new ArrayList<UUID>(size);
		List<Outfit> outfits =  new ArrayList<Outfit>(size);
		HashMap<UUID, PictureProfile> idToPictureProfile = new HashMap<UUID, PictureProfile>();

		for(Object[] tuple : outfitTuples){
			Outfit o = (Outfit)tuple[0];
			LikeCount lc = (LikeCount)tuple[1];
			PictureProfile pp = (PictureProfile)tuple[2];
			o.setLikeCount(lc);
			o.setPictureProfileId(pp.getId());
			outfits.add(o);
			likeCountIds.add(lc.getId());
			
			if(idToPictureProfile.get(pp.getId()) == null){
				idToPictureProfile.put(pp.getId(), pp);
			}
		}
		logger.debug("idToPictureProfile size = " + idToPictureProfile.size());
		
		Map<LikeCount, Profile> likeCountProfiles = new HashMap<LikeCount, Profile>();

		//Build the LikeCount/Profile Hashmap to use when setting outfitDto isLiked property
		if(likeCountIds.size() > 0){
			List<Object[]> lcpTuples = session.createSQLQuery(lcpQuery)
				.addEntity("lcp", LikeCountProfile.class)
				.addEntity("p", Profile.class)
				.addEntity("lc", LikeCount.class)
				.setParameterList("likeCountIds", likeCountIds, UUIDBinaryType.INSTANCE)
				.list();
	
			for(Object[] tuple : lcpTuples){
				LikeCountProfile lcp = (LikeCountProfile)tuple[0];
				Profile p = (Profile)tuple[1];
				LikeCount lc = (LikeCount)tuple[2];
				lcp.setProfile(p);
				lcp.setLikeCount(lc);
	
				likeCountProfiles.put(lc, p);
			}
		}

		List<OutfitDto> outfitDtos = outfits.stream().map(outfit -> {

			Profile profile = likeCountProfiles.get(outfit.getLikeCount());
			OutfitDto outfitDto = new OutfitDto(outfit);
			String smalluri = idToPictureProfile.get(outfit.getPictureProfileId()) != null ? idToPictureProfile.get(outfit.getPictureProfileId()).getSmalluri() : "";
			outfitDto.setProfilePictureUri(smalluri);

			if(profile == null ){
				logger.debug("profile not found in likeCountProfiles");
			}

			//check if caller matches the profile.username found in outfits likeCount entity
			if(profile != null && profile.getUsername().compareTo(callerName) == 0){
				outfitDto.setIsLiked(true);
				return outfitDto;
			}

			return outfitDto;

		}).collect(Collectors.toList());

		return new PageImpl<OutfitDto>(outfitDtos, pageable, outfitCount);
	}

	@Override
	public Page<Outfit> getAllOutfitsWithUsername(Pageable pageable){
		Outfit outfit;
		Profile profile;
		List<Outfit> outfits = new ArrayList<Outfit>();
		Session session = entityManager.unwrap(Session.class);	
		String outfitQ = "select {o.*}, {p.*} from outfit o join profile p on p.id=o.profile_id";
		String countQ = "select count(o.id) as cnt from outfit o";
 
		Long outfitCount = (Long)session.createSQLQuery(countQ)
			.addScalar("cnt", LongType.INSTANCE)
			.uniqueResult();

		List<Object[]> outfitProfileTuples = session.createSQLQuery(outfitQ)
			.addEntity("o", Outfit.class)
			.addEntity("p", Profile.class)
			.setFirstResult((int)pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.list();

		for(Object[] tuple : outfitProfileTuples){
			outfit = (Outfit)tuple[0];
			profile = (Profile)tuple[1];
			if(profile != null){
				if(profile.getUsername() != null){
					logger.debug("profile.username = " + ((Profile)tuple[1]).getUsername());
				}else{
					logger.debug("profile.username is null!");
				}
			}else{
				logger.debug("profile is null");
			}
			outfit.setUsername(profile.getUsername());
			outfits.add(outfit);
		}

		logger.debug("outfitProfileTuples Size = ");
		logger.debug(outfitProfileTuples.size());
		return new PageImpl<Outfit>(outfits, pageable, outfitCount);
	}

	@Override
	public List<Outfit> getOutfitsByIds(List<String> ids) throws Exception{

		Session session = entityManager.unwrap(Session.class);	
		List<Outfit> outfits;

		if(ids.size() <= this.MAX_PAGE_SIZE){
			
			Outfit outfit;	
			String outfitQ = "select {o.*} from outfit o where o.id_text in (:ids)";
	
			outfits = session.createSQLQuery(outfitQ)
				.addEntity("o", Outfit.class)
				.setParameterList("ids", ids, StringType.INSTANCE)
				.list();
		}else{
			throw new Exception("parameter exceeds page size limit");
		}

		return outfits;
	}
}