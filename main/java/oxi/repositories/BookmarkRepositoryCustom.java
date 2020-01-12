package oxi.repositories;

import oxi.models.retailer.*;
import oxi.models.dto.retailer.SizeChartDto;

import java.lang.*;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.UUID;

public interface BookmarkRepositoryCustom /*extends PagingAndSortingRepository<Outfit, Long>*/{

	//@Query(value="")  //This query is overriden in the implementation
	HashMap<String, Date> customfindIdsByUsername(String username);
}