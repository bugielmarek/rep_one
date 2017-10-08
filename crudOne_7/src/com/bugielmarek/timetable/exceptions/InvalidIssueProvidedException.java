package com.bugielmarek.timetable.exceptions;

import org.springframework.validation.BindingResult;

public class InvalidIssueProvidedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private BindingResult result;

	public InvalidIssueProvidedException(BindingResult result) {
		this.result = result;
	}
	
	public BindingResult getResult() {
		return result;
	}
}
