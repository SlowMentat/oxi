package oxi.models.dto;

import oxi.models.projection.*;
import oxi.models.*;
import oxi.models.dto.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
//import org.springframework.hateoas.core.*;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.Identifiable;
import com.fasterxml.jackson.annotation.*;
import java.util.stream.Collectors;


public class ProfileDto extends RelatedEntity implements Serializable/*, Identifiable<.*>*/
{
	//private Long Id;
	@JsonProperty("id")
	private String id;
	private String username;
	private String country;
	private String dateOfBirth;
	/*private String bodyShape;
	private boolean mens;
	private boolean womens;
	private float height;
	private float neck;
	private float fullShoulder;
	private float halfShoulder;
	private float chest;
	private float waist;
	private float hip;
	private float sleeve;
	private float frontLength;
	private float backLength;
	private float pantOutseam;
	private float pantInseam;
	private float thigh;
	private float calf;*/
	private ToleranceDto toleranceDto;
	private ProfileStatsDto profileStatsDto;
	private UserMetricsDto userMetricsDto;
	//private List<String> likeCountIds;
	/*private List<OutfitDto> outfits;
	private List<ItemDto> items;
	private UserDto user;*/
	private PictureDto pictureDto;

	public ProfileDto(){
	}

	public ProfileDto(Profile profile){
		if(profile != null){
			this.id = profile.getIdText();
			this.username = profile.getUsername();
			this.country = profile.getCountry();
			this.dateOfBirth = profile.getDateOfBirth();
			this.toleranceDto = new ToleranceDto(profile.getTolerance());
			this.profileStatsDto = new ProfileStatsDto(profile.getProfileStats());
			this.userMetricsDto = new UserMetricsDto(profile.getUserMetrics());
			this.pictureDto = new PictureDto(profile.getPictureProfile());
			//this.likeCountIds = profile.getLikeCounts().stream().map(likeCountProfile -> likeCountProfile.getLikeCount().getIdText()).collect(Collectors.toList());	
		}	
	}

	public ProfileDto(
		String id,
		String username,
		String country,
		String dateOfBirth,
		UserMetricsDto userMetricsDto,
		ToleranceDto toleranceDto,
		ProfileStatsDto profileStatsDto){
		this.id = id;
		this.username = username;
		this.country = country;
		this.dateOfBirth = dateOfBirth;
		this.toleranceDto = toleranceDto;
		this.profileStatsDto = profileStatsDto;
		this.userMetricsDto = userMetricsDto;
		this.toleranceDto = toleranceDto;
	}
	
	
	//Setters
	public void setId(String id){this.id = id;}

	public void setUsername(String username){this.username = username;}
	
	public void setCountry(String country){this.country = country;}
	
	public void setDateOfBirth(String dateOfBirth){this.dateOfBirth = dateOfBirth;}
	
	public void setToleranceDto(ToleranceDto toleranceDto){this.toleranceDto = toleranceDto;}

	public void setProfileStatsDtoId(ProfileStatsDto profileStatsDto){this.profileStatsDto = profileStatsDto;}

	public void setUserMetricsDto(UserMetricsDto userMetricsDto){this.userMetricsDto = userMetricsDto;}

	public void setPictureDto(PictureDto pictureDto){this.pictureDto = pictureDto;}

	
	
	//Getters
	//@Override
	public String getId(){return (this.id == null) ? this.id : this.id.toLowerCase();}

	public String getUsername(){return this.username;}
	
	public String getCountry(){return this.country;}
	
	public String getDateOfBirth(){return this.dateOfBirth;}

	public ToleranceDto getToleranceDto(){return this.toleranceDto;}

	public ProfileStatsDto getProfileStatsDto(){return this.profileStatsDto;}

	public UserMetricsDto getUserMetricsDto(){return this.userMetricsDto;}

	//public List<String> getLikeCountIds(){return this.likeCountIds;}

	public PictureDto getPictureDto(){return this.pictureDto;}
}