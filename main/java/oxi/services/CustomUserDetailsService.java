package oxi.services;

import oxi.repositories.UserRepository;
import oxi.repositories.RoleRepository;
import oxi.models.User;
import oxi.models.Role;
import oxi.models.Privilege;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.MessageSource;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Service("customUserDetailsService")
@Transactional
public class CustomUserDetailsService implements UserDetailsService{
	private static final Logger logger = LogManager.getLogger(CustomUserDetailsService.class);
	@Autowired
	private UserRepository userRepository;	
	//@Autowired
	//private IUserService service;	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{	
		boolean enabled = false;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		try{
			User user = userRepository.findByUsername(username);

			if (user == null) {
				throw new UsernameNotFoundException("No user found with username: " + username);
			    //return new org.springframework.security.core.userdetails.User(
			    //	" ", 
			    //	" ", 
			    //	true, 
			    //	true, 
			    //	true, 
			    //	true, 
			    //	getAuthorities(Arrays.asList(roleRepository.findByName("ROLE_USER")))
			    //);
			}

			return new org.springframework.security.core.userdetails.User(
				user.getUsername(), 
				user.getPassword(), 
				user.getEnabled(), 
				accountNonExpired, 
				credentialsNonExpired, 
				accountNonLocked, 
				getAuthorities(user.getRoles())
			);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles){	

		List<String> privileges = getPrivileges(roles);
		return getGrantedAuthorities(privileges);
	}
	
	private List<String> getPrivileges(Collection<Role> roles) {		
		List<String> privileges = new ArrayList<>();
		List<Privilege> collection = new ArrayList<>();
		for (Role role : roles) {
			//Include roles to the provileges list. 
			privileges.add(role.getName());
			collection.addAll(role.getPrivileges());
		}
		for (Privilege item : collection) {
			//Include privileges to the privileges list
			privileges.add(item.getName());
		}
		return privileges;
	}
	
	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges){
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}
}