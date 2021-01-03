package oxi.repositories;

import oxi.models.LikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

import org.springframework.data.repository.query.Param;

import java.util.*;

////import oxi.models.projection.LikeProjection;
import oxi.models.dto.*;

//@RepositoryRestResource(collectionResourceRel="Like", path="like")
public interface LikeRepository extends JpaRepository<LikeCount, UUID>{
	//@Query("SELECT l FROM Like AS l WHERE l.id = ?1")
	//Like findById(UUID Id);

	//@Query("SELECT likCount FROM LikeCount AS likeCount WHERE likeCount.// = ?1")
	//LikeCount findByOutfitId(UUID outfitId);
//
	//@Query("DELETE FROM Like AS l WHERE l.username = ?1 AND l.outfitId = ?2")
	//void deleteByUsernameAndOutfitId(String username, UUID outfitId);
} 