package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

import org.springframework.data.repository.query.Param;

import java.util.*;

import oxi.models.projection.OutfitProjection;

@RepositoryRestResource(collectionResourceRel="Outfit", path="outfit")
public interface OutfitRepository extends JpaRepository<Outfit, Long>{
	

	@Query(value = "SELECT * FROM outfit WHERE profile_id = ?1", nativeQuery = true)
	List<Outfit> findByProfileId(long profileid);

	/*@RestResource(exported = false)
	@Query(value = "SELECT * FROM outfit WHERE id = ?1", nativeQuery = true)
	OutfitProjection getOneDto(long id);

	@RestResource(exported = false)
	//@Query(value = "SELECT o as outfit, WHERE id = ?1", nativeQuery = true)
	Page<OutfitProjection> findAll(Pageable pageable);*/



	/*@RestResource(exported = false)
	@Query(value = "SELECT * FROM outfit o WHERE id = ?1")
	OutfitProjection getDto(Long id);*/

	
	/*@PreAuthorize("#profileid == prinicipal.username")
	<S extends T> List<S> save(Iterable<S> entities);
	
	@preAuthorize("#profileid == prinicipal.username")
	<S extends T> S saveAndFlush(S entity);*/
} 