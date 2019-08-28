package com.ca.samples.cloud.authservice;
 
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * AuthorizationServerConfiguration
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }
    
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    
    /**
     * 
     * This method overrides the original one in the spring framework, 
     * thus property of "spring.main.allow-bean-definition-overriding" 
     * should be set to true in the application properties file. 
     * 
     * @param clientRepository
     * @return
     */
    @Bean
    public ClientDetailsService clientDetailsService(ClientRepository clientRepository) {
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
        
        //not used and left as an example for customized ClientDetailsService.
        //can be used together with ClientConfiguration class.
        /* @Autowired
        private ClientDetailsService myClientDetailsService;
        
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(myClientDetailsService);
            //Creating in memory client
            // clients.inMemory()
            //         .withClient("cat")
            //         .secret("{noop}catsecret")
            //         .authorizedGrantTypes("password")
            //         .scopes("openid");
        } */
        
        // Since we defined passwordencoder in websecurityconfiguration class, we can comment out the one below.
        /* @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        } */
        
    }