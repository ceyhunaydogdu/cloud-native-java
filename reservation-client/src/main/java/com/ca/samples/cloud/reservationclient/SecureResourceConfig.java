package com.ca.samples.cloud.reservationclient;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * SecureResourceConfig
 */
@Configuration
@EnableResourceServer
public class SecureResourceConfig extends ResourceServerConfigurerAdapter{

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("api**").authorizeRequests().anyRequest().authenticated();
    }

    
}