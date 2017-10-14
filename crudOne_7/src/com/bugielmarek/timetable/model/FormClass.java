package com.bugielmarek.timetable.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class FormClass {

	private Payment payment;
	private Convergence convergence;
	private Contact contact;
	private Issue issue;
	
	public FormClass() {
	}
	
	public FormClass(Issue issue) {
		this.issue = issue;
	}

	public FormClass(Convergence convergence) {
		this.convergence = convergence;
	}

	public FormClass(Payment payment) {
		this.payment = payment;
	}

	public FormClass(Contact contact) {
		this.contact = contact;
	}
}
