package oxi.services;

import java.lang.*;
import java.util.Optional;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Collections;
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
import oxi.models.retailer.*;
import oxi.repositories.*;
import oxi.repositories.retailer.*;
//import oxi.util.assemblers.*;
import oxi.models.dto.*;
import oxi.models.dto.retailer.*;
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
public class RetailerService{
	//Repositories
	@Autowired private OutfitRepository outfitRep;
	@Autowired private MyContentRepository contentRep;
	@Autowired private ItemRepository itemRep;
	@Autowired private PictureRepository pictureRep;
	@Autowired private PictureDeleteRepository pictureDeleteRep;

	@Autowired private BookmarkRepository bookmarkRep;
	@Autowired private LikeRepository likeRep;
	@Autowired private FollowingRepository followRep;

	@Autowired private ProfileRepository profileRep;
	@Autowired private UserRepository userRep;
	@Autowired private RoleRepository roleRep;

	@Autowired private BrandRepository brandRep;
	@Autowired private RetailerRepository retailerRep;
	@Autowired private SizeLabelRepository sizeLabelRep;

	@Autowired private RetailerAccountRepository retailerAccountRep;
	@Autowired private SizeChartRepository sizeChartRep;

	//Resource Assemblers
	//@Autowired 
	//private OutfitResourceAssembler outfitRA;
	//@Autowired private ContentResourceAssembler contentRA;
	//@Autowired private ItemResourceAssembler itemRA;
	//@Autowired private PictureResourceAssembler pictureRA;
	//@Autowired private ProfileResourceAssembler profileRA;
	//@Autowired private UserResourceAssembler userRA;

	//Paged Resource Assemblers 
	@Autowired private PagedResourcesAssembler<Outfit> outfitPRA;
	@Autowired private PagedResourcesAssembler<OutfitDto> outfitPRAP;

	@Autowired private PagedResourcesAssembler<Content> contentPRA;
	@Autowired private PagedResourcesAssembler<ContentDto> contentPRAP;

	@Autowired private PagedResourcesAssembler<Item> itemPRA;
	@Autowired private PagedResourcesAssembler<ItemDto> itemPRAP;
	@Autowired private PagedResourcesAssembler<ProductDto> productPRAP;

	@Autowired private PagedResourcesAssembler<Brand> brandPRA;
	@Autowired private PagedResourcesAssembler<BrandDto> brandPRAP;

	@Autowired private PagedResourcesAssembler<Retailer> retailerPRA;
	//@Autowired private PagedResourcesAssembler<RetailerDto> retailerPRAP; //TODO:  create this class

	@Autowired 
	private RepositoryEntityLinks links;

	@Autowired 
	private ImageService imageService;

	private static final Logger logger = LogManager.getLogger(RetailerService.class);
	private static String imgfolder = "/usr/images/";

	//private final FileOutputStream fos = null;
	private InputStream iStream = null;

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	/*@Autowired
	SessionFactory sessionFactory;*/

	@Autowired
	private BCryptPasswordEncoder companyPasswordEncoder;


	public RetailerService(){

	}
	//=====================================================================================


	private ProductDto convertToProductDto(final Item item){
		return new ProductDto(item);
	}


	private static PictureDto copyToPictureDto(Picture picture){
		if(picture != null){
			return new PictureDto(
				picture.getId().toString(), 
				picture.getThumbnailuri(), 
				picture.getSmalluri(), 
				picture.getLargeuri());
		}
		return null;
	}

	private static Picture copyToPicture(PictureDto pictureDto){
		if(pictureDto != null){
			if(pictureDto.getId() != null) return new Picture(
				UUID.fromString(pictureDto.getId()), 
				pictureDto.getThumbnailuri(), 
				pictureDto.getSmalluri(), 
				pictureDto.getLargeuri());	
			else return new Picture(
				null, 
				pictureDto.getThumbnailuri(), 
				pictureDto.getSmalluri(), 
				pictureDto.getLargeuri());
		}
		return null;
	}

	private static SizeChart copyToSizeChart(SizeChartDto sizeChartDto){
		if(sizeChartDto != null){			
			return new SizeChart(sizeChartDto);
			/*if(sizeChartDto.getId() != null) return new SizeChart(
				UUID.fromString(sizeChartDto.getId()),
				sizeChartDto.getChartName(),
				copyTosizeGroups(sizeChartDto.getSizeGroupDtos()),
				//copyToItems(sizeChartDto.getItems()))
			else return new SizeChart(
				null,
				sizeChartDto.getSizeGroupDtos());*/
		}
		return null;
	}

