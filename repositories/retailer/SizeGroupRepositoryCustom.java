package oxi.repositories.retailer;

import oxi.models.retailer.*;
import oxi.models.dto.retailer.SizeChartDto;
import oxi.models.dto.retailer.SizeGroupDto;

import java.lang.*;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SizeGroupRepositoryCustom{

	// ***Depricated***
	HashMap<UUID, SizeGroupDto> customFindByIds(HashSet<UUID> itemIds);
}