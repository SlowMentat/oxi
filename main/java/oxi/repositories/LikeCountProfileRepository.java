package oxi.repositories;

import oxi.models.LikeCountProfile;
import oxi.models.LikeCountProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

import org.springframework.data.repository.query.Param;

import java.util.*;

import oxi.models.dto.*;


public interface LikeCountProfileRepository extends JpaRepository<LikeCountProfile, LikeCountProfileId>, LikeCountProfileRepositoryCustom{

	//@Query("SELECT likeCount FROM LikeCount AS likeCount WHERE likeCount.outfit.id = ?1")
	//LikeCount findByOutfitId(UUID outfitId);
	
	@Query( "select lcp FROM LikeCountProfile lcp WHERE id IN :ids" )
	List<LikeCountProfile> findByIds(@Param("ids") List<LikeCountProfileId> ids);
} 