	private static List<Item> copyToItems(List<ProductDto> productDtos){
		if(productDtos != null){
			List<Item> items = new ArrayList<Item>(productDtos.size());
			for(ProductDto productDto : productDtos){
				items.add(copyToItem(productDto));
			}
			return items;
		}else{
			return null;
		} 
	}

	private static Item copyToItem(ProductDto productDto){
		if(productDto != null){
			return new Item(
				null,
				productDto.getProductId(),
				productDto.getApparelType(),
				UUID.fromString(productDto.getSizeGroupId()),
				productDto.getLink(),
				productDto.getIsRetailPicture(),
				productDto.getIsActive(),
				copyToPicture(productDto.getPictureDto()),
				UUID.fromString(productDto.getSizeChartDto().getId()),
				null
			);
		}else{	
			return null;
		}
	}

	private static List<SizeGroup> copyTosizeGroups(List<SizeGroupDto> sizeGroupDtos){
		if(sizeGroupDtos != null){
			List<SizeGroup> sizeGroups = new ArrayList<SizeGroup>(sizeGroupDtos.size());
			for(SizeGroupDto sizeGroupDto : sizeGroupDtos){
				sizeGroups.add(copyToSizeGroup(sizeGroupDto));
			}
			return sizeGroups;
		}else{
			return null;
		}
	}

	private static SizeGroup copyToSizeGroup(SizeGroupDto sizeGroupDto){
		if(sizeGroupDto != null){
			return new SizeGroup(sizeGroupDto);
			/*return new SizeGroup(
				UUID.fromString(sizeGroupDto.getId()),
				sizeGroupDto.getSizeLabel(),
				sizeGroupDto.getNeck(),
				sizeGroupDto.getFullShoulder(),
				sizeGroupDto.getHalfShoulder(),
				sizeGroupDto.getChest(),
				sizeGroupDto.getWaist(),
				sizeGroupDto.getHip(),
				sizeGroupDto.getSleeve(),
				sizeGroupDto.getFrontLength(),
				sizeGroupDto.getBackLength(),
				sizeGroupDto.getPantOutseam(),
				sizeGroupDto.getPantInseam(),
				sizeGroupDto.getThigh(),
				sizeGroupDto.getCalf()
			);*/
		}else{
			return null;
		}
	}

	@Transactional
	public PagedResources<?> getProducts(String username, String filter, Pageable pageable) throws Exception{
		RetailerAccount retailerAccount = null;
		
		if(username != null){
			retailerAccount = retailerAccountRep.findByCompanyName(username);
		}else {
			throw new Exception("invalid username");
		}

		if(retailerAccount != null){
			switch(filter){
				case "all":
					/*Page<ProductDto> productDtos = itemRep.getAllItemsWithRetailerAccount(retailerAccount.getId(), pageable).map(new Converter<Item, ProductDto>(){
						@Override
						public ProductDto convert(Item item){
							return new ProductDto(item);
						}
					});*/
					Page<ProductDto> productDtos = itemRep.getAllItemsWithRetailerAccount(retailerAccount.getId(), pageable).map(this::convertToProductDto);
					return productPRAP.toResource(productDtos, this::toResource);
				default:
					return null;
			}
		}else{
			throw new Exception("User does not have a retailer account");
		}
	}

