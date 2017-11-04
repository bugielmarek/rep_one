package com.bugielmarek.crudone.models;

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

/**
 * Object <code>Payment</code> represents payment that has: the primary key
 * 'id', field 'name' indicates the debtor's name - either person or company.
 * Field 'caseType' is limited to only three options which cover all possible
 * case types known: KM, KMS, KMP, thus usage of enum. SygNumber is made of two
 * parts separated by a slash. First part is a following number and then there
 * is a two digit representation of the year in which case was registered in the
 * office. Both those fields form case signature which is unique and can not be
 * duplicated in DB. Field 'deadline' holds date by which payment should be
 * done. Field 'date' is a String representation of 'deadline' field which makes
 * working with form easy across all web browsers. Each payment is created by a
 * user, field 'user' holds the information who created/last modified payment.
 * Field 'user' comes in handy because it provides the information about
 * employee who, most likely, talked last with debtor about paying.
 *
 * @author Bugiel Marek
 */
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
	private String date;

	/**
	 * Returns username of payment's user. When user was deleted from DB then
	 * what is returned is a dash(-).
	 * 
	 * @return username of user of this payment
	 */
	public String getUsername() {
		if (user == null) {
			return "-";
		}
		return user.getUsername();
	}
}
