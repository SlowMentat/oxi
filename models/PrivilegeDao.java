package oxi.models;


import java.util.List;
import java.util.UUID;
import java.util.Collection;
import java.io.Serializable;
import java.lang.*;


public interface PrivilegeDao extends Serializable, Identifiable<Long>
{
    //Getter
    public String getName();
	public <T extends RoleDao> Collection<T> getRoles();

    //Setter
    public void setName(String name);
}