	//TODO:  currently returning payload on successful transaction...
	@Transactional
	public ArrayList<ProductDto> updateProducts(ArrayList<ProductDto> productDtos, String companyName){
		RetailerAccount retailerAccount = retailerAccountRep.findByCompanyName(companyName);
		ArrayList<Item> items = new ArrayList<Item>(productDtos.size());

		if(retailerAccount.getCompanyName().equals(companyName)){
			for(ProductDto productDto : productDtos){

				SizeChart sizeChart = sizeChartRep.findById(UUID.fromString(productDto.getSizeChartDto().getId())).get();
				Item item = itemRep.findById(UUID.fromString(productDto.getId())).get();

				//item.setApparelType(productDto.getApparelType());
				//item.setLink(productDto.getLink());
				item.setIsActive(productDto.getIsActive());
				item.setPicture(copyToPicture(productDto.getPictureDto()));
				//item.setIsRetailPicture(productDto.getIsRetailPicture());
				item.setSizeChart(sizeChart);
				item.setSizeGroupId(UUID.fromString(productDto.getSizeGroupId()));

				//entityManager.merge(sizeChart);
				items.add(entityManager.merge(item));
			}

		}else{
			logger.warn("Company, " + companyName + " does not have permissions to edit item");
		}
		return productDtos;
	}

//	@Transactional
//	public ArrayList<ProductDto> updateProducts(HashMap<String, ArrayList<ProductDto>> sizeChartIdToProductDtoMap, String companyName){
//		Outfit outfit = outfitRep.findById(UUID.fromString(outfitId));
//		RetailerAccount retailerAccount = retailerAccountRep.findByCompanyName(companyName);
//		
//
//		if(retailerAccount.companyName().equals(companyName)){
//			for(String sizeChartId : sizeChartIdToProductDtoMap.keySet()){
//
//				SizeChart sizeChart = sizeChartRep.findById(UUID.fromString(sizeChartId));
//				List<SizeChartSizeGroup> sizeChartsizeGroups = sizeChart.getItems();
//
//				for(ProductDto productDto : sizeChartIdToProductDtoMap.get(sizeChartId)){
//					//logger.debug(productDto.getId() + "\n");
//					int ind = -1;
//					for(SizeChartSizeGroup sizeChartSizeGroup : sizeChartsizeGroups){
//						ind++;
//						if(sizeChart.getId().equals(sizeChartSizeGroup.getSizeChart().getId()) && productDto.getId().equals(sizeChartSizeGroup.getItem().getId().toString())){
//							logger.debug("RetailerService#updateItems: sizeChartsizeGroups = \n" + sizeChartSizeGroup.toString());
//							sizeChartSizeGroup.getItem().setApparelType(productDto.getApparelType()); 
//							sizeChartSizeGroup.getItem().setSizeGroup(UUID.fromString(productDto.getSizeGroups()));
//							sizeChartSizeGroup.getItem().setRetailer(UUID.fromString(productDto.getRetailer()));
//							sizeChartSizeGroup.getItem().setBrand(UUID.fromString(productDto.getBrand()));
//							entityManager.merge(sizeChartSizeGroup);
//							sizeChartsizeGroups.set(ind, sizeChartSizeGroup);
//							logger.debug("RetailerService#updateItems: updated sizeChartSizeGroup = \n" + sizeChartSizeGroup.toString());
//							break;
//						}
//					}
//
//
//				}
//
//				sizeChart = entityManager.merge(sizeChart);
//				//TODO:  add sizeChart to Retailer Account entity
//				//outfit.getContents().set(outfit.getContents().indexOf(sizeChart), sizeChart);
//			}
//		}else{
//			logger.warn("User, " + username + " does not have permissions to edit item");
//		}
//		return copyToOutfitDto(outfit);
//		//return copyToContentDto(sizeChart)
//	}

	@Transactional
	public ArrayList<ProductDto> createProducts(ArrayList<ProductDto> productDtos, String companyName){
		RetailerAccount retailerAccount = retailerAccountRep.findByCompanyName(companyName);
		if(retailerAccount.getCompanyName().equals(companyName)){

			List<Item> items = new ArrayList<Item>(productDtos.size());
			for(ProductDto productDto : productDtos){

				/*SizeChart sizeChart = null;
				Picture picture = null;

				if(productDto.getSizeChartDto().getId() != null) sizeChart = sizeChartRep.findById(UUID.fromString(productDto.getSizeChartDto().getId()));
				else sizeChart = copyToSizeChart(productDto.getSizeChartDto());

				//Note picture reference should already have been persisted to DB before making call to createProducts
				if(productDto.getPictureDto().getId() != null) picture = pictureRep.findById(UUID.fromString(productDto.getPictureDto().getId()));
				else picture = copyToPicture(productDto.getPictureDto());

				Item item = new Item(
					null,
					productDto.getProductId(),
					productDto.getApparelType(),
					productDto.getSizeGroupId(),
					productDto.getLink(),
					productDto.getIsRetailPicture(),
					productDto.getIsActive(),
					null,
					null,
					null
				);				
				item.setSizeChart(sizeChart);
				item.setPicture(picture);*/
				Item item = new Item(productDto);
				item.setRetailerAccount(retailerAccount);
				entityManager.persist(item);
				items.add(item);
			}

			int n = 0;
			for(ProductDto productDto : productDtos){
				productDto.setId(items.get(n).getId().toString());
				n++;
			}
		}else{
			logger.warn("Company, " + companyName + " does not have permissions to edit item");
		}

		return(productDtos);
	}

