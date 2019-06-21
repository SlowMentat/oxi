package oxi.repositories.es;

import oxi.models.dto.es.ItemEsDto;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.*;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemEsRepository extends ElasticsearchRepository<ItemEsDto, String>{
	//@Query()
	Page<ItemEsDto> findByRetailer(String retailer, Pageable pageable);
}
