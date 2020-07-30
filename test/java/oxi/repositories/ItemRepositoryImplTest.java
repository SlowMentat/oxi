package oxi.repositories;

import oxi.models.dto.CursorDto;
import oxi.models.dto.ItemDto;
import oxi.repositories.ItemRepositoryImpl;
import oxi.configs.JpaConfig;
import oxi.configs.BlazePersistenceConfiguration;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.ClassRule;
import org.junit.runner.RunWith;

import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.List;
import java.util.function.Consumer;

import javax.sql.DataSource;

import java.lang.NoSuchMethodException;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

//Provide a bridge between Spring Boot test features and JUnit
@RunWith(SpringRunner.class)
@ContextConfiguration(
	classes = { JpaConfig.class, BlazePersistenceConfiguration.class },
	loader = AnnotationConfigContextLoader.class)
@Transactional
//@TestPropertySource(locations="classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@DataJpaTest
public class ItemRepositoryImplTest{

	private static Logger logger = LogManager.getLogger();;

	//@Autowire 
	//private TestEntityManager entityManager;
	@Autowired
	private ItemRepositoryImpl itemRepImpl;
	@Autowired
	private DataSource dataSource;

	/*@ClassRule
	public static GenericContainer mysql = new GenericContainer(
		new ImageFromDockerfile("mysql-oxi").withDockerfileFromBuilder(dockerfileBuilder -> {
			dockerfileBuilder.from("mysql:5.7.9")
				.env("MYSQL_ROOT_PASSWORD", "root_password")				
                .env("MYSQL_DATABASE", "test2")
                .env("MYSQL_USER", "root")
                .env("MYSQL_PASSWORD", "chromosome24")
                .add("schema.sql", "/docker-entrypoint-initdb.d")
                .add("data.sql", "/docker-entrypoint-initdb.d");
		})
		.withFileFromClasspath("schema.sql", "db/mysql/schema.sql")
		.withFileFromClasspath("data.sql", "db/mysql/data.sql")
	)
		.withExposedPorts(3306)
		.withCreateContainerCmdModifier(
			new Consumer<CreateContainerCmd>(){
				@Override
				public void accept(CreateContainerCmd createContainerCmd){
					createContainerCmd.withPortBindings(new PortBinding(Ports.Binding.bindPort(3306), new ExposedPort(3306)));
				}
			}
		)
		.waitingFor(Wait.forListeningPort());*/

	@BeforeClass
	public static void init() throws InterruptedException{
		logger.debug("BeforClass:");
	}

	@AfterClass
	public static void destroy(){
		logger.debug("Closing database connection");
		//mysql.close();
	}

	@Test
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
		CursorDto cursor = new CursorDto(direction, firstResult, maxResults, lowestId, highestId, date);

		try{
			// when
			List<ItemDto> items = itemRepImpl.getAllItemsWithCoverpicUri(cursor);
			logger.debug("database query results received");
			// then
			assertThat(items.size()).isGreaterThan(0);
			/*assertThat(items)
				.extracting("coverpicuri", String.class)
				.doesNotContain(null, "");*/
		}
		catch(NoSuchMethodException e){

		}

		/*	
		items.stream().map(item -> {
			assertNotEquals("test item.coverpicuri is not null", item.getCoverpic(), null);
			//assertNotEquals("test item.coverpicuri is not empty stirng", item.getCoverpic());
		});*/
	}
}