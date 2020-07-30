package oxi.repositories;


import oxi.models.User;
import oxi.models.UserVerificationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.stream.Stream;
import java.util.UUID;


public interface UserVerificationTokenRepository extends JpaRepository<UserVerificationToken, Long>{
	
	UserVerificationToken findByToken(String token);
	
	UserVerificationToken findByUser(User user);

	UserVerificationToken findByUserId(UUID userId);

	void deleteByExpiryDateLessThan(Date now);

	void deleteById(UUID id);

	//@Modifying
	//@Query("delete from UserVerificationToken t where t.expiryDate <= 1?")
	//void deleteAllExpiredSince(Date now);
}