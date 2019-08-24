package com.ca.samples.cloud.reservationclient;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@EnableBinding(ReservationChannels.class)
@EnableCircuitBreaker
@EnableZuulProxy
@EnableEurekaClient
// @EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {

	@Bean
	@LoadBalanced
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}

}

interface ReservationChannels {
	@Output
	MessageChannel output();
}

@EnableResourceServer
@EnableOAuth2Client
@RestController
@RequestMapping(path = "/reservations")
class ReservationApiGatewayRestController {
	@Autowired
	private UserInfoRestTemplateFactory factory;

	@Bean
	@Lazy
	@LoadBalanced
	public OAuth2RestTemplate authRestTemplate() {
		return factory.getUserInfoRestTemplate();
	}

	@Autowired
	@LoadBalanced
	private RestTemplate rtemplate;
	private final MessageChannel out;

	public List<String> saver() {
		return new ArrayList<>();
	}
	
	// @HystrixCommand(fallbackMethod = "saver")
	@GetMapping(value="/names")
	public List<String> getNames() {
		//Return type can be String.class or JsonNode.class or Map.class instead we use the one below
		ParameterizedTypeReference<Resources<Reservation>> ptr = new ParameterizedTypeReference<Resources<Reservation>>() {};
		ResponseEntity<Resources<Reservation>> responseEntity = this.rtemplate.exchange("http://reservation-service/reservations", HttpMethod.GET, null, ptr);

		return responseEntity
			.getBody()
			.getContent()
			.stream()
			.map(Reservation::getReservationName)
			.collect(Collectors.toList());
	}

	@GetMapping(value="/pr")
	public String getPrincipal(Principal principal) {
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			System.out.println("bul: "+principal.getName());
			username = principal.toString();
		  }
		return username;
	}

	@GetMapping(value="/at")
	public String getAuthentication(Authentication authentication) {
		System.out.println("bul: "+authentication.getName());
		return authentication.toString();
	}
	

	@PostMapping
	public void write(@RequestBody Reservation	r) {
		Message<String> message = org.springframework.messaging.support.MessageBuilder.withPayload(r.getReservationName()).build();
		this.out.send(message);
	}


	@Autowired
	public ReservationApiGatewayRestController (ReservationChannels rChannels) {
		this.out=rChannels.output();
	}
	

}

class Reservation {
	String reservationName;

	/**
	 * @return the reservationName
	 */
	public String getReservationName() {
		return reservationName;
	}

	/**
	 * @param reservationName the reservationName to set
	 */
	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}

	
}

// @Component
class RLimiter extends ZuulFilter {


	private final RateLimiter rlimiter=RateLimiter.create(1.0/10.0);

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletResponse response = ctx.getResponse();
		if (!rlimiter.tryAcquire()) {
			response.setStatus(TOO_MANY_REQUESTS.value());
			ctx.setSendZuulResponse(false);
			throw new ZuulException("can not proceed", TOO_MANY_REQUESTS.value(), TOO_MANY_REQUESTS.getReasonPhrase());
		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true; //always filter
	}

	@Override
	public int filterOrder() {
		return Ordered.HIGHEST_PRECEDENCE+100;
	}

	@Override
	public String filterType() {
		return "pre";
	}}
