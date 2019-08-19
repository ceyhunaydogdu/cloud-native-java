package com.ca.samples.cloud.authservice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Client
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue
    private Long id;

    private String clientId, secret;

    private String scopes= StringUtils.arrayToCommaDelimitedString(new String[] {"openid"});
    
    private String authorizedGrantTypes= StringUtils.arrayToCommaDelimitedString(new String[] {"authorization_code", "refresh_token", "password"});
    
    private String authorities= StringUtils.arrayToCommaDelimitedString(new String[] {"ROLE_ADMIN", "ROLE_USER"});

    private String autoApproveScopes =  StringUtils.arrayToCommaDelimitedString(new String[] {"."});

    /**
     * @param cliendId
     * @param secret
     */

    public Client(String cliendId, String secret) {
        this.clientId = cliendId;
        this.secret = secret;
    }

    
}