package oxi.repositories;


import oxi.models.User;
import oxi.models.UserPasswordVerificationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.stream.Stream;
import java.util.UUID;


public interface UserPasswordVerificationTokenRepository extends JpaRepository<UserPasswordVerificationToken, Long>{
	
	UserPasswordVerificationToken findByToken(String token);
	
	UserPasswordVerificationToken findByUser(User user);

	UserPasswordVerificationToken findByUserId(UUID userId);

	void deleteByExpiryDateLessThan(Date now);

	void deleteById(UUID id);

	//@Modifying
	//@Query("delete from UserPasswordVerificationToken t where t.expiryDate <= 1?")
	//void deleteAllExpiredSince(Date now);
}