package oxi.repositories;


import oxi.models.Company;
import oxi.models.CompanyVerificationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.stream.Stream;


public interface CompanyVerificationTokenRepository extends JpaRepository<CompanyVerificationToken, Long>{
	
	CompanyVerificationToken findByToken(String token);
	
	CompanyVerificationToken findByCompany(Company company);

	void deleteByExpiryDateLessThan(Date now);

	//@Modifying
	//@Query("delete from CompanyVerificationToken t where t.expiryDate <= 1?")
	//void deleteAllExpiredSince(Date now);
}