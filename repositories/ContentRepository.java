package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.*;

@RepositoryRestResource(collectionResourceRel="Content", path="content")
public interface ContentRepository extends JpaRepository<Content, Long>{
	
} 