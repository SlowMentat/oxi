package oxi.repositories.retailer;

import oxi.models.retailer.*;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

import org.springframework.data.repository.query.Param;

import java.util.*;
/*
//import oxi.models.projection.OutfitProjection;
import oxi.models.dto.*;*/

@RepositoryRestResource(collectionResourceRel="SizeGroup", path="sizeGroup")
public interface SizeGroupRepository extends JpaRepository<SizeGroup, UUID>, SizeGroupRepositoryCustom{
} 