//package oxi.util.assemblers;
//
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//
//import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
//import org.springframework.stereotype.*;
//import org.springframework.data.domain.*;
//
//
//import org.springframework.hateoas.*;
//import org.springframework.hateoas.server.*;
//
//
//@Component
////public abstract class DtoModelAssembler<T, D extends RepresentationModel<?>> extends RepresentationModelAssemblerSupport<T, D extends RepresentationModel<?>> {
//public abstract class DtoModelAssembler<T, D extends RepresentationModel<?>> implements RepresentationModelAssembler<T, D extends RepresentationModel<?>> {
//
//	//private static final Logger logger = LogManager.getLogger(ContentResourceAssembler.class);
//
//	public DtoModelAssembler(@Value("controllerClass") Class<?> controllerClass, @Value("resourceType") Class<D> resourceType){
//		super(controllerClass, resourceType);
//	}
//
//	@Override
//	private <T extends Object> RepresentationModel toModel(T dto){
//		return new EntityModel<T>(dto);
//	}
//}