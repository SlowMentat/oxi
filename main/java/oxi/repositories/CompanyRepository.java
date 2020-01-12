package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;

import java.util.UUID;

//@RepositoryRestResource(collectionResourceRel="Company", path="company")
public interface CompanyRepository extends JpaRepository<Company, UUID>{
	@RestResource(exported = false)
	@Query(value = "SELECT * FROM company WHERE email = ?1", nativeQuery = true)
	Company findByEmail(String email);

	@RestResource(exported = false)
	@Query(value = "SELECT * FROM company WHERE company_name = ?1", nativeQuery = true)
	Company findByCompanyName(String companyName);
} 