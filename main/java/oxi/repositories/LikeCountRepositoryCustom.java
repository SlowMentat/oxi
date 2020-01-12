package oxi.repositories;

import oxi.models.LikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.domain.*;

import org.springframework.data.repository.query.Param;

import java.util.*;

import oxi.models.dto.*;

public interface LikeCountRepositoryCustom {

	LikeCount customFindByOutfitId(UUID outfitId) throws Exception;
}