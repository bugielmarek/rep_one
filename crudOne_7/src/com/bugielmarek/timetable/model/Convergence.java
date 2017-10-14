package com.bugielmarek.timetable.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "convergences")
@Getter
@Setter
@EqualsAndHashCode(exclude = "date")
@ToString
@Builder
@AllArgsConstructor
public class Convergence {

	@Id
	@Column(name = "convergence_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 3, max = 80)
	private String name;

	@Column(nullable = false, name = "case_type")
	@Enumerated(EnumType.STRING)
	@NotNull
	private CaseType caseType;

	@Pattern(regexp = "[1-9]+\\d{0,3}/[0-9]{1}\\d{1}\\b")
	@Column(name = "syg_number")
	private String sygNumber;

	@Size(min = 2, max = 80)
	private String office;

	@Column(name = "arrived_at")
	private LocalDate arrivedAt;

	@Transient
	@Pattern(regexp = "[0-9]{4}-[0-1]?[0-9]-[0-3]?[0-9]")
	private String date;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Convergence() {
	}

	public String getUsername() {
		if (user == null) {
			return "-";
		}
		return user.getUsername();
	}
}
