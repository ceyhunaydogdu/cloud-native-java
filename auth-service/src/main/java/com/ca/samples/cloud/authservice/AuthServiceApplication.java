package com.ca.samples.cloud.authservice;

import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@EnableEurekaClient
@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}



/**
 * Client
 */

class Client {

	private Long id;
}


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
				boolean active=person.isActive();
				return new User(person.getUsername(), person.getPassword(), active, active, active, active, AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER"));
			})
			.orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
	}
}

/**
 * PersonRepository
 */
interface PersonRepository extends JpaRepository<Person, Long> {

	public Optional<Person> findByUsername(String username);
}

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
class Person{

	@Id
	@GeneratedValue
	private Long id;

	private String username, password;

	private boolean active;

	/**
	 * @param username
	 * @param password
	 * @param active
	 */
	
	public Person(String username, String password, boolean active) {
		this.username = username;
		this.password = password;
		this.active = active;
	}
	
}

