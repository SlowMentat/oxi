package oxi;

//import oxi.models.dto.CursorDto;
//import oxi.models.dto.ItemDto;
//import oxi.models.*;
//import oxi.models.Role;
//import oxi.repositories.ItemRepositoryImpl;
import oxi.configs.JpaConfig;
import oxi.configs.BlazePersistenceConfiguration;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.ClassRule;
//import org.junit.runner.RunWith;
import org.junit.jupiter.api.*;
//import org.junit.BeforeClass;
//import org.junit.AfterClass;
//import org.junit.BeforeAll;
//import org.junit.AfterAll;
//import org.junit.Rule;
//import org.junit.rules.ExternalResource;
import org.testng.annotations.*;


import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.test.context.junit4.SpringRunner; // Alias for SpringJUnit4ClassRunner
//import org.springframework.test.context.junit5.SpringRunner;
import org.springframework.test.context.junit4.rules.*;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.context.annotation.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.sql.DataSource;

import java.lang.NoSuchMethodException;

//import javax.persistence.PersistenceUnit;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
//import oxi.PropertyOverrideContextInitializer;
import org.springframework.core.env.Environment;
import java.util.stream.Collectors;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;

//Provide a bridge between Spring Boot test features and JUnit
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = { JpaConfig.class, BlazePersistenceConfiguration.class })
@ExtendWith(SpringExtension.class)
//@SpringBootTest
@ContextConfiguration(
	//initializers = PropertyOverrideContextInitializer.class,
	classes = { JpaConfig.class, BlazePersistenceConfiguration.class },
	loader = AnnotationConfigContextLoader.class )
@Transactional
//@TestPropertySource(locations="classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@PropertySource("classpath:application-test.properties")
@TestPropertySource(locations = {"classpath:application-test.properties"})
@DataJpaTest
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest{

	protected static final Logger logger = LogManager.getLogger();
	protected static final String PATTERN = "dd-M-yyyy hh:mm:ss";
	protected static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
	//protected TestEntityManager tem;
	//protected Environment env;
	//@Autowired 
	//protected TestEntityManager tem;
	//@Autowired
	//protected Environment env;	

    //@ClassRule
    //public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
//
    //@Rule
    //public final SpringMethodRule springMethodRule = new SpringMethodRule();

	//public AbstractIntegrationTest(){

	//}

	//@Autowired
	//public AbstractIntegrationTest(TestEntityManager tem, Environment env){
	//	this.tem = tem;
	//	this.env = env;
	//}

	public void initializeDatabase(){}
	public void cleanupDatabase(){}

	//@BeforeAll//(dependsOnMethods={"springTestContextPrepareTestInstance"})
	//public void iniTask(){
	//	logger.debug("iniTask called beforeclass");
	//	initializeDatabase();
	//}
//
	//@AfterClass
	//public void cleanupTask(){
	//	cleanupDatabase();
	//}

	//@Rule
	//public ExternalResource resource = new ExternalResource(){
	//	@Override 
	//	protected void before() throws Throwable{
	//		logger.debug("iniTask called beforeclass");
	//		initializeDatabase();
	//	}

	//	@Override
	//	protected void after(){
	//		logger.debug("desTask called afterClass");
	//		cleanupDatabase();
	//	}
	//};

	//@Autowired
    //public final void setTestEntityManager(TestEntityManager tem) {
    //    this.tem = tem;
    //}
    //@Autowired
    //public final void setEnvironment(Environment env) {
    //    this.env = env;
    //}

	//public Logger getLogger(){ return AbstractIntegrationTest.logger; }
	//public TestEntityManager getTestEntityManager(){ return this.tem; }
	//public Environment getEnvironment(){ return this.env; }
}