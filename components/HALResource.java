package oxi.components;

import java.lang.*;
import java.util.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.*;
import org.springframework.hateoas.ResourceSupport;

public abstract class HALResource extends ResourceSupport {

    private final Map<String, ResourceSupport> embedded = new HashMap<String, ResourceSupport>();

    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("_embedded")
    public Map<String, ResourceSupport> getEmbeddedResources() {
        return embedded;
    }

    public void embedResource(String relationship, ResourceSupport resource) {

        embedded.put(relationship, resource);
    }  
}