package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import java.util.UUID;

//@RepositoryRestResource(collectionResourceRel="PictureProfile", path="picture")
public interface PictureProfileRepository extends JpaRepository<PictureProfile, UUID>{

	PictureProfile findByOriginaluri(String originaluri);
} 