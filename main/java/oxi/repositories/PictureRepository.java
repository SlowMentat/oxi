package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel="Picture", path="picture")
public interface PictureRepository extends JpaRepository<Picture, UUID>{
	//@Query("SELECT p FROM Picture AS p WHERE p.id = ?1")
	//Picture findById(UUID Id);

	Picture findByOriginaluri(String originaluri);
} 