	@Transactional
	public SizeChartDto setSizeChartsByProductId(String companyName, SizeChartDto sizeChartDto){

		RetailerAccount ra = retailerAccountRep.findByCompanyName(companyName);

		List<UUID> affectedItemIds = sizeChartDto.getAffectedProductIds().stream()
			.map(id -> {
				List<String> itemIds = ra.getItems().stream().map((Item item) -> item.getIdText()).collect(Collectors.toList());				
				/*
				* TODO: use an ORDER BY clause in the custom SQL statemnt to retrieve the entitites in the defined order.
				* This is so that we can binary search on items in retailerAccount against id
				*/
				if(Collections.binarySearch(itemIds, id) >= 0){
					return UUID.fromString(id);
				}

				// trying to access an item id that does not belong to this company, so return empty string
				return null;
			})
			.collect(Collectors.toList());

		/*
		* TODO:  create custom function in itemRep to findByRetailerAccountAndIdIn
		*/
		List<Item> items = itemRep.findByIdIn( affectedItemIds );
		List<String> result = new ArrayList<String>(items.size());


		SizeChart sc = new SizeChart(sizeChartDto);
		entityManager.persist(sc);
		
		for(Item item : items){
			item.setSizeChart(sc);
			entityManager.persist(item);
		}

		sizeChartDto.setId(sc.getIdText());

		return sizeChartDto;
	}

	/*@Transactional
	public ArrayList<ProductDto> activateProducts(ArrayList<ProductDto> productDtos, String companyName){
		
	}*/

	@Transactional
	private void savePicture(String imgFileName){
		if(imgFileName != null){
			Picture picture = new Picture();
			picture.setLargeuri(imgFileName);
			pictureRep.saveAndFlush(picture);
		}else{
			return;
		}
	}



	/*
	saves jpeg data to filesystem
	@param MulitpartHttpServletRequest data 
	@return String indicating generated filename.
	*/
	@Transactional
	public PictureDto saveImage(MultipartHttpServletRequest data){
		logger.debug("ConsumerService.saveImage() invoked");
		PictureUpdateDto pictureUpdateDto = imageService.saveImage(data);
		Picture picture = copyToPicture(pictureUpdateDto);
		entityManager.persist(picture);
		pictureUpdateDto.setId(picture.getId().toString());
		return pictureUpdateDto;
	}
	
	/*
	saves jpeg data to filesystem, and creates an entry in PictureDeleted with associating picture entity.
	TODO:  Finiah implementing multiple files identified by a multipart contentId parameter
	@param MulitpartHttpServletRequest data 
	@param String contentId
	@returns json picture object of existing picture entity having properties modified with new image filnames.
	*/
	@Transactional
	public List<PictureUpdateDto> updateImage(MultipartHttpServletRequest data, String contentId){
		//send existing image to the PictureDeleteTable
		Content content = contentRep.findById(UUID.fromString(contentId)).get();
		Picture picture = content.getPicture();
		//Picture picture = pictureRep.findById(UUID.fromString(pictureId));
		PictureDelete pd = new PictureDelete(picture);
		logger.debug("PictureDelete created = " + pd.toString());
		pictureDeleteRep.saveAndFlush(pd);
		//save the new image data to fs and return in picture json with existing id.
		PictureUpdateDto pictureUpdateDto = imageService.saveImage(data);
		pictureUpdateDto.setId(picture.getId().toString());
		pictureUpdateDto.setContentId(contentId);
		List<PictureUpdateDto> pictureUpdateDtos = new ArrayList<PictureUpdateDto>();
		pictureUpdateDtos.add(pictureUpdateDto);
		return pictureUpdateDtos;		
	}

	public byte[] getImage(String filename/*, HttpServletResponse response*/){
		//Path file = Paths.get(imgfolder+filename);
		byte[] image = null;
		logger.debug("in ConsumerService.getImage()");
		String subfolder = "";
		switch(filename.substring(0, Math.min(filename.length(), 3))){
			case "sml":
				subfolder = "small/";
				break;
			case "lrg":
				subfolder = "large/";
				break;
			case "tnl":
				subfolder = "thumbnail/";
				break;
			default:
				logger.debug("no matching prefix in filename");
				break;
		}
		try{
			iStream = new FileInputStream(imgfolder + subfolder + filename + ".jpg");
			image = IOUtils.toByteArray(iStream);
			logger.debug("image byte[] length = " + image.length);
			return image;
			/*Files.copy(file, response.getOutputStream());
			response.getOutputStream.flush();*/
		}catch(IOException e){
			logger.debug(e);
		}
		return image;
	}


	private <T extends Identifiable<String>> ResourceSupport toResource(T dto){		
		//Link outfitLink = null;// links.linkForSingleResource(dto).withRel("outfit");
		//SLink selfLink = links.linkForSingleResource(dto).withSelfRel();
		return new Resource<T>(dto/*, null, selfLink*/);
	}
}