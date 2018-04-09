package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;

@RepositoryRestResource(collectionResourceRel="Profile", path="profile")
public interface ProfileRepository extends JpaRepository<Profile, Long>{
	@Query(value = "SELECT profile_id FROM user WHERE username = ?1", nativeQuery = true)
	long findByFirstname(String firstname);	
} 