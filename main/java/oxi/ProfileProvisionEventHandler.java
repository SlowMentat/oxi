package oxi.services;

import oxi.models.*;
import org.apache.commons.logging.*;
//import org.springframework.data.rest.repository.annotation.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
 
@Component
@RepositoryEventHandler (Profile.class)
public class ProfileProvisionEventHandler{
 
	private Logger logger = LogManager.getLogger(ProfileProvisionEventHandler.class);
 
	@HandleBeforeCreate
	public void handleBeforeCreate(Profile profile){
		logger.debug("HandleBeforeCreate EventHandler invoked:");
		if(profile.getOutfits() == null){
			logger.debug("otufits<Outfit> property of Profile object is null");
			//throw new ProfileWriteException(Profile, new RuntimeException ("you must specify a 'firstName' and a 'lastName' and a valid user reference."));
		}
	}
 
	@HandleAfterSave
	public void handleAfterSave(Profile profile) {
		logger.debug("saved customer #" + profile.getId());
	}
 
	@HandleAfterDelete
	public void handleAfterDelete(Profile profile) {
		logger.debug("deleted customer #" + profile.getId());
	}
}