package oxi.repositories.retailer;

import oxi.models.retailer.*;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

import org.springframework.data.repository.query.Param;

import java.util.*;
/*
import oxi.models.projection.OutfitProjection;
import oxi.models.dto.*;*/

@RepositoryRestResource(collectionResourceRel="SizeChart", path="sizeChart")
public interface SizeChartRepository extends JpaRepository<SizeChart, UUID>, SizeChartRepositoryCustom{
	

	/*@Query(value = "SELECT * FROM outfit WHERE profile_id = ?1", nativeQuery = true)
	List<Outfit> findByProfileId(Long profileid);*/

	//@RestResource(exported = false)
	//@Query(value = "SELECT * FROM outfit WHERE id = ?1", nativeQuery = true)
	///@Query("SELECT new oxi.models.dto.OutfitDto(o.Id, o.likes, o.comments, o.contents, o.coverpicuri) FROM Outfit AS o WHERE o.Id = ?1")
	
	//@Query("SELECT sc FROM SizeChart AS sc WHERE sc.id = ?1")
	//SizeChart findById(UUID Id);

	//@RestResource(exported = false)
	//@Query(value = "SELECT * FROM outfit \n#pageable\n", countQuery = "SELECT count(*) FROM outfit", nativeQuery = true)
	/*@Query(value = "SELECT * FROM outfit \n#pageable\n", 
		countQuery = "SELECT count(*) FROM outfit", 
		nativeQuery = true)*/
	///@Query(value = "SELECT new oxi.models.dto.OutfitDto(o.Id, o.likes, o.comments, c, o.coverpicuri) FROM Outfit AS o JOIN o.contents c")
	/*@Query("SELECT o FROM Outfit o WHERE o.profile.Id = ?1")
	List<OutfitProjection> findByProfileId(Long Id, Pageable pageable);*/



	/*@RestResource(exported = false)
	@Query(value = "SELECT * FROM outfit o WHERE id = ?1")
	OutfitProjection getDto(Long id);*/

	
	/*@PreAuthorize("#profileid == prinicipal.username")
	<S extends T> List<S> save(Iterable<S> entities);
	
	@preAuthorize("#profileid == prinicipal.username")
	<S extends T> S saveAndFlush(S entity);*/
} 