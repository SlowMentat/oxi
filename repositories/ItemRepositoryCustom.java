package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;
import java.util.*;
import java.lang.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom /*extends JpaRepository<Item, UUID>*/{
	
	//@RestResource(exported=true, path="byid", rel="SearchAllById")
	//@Query(value = "SELECT * FROM item WHERE profile_id = ?1", nativeQuery = true)
	//List<Item> findByProfileId(long profileid/*, Pageable pageable*/);
	@Query(value="select i from Item i where i.retailerAccount = ?1")
	Page<Item> getAllItemsWithRetailerAccount(UUID retailerAccountId, Pageable pageable);

	//List<Item> findByProductOwner(String productOwner);
} 