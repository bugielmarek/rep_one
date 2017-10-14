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
@Table(name = "payments")
@Getter
@Setter
@EqualsAndHashCode(exclude = { "date" })
@ToString
@Builder
@AllArgsConstructor
public class Payment {

	public Payment() {
	}

	@Id
	@Column(name = "payment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 3, max = 80)
	@Column(nullable = false)
	@NotNull
	private String name;

	@ManyToOne
	@JoinColumn(name = "user_id", updatable = true)
	private User user;

	@Column(nullable = false)
	private LocalDate deadline;

	@Column(nullable = false, name = "case_type")
	@Enumerated(EnumType.STRING)
	@NotNull
	private CaseType caseType;

	@Pattern(regexp = "[1-9]+\\d{0,3}/\\d{2}\\b")
	@Column(nullable = false, name = "syg_number")
	@NotNull
	private String sygNumber;

	@Transient
	@Pattern(regexp = "\\d{4}-[0-1]?[0-9]-[0-3]?[0-9]\\b")
	@NotNull
	private String date;

	public String getUsername() {
		if (user == null) {
			return "-";
		}
		return user.getUsername();
	}
}
