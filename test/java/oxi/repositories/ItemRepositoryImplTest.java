package oxi.repositories;

import oxi.models.dto.CursorDto;
import oxi.models.dto.ItemDto;
import oxi.repositories.ItemRepositoryImpl;
import oxi.configs.JpaConfig;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Optional;
import java.util.List;

import java.lang.NoSuchMethodException;

import static org.assertj.core.api.Assertions.assertThat;

//Provide a bridge between Spring Boot test features and JUnit
@RunWith(SpringRunner.class)
@ContextConfiguration(
	classes = { JpaConfig.class },
	loader = AnnotationConfigContextLoader.class)
//@Import(JpaConfig.class)
@Transactional
//@DataJpaTest
public class ItemRepositoryImplTest{

	//@Autowire 
	//private TestEntityManager entityManager;

	@Autowired
	private ItemRepositoryImpl itemRepImpl;

	@Test
	public void whenGetAllItemsWithCoverpicUri_thenReturnItemsWithCoverpicuri(){
		// given
		Integer direction = 0;
		Integer firstResult = 0;
		Integer maxResults = 10;
		String lowestId = null;
		String highestId = null;
		String date = "2020-01-11T09:44:54.360Z";

		CursorDto cursor = new CursorDto(direction, firstResult, maxResults, lowestId, highestId, date);

		try{
			// when
			List<ItemDto> items = itemRepImpl.getAllItemsWithCoverpicUri(cursor);

			// then
			assertThat(items)
				.extracting("coverpicuri", String.class)
				.doesNotContain(null, "");
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