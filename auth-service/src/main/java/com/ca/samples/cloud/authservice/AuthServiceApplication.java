package com.ca.samples.cloud.authservice;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableEurekaClient
@SpringBootApplication
public class AuthServiceApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Autowired
	PersonRepository personRepository;

	@Autowired
	ClientRepository clientRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//Registering custom users to the auth-service
		Stream.of("ceyhun,{noop}native", "timtim,{noop}tim")
			.map(s->s.split(","))
			.forEach(tuple -> personRepository.save(new Person(tuple[0], tuple[1], true)));

		personRepository.findAll().forEach(System.out::println);
		//Registering custom client apps to the auth-service
		Stream.of("reservation-client,{noop}res-secret", "android-app,{noop}and-secret")
			.map(s->s.split(","))
			.forEach(tuple -> clientRepository.save(new Client(tuple[0],tuple[1])));

		clientRepository.findAll().forEach(System.out::println);
	}

}