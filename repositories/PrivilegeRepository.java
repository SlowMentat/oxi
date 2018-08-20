package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;


//@RepositoryRestResource(collectionResourceRel="User", path="user")
public interface PrivilegeRepository extends JpaRepository<Privilege, Long>{
	@RestResource(exported = false)
	@Query(value = "SELECT * FROM privilege WHERE name = ?1", nativeQuery = true)
	Privilege findByName(String name);
} 