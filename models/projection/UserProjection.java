package oxi.models.projection;

import oxi.models.projection.ProfileProjection;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.springframework.hateoas.core.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Identifiable;


public interface UserProjection extends Serializable, Identifiable<String>
{
	//Getters
	public String getEmail();
	public String getPassword();
	public String getUsername();
	public <T extends ProfileProjection> T getProfile();

	//Setters
	public void setId(String id);
	public void setEmail(String email);
	public void setPassword(String password);
	public void setUsername(String username);
	public <T extends ProfileProjection> void setProfile(T profile);
}