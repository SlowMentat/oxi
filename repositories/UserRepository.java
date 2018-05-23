package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;

@RepositoryRestResource(collectionResourceRel="User", path="user")
public interface UserRepository extends JpaRepository<User, Long>{
	
} 