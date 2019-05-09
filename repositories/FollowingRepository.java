package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Example;

import org.springframework.data.repository.query.Param;

import java.util.*;

//import oxi.models.projection.FollowProjection;
import oxi.models.dto.*;

//@RepositoryRestResource(collectionResourceRel="Following", path="follow")
public interface FollowingRepository extends JpaRepository<Following, FollowingId>/*, FollowingRepositoryCustom*/{
	/*@Query("SELECT f FROM Following AS f WHERE f.id = ?1")
	Following findById(UUID Id);

	@Query("SELECT f FROM Following AS f WHERE f.followerUsername = ?1 AND f.followedUsername = ?2")
	Following findByFollowerUsernameAndFollowedUsername(String followerUsername, String followedUsername);

	@Query("DELETE FROM Following AS f WHERE f.followerUsername = ?1 AND f.followedUsername = ?2")
	void deleteByFollowerUsernameAndFollowedUsername(String followerUsername, String followedUsername);*/
 
	
	//List<Following> findAll(Example<Following> following);

	//Following deleteById(FollowingId id);
}