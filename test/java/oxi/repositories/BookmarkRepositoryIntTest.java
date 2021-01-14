package oxi.repositories;

//import oxi.PropertyOverrideContextInitializer;
import oxi.models.dto.CursorDto;
import oxi.models.dto.ItemDto;
import oxi.models.*;
import oxi.models.Role;
import oxi.repositories.BookmarkRepository;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.*;

//import org.testng.annotations.*;

import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.lang.NoSuchMethodException;

import javax.sql.DataSource;
//import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.hamcrest.collection.*;

@TestInstance(Lifecycle.PER_CLASS)
public class BookmarkRepositoryIntTest extends AbstractIntegrationTest{

	@Autowired
	private BookmarkRepository bookmarkRep;

	private Item item = new Item();
	private Bookmark bookmark = new Bookmark();
	private UUID itemId1;
	private UUID itemId2;
	private UUID itemId3;
	private Bookmark bookmark1;
	private Bookmark bookmark2;
	private Bookmark bookmark3;
	private Bookmark bookmark4;
	private Bookmark bookmark5;
	private final String USERNAME_1 = "tester1";
	private final String USERNAME_2 = "tester2";
	private final boolean CLEAR_DB = true;

	@Autowired
	public BookmarkRepositoryIntTest(){
		super();
		initializeFields();
	}

	public void initializeFields(){
		logger.debug("BookmarkRepositoryIntTest Constructor");

		itemId1 = UUID.randomUUID();
		itemId2 = UUID.randomUUID();
		itemId3 = UUID.randomUUID();

		// Create several bookmarks from users for the same item
		bookmark1 = initializeBookmark(null, USERNAME_1, itemId2);
		bookmark2 = initializeBookmark(null, USERNAME_1, itemId1);
		bookmark3 = initializeBookmark(null, USERNAME_2, itemId1);
		bookmark4 = initializeBookmark(null, USERNAME_2, itemId1);
		bookmark5 = initializeBookmark(null, USERNAME_1, itemId3);
	}

	private Bookmark initializeBookmark(UUID id, String username, UUID itemId){
		username = username == null ? USERNAME_1 : username;
		itemId = itemId == null ? UUID.randomUUID() : itemId;

		Bookmark bookmark = new Bookmark();

		//bookmark.setId(id);
		bookmark.setUsername(username);
		bookmark.setItemId(itemId);

		return bookmark;
	}

	@Override
	@BeforeAll
	public void initializeDatabase(){
		logger.debug("UserAccountRepositoryIntTest: Pre-testing setup");

		bookmark1 = bookmarkRep.save(bookmark1);
		bookmark2 = bookmarkRep.save(bookmark2);
		bookmark3 = bookmarkRep.save(bookmark3);
		bookmark4 = bookmarkRep.save(bookmark4);
		bookmark5 = bookmarkRep.save(bookmark5);
	}

	@Override
	@AfterAll
	public void cleanupDatabase(){
		logger.debug("UserAccountRepositoryIntTest: Post-testing cleanup");

		if(CLEAR_DB){
			bookmarkRep.delete(bookmark1);
			bookmarkRep.delete(bookmark2);
			bookmarkRep.delete(bookmark3);
			bookmarkRep.delete(bookmark4);
			bookmarkRep.delete(bookmark5);

			logger.debug("UserAccountRepositoryIntTest: Database deleted.");
		}
		else{
			logger.debug("UserAccountRepositoryIntTest: Database records not deleted.");
		}
	}

	private void validateBookmark(Bookmark actualBookmark, Bookmark expectedBookmark){
		assertThat(actualBookmark, notNullValue());
		assertThat(actualBookmark.getUsername(), is(expectedBookmark.getUsername()));
		assertThat(actualBookmark.getItemId(), is(expectedBookmark.getItemId()));
		assertThat(actualBookmark.getId(), is(expectedBookmark.getId()));
	}

	private void validateBookmarks(List<Bookmark> actualBookmarks, List<Bookmark> expectedBookmarks, List<UUID> expectedBookmarkIds, Set<UUID> expectedItemIds, Set<String> expectedUsernames, Integer expectedSize){
		assertThat(actualBookmarks, notNullValue());
		assertThat(actualBookmarks, hasSize(expectedSize));
		assertThat(actualBookmarks, hasItems(expectedBookmarks.toArray(new Bookmark[expectedSize])));
	}


	@Test
	public void canFindBookmarkByItemId() throws Exception{
		Bookmark actualBookmark = bookmarkRep.findByItemId(bookmark1.getItemId());
		validateBookmark(actualBookmark, bookmark1);
	}

