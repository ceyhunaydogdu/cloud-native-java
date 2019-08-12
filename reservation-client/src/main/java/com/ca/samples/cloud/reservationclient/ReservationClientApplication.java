package com.ca.samples.cloud.reservationclient;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

import javax.servlet.http.HttpServletResponse;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@EnableZuulProxy
@EnableEurekaClient
// @EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}

}

@Component
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
