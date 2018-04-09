package oxi.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.expression.method.*;

import oxi.repositories.*;

/**
 * Extended expression root object which contains extra method-specific functionality.
 *
 * @author Luke Taylor
 * @since 3.0
 */
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
	private Object filterObject;
	private Object returnObject;
	private Object target;
	
	//***CUSTOM PROPERTIES****/
	//@Autowired
	//private ProfileRepository profileRep;
	//***CUSTOM PROPERTIES****/

	public CustomMethodSecurityExpressionRoot(Authentication a) {
		super(a);
	}

	public void setFilterObject(Object filterObject) {
		this.filterObject = filterObject;
	}

	public Object getFilterObject() {
		return filterObject;
	}

	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	/**
	 * Sets the "this" property for use in expressions. Typically this will be the "this"
	 * property of the {@code JoinPoint} representing the method invocation which is being
	 * protected.
	 *
	 * @param target the target object on which the method in is being invoked.
	 */
	public void setThis(Object target) {
		this.target = target;
	}

	public Object getThis() {
		return target;
	}
	
	/**
	Customized portion of of Class
	**/
	
	public boolean isOwner(long id){
		/*User user= (User)(auth.getPrincipal());
		long profileid = this.profileRep.findByFirstname(user.getProfileid);
		this.*/
		return true;
	}
	
}