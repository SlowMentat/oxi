package oxi.services;

import java.lang.*;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Arrays;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FilenameUtils;

import oxi.models.*;
import oxi.repositories.*;
//import oxi.util.assemblers.*;
import oxi.models.dto.*;
import oxi.models.projection.*;

import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.hateoas.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.*;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.core.convert.converter.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.DatatypeConverter;

import org.hibernate.SessionFactory;
import org.hibernate.Session;

import org.springframework.security.crypto.bcrypt.*;



@Service
public class ValueService{
	//Repositories
	@Autowired private ItemRepository itemRep;
	@Autowired private BookmarkRepository bookmarkRep;
	@Autowired private LikeCountRepository likeCountRep;
	@Autowired private FollowingRepository followingRep;
	@Autowired private ProfileRepository profileRep;
	@Autowired private OutfitRepository outfitRep;

	//Paged Resource Assemblers 
	@Autowired private PagedResourcesAssembler<Bookmark> bookmarPRA;
	//@Autowired private PagedResourcesAssembler<BookmarkDto> bookmarkPRAP; //TODO:  create this class

	@Autowired private PagedResourcesAssembler<LikeCountProfile> likeCountProfilePRA;
	//@Autowired private PagedResourcesAssembler<LikeDto> likePRAP; //TODO:  create this class

	@Autowired private PagedResourcesAssembler<Following> followPRA;
	//@Autowired private PagedResourcesAssembler<FollowDto> followPRAP; //TODO:  create this class

	@Autowired 
	private RepositoryEntityLinks links;

	@Autowired 
	private ImageService imageService;

	private static final Logger logger = LogManager.getLogger(ValueService.class);
	private static String imgfolder = "/usr/images/";

	//private final FileOutputStream fos = null;
	private InputStream iStream = null;

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	/*@Autowired
	SessionFactory sessionFactory;*/

	@Autowired
	private BCryptPasswordEncoder userPasswordEncoder;


	public ValueService(){

	}
	//=====================================================================================



	//Todo:  cache this.
	@Transactional
	public void follow(FollowingDto followingDto){
		//build Following entity
		Profile followeeProfile = profileRep.findByUsername(followingDto.getFolloweeUsername());
		FollowingId id = new FollowingId(
			profileRep.findByUsername(followingDto.getFollowerUsername()).getId(),
			followeeProfile.getId()
		);
		entityManager.persist(new Following(id));

		//update followee profiles.followers property
		followeeProfile.getProfileStats().setFollowing((followeeProfile.getProfileStats().getFollowing() + 1L));
		logger.debug("followeeProfile.followers = " + followeeProfile.getProfileStats().getFollowing());
		entityManager.merge(followeeProfile);
	}

	@Transactional
	public void unfollow(FollowingDto followingDto){		
		Profile followeeProfile = profileRep.findByUsername(followingDto.getFolloweeUsername());
		FollowingId id = new FollowingId(
			profileRep.findByUsername(followingDto.getFollowerUsername()).getId(),
			followeeProfile.getId()
		);

		Following following = new Following(id);
		
		followingRep.deleteById(id);
	}

	//Todo:  Currently doesn't work.  Eventually cache this.
	@Transactional
	public List<Following> getFollowing(String followeeUsername){		
		Profile followeeProfile = profileRep.findByUsername(followeeUsername);
		Following following = new Following();
		FollowingId id = new FollowingId();

		following.setId(id);

		id.setFolloweeProfileId(followeeProfile.getId());
		Example<Following> followingExample = Example.of(following);
		
		//Retreive cached followers' profile data snipets
		return followingRep.findAll(followingExample);
	}

	@Transactional
	public LikeCountDto like(String outfitId, String principle){

		Profile profile = profileRep.findByUsername(principle);
		LikeCount likeCount = null;

		try{
			likeCount = likeCountRep.customFindByOutfitId(UUID.fromString(outfitId));
		}catch(Exception e){
			logger.error(e.toString());
			return null;
		}
		
		likeCount.addProfile(profile);
		entityManager.persist(likeCount);

		return new LikeCountDto(likeCount);
	}

	@Transactional
	public LikeCountDto unlike(String outfitId, String principle){

		Profile profile = profileRep.findByUsername(principle);
		LikeCount likeCount = null;

		try{
			likeCount = likeCountRep.customFindByOutfitId(UUID.fromString(outfitId));
		}catch(Exception e){
			logger.error(e.toString());
		}
		
		likeCount.removeProfile(profile);
		entityManager.merge(likeCount);

		return new LikeCountDto(likeCount);
	}

	//@Transactional
	//public LikeCountDto getLikes(String targetUserName){
//
	//	return new LikeCountDto(outfitRep.findByUsername(targetUserName).getLikeCount())//;
	//}

	private <T extends Identifiable<String>> ResourceSupport toResource(T dto){	

		//Link outfitLink = null;// links.linkForSingleResource(dto).withRel("outfit");
		//SLink selfLink = links.linkForSingleResource(dto).withSelfRel();
		return new Resource<T>(dto/*, null, selfLink*/);
	}
}