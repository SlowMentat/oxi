package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel="Profile", path="userProfile")
public interface ProfileRepository extends JpaRepository<Profile, UUID>{
	@Query(value = "SELECT * FROM profile p JOIN user u on u.id = p.user_id WHERE u.username = ?1", nativeQuery = true)
	Profile findByUsername(String username);

	@Query(value = "SELECT * FROM profile p JOIN outfit o on o.profile_id = p.id WHERE o.id = ?1", nativeQuery = true)
	Profile findByOutfitId(UUID outfitId);
}