package com.bugielmarek.crudone.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Object <code>FormClass</code> wraps all domain objects in order to allow
 * Spring displaying error message for proper domain object. Searching in
 * home.jsp is done in four forms, one for each domain object. Whenever
 * searching ends up with no results found controller rejects the user input and
 * displays information that 'no results were found' next to the domain object
 * field. If that field name is for example 'name', then Spring returns such
 * message for each field named that way. In our case such message would be
 * returned for each form as each one has field called 'name'.
 * Wrapping all domain objects into one object allows naming form fields in
 * unique way - 'payment.name', 'contact.name'.
 * 
 * @author Bugiel Marek
 *
 */
@Getter
@Setter
@EqualsAndHashCode
public class FormClass {

	private Payment payment;
	private ReceivedCaseFile receivedCaseFile;
	private Contact contact;
	private Issue issue;

	public FormClass() {
	}

	public FormClass(Issue issue) {
		this.issue = issue;
	}

	public FormClass(ReceivedCaseFile receivedCaseFile) {
		this.receivedCaseFile = receivedCaseFile;
	}

	public FormClass(Payment payment) {
		this.payment = payment;
	}

	public FormClass(Contact contact) {
		this.contact = contact;
	}
}
