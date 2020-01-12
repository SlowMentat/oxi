package oxi.repositories.es;

import oxi.models.dto.es.SizeLabelEsDto;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.*;
import org.springframework.stereotype.Repository;


@Repository
public interface SizeLabelEsRepository extends ElasticsearchRepository<SizeLabelEsDto, Integer>{
	//@Query()
	Page<SizeLabelEsDto> findByName(String name, Pageable pageable);
}
