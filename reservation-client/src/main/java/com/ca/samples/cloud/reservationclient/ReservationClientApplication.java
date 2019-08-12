package com.ca.samples.cloud.reservationclient;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

@RestController
@RequestMapping(path = "/reservations")
class ReservationApiGatewayRestController {

	private final RestTemplate rtemplate;

	

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

	/**
	 * @param rtemplate
	 */
	@Autowired
	public ReservationApiGatewayRestController(RestTemplate rtemplate) {
		this.rtemplate = rtemplate;
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
