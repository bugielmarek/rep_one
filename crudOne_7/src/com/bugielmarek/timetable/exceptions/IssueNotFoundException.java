package com.bugielmarek.timetable.exceptions;

public class IssueNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private long issueId;

	public IssueNotFoundException(long issueId) {
		this.issueId = issueId;
	}

	public long getIssueId() {
		return issueId;
	}
}
