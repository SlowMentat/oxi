package oxi.repositories;

import oxi.models.*;
import oxi.models.dto.OutfitDto;
import oxi.models.dto.ContentDto;
import oxi.models.dto.ContentWithOutfitDto;
import oxi.models.dto.ItemDto;
import oxi.models.dto.PictureDto;
import oxi.models.projection.OutfitProjection;
import oxi.models.projection.ContentProjection;
import oxi.repositories.MyContentRepositoryCustom;

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
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.type.LongType;
import org.hibernate.type.UUIDBinaryType;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
//import org.apache.spark.sql.types.LongType;

//import org.springframework.data.domain.Page;

public class MyContentRepositoryImpl implements MyContentRepositoryCustom {	
	@Transient
	private static final Logger logger = LogManager.getLogger(OutfitRepositoryImpl.class);
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	/*private static String buildPagedQuery(String query, Pageable page){

	}*/

	public List<ContentDto> findByOutfitId(UUID id){
		Session session = entityManager.unwrap(Session.class);
		Content content;
		Item item;
		ItemContent itemContent;
		List<ContentDto> contentDtos = new ArrayList<ContentDto>();
		ArrayList<ItemDto> itemDtos;
		//HashMap used to build OutfitDTO with nested List<Content>
		HashMap<Content, ArrayList<ItemDto>> contentToItemMap = new HashMap<Content, ArrayList<ItemDto>>();
		//Example 553. Hibernate native query selecting entities with joined one-to-many association
		String contentQ = "select {c.*}, {i.*} " +
			"from item i, content c " +
			"join item_content ic on c.id = ic.content_id "+
			"where c.outfit_id = :outfitId and i.id = ic.item_id "+
			"union all select c.*, null, null, null, null, null, null, null, null " +
			"from content c, item_content ic " +
			"where c.outfit_id = :outfitId and not c.id = ic.content_id";

		List<Object[]> contentItemTuples = session.createSQLQuery(contentQ)
			.addEntity("c", Content.class)
			.addEntity("i", Item.class)
			.addEntity("ic", ItemContent.class)
			.setParameter("outfitId", id)
			.list();
		for(Object[] tuple : contentItemTuples){
			//put <key, value> into HasMap
			content = (Content)tuple[0];
			item = (Item)tuple[1];
			itemContent = (ItemContent)tuple[2];
			logger.debug("(Content)tuple[0] = " + content);
			logger.debug("(Item)tuple[1] = " + item);
			if(!contentToItemMap.containsKey(content)){
				itemDtos = new ArrayList<ItemDto>(9);
			}else{
				itemDtos = contentToItemMap.get(content);
			}
			ItemDto iDto = new ItemDto(item);
			iDto.setPositionx(itemContent.getPositionx());
			iDto.setPositiony(itemContent.getPositiony());
			if(item != null) itemDtos.add(iDto/*new ItemDto(
				item.getIdText(), 
				itemContent.getPositionx(), 
				itemContent.getPositiony(), 
				item.getApparelType(), 
				item.getSizeGroupId(), 
				item.getRetailerText(), 
				item.getBrandText())*/);
			contentToItemMap.put(content, itemDtos);
		}
		Picture picture = null;
		for(Content c : contentToItemMap.keySet()){
			picture = c.getPicture();
			PictureDto pictureDto = picture == null ? null : new PictureDto(
				picture.getIdText(), 
				picture.getThumbnailuri(), 
				picture.getSmalluri(), 
				picture.getLargeuri());

			contentDtos.add(new ContentDto(
				c.getIdText(), 
				c.getCoverpicuri(), 
				pictureDto, 
				contentToItemMap.get(c),
				null));
		}

		return contentDtos;//outfitDtos;
	}

	@Override
	public Page<ContentWithOutfitDto> getContentWithOutfitByItemId(UUID itemId, Pageable pageable){
	//public ContentWithOutfitDto getContentWithOutfitByItemId(UUID itemId, Pageable pageable){
		Session session = entityManager.unwrap(Session.class);
		Content content;
		ItemContent itemContent;
		List<ContentDto> contentDtos;
		List<OutfitDto> outfitDtos;
		List<ContentWithOutfitDto> contentWithOutfitDtos;
		Set<UUID> outfitIds = new HashSet<UUID>();
		//HashMap used to build OutfitDTO with nested List<Content>
		HashMap<Content, ArrayList<ItemDto>> contentToItemMap = new HashMap<Content, ArrayList<ItemDto>>();

		String contentQ = "select {c.*}, {ic.*} from content c, item_content ic where c.id = ic.content_id and ic.item_id = :itemId";
		//String outfitQ = "select {o.*} from outfit o where o.id in (:outfitIds)";

		List<Object[]> contentItemTuples = session.createSQLQuery(contentQ)
			.addEntity("c", Content.class)
			.addEntity("ic", ItemContent.class)
			.setFirstResult((int)pageable.getOffset())
			.setMaxResults(pageable.getPageSize())
			.setParameter("itemId", itemId, UUIDBinaryType.INSTANCE)
			.list();

		int resultCount = contentItemTuples.size();

		contentWithOutfitDtos = contentItemTuples.stream().map(tuple -> new ContentWithOutfitDto( 
			(Content)tuple[0],
			((Content)tuple[0]).getOutfit(),
			((Content)tuple[0]).getOutfit().getLikeCount()
		)).collect(Collectors.toList());
		
		Page<ContentWithOutfitDto> pagedResult = new PageImpl<ContentWithOutfitDto>(
			contentWithOutfitDtos, 
			pageable, 
			resultCount 
		);

		return pagedResult;
	}
}