package com.ca.samples.cloud.authservice;

import java.security.Principal;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * PrincipalRestController
 */
@RestController
@EnableResourceServer
public class PrincipalRestController {

    @GetMapping(value="/user")
    public Principal principal(Principal principal) {
        return principal;
    }
    
    
}