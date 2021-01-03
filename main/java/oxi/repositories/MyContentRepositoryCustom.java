package oxi.repositories;

//import oxi.models.projection.OutfitProjection;
import oxi.models.dto.ContentDto;
import oxi.models.dto.ContentWithOutfitDto;
import oxi.models.dto.CursorDto;
import oxi.models.*;

import java.lang.NoSuchMethodException;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MyContentRepositoryCustom /*extends PagingAndSortingRepository<Outfit, Long>*/{
	@Query(value="")
	List<ContentDto> findByOutfitId(UUID id);

	//Page<ContentWithOutfitDto> getContentWithOutfitByItemId(UUID itemId, Pageable pageable);
	List<ContentWithOutfitDto> getContentWithOutfitByItemId(UUID itemId, CursorDto cursor) throws NoSuchMethodException;

	List<Content> getContentsByIds(List<String> ids);

	//@Query("delete from content c where c.id_text in ?1")
	//void deleteByIds(List<String> ids);
}