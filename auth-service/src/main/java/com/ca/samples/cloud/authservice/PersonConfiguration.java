package com.ca.samples.cloud.authservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * PersonConfiguration
 */
@Configuration
class PersonConfiguration {

	@Bean
	UserDetailsService	userDetailsService(PersonRepository personRepository) {
		
		return username -> personRepository
			.findByUsername(username)
			.map(person -> {
				System.out.println("In Userdetailsservice : "+person.toString());
				boolean active=person.isActive();
				return new User(person.getUsername(), person.getPassword(), active, active, active, active, AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER"));
			})
			.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
	}
}
