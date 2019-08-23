package com.ca.samples.cloud.authservice;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * This ClientConfiguration class creates customized ClientDetailsService.
 * Since we have overridden the original ClientDetailsService with one in AuthorizationServerConfiguartion class,
 * this class is not used and left as an example.
 */
// @Configuration
public class ClientConfiguration {

    private final LoadBalancerClient loadBalancerClient;

    /**
     * @param loadBalancerClient
     */
    @Autowired
    public ClientConfiguration(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    // @Bean
    public ClientDetailsService myClientDetailsService(ClientRepository clientRepository) {
        return clientId -> clientRepository
            .findByClientId(clientId)
            .map(
                client -> {
                    BaseClientDetails baseClientDetails= new BaseClientDetails(client.getClientId(), null,
                                    client.getScopes(), client.getAuthorizedGrantTypes(), client.getAuthorities());
                    baseClientDetails.setClientSecret(client.getSecret());
                    // baseClientDetails.setAutoApproveScopes(List.of(client.getAutoApproveScopes().split(",")));
                    String reservationClientRedirectUri=Optional
                    .ofNullable(this.loadBalancerClient.choose("reservation-client"))
                    .map(rsi-> "http://"+rsi.getHost()+":"+rsi.getPort()+"/reservations/names")
                    .orElseThrow(()-> new ClientRegistrationException("Couldn't find and bind reservation-client IP"));
                    
                    baseClientDetails.setRegisteredRedirectUri(Set.of(reservationClientRedirectUri));
                    return baseClientDetails;
                })
            .orElseThrow(()-> new ClientRegistrationException(String.format("Client: %s not registered", clientId)));
    }
    
}