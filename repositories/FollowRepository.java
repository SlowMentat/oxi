package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

import org.springframework.data.repository.query.Param;

import java.util.*;

//import oxi.models.projection.FollowProjection;
import oxi.models.dto.*;

//@RepositoryRestResource(collectionResourceRel="Follow", path="follow")
public interface FollowRepository extends JpaRepository<Follow, UUID>{
	@Query("SELECT f FROM Follow AS f WHERE f.id = ?1")
	Follow findById(UUID Id);

	@Query("SELECT f FROM Follow AS f WHERE f.followerUsername = ?1 AND f.followedUsername = ?2")
	Follow findByFollowerUsernameAndFollowedUsername(String followerUsername, String followedUsername);

	@Query("DELETE FROM Follow AS f WHERE f.followerUsername = ?1 AND f.followedUsername = ?2")
	void deleteByFollowerUsernameAndFollowedUsername(String followerUsername, String followedUsername);
}