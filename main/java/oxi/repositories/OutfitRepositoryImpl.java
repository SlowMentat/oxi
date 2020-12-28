package oxi.repositories;

import oxi.models.*;
import oxi.models.dto.OutfitDto;
import oxi.models.dto.ContentDto;
import oxi.models.dto.CursorDto;
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
import javax.xml.bind.DatatypeConverter;
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
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PagedArrayList;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
//import com.blazebit.persistence.deltaspike.data.KeysetAwarePage;
import com.blazebit.persistence.spring.data.base.query.KeysetAwarePageImpl;
//import org.apache.spark.sql.types.LongType;

//import org.springframework.data.domain.Page;

public class OutfitRepositoryImpl implements OutfitRepositoryCustom {

	private int MAX_PAGE_SIZE = 50;

	@Transient
	private static final Logger logger = LogManager.getLogger(OutfitRepositoryImpl.class);
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	CriteriaBuilderFactory cbf;

	/*private static String buildPagedQuery(String query, Pageable page){

	}*/

	private HashMap<UUID, OutfitDto> getCompleteOutfit(List<Object[]> outfitPPTuples, long outfitCount){
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

		// THIS IS STUPID
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
				"null, null, null, null, null, null " + 
			"from content c, item_content ic " +
			"where c.outfit_id in (:outfitIdList) " +
			"and not c.id=ic.content_id";

		String sizeChartSizeGroupQ = 
			"select {sc.*}, {sg.*}, {scsg.*} " +
			"from size_chart sc, size_group sg " +
			"join size_chart_size_group scsg on scsg.size_group_id = sg.id " +
			"where sc.id = scsg.size_chart_id " +
			"and sc.id in (:sizeChartIdList)";

		if(outfitPPTuples.size() == 0){
			idToOutfitDtoMap.put(null, new OutfitDto());
			idToOutfitMap.put(null, new Outfit());
		}

		else{
			//Populates HashMap with id columns as key and outfitDto object as value.
			//Assumes rows with unique id's to be returned from query
			for(Object[] tuple : outfitPPTuples){
				Outfit o = (Outfit)tuple[0];
				PictureProfile pp = (PictureProfile)tuple[1];
				//Outfit o = (Outfit)tuple[0];
				logger.debug("outfit id_text:");
				logger.debug(o.getIdText());

				OutfitDto oDto = new OutfitDto(o);
				oDto.setProfilePictureUri(pp.getSmalluri());

				idToOutfitDtoMap.put(o.getId(), oDto);//new OutfitDto(o.getIdText(), o.getLikes(), o.getComments(), new ArrayList<ContentDto>(5), o.getCoverpicuri()));
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
					idToOutfitDtoMap.get(c.getOutfit().getId()).getContents().add(new ContentDto(c.getIdText(), c.getCoverpicuri(), pictureDto, items, null, c.getCreatedOn().toString()));
					//idToOutfitDtoMap.get(c.getOutfit().getId()).getContents().add(new ContentDto(c));
				}
			}
		}

