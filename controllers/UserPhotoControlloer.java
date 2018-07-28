package oxi.controllers;

import oxi.services.ConsumerService;

/*import com.jl.crm.services.*;*/
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.beans.factory.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.io.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Controller
public class UserPhotoControlloer{
	@Autowired
	private ConsumerService consumerService;
	private static final Logger logger = LogManager.getLogger(UserPhotoControlloer.class);

	@RequestMapping(value="/uploadPhoto", method=RequestMethod.POST)
	public void uploadImage(MultipartHttpServletRequest requestData){
		String imageUrl = consumerService.saveImage(requestData);
		//create new Picture Record with imageUrl
		//logger.debug("Image filename = " + imageUrl);
	}
	
	@RequestMapping(value="/image/{filename}", method=RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException{
		byte[] image = consumerService.getImage(filename);
		logger.debug("image = " + image);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
	}
}




	/*@Autowired
	private OutfitRepository outfitRepo;
	@Autowired
	private RepositoryEntityLinks links;
    @Autowired 
    private PagedResourcesAssembler<OutfitProjection> assembler;*/

    /**
    * Single DTO
    */
    /*@GetMapping("/{id}/outfit")
    public ResponseEntity<?> getDto(@PathVariable("id") Long outfitId) {
        OutfitProjection dto = outfitRepo.getDto(outfitId);
        
        return ResponseEntity.ok(toResource(dto));
    }*/
    
    /**
    * List of DTO
    */
    /*@GetMapping("/outfits")
    public ResponseEntity<?> getDtos() {
        List<OutfitProjection> dtos = outfitRepo.getDtos();
    
        Link listSelfLink = links.linkFor(Outfit.class).slash("/outfit").withSelfRel();
        List<?> resources = dtos.stream().map(this::toResource).collect(toList());

        return ResponseEntity.ok(new Resources<>(resources, listSelfLink));
    }*/

    /**
    * Paged list of DTO
    */
    /*@GetMapping("/outfitPaged")
    public ResponseEntity<?> getDtosPaged(Pageable pageable) {
        Page<OutfitProjection> dtos = outfitRepo.getDtos(pageable);

        Link pageSelfLink = links.linkFor(Outfit.class).slash("/outfitPaged").withSelfRel();
        PagedResources<?> resources = assembler.toResource(dtos, this::toResource, pageSelfLink);

        return ResponseEntity.ok(resources);
    }*/
/*
    private ResourceSupport toResource(OutfitProjection projection) {
        OutfitDto dto = new OutfitDto(projection.getId(), projection.getLikes(), projection.getComments(), projection.getContents(), projection.getCoverpicuri());
        
        Link contentsLink = links.linkToCollectionResource(Content).withRel("contents");
        Link selfLink = links.linkForSingleResource(Outfit).slash("/outfit").withSelfRel();
        
        return new Resource<>(dto, contentsLink, selfLink);
    }*/