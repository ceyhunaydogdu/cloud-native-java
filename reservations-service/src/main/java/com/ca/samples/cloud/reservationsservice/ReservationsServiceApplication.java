package com.ca.samples.cloud.reservationsservice;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableBinding(ReservationChannels.class)
@SpringBootApplication
@EnableEurekaClient
// @EnableResourceServer
// The one below also works
// @EnableDiscoveryClient
public class ReservationsServiceApplication {

	@Autowired
	ReservationRepository reservationRepo;

	@Bean
	CommandLineRunner commandLineRunner(){
		return args -> {
			Stream.of("Ceyhun", "Micheal", "Axel", "Salih", "Mehmet", "Hans", "Jennifer")
				.forEach(name -> this.reservationRepo.save(new Reservation(name)));
			this.reservationRepo.findAll().forEach(System.out::println);
		};
	}

	// @Autowired
	// private UserInfoRestTemplateFactory factory;

	// @Bean
	// @Lazy
	// @LoadBalanced
	// public OAuth2RestTemplate authRestTemplate() {
	// 	return factory.getUserInfoRestTemplate();
	// }

	public static void main(String[] args) {
		SpringApplication.run(ReservationsServiceApplication.class, args);
	}

}

@RefreshScope
@RestController
class MessageController{

	private final String value;

	/**
	 * @param value
	 */

	public MessageController(@Value("${message}") String value) {
		this.value = value;
	}
	
	@GetMapping(value="/message")
	public String getMessage(){
		return this.value;
	}

	@GetMapping(value = "/username")
	public String getPrincipal(Principal principal) {
		return principal.getName();
	}
	
}

@MessageEndpoint
class ReservationProcessor {
	private ReservationRepository reservationRepo;

	@Autowired
	public ReservationProcessor(ReservationRepository reservationRepository) {
		this.reservationRepo=reservationRepository;
	}

	@ServiceActivator(inputChannel = "input")
	public void acceptNewReservation(Message<String> msg) {
		this.reservationRepo.save(new Reservation(msg.getPayload()));
	}
}

interface ReservationChannels {
	@Input
	SubscribableChannel input();
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long>{
	@RestResource(path = "by-message")
	List<Reservation> findByReservationNameContainingIgnoringCase(@Param(value = "rn") String rn);
}

@Entity
class Reservation {
	@Id
	@GeneratedValue
	private Long id;

	private String reservationName;
	
	public Reservation() {
	}

	/**
	 * @param reservationName
	 */

	public Reservation(String reservationName) {
		this.reservationName = reservationName;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", reservationName=" + reservationName + "]";
	}

	
}