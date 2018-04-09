package oxi.dao;

import java.util.List;

import oxi.models.*;
import oxi.repositories.*;
import oxi.dao.*;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
 

import javax.annotation.Resource;
import java.util.List;

@Repository("UserDAO")
@Transactional(propagation = Propagation.REQUIRED)
public class UserDAO{

	@PersistenceContext
	private EntityManager entitymanager;
	private static final String SPEC_QUERY = "SELECT * FROM content";
	
	
	public EntityManager getEntityManager(){
		return this.entitymanager;
	}	
	public void setEntityManager(EntityManager entitymanager){
		this.entitymanager = entitymanager;
	}
	
	public List<User> selectAll() {
		Query query = entitymanager.createQuery(SPEC_QUERY);
		List<User> users = (List<User>) query.getResultList();
		return users;
	}
}