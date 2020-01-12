package oxi.repositories.retailer;

import oxi.models.retailer.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel="RetailerAccount", path="retailerAccount")
public interface RetailerAccountRepository extends JpaRepository<RetailerAccount, UUID>{
	@RestResource(exported = false)
	@Query(value = "SELECT * FROM retailer_account WHERE work_email = ?1", nativeQuery = true)
	RetailerAccount findByWorkEmail(String workEmail);

	@RestResource(exported = false)
	@Query(value = "SELECT * FROM retailer_account WHERE company_name = ?1", nativeQuery = true)
	RetailerAccount findByCompanyName(String companyName);
} 