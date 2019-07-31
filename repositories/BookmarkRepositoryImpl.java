package oxi.repositories;

import oxi.models.retailer.*;
import oxi.models.Item;
import oxi.models.Bookmark;
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
//import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

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
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.type.LongType;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
//import org.apache.spark.sql.types.LongType;

//import org.springframework.data.domain.Page;

public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {	
	@Transient
	private static final Logger logger = LogManager.getLogger(BookmarkRepositoryImpl.class);
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public HashMap<String, Date> customfindIdsByUsername(String username){		
		Session session = entityManager.unwrap(Session.class);	
		HashMap<String, Date> itemIdToDate = new HashMap<String, Date>();
		Bookmark bookmark;
		String bookmarkQ = "select {b.*} from bookmark b where b.username = :username";

		List<Bookmark> bookmarkTuples = session.createSQLQuery(bookmarkQ)
			.addEntity("b", Bookmark.class)
			.setParameter("username", username, StringType.INSTANCE)
			.list();

		for(Bookmark tuple : bookmarkTuples){			
			bookmark = (Bookmark)tuple;

			if(bookmark != null){
				itemIdToDate.put(bookmark.getItemIdText(), bookmark.getCreatedOn());
			}
		}

		return itemIdToDate;
	}
}