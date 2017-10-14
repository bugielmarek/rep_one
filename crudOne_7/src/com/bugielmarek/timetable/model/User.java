package com.bugielmarek.timetable.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(exclude={"password", "formPassword"})
@ToString
@Builder
@AllArgsConstructor
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Size(min = 6, max = 6)
	@Pattern(regexp = "\\w{6}\\b")
	@Column(nullable = false)
	private String username;

	@Pattern(regexp = "\\S+\\b")
	@Size(min = 6, max = 15)
	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String authority;

	@Column(nullable = false)
	private boolean enabled;
	
	@Transient
	@Pattern(regexp = "\\S+\\b")
	@Size(min = 6, max = 15)
	private String formPassword;

	public User() {
	}
}