	@Test
	public void canFindBookmarkByItemIds(){
		Set<UUID> expectedItemIds = new HashSet<UUID>();
		List<UUID> expectedBookmarkIds = new ArrayList<UUID>();
		Set<String> expectedUsernames = new HashSet<String>(); 

		List<Bookmark> expectedBookmarks = new ArrayList<Bookmark>();
		expectedBookmarks.add(bookmark1);
		expectedBookmarks.add(bookmark5);
		
		expectedItemIds.add(bookmark1.getItemId());
		expectedItemIds.add(bookmark5.getItemId());

		// Chose bookmarks with different expectedUsernames for this test
		expectedBookmarkIds.add(bookmark1.getId());
		expectedBookmarkIds.add(bookmark5.getId());

		expectedUsernames.add(bookmark1.getUsername());
		expectedUsernames.add(bookmark5.getUsername());

		List<UUID> expectedItemIdsList = new ArrayList<UUID>(expectedItemIds);
		List<Bookmark> actualBookmarks = bookmarkRep.findByItemIds(expectedItemIdsList);

		logger.debug("expectedItemIdsList size = " + expectedItemIdsList.size());
		logger.debug("bookmarks = " + actualBookmarks.toString());

		// Test Results
		validateBookmarks(actualBookmarks, expectedBookmarks, expectedBookmarkIds, expectedItemIds, expectedUsernames, expectedItemIdsList.size());		
	}

	@Test
	public void canfindBookmarkByUsernameAndItemId(){
		Bookmark bookmark = bookmarkRep.findByUsernameAndItemId(bookmark1.getUsername(), bookmark1.getItemId());
		validateBookmark(bookmark, bookmark1);
	}

	@Test
	public void canfindBookmarkByUsernameAndItemIds(){
		Set<UUID> expectedItemIds = new HashSet<UUID>();
		List<UUID> expectedBookmarkIds = new ArrayList<UUID>();
		Set<String> expectedUsernames = new HashSet<String>(); 
		
		//Bookmark[] expectedBookmarks = new Bookmark[]{bookmark2, bookmark5};
		List<Bookmark> expectedBookmarks = new ArrayList<Bookmark>();
		expectedBookmarks.add(bookmark2);
		expectedBookmarks.add(bookmark5);

		expectedItemIds.add(bookmark2.getItemId());
		expectedItemIds.add(bookmark5.getItemId());

		// choose bookmarks with the same username for this test
		expectedBookmarkIds.add(bookmark2.getId());
		expectedBookmarkIds.add(bookmark5.getId());

		expectedUsernames.add(bookmark2.getUsername());
		expectedUsernames.add(bookmark5.getUsername());

		List<UUID> expectedItemIdsList = new ArrayList<UUID>(expectedItemIds);

		List<Bookmark> actualBookmarks = bookmarkRep.findByUsernameAndItemIds(bookmark2.getUsername(), expectedItemIdsList);

		// Test Results
		validateBookmarks(actualBookmarks, expectedBookmarks, expectedBookmarkIds, expectedItemIds, expectedUsernames, expectedItemIdsList.size());	
	}

	@Test
	public void canFindBookmarkItemIdsByUsername(){
		List<String> expectedItemIds = new ArrayList<String>();

		// List of bookmarks with target username
		expectedItemIds.add(bookmark1.getItemId().toString().toUpperCase());
		expectedItemIds.add(bookmark2.getItemId().toString().toUpperCase());
		expectedItemIds.add(bookmark5.getItemId().toString().toUpperCase());

		List<String> actualItemIds = bookmarkRep.findItemIdsByUsername(bookmark1.getUsername());
		
		assertThat(actualItemIds, notNullValue());
		assertThat(actualItemIds, hasSize(expectedItemIds.size()));
		assertThat(actualItemIds, hasItems(expectedItemIds.toArray(new String[expectedItemIds.size()])));
	}

	@Test
	public void canFindBookmarkIdToDateHashMapByUsername(){
		List<Bookmark> expectedBookmarks = new ArrayList<Bookmark>();

		// List of bookmarks with the same username
		expectedBookmarks.add(bookmark1);
		expectedBookmarks.add(bookmark2);
		expectedBookmarks.add(bookmark5);

		Set<String> expectedDates = expectedBookmarks.stream().map(bookmark -> simpleDateFormat.format(bookmark.getCreatedOn())).collect(Collectors.toSet());

		HashMap<String, Date> itemIdToDateMap = bookmarkRep.customfindItemIdsByUsername(bookmark1.getUsername());

		assertThat(itemIdToDateMap, notNullValue());
		assertThat(itemIdToDateMap, aMapWithSize(expectedBookmarks.size()));

		expectedBookmarks.stream().forEach(bookmark -> {
			Date actualDate = itemIdToDateMap.get(bookmark.getItemId().toString().toUpperCase());
			assertThat(actualDate, notNullValue());
		});
	}

	/*
	*	Depends on canFindBookmarkByItemId passing	*
	*/
	@Test
	public void canDeleteBookmarksByUsernameAndItemId(){
		bookmarkRep.deleteByUsernameAndItemId(bookmark3.getUsername(), bookmark3.getItemId());
		Bookmark actualBookmark = bookmarkRep.findByItemId(bookmark3.getId());

		assertThat(actualBookmark, nullValue());
	}

	/*
	*	Depends on canFindBookmarkByItemIds passing	*
	*/
	@Test
	public void canDeleteBookmarksByUsernameAndItemIds(){
		List<UUID> itemIds = new ArrayList<UUID>();
		itemIds.add(bookmark1.getItemId());
		itemIds.add(bookmark5.getItemId());

		bookmarkRep.deleteByUsernameAndItemIds(bookmark1.getUsername(), itemIds);
		List<Bookmark> actualBookmarks = bookmarkRep.findByItemIds(itemIds);

		assertThat(actualBookmarks, notNullValue());
		assertThat(actualBookmarks, hasSize(0));
	}
}