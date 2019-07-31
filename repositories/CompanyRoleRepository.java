package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;

//@RepositoryRestResource(collectionResourceRel="User", path="user")
public interface CompanyRoleRepository extends JpaRepository<CompanyRole, Long>{
	@RestResource(exported = false)
	@Query(value = "SELECT * FROM company_role WHERE name = ?1", nativeQuery = true)
	CompanyRole findByName(String name);
} 