package oxi.repositories;

import oxi.models.projection.OutfitProjection;
import oxi.models.dto.ContentDto;
import oxi.models.*;

import java.lang.*;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContentRepositoryCustom /*extends PagingAndSortingRepository<Outfit, Long>*/{
	@Query(value="")
	List<ContentDto> findByOutfitId(UUID id);
}