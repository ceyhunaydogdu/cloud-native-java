package com.ca.samples.cloud.reservationsservice;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@SpringBootApplication
public class ReservationsServiceApplication {

	@Autowired
	ReservationRepository reservationRepo;

	@Bean
	CommandLineRunner commandLineRunner(){
		return args -> {
			Stream.of("Ceyhun", "Demet", "Fırat", "Sare", "Gonca", "Salih", "Havva")
				.forEach(name -> this.reservationRepo.save(new Reservation(name)));
			this.reservationRepo.findAll().forEach(System.out::println);
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(ReservationsServiceApplication.class, args);
	}

}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long>{
	@RestResource(path = "by-message")
	List<Reservation> findByReservationName(@Param(value = "rn") String rn);
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