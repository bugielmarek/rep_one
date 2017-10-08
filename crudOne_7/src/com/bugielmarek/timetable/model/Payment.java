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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "username" })
@JsonPropertyOrder({ "user", "name", "deadline", "sygType", "sygNumber", "date" })
@Entity
@Table(name = "payments")
public class Payment {

	public Payment() {
	}

	@Id
	@Column(name = "payment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonProperty("nejm")
	@Size(min = 3, max = 80)
	@Column(nullable = false)
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
	private String sygNumber;
	
	@Transient
	@Pattern(regexp = "\\d{4}-[0-1]?[0-9]-[0-3]?[0-9]\\b")
	private String date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSygNumber() {
		return sygNumber;
	}

	public void setSygNumber(String sygNumber) {
		this.sygNumber = sygNumber;
	}

	public CaseType getCaseType() {
		return caseType;
	}
	
	public void setCaseType(CaseType caseType) {
		this.caseType = caseType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		if (user == null) {
			return "-";
		}
		return user.getUsername();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", name=" + name + ", user=" + user + ", deadline=" + deadline + ", caseType="
				+ caseType + ", sygNumber=" + sygNumber + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caseType == null) ? 0 : caseType.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((deadline == null) ? 0 : deadline.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((sygNumber == null) ? 0 : sygNumber.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Payment other = (Payment) obj;
		if (caseType != other.caseType)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (deadline == null) {
			if (other.deadline != null)
				return false;
		} else if (!deadline.equals(other.deadline))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sygNumber == null) {
			if (other.sygNumber != null)
				return false;
		} else if (!sygNumber.equals(other.sygNumber))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	private Payment(PaymentBuilder buildier){
		this.id = buildier.id;
		this.name = buildier.name;
		this.caseType = buildier.caseType;
		this.sygNumber = buildier.sygNumber;
		this.date = buildier.date;
		this.deadline = buildier.deadline;
		this.user = buildier.user;
	}

	public static class PaymentBuilder{
		
		private Long id;
		private String name;
		private CaseType caseType;
		private String sygNumber;
		private String date;
		private LocalDate deadline;
		private User user;
		
		public PaymentBuilder id(Long id){
			this.id = id;
			return this;
		}
		
		public PaymentBuilder name(String name){
			this.name = name;
			return this;
		}
		
		public PaymentBuilder caseType(CaseType caseType){
			this.caseType = caseType;
			return this;
		}
		
		public PaymentBuilder sygNumber(String sygNumber){
			this.sygNumber = sygNumber;
			return this;
		}
		
		public PaymentBuilder date(String date){
			this.date = date;
			return this;
		}
		
		public PaymentBuilder deadline(LocalDate deadline){
			this.deadline = deadline;
			return this;
		}
		
		public PaymentBuilder user(User user){
			this.user = user;
			return this;
		}
		
		public Payment build(){
			return new Payment(this);
		}
	}
}
