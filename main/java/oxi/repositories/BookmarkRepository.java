package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

import org.springframework.data.repository.query.Param;

import java.util.*;

////import oxi.models.projection.OutfitProjection;
import oxi.models.dto.*;


//@RepositoryRestResource(collectionResourceRel="Bookmark", path="bookmark")
public interface BookmarkRepository extends JpaRepository<Bookmark, UUID>, BookmarkRepositoryCustom{
	//@Query("SELECT b FROM Bookmark AS b WHERE b.id = ?1")
	//Bookmark findById(UUID Id);

	@Query("SELECT b FROM Bookmark AS b WHERE b.itemId = ?1")
	Bookmark findByItemId(UUID Id);

	@Query("SELECT b FROM Bookmark AS b WHERE b.username = ?1 AND b.itemId = ?2")
	Bookmark findByUsernameAndItemId(String username, UUID id);

	@Query("SELECT b FROM Bookmark AS b WHERE b.itemId in (:ids)")
	List<Bookmark> findByItemIds(@Param("ids") List<UUID> ids);

	@Query("SELECT b FROM Bookmark AS b WHERE b.username = :username AND b.itemId in (:ids)")
	List<Bookmark> findByUsernameAndItemIds(@Param("username") String username, @Param("ids") List<UUID> ids);


	@Query("SELECT b.itemIdText FROM Bookmark AS b WHERE b.username = :username")
	List<String> findIdsByUsername(@Param("username") String username);

	HashMap<String, Date> customfindIdsByUsername(@Param("username") String username);

	@Transactional
	@Modifying
	@Query("DELETE FROM Bookmark AS b WHERE b.username = ?1 AND b.itemId = ?2")
	void deleteByUsernameAndItemId(String username, UUID itemId);

	@Query("DELETE FROM Bookmark AS b WHERE b.username = :username AND b.itemId in (:itemIds)")
	void deleteByUsernameAndItemIds(@Param("username") String username, @Param("itemIds") List<UUID> itemIds);
} 