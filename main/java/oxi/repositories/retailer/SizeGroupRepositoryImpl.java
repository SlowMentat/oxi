package oxi.repositories.retailer;

import oxi.models.retailer.*;
import oxi.models.Item;
import oxi.models.SizeLabel;
import oxi.models.dto.retailer.*;
import oxi.models.dto.ItemDto;

import org.hibernate.transform.*;
import org.hibernate.SQLQuery;
import org.hibernate.*;
import org.hibernate.type.UUIDBinaryType;
import org.hibernate.type.IntegerType;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.*;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.Transient;

import java.lang.Long;
import static java.lang.Math.toIntExact;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.type.LongType;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class SizeGroupRepositoryImpl implements SizeGroupRepositoryCustom {	
	@Transient
	private static final Logger logger = LogManager.getLogger(SizeGroupRepositoryImpl.class);
	
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	// ***Depricated***	
	@Override
	public HashMap<UUID, SizeGroupDto> customFindByIds(HashSet<UUID> itemIds){

		Session session = entityManager.unwrap(Session.class);	
		HashMap<UUID, SizeGroupDto> itemIdToSizeGroupDto = new HashMap<UUID, SizeGroupDto>();

		//Example 553. Hibernate native query selecting entities with joined one-to-many association
		String sizeGroupQ = "select {sg.*}, {sl.*}, {i.*} from size_group sg size_label sl item i where sl.id = sg.size_label_id sg.id in (:itemIds)";

		List<Object[]> sizeGroupTuples = session.createSQLQuery(sizeGroupQ)
			.addEntity("sg", SizeGroup.class)
			.addEntity("sl", SizeLabel.class)
			.addEntity("i", Item.class)
			.setParameterList("itemIds", itemIds, UUIDBinaryType.INSTANCE)
			.list();

		for(Object[] tuple : sizeGroupTuples){
			SizeGroup sg = (SizeGroup)tuple[0];
			SizeLabel sl = (SizeLabel)tuple[1];
			Item i = (Item)tuple[2];

			if(sg != null && i != null && sl != null){
				SizeGroupDto sizeGroupDto = new SizeGroupDto(sg);
				sizeGroupDto.setSizeLabel(sl.getName());
				itemIdToSizeGroupDto.put(i.getId(), sizeGroupDto);
			}
		}

		return itemIdToSizeGroupDto;		
	}
}