package com.ca.samples.cloud.authservice;

import org.springframework.boot.actuate.trace.http.HttpTrace.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * PrincipalRestController
 */
@RestController
public class PrincipalRestController {

    @GetMapping(value="/user")
    public Principal principal(Principal principal) {
        return principal;
    }
    
    
}