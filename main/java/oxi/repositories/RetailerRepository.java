package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

import org.springframework.data.repository.query.Param;

import java.util.*;

//import oxi.models.projection.OutfitProjection;
import oxi.models.dto.*;

@RepositoryRestResource(collectionResourceRel="Retailer", path="retailer")
public interface RetailerRepository extends JpaRepository<Retailer, UUID>{
} 