/*package oxi.services;

import oxi.repositories.CompanyRepository;
import oxi.repositories.RoleRepository;
import oxi.models.Shop;
import oxi.models.CompanyRole;
import oxi.models.CompanyPrivilege;

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

@Service("customRetailerDetailsService")
@Transactional
public class CustomShopDetailsService implements UserDetailsService{
	private static final Logger logger = LogManager.getLogger(CustomCompanyDetailsService.class);
	@Autowired
	private CompanyRepository companyRepository;	
	//@Autowired
	//private IUserService service;	
	//@Autowired
	//private RoleRepository roleRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{	
		boolean enabled = false;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		try{
			Shop shop = shopRepository.findByShopName(username);
			if (company == null) {
				throw new UsernameNotFoundException("No shop found with shop name: " + shop);
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
				company.getCompanyName(), 
				shop.getAccessToken(), 
				company.getEnabled(), 
				accountNonExpired, 
				credentialsNonExpired, 
				accountNonLocked, 
				getAuthorities(company.getRoles())
			);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(Collection<CompanyRole> roles){	
		return getGrantedAuthorities(getPrivileges(roles));
	}
	
	private List<String> getPrivileges(Collection<CompanyRole> roles) {		
		List<String> privileges = new ArrayList<>();
		List<CompanyPrivilege> collection = new ArrayList<>();
		for (CompanyRole companyRole : roles) {
			//Include roles to the provileges list. 
			privileges.add(companyRole.getName());
			collection.addAll(companyRole.getPrivileges());
		}
		for (CompanyPrivilege item : collection) {
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
}*/