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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * Object <code>Issue</code> represents information about some important issue.
 * This issue can be associated with particular case or can be more general one
 * - like term of validity of some software used in office etc. Issue has: the
 * primary key 'id', field 'name' for general name of issue or
 * person's/company's name associated with the issue. For issues associated with
 * case there are fields 'caseType' and 'sygNumber'. For detailed description of
 * a problem there is a field 'text'. Field 'deadline' holds the date when
 * something important happens. Field 'date' is a String representation of
 * 'deadline' field which makes working with form easy across all web browsers.
 * Each issue is created by a user, field 'user' holds the information who
 * created/last modified issue. Field 'user' allows users to create their own
 * issues, for them to remember and cope with.
 * 
 * @see com.bugielmarek.crudone.models.Payment Detailed explanation of
 *      caseType and sygNumber fields
 * @author Bugiel Marek
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "user" })
@Entity
@Table(name = "issues")
@Getter
@Setter
@EqualsAndHashCode(exclude = { "date" })
@ToString
@Builder
@AllArgsConstructor
public class Issue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "issue_id")
	private Long id;

	@Column(nullable = false, name = "case_type")
	@Enumerated(EnumType.STRING)
	@NotNull
	private CaseType caseType;

	@Pattern(regexp = "[1-9]+\\d{0,3}/\\d{2}\\b")
	@Column(name = "syg_number")
	private String sygNumber;

	@Size(min = 3, max = 500)
	private String text;

	private String name;

	@ManyToOne
	@JoinColumn(name = "user_id", updatable = true)
	private User user;

	private LocalDate deadline;

	@Transient
	@Pattern(regexp = "\\d{4}-[0-1]?[0-9]-[0-3]?[0-9]\\b")
	private String date;

	public Issue() {
	}

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
