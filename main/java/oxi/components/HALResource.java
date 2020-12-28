package oxi.components;

import java.lang.*;
import java.util.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

public abstract class HALResource extends ResourceSupport {

    //private final Map<String, ResourceSupport> embedded = new HashMap<String, ResourceSupport>();
    private Map<String, Collection<?>> embedded = new HashMap<String, Collection<?>>();

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("_embedded")
    //public Map<String, ResourceSupport> getEmbeddedResources() {
    public Map<String, Collection<?>> getEmbeddedResources() {
        return embedded;
    }

    public void embedResource(String relationship, Resources<?> resource) {
        //embedded.put(relationship, resource);
        embedded.put(relationship, resource.getContent());
    }  
}