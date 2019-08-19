package com.ca.samples.cloud.authservice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ClientRepository
 */
public interface ClientRepository extends JpaRepository<Client, Long>{

    Optional<Client> findByClientId(String clientId);
}