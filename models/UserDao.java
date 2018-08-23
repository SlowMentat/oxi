package oxi.models;

import java.util.List;
import java.util.Collection;
import java.io.Serializable;
import java.lang.*;


public interface User extends Serializable, Identifiable<Long>
{	
	//Setters
	public void setId(Long id);
	//public void setIdText(String idText);
	public void setEmail(String email);	
	public void setPassword(String password);	
	public void setUsername(String username);
	public void setEnabled(boolean enabled);
	public <T extends ProfileDao> void setProfile(T profile);
	//public void setTokenExpired(boolean isExpired);;
	public <T extends RoleDao> void setRoles(Collection<T> roles);
	/*public void setRoles(List<Role> roles){
		this.roles = this.<Role, User>setManyToManyParents(roles, this.roles, this);
	}*/

	
	//Getters
	//@Override
	//public UUID getId();
	public String getIdText();
	public String getEmail();	
	public String getPassword();	
	public String getUsername();
	public boolean getEnabled();	
	public <T extends ProfileDao> T getProfile();
	//public boolean getTokenExpired();
	public <T extends RoleDao> Collection<T> getRoles();
}