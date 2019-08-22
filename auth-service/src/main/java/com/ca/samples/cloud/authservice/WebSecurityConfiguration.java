package com.ca.samples.cloud.authservice;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * WebSecurityConfiguration
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	UserDetailsService	userDetailsService(PersonRepository personRepository) {
		
		return username -> personRepository
			.findByUsername(username)
			.map(person -> {
				boolean active=person.isActive();
				return new User(person.getUsername(), person.getPassword(), active, active, active, active, AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER"));
			})
			.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
	}
}
