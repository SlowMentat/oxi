package oxi.services;

import java.lang.*;
import java.util.Iterator;
import java.io.*;
import java.util.ArrayList;
import java.io.Serializable;
import org.apache.commons.io.IOUtils;

import oxi.models.*;
import oxi.models.dto.*;
import oxi.repositories.*;

import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.*;
import org.springframework.data.domain.*;


@Service
public interface BasicServable{
	/*public void saveOutfit(Outfit outfit);
	public ResourceSupport readOutfit(Long id);
	public PagedResources<OutfitDto> readOutfits(Pageable p);
	public String savePhoto(MultipartHttpServletRequest data);*/
	//public T readEntityAsResource(Long id);
}