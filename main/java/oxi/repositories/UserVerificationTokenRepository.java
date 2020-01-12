package oxi.repositories;


import oxi.models.User;
import oxi.models.UserVerificationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.stream.Stream;


public interface UserVerificationTokenRepository extends JpaRepository<UserVerificationToken, Long>{
	
	UserVerificationToken findByToken(String token);
	
	UserVerificationToken findByUser(User user);

	void deleteByExpiryDateLessThan(Date now);

	//@Modifying
	//@Query("delete from UserVerificationToken t where t.expiryDate <= 1?")
	//void deleteAllExpiredSince(Date now);
}