package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel="User", path="user")
public interface UserRepository extends JpaRepository<User, UUID>{
	//@RestResource(exported = false)
	@Query(value = "SELECT * FROM user WHERE email = ?1", nativeQuery = true)
	User findByEmail(String email);

	//@RestResource(exported = false)
	@Query(value = "SELECT * FROM user WHERE username = ?1", nativeQuery = true)
	User findByUsername(String username);

	@Query(value = "SELECT * FROM user WHERE password = ?1", nativeQuery = true)
	User findByPassword(String password);
} 