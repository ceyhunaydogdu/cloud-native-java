package com.ca.samples.cloud.authservice;

import org.springframework.boot.actuate.trace.http.HttpTrace.Principal;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * PrincipalRestController
 */
@RestController
// @EnableResourceServer
public class PrincipalRestController {

    @GetMapping(value="/user")
    public Principal principal(Principal principal) {
        System.out.printf("Hello from %s", principal.getName());
        return principal;
    }
    
    
}