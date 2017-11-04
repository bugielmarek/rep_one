package com.bugielmarek.crudone.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

/**
 * Object <code>ReceivedCaseFile</code> represents case file that arrived at the
 * office due to convergence of execution - a situation where two, or more,
 * offices occupy the same asset. ReceivedCaseFile has: the primary key 'id',
 * field 'name' indicates debtor's name. 'office' field holds the name of the office
 * with which convergence occurred. Each case file has case signature which
 * consists of 'caseType' and 'sygNumber' fields. 'arrivedAt' field specifies when case
 * file was received. Field 'date' is a String representation of 'arrivedAt'
 * field which makes working with form easy across all web browsers.
 * 
 * @author Bugiel Marek
 * @see com.bugielmarek.crudone.models.Payment Detailed explanation of
 *      caseType and sygNumber fields
 */
@Entity
@Table(name = "received_case_files")
@Getter
@Setter
@EqualsAndHashCode(exclude = "date")
@Builder
@AllArgsConstructor
public class ReceivedCaseFile {

	@Id
	@Column(name = "received_case_file_id")
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

	public ReceivedCaseFile() {
	}
}
