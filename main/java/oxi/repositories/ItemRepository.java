package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;
import java.util.*;

import org.springframework.data.repository.query.Param;

//@RepositoryRestResource(collectionResourceRel="Item", path="item")
public interface ItemRepository extends JpaRepository<Item, UUID>, ItemRepositoryCustom{
	
	@RestResource(exported=true, path="byid", rel="SearchAllById")
	@Query(value = "SELECT * FROM item WHERE profile_id = ?1", nativeQuery = true)
	List<Item> findByProfileId(long profileid/*, Pageable pageable*/);

	List<Item> findByIdIn(List<UUID> ids);

	//Item findById(UUID id); 

	//List<Item> findByProductOwner(String productOwner);

	@Query(value="SELECT i FROM Item i where i.picture.id in :pictureIds")
	List<Item> findByPictureIds(@Param("pictureIds") List<UUID> pictureIds);
} 