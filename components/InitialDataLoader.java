package oxi.components;

import java.lang.*;
import java.util.Collection;
import java.util.Arrays;
import java.util.List;

import oxi.models.User;
import oxi.models.Role;
import oxi.models.Privilege;
import oxi.models.CompanyRole;
import oxi.models.CompanyPrivilege;
import oxi.repositories.UserRepository;
import oxi.repositories.RoleRepository;
import oxi.repositories.CompanyRoleRepository;
import oxi.repositories.PrivilegeRepository;
import oxi.repositories.CompanyPrivilegeRepository;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;


@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent>{
	//ContextRefreshedEvent may be fired multiple times depending on the number of contexts configured
	//alreadySetup is used to distinguish determin if setup needs to run in the case of multiple contexts
	private boolean alreadySetup = false;

	//@Autowired
	//private UserRepository userRep;
	@Autowired
	private RoleRepository roleRep;
	@Autowired
	private PrivilegeRepository privilegeRep;	
	@Autowired
	private CompanyRoleRepository companyRoleRep;
	@Autowired
	private CompanyPrivilegeRepository companyPrivilegeRep;
	//@Autowired
	//private PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event){
		
		if (alreadySetup) return;

		//Create privileges for use accoutns
		Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
		Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

		//create privileges for company accounts
		CompanyPrivilege companyReadPrivilege = createCompanyPrivilegeIfNotFound("READ_PRIVILEGE");
		CompanyPrivilege companyWritePrivilege = createCompanyPrivilegeIfNotFound("WRITE_PRIVILEGE");
	
		List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege); 
		List<CompanyPrivilege> adminCompanyPrivileges = Arrays.asList(companyReadPrivilege, companyWritePrivilege);

		//create roles for user accounts
		createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
		createRoleIfNotFound("ROLE_USER", adminPrivileges);

		//create roles for company accounts		
		createCompanyRoleIfNotFound("ROLE_RETAILER_ADMIN", adminCompanyPrivileges);
		createCompanyRoleIfNotFound("ROLE_RETAILER_USER", adminCompanyPrivileges);
		createCompanyRoleIfNotFound("ROLE_ANONYMOUS", Arrays.asList(companyReadPrivilege));
	
		Role adminRole = roleRep.findByName("ROLE_ADMIN");
		/*User user = new User();
		user.setUsername("Test");
		user.setPassword(passwordEncoder.encode("test"));
		user.setEmail("test@test.com");
		user.setRoles(Arrays.asList(adminRole));
		user.setEnabled(true);
		userRep.saveAndFlush(user);*/
	
		alreadySetup = true;
	}

	//User Roles and Privileges

	@Transactional
	private Privilege createPrivilegeIfNotFound(String name){	
		Privilege privilege = privilegeRep.findByName(name);
		if (privilege == null) {
		    privilege = new Privilege(name);
		    privilegeRep.saveAndFlush(privilege);
		}
		return privilege;
	}
	
	@Transactional
	private Role createRoleIfNotFound(String name, Collection<Privilege> privileges){	
	 	Role role = roleRep.findByName(name);
	 	if (role == null) {
	 	    role = new Role(name);
	 	    role.setPrivileges(privileges);
	 	    roleRep.saveAndFlush(role);
	 	}
	 	return role;
	}

	//Company Roles and Privilages

	@Transactional
	private CompanyPrivilege createCompanyPrivilegeIfNotFound(String name){	
		CompanyPrivilege companyPrivilege = companyPrivilegeRep.findByName(name);
		if (companyPrivilege == null) {
		    companyPrivilege = new CompanyPrivilege(name);
		    companyPrivilegeRep.saveAndFlush(companyPrivilege);
		}
		return companyPrivilege;
	}
	
	@Transactional
	private CompanyRole createCompanyRoleIfNotFound(String name, Collection<CompanyPrivilege> companyPrivileges){	
	 	CompanyRole companyRole = companyRoleRep.findByName(name);
	 	if (companyRole == null) {
	 	    companyRole = new CompanyRole(name);
	 	    companyRole.setPrivileges(companyPrivileges);
	 	    companyRoleRep.saveAndFlush(companyRole);
	 	}
	 	return companyRole;
	}
}