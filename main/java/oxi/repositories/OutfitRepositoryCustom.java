package oxi.repositories;

//import oxi.models.projection.OutfitProjection;
import oxi.models.dto.OutfitDto;
import oxi.models.dto.CursorDto;
import oxi.models.Outfit;
import oxi.models.*;

import java.lang.*;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutfitRepositoryCustom /*extends PagingAndSortingRepository<Outfit, Long>*/{
	//@Query(value="select new oxi.models.dto.OutfitDto(o) from Outfit o")
	//Page<OutfitDto> findByProfileId(UUID id, Pageable pageable);

	List<OutfitDto> findByProfileId(String callerName, CursorDto cursor, UUID id) throws NoSuchMethodException;

	//@Query(value="")
	List<OutfitDto> customFindAll(String callerName, CursorDto cursor, UUID profileId) throws NoSuchMethodException;
	//@Query(value="")
	//Outfit persist(Outfit outfit);
	@Query(value="SELECT o FROM Outfit o")
	Page<Outfit> getAllOutfitsWithUsername(Pageable pageable);

	//@Query(value="")
	List<Outfit> getOutfitsByIds(List<String> ids) throws Exception;

	OutfitDto getOutfitById(UUID id);
}