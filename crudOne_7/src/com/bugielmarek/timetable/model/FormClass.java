package com.bugielmarek.timetable.model;

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
	public Issue getIssue() {
		return issue;
	}
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public Convergence getConvergence() {
		return convergence;
	}
	public void setConvergence(Convergence convergence) {
		this.convergence = convergence;
	}
	public Contact getContact() {
		return contact;
	}
	
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result + ((convergence == null) ? 0 : convergence.hashCode());
		result = prime * result + ((issue == null) ? 0 : issue.hashCode());
		result = prime * result + ((payment == null) ? 0 : payment.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormClass other = (FormClass) obj;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (convergence == null) {
			if (other.convergence != null)
				return false;
		} else if (!convergence.equals(other.convergence))
			return false;
		if (issue == null) {
			if (other.issue != null)
				return false;
		} else if (!issue.equals(other.issue))
			return false;
		if (payment == null) {
			if (other.payment != null)
				return false;
		} else if (!payment.equals(other.payment))
			return false;
		return true;
	}
	
}
