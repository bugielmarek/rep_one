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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "user" })
@Entity
@Table(name = "issues")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CaseType getCaseType() {
		return caseType;
	}

	public void setCaseType(CaseType caseType) {
		this.caseType = caseType;
	}

	public String getSygNumber() {
		return sygNumber;
	}

	public void setSygNumber(String sygNumber) {
		this.sygNumber = sygNumber;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		Issue other = (Issue) obj;
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
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Issue [id=" + id + ", caseType=" + caseType + ", sygNumber=" + sygNumber + ", text=" + text + ", name="
				+ name + ", user=" + user + ", deadline=" + deadline + ", date=" + date + "]";
	}

	private Issue(IssueBuilder buildier) {
		this.id = buildier.id;
		this.caseType = buildier.caseType;
		this.sygNumber = buildier.sygNumber;
		this.text = buildier.text;
		this.name = buildier.name;
		this.user = buildier.user;
		this.date = buildier.date;
		this.deadline = buildier.deadline;
	}

	public static class IssueBuilder {
		private Long id;
		private CaseType caseType;
		private String sygNumber;
		private String text;
		private String name;
		private User user;
		private LocalDate deadline;
		private String date;

		public IssueBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public IssueBuilder caseType(CaseType caseType) {
			this.caseType = caseType;
			return this;
		}

		public IssueBuilder sygNumber(String sygNumber) {
			this.sygNumber = sygNumber;
			return this;
		}

		public IssueBuilder text(String text) {
			this.text = text;
			return this;
		}

		public IssueBuilder name(String name) {
			this.name = name;
			return this;
		}

		public IssueBuilder user(User user) {
			this.user = user;
			return this;
		}

		public IssueBuilder date(String date) {
			this.date = date;
			return this;
		}

		public IssueBuilder deadline(LocalDate deadline) {
			this.deadline = deadline;
			return this;
		}

		public Issue build() {
			return new Issue(this);
		}
	}
}
