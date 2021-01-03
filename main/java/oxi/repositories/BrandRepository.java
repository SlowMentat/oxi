package oxi.repositories;

import oxi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

import org.springframework.data.repository.query.Param;

import java.util.*;

////import oxi.models.projection.BrandProjection;
import oxi.models.dto.*;

@RepositoryRestResource(collectionResourceRel="Brand", path="brand")
public interface BrandRepository extends JpaRepository<Brand, UUID>, BrandRepositoryCustom{
} 