package oxi.repositories.es;

import oxi.models.dto.es.RetailerEsDto;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.*;
import org.springframework.stereotype.Repository;


@Repository
public interface RetailerEsRepository extends ElasticsearchRepository<RetailerEsDto, String>{
	//@Query()
	Page<RetailerEsDto> findByName(String name, Pageable pageable);
}
