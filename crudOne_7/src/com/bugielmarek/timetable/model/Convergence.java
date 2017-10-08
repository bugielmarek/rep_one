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

@Entity
@Table(name = "convergences")
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

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public LocalDate getArrivedAt() {
		return arrivedAt;
	}

	public void setArrivedAt(LocalDate arrivedAt) {
		this.arrivedAt = arrivedAt;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Convergence(){
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrivedAt == null) ? 0 : arrivedAt.hashCode());
		result = prime * result + ((caseType == null) ? 0 : caseType.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((office == null) ? 0 : office.hashCode());
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
		Convergence other = (Convergence) obj;
		if (arrivedAt == null) {
			if (other.arrivedAt != null)
				return false;
		} else if (!arrivedAt.equals(other.arrivedAt))
			return false;
		if (caseType != other.caseType)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
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
		if (office == null) {
			if (other.office != null)
				return false;
		} else if (!office.equals(other.office))
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Convergence [id=" + id + ", name=" + name + ", caseType=" + caseType + ", sygNumber=" + sygNumber
				+ ", office=" + office + ", arrivedAt=" + arrivedAt + ", date=" + date + ", user=" + user + "]";
	}

	private Convergence(ConvergenceBuilder buildier){
		this.id = buildier.id;
		this.name = buildier.name;
		this.caseType = buildier.caseType;
		this.sygNumber = buildier.sygNumber;
		this.office = buildier.office;
		this.arrivedAt = buildier.arrivedAt;
		this.date = buildier.date;
		this.user = buildier.user;
	}
	
	public static class ConvergenceBuilder{
		
		private Long id;
		private String name;
		private CaseType caseType;
		private String sygNumber;
		private String office;
		private LocalDate arrivedAt;
		private String date;
		private User user;
		
		public ConvergenceBuilder id(Long id){
			this.id = id;
			return this;
		}
		
		public ConvergenceBuilder name(String name){
			this.name = name;
			return this;
		}
		
		public ConvergenceBuilder caseType(CaseType caseType){
			this.caseType = caseType;
			return this;
		}
		
		public ConvergenceBuilder sygNumber(String sygNumber){
			this.sygNumber = sygNumber;
			return this;
		}
		
		public ConvergenceBuilder office(String office){
			this.office = office;
			return this;
		}
		
		public ConvergenceBuilder arrivedAt(LocalDate arrivedAt){
			this.arrivedAt = arrivedAt;
			return this;
		}
		
		public ConvergenceBuilder date(String date){
			this.date = date;
			return this;
		}
		
		public ConvergenceBuilder user(User user){
			this.user = user;
			return this;
		}
		
		public Convergence build(){
			return new Convergence(this);
		}
	}
}
