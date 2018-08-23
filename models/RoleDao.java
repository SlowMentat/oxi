package oxi.models;

import java.util.List;
import java.util.UUID;
import java.util.Collection;
import java.io.Serializable;
import java.lang.*;

public interface RoleDao extends Serializable, Identifiable<Long>
{
	//Getter
	public String getName();
	public <T extends PrivilegeDao> Collection<T> getPrivileges();

	//Setter
	public void setId(Long id);
	public void setName(String name);
	public <T extends PrivilegeDao> void setPrivileges(Collection<T> privileges);
}