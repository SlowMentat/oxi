package oxi.repositories;

import oxi.models.*;
import oxi.models.projection.ContentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

@RepositoryRestResource(collectionResourceRel="Content", path="content")
public interface ContentRepository extends JpaRepository<Content, Long>{
	/*@RestResource(exported = false)
	@Query(value = "SELECT * FROM content WHERE id = ?1", nativeQuery = true)
	ContentProjection getOneDto(long id);*/
	//@Query("SELECT new oxi.models.dto.ContentDto(c.Id, c.coverpicuri, c.picture, c.items) FROM Content as c WHERE c.Id = ?1")
	/*@Query("SELECT c FROM Content AS c WHERE c.Id = ?1")
	ContentProjection findById(Long Id);*/

	//@RestResource(exported = false)
	//@Query(value = "SELECT * FROM outfit \n#pageable\n", countQuery = "SELECT count(*) FROM outfit", nativeQuery = true)
	/*@Query(value = "SELECT * FROM outfit \n#pageable\n", 
		countQuery = "SELECT count(*) FROM outfit", 
		nativeQuery = true)*/
	//@Query(value = "SELECT new oxi.models.dto.ContentDto(c.Id, c.coverpicuri, c.picture, c.items) FROM Content AS c")
	/*@Query("SELECT c FROM Content c WHERE c.outfit.Id = ?1")
	Page<ContentProjection> findByOutfitId(Long Id, Pageable pageable);*/
} 