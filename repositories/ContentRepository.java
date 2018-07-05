package oxi.repositories;

import oxi.models.*;
import oxi.models.projection.ContentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;

@RepositoryRestResource(collectionResourceRel="Content", path="content")
public interface ContentRepository extends JpaRepository<Content, Long>{
	/*@RestResource(exported = false)
	@Query(value = "SELECT * FROM content WHERE id = ?1", nativeQuery = true)
	ContentProjection getOneDto(long id);*/
} 