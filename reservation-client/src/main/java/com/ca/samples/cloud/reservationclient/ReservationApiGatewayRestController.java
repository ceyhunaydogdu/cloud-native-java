package com.ca.samples.cloud.reservationclient;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
// import org.springframework.messaging.Message;
// import org.springframework.messaging.MessageChannel;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableResourceServer
// @EnableOAuth2Client
@RestController
@RequestMapping(path = "/reservations")
public class ReservationApiGatewayRestController {
	@Autowired
	private UserInfoRestTemplateFactory factory;

	@Bean
	@Lazy
	@LoadBalanced
	public OAuth2RestTemplate authRestTemplate() {
        return factory.getUserInfoRestTemplate();
	}
    
	@Autowired
	private RestTemplate rtemplate;
	// private final MessageChannel out;

    // @Autowired
    // public ReservationApiGatewayRestController(ReservationChannels rChannels) {
    //     this.out = rChannels.output();
    // }

	public List<String> saver() {
		return new ArrayList<>();
	}

	@HystrixCommand(fallbackMethod = "saver")
	@GetMapping(value = "/names")
	public List<String> getNames() {
		// Return type can be String.class or JsonNode.class or Map.class instead we use
		// the one below
		ParameterizedTypeReference<Resources<Reservation>> ptr = new ParameterizedTypeReference<Resources<Reservation>>() {
		};
		ResponseEntity<Resources<Reservation>> responseEntity = this.rtemplate
				.exchange("http://reservation-service/reservations", HttpMethod.GET, null, ptr);

		return responseEntity.getBody().getContent().stream().map(Reservation::getReservationName)
				.collect(Collectors.toList());
	}

	@GetMapping(value = "/userdetails")
	public String getPrincipal(Principal principal) {
		OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
		Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
		Map<String, Object> details = (Map<String, Object>)((Map<String, Object>)((Map<String, Object>) userAuthentication.getDetails()).get("userAuthentication")).get("details");
		return details.toString();
	}

	// @PostMapping
	// public void write(@RequestBody Reservation r) {
	// 	Message<String> message = org.springframework.messaging.support.MessageBuilder
	// 			.withPayload(r.getReservationName()).build();
	// 	this.out.send(message);
	// }

}