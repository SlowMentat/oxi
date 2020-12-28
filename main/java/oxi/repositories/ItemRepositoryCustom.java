package oxi.repositories;

import oxi.models.*;
import oxi.models.dto.ItemDto;
import oxi.models.dto.CursorDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;
import java.util.*;
import java.lang.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.blazebit.persistence.PagedArrayList;
import com.blazebit.persistence.PagedList;

public interface ItemRepositoryCustom /*extends JpaRepository<Item, UUID>*/{
	
	//@RestResource(exported=true, path="byid", rel="SearchAllById")
	//@Query(value = "SELECT * FROM item WHERE profile_id = ?1", nativeQuery = true)
	//List<Item> findByProfileId(long profileid/*, Pageable pageable*/);
	//@Query(value="select i from Item i where i.retailerAccount = ?1")
	Page<Item> getAllItemsWithRetailerAccount(UUID retailerAccountId, Pageable pageable);

	//@Query(value="")
	//Page<ItemDto> getAllItemsWithCoverpicUri(Pageable pageable);
	//Page<ItemDto> getAllItemsWithCoverpicUri(CursorDto cursor);
	List<ItemDto> getAllItemsWithCoverpicUri(CursorDto cursor) throws NoSuchMethodException; 

	//List<Item> findByProductOwner(String productOwner);
	Item findByExistingCustomItem(String udr, String uds, Integer apparel_type_id);
} 