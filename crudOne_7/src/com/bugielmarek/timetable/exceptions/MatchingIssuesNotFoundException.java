package com.bugielmarek.timetable.exceptions;

public class MatchingIssuesNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String name;

	public MatchingIssuesNotFoundException(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
