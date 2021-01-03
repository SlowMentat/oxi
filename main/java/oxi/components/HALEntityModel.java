package oxi.components;

import java.lang.*;
import java.util.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.CollectionModel;

public abstract class HALEntityModel extends RepresentationModel {

    //private final Map<String, RepresentationModel> embedded = new HashMap<String, RepresentationModel>();
    private Map<String, Collection<?>> embedded = new HashMap<String, Collection<?>>();

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("_embedded")
    //public Map<String, RepresentationModel> getEmbeddedResources() {
    public Map<String, Collection<?>> getEmbeddedResources() {
        return embedded;
    }

    public void embedResource(String relationship, CollectionModel<?> resource) {
        //embedded.put(relationship, resource);
        embedded.put(relationship, resource.getContent());
    }  
}