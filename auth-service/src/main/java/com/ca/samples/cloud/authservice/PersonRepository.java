package com.ca.samples.cloud.authservice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PersonRepository
 */
interface PersonRepository extends JpaRepository<Person, Long> {

	public Optional<Person> findByUsername(String username);
}
