package oxi.repositories.retailer;

import oxi.models.retailer.*;
import oxi.models.dto.retailer.SizeChartDto;

import java.lang.*;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SizeChartRepositoryCustom /*extends PagingAndSortingRepository<Outfit, Long>*/{
	@Query(value="")
	Page<SizeChartDto> findByRetailerAccountId(UUID id, Pageable pageable);

	@Query(value="")
	Page<SizeChart> findAll(Pageable pageable);
	//@Query(value="")
	//Outfit persist(Outfit outfit);
	@Query(value="SELECT sc FROM SizeChart sc")
	Page<SizeChart> getAllSizeChartsWithCompanyName(Pageable pageable);
}