package oxi.repositories.es;

import oxi.models.dto.es.ApparelTypeEsDto;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.*;
import org.springframework.stereotype.Repository;


@Repository
public interface ApparelTypeEsRepository extends ElasticsearchRepository<ApparelTypeEsDto, String>{
	//@Query()
	Page<ApparelTypeEsDto> findByName(String name, Pageable pageable);

	/*@Query("{\"bool\": {\"must\": [{\"match\": {\"apparel_type.name\": \"?0\"}}]}}")
	Page<ApparelTypeEsDto> findByNameUsingCustomQuery(String name, Pageable pageable);*/
}
