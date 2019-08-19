package com.ca.samples.cloud.authservice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person{

	@Id
	@GeneratedValue
	private Long id;

	private String username, password;

	private boolean active;

	/**
	 * @param username
	 * @param password
	 * @param active
	 */
	
	public Person(String username, String password, boolean active) {
		this.username = username;
		this.password = password;
		this.active = active;
	}
	
}
