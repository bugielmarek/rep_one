package com.bugielmarek.crudone.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Object <code>Contact</code> represents contact informations. Almost all fields
 * are self-explanatory: 'name', 'firstName', 'lastName', 'email',
 * 'phoneNumber'. Field 'text' is there to allow adding some additional notes,
 * like working hours of particular person/company, department name or something along this lines.
 * 
 * 
 * @author Bugiel Marek
 *
 */
@Entity
@Table(name = "contacts")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contact_id")
	private Long id;

	private String name;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private String email;

	@Column(name = "phone_numbers")
	private String phoneNumber;

	private String text;

	public Contact() {
	}
}