		return idToOutfitDtoMap;
	}

	public OutfitDto getOutfitById(UUID id){
		Session session = entityManager.unwrap(Session.class);
		String outfitQ = "select {o.*}, {pp.*} from outfit o join picture_profile pp on pp.id = o.picture_profile_id where o.id = :id";

		//values used as a Parameter List in the cotnentItemQ SQL query
		List<Object[]> outfitPPTuples = session.createSQLQuery(outfitQ)
			.addEntity("o", Outfit.class)
			.addEntity("pp", PictureProfile.class)
			.setParameter("id", id, UUIDBinaryType.INSTANCE)
			.list();

		OutfitDto outfitDto = getCompleteOutfit(outfitPPTuples, 1).get(id);
		// Temporary workaround.  If needed set isLiked based on like_count_profile table
		outfitDto.setIsLiked(null);

		return outfitDto;
	}

	@Override
	public List<OutfitDto> findByProfileId(String callerName, CursorDto cursor, UUID id) throws NoSuchMethodException{
		return customFindAll(callerName, cursor, id);
	}	

	/*public Page<OutfitDto> findByProfileId(UUID id, Pageable pageable){
	//public Page<OutfitDto> findByProfileId(UUID id, CursorDto cursor){
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
	}*/

	/**
	*Queries database for all outfits
	*@param callerName username of the caller.  This is used to determine if an outfits likeCount 
	*@param CursorDto Object with properties contianing information needed for a keyset query
	**/
	@Override
	//public Page<OutfitDto> customFindAll(String callerName, Pageable pageable){
	public List<OutfitDto> customFindAll(String callerName, CursorDto cursor, UUID profileId) throws NoSuchMethodException{
		Session session = entityManager.unwrap(Session.class);
		List<UUID> likeCountIds = null;
		Map<String, Profile> lcIdToProfile = new HashMap<String, Profile>();
		Date date = DatatypeConverter.parseDateTime(cursor.getDate()).getTime();

		CriteriaBuilder<OutfitDto> cb = cbf.create(entityManager, OutfitDto.class);

		// Query db for paged list of outfits
		cb.from(Outfit.class, "o");

		// Condition query on profile id
		if(profileId != null){
			cb.where("o.profile.id").eqExpression(":profile_id")
				.setParameter("profile_id", profileId);
		}

		cb.leftJoinOn(LikeCount.class, "lc")
				.on("o.id").eqExpression("lc.outfit.id")
			.end()
			.leftJoinOn(PictureProfile.class, "pp")
				.on("o.pictureProfileId").eqExpression("pp.id")
			.end();
		
		// Add keyset pagination
		PagedList<OutfitDto> outfitPage = cb.selectNew(OutfitDto.class.getConstructor(Outfit.class, PictureProfile.class))
				.with("o")
				.with("pp")
			.end()
			.orderByDesc("o.id")
			//.orderByDesc("o.createdOn")
			.page((cursor.getFirstId() == null || cursor.getLastId() == null ? null : cursor), cursor.getNextFirstResult(), cursor.getMaxResults())
			.getResultList();

		// Extract the likeCount ids outfitPage
		likeCountIds = outfitPage.stream().map(outfitDto -> UUID.fromString(outfitDto.getLikeCount().getId())).collect(Collectors.toList());

		// Build the LikeCount-to-Profile Hashmap used when setting outfitDto isLiked property
		if(likeCountIds.size() > 0){
			String lcpQuery =
				"select {lcp.*}, {p.*}, {lc.*} from like_count_profile lcp " +
				"join profile p on p.id=lcp.profile_id " +
				"join like_count lc on lc.id=lcp.likeCount_id " +
				"where lcp.likeCount_id in (:likeCountIds)";

			List<Object[]> lcpTuples = session.createSQLQuery(lcpQuery)
				.addEntity("lcp", LikeCountProfile.class)
				.addEntity("p", Profile.class)
				.addEntity("lc", LikeCount.class)
				.setParameterList("likeCountIds", likeCountIds, UUIDBinaryType.INSTANCE)
				.list();
	
			lcpTuples.stream().forEach(tuple -> {
				LikeCountProfile lcp = (LikeCountProfile)tuple[0];
				Profile p = (Profile)tuple[1];
				LikeCount lc = (LikeCount)tuple[2];
				lcp.setProfile(p);
				lcp.setLikeCount(lc);

				logger.debug("OutfitRepositoryImpl#customFindAll: profile cast from tuple = " + p.toString());
				logger.debug("OutfitRepositoryImpl#customFindAll: likeCount cast from tuple = " + lc.toString());

				lcIdToProfile.put(lc.getIdText(), p);
			});
		}

		logger.debug("lcIdToProfile HashMap ========================================= start");

		lcIdToProfile.keySet().stream().forEach(key -> {
			logger.debug("OutfitRepositoryImpl#customFindAll: lcIdToProfile key = " + key);
			logger.debug("OutfitRepositoryImpl#customFindAll: lcIdToProfile value = " + (lcIdToProfile.get(key) == null ? "null" : lcIdToProfile.get(key).toString()));
		});

		logger.debug("lcIdToProfile HashMap ========================================= end");

		outfitPage.stream().forEach(outfitDto -> {
			logger.debug("OutfitRepositoryImpl#customFindAll: lcIdToProfile size = " + lcIdToProfile.size());
			logger.debug("OutfitRepositoryImpl#customFindAll: outfitDto.getLikeCount().getId() = " + outfitDto.getLikeCount().getId());
			Profile profile = lcIdToProfile.get(outfitDto.getLikeCount().getId().toUpperCase());

			if(profile == null ){
				logger.debug("profile not found in lcIdToProfile");
			}

			// Check if caller username matches the profile.username found in outfits likeCount entity
			if(profile != null && profile.getUsername().compareTo(callerName) == 0){
				outfitDto.setIsLiked(true);
			}
		});

		return outfitPage;
	}

	/**
	*Queries database for all outfits that contian a username
	*@param callerName String representing the username of the caller
	*@param cursor CursorDto object used for a keyset query
	**/
	@Override
	public Page<Outfit> getAllOutfitsWithUsername(Pageable pageable){
	//public List<Outfit> getAllOutfitsWithUsername(CursorDto cursor){
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
				}
				else{
					logger.debug("profile.username is null!");
				}
			}
			else{
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