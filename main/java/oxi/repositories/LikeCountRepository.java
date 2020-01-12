package oxi.repositories;

import oxi.models.LikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

import org.springframework.data.repository.query.Param;

import java.util.*;

import oxi.models.dto.*;


public interface LikeCountRepository extends JpaRepository<LikeCount, UUID>, LikeCountRepositoryCustom{

	//@Query("SELECT likeCount FROM LikeCount AS likeCount WHERE likeCount.outfit.id = ?1")
	//LikeCount findByOutfitId(UUID outfitId);
} 