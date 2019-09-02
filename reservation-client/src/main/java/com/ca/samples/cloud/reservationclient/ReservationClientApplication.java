package com.ca.samples.cloud.reservationclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
// import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

// @EnableBinding(ReservationChannels.class)
// @EnableOAuth2Client
@EnableCircuitBreaker
@EnableZuulProxy
@EnableEurekaClient
// @EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {

	@Bean
	@Lazy
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	
	
	// @Bean
	// RequestIntercepter requestIntercepter(OAuth2ClientContext oauth2ClientContext){ 			
	// 	return requestTemplate -> requestTemplate.header(HttpHeaders.AUTHORIZATION, oauth2ClientContext.getAccessToken().getTokenType()+" "+oauth2ClientContext.getAccessToken().getValue());
	// }

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}

}
