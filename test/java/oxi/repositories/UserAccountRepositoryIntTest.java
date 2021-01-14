package oxi.repositories;

//import oxi.PropertyOverrideContextInitializer;
import oxi.models.dto.CursorDto;
import oxi.models.dto.ItemDto;
import oxi.models.*;
import oxi.models.Role;
import oxi.repositories.ItemRepositoryImpl;
import oxi.configs.JpaConfig;
import oxi.configs.BlazePersistenceConfiguration;
import oxi.AbstractIntegrationTest;
//import oxi.BeanUtil;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.ClassRule;
//import org.junit.runner.RunWith;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.*;
//import org.testng.annotations.*;

import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.ActiveProfiles;

import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.junit5.SpringRunner;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.lang.NoSuchMethodException;
import java.util.stream.Collectors;

import javax.sql.DataSource;


import static org.assertj.core.api.Assertions.assertThat;

//import com.github.dockerjava.api.command.CreateContainerCmd;
//import com.github.dockerjava.api.model.ExposedPort;
//import com.github.dockerjava.api.model.PortBinding;
//import com.github.dockerjava.api.model.Ports;

//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.containers.wait.strategy.Wait;
//import org.testcontainers.images.builder.ImageFromDockerfile;

// //Provide a bridge between Spring Boot test features and JUnit
// @RunWith(SpringRunner.class)
// //@SpringBootTest(classes = { JpaConfig.class, BlazePersistenceConfiguration.class })
// @ContextConfiguration(
// 	//initializers = PropertyOverrideContextInitializer.class,
// 	classes = { JpaConfig.class, BlazePersistenceConfiguration.class },
// 	loader = AnnotationConfigContextLoader.class )
// //@Transactional
// //@TestPropertySource(locations="classpath:application-test.properties")
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// //@PropertySource("classpath:application-test.properties")
// @TestPropertySource(locations = {"classpath:application-test.properties"})
// @DataJpaTest
// @ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class UserAccountRepositoryIntTest extends AbstractIntegrationTest{

	//private static Logger logger = LogManager.getLogger();
	
	private final String READ_PRIVILEGE = "READ_PRIVILEGE";
	private final String WRITE_PRIVILEGE = "WRITE_PRIVILEGE";

	//private TestEntityManager tem;
	//private Environment env;

	private User user;

	@Autowired
	private UserRepository userRep;
	@Autowired
	private ItemRepositoryImpl itemRepImpl;
	@Autowired 
	private ProfileRepository profileRep;
	@Autowired 
	private RoleRepository roleRep;

	@Autowired
	public UserAccountRepositoryIntTest(){
		super();
		initializeFields();
	}

	private void initializeFields(){
		user = new User();
		user.setUsername("tester1");
		user.setPassword("tester1_password");
		user.setEmail("tester1@email.com");
		user.setRoles(null);
	}

	@Override
	@BeforeAll
	public void initializeDatabase(){
		logger.debug("UserAccountRepositoryIntTest: Pre-testing setup");
		//TestEntityManager tem = this.getTestEntityManager();
		//Environment env = this.getEnvironment();

		//logger.info(String.format("Initializing user table in %s database", this.env.getProperty("spring.datasource.testdatabase")));
//
		// Initialize privileges
		//List<Privilege> privileges = new ArrayList<Privilege>();
		//
		//Privilege readPrivilege = new Privilege(READ_PRIVILEGE);
		//Privilege writePrivilege = new Privilege(WRITE_PRIVILEGE);
		//
		//tem.persist(readPrivilege);
		//tem.persist(writePrivilege);

		//privileges.add(readPrivilege);
		//privileges.add(writePrivilege);

		//// Initialize roles
		//Role role = new Role("tester");
		//role.setPrivileges(privileges);
		//
		//tem.persist(role);
		//
		//List<Role> roles = new ArrayList<Role>();		
		//roles.add(role);

		// Initialize Profile
		//Profile profile = new Profile();
//

		// Initialize user
		//user.setProfile(profile);

		userRep.save(user);
	}
	
	@Override
	@AfterAll
	public void cleanupDatabase(){
		logger.debug("UserAccountRepositoryIntTest: Post-testing cleanup");

		userRep.delete(user);
	}

	//@Before
	//public void iniTask(){
	//	initUser();
	//}
//
	//@After
	//public void cleanupTask(){
	//	
	//}

	@Test
	public void canFindUserByUsername() throws Exception{
		User user = userRep.findByUsername("tester1");

		assertThat(user == null).isFalse();
		assertThat(user.getUsername()).isEqualTo("tester1");
	}

	@Test
	public void canFindUserByEmail(){
		User user = userRep.findByEmail("tester1@email.com");
		
		assertThat(user == null).isFalse();
		assertThat(user.getEmail()).isEqualTo("tester1@email.com");
	}

	@Test
	public void canFindUserByPassword(){
		User user = userRep.findByPassword("tester1_password");
		
		assertThat(user == null).isFalse();
		assertThat(user.getPassword()).isEqualTo("tester1_password");
	}

	//@Test
	public void whenGetAllItemsWithCoverpicUri_thenReturnItemsWithCoverpicuri(){
		logger.debug("begin whenGetAllItemsWithCoverpicUri_thenReturnItemsWithCoverpicuri");

		// given
		Integer direction = 0;
		Integer firstResult = 0;
		Integer maxResults = 10;
		String lowestId = null;
		String highestId = null;
		String date = "2020-01-11T09:44:54.360Z";

		logger.debug("Instantiating CursorDto");
		CursorDto cursorDto = new CursorDto(direction, firstResult, maxResults, lowestId, highestId, date);

		try{
			// when
			List<ItemDto> items = itemRepImpl.getAllItemsWithCoverpicUri(cursorDto);
			logger.debug("database query results received");
			// then
			assertThat(items.size()).isGreaterThan(0);
			assertThat(true).isFalse();
			/*assertThat(items)
				.extracting("coverpicuri", String.class)
				.doesNotContain(null, "");*/
		}
		catch(Exception e){
			logger.error(e.toString());
			assertThat(e != null).isFalse();
		}

		/*	
		items.stream().map(item -> {
			assertNotEquals("test item.coverpicuri is not null", item.getCoverpic(), null);
			//assertNotEquals("test item.coverpicuri is not empty stirng", item.getCoverpic());
		});*/
	}
}