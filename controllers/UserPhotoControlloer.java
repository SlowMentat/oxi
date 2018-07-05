/*package oxi.controllers;
 
import com.jl.crm.services.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
 
import javax.inject.Inject;
import java.net.URI;
import java.util.Collections;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Controller
@RequestMapping(value="/photo")
public class UserPhotoControlloer{
	
	//@Secure({"ROLE_USER"})
	@RequestMapping(method = RequestMethod.POST)
	HttpEntity WritePhoto(@PathVariable long user, MultipartHttpServletRequest file) throws Throwable {
		logger.debug("Hitting uploadImage method!");
		// write it
		byte bytesForProfilePhoto[] = FileCopyUtils.copyToByteArray(file.getInputStream());
		MediaType mt = MediaType.parseMediaType(file.getContentType());
		this.crmService.writeUserProfilePhoto(user, mt, bytesForProfilePhoto);

		if(!file.isEmpty()){
			File imgpath = null;
			try{
				imgpath = File.createTempFile("lrg",".jpg", new File(imgfolder));
			}catch(Exception e){
				return e.toString();
			}
			//tring fullfilename = imgpath + filename;			
			//insert new image entry in database
			try{
				byte[] bytes = file.getBytes();
				BufferedOutputStream ostream = new BufferedOutputStream(new FileOutputStream(imgpath));
				ostream.write(bytes);
				ostream.close();
				
				//update image database
				return "file \"" + imgpath.toString() + "\" successfully uploaded";
			}catch(Exception e){
				return "Failed to upload file " + imgpath.toString();
			}
		}else{
			return "File is empty";
		}
	}
}*/	




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