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

import java.lang.Exception;
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

public class LikeCountRepositoryImpl implements LikeCountRepositoryCustom {	
	@Transient
	private static final Logger logger = LogManager.getLogger(OutfitRepositoryImpl.class);
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	/*private static String buildPagedQuery(String query, Pageable page){

	}*/
	@Override
	public LikeCount customFindByOutfitId(UUID outfitId) throws Exception{

		Session session = entityManager.unwrap(Session.class);
		Content content;
		ItemContent itemContent;
		List<ContentDto> contentDtos;
		List<OutfitDto> outfitDtos;
		Set<UUID> outfitIds = new HashSet<UUID>();
		//HashMap used to build OutfitDTO with nested List<Content>
		HashMap<Content, ArrayList<ItemDto>> contentToItemMap = new HashMap<Content, ArrayList<ItemDto>>();

		String contentQ = "select {lc.*} from like_count lc where lc.outfit_id = :outfitId";
		//String outfitQ = "select {o.*} from outfit o where o.id in (:outfitIds)";

		List<LikeCount> likeCounts = session.createSQLQuery(contentQ)
			.addEntity("lc", LikeCount.class)
			.setParameter("outfitId", outfitId, UUIDBinaryType.INSTANCE)
			.list();

		if(likeCounts.size() > 1){
			throw new Exception("expected 1 LikeCount entity, but got Multiple LikeCount entities");
		}

		return likeCounts.get(0);
	}
}