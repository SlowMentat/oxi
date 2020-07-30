package oxi.repositories;

import oxi.models.ItemContent;
import oxi.models.ItemContentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;
import java.util.*;

//@RepositoryRestResource(collectionResourceRel="ItemContent", path="itemContent")
public interface ItemContentRepository extends JpaRepository<ItemContent, ItemContentId>, ItemContentRepositoryCustom{
	